package com.launchkey.sdk.transport.apachehttp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.cache.Cache;
import com.launchkey.sdk.cache.CacheException;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.jwe.JWEFailure;
import com.launchkey.sdk.crypto.jwe.JWEService;
import com.launchkey.sdk.crypto.jwt.JWTData;
import com.launchkey.sdk.crypto.jwt.JWTError;
import com.launchkey.sdk.crypto.jwt.JWTService;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.Transport;
import com.launchkey.sdk.transport.domain.*;
import com.launchkey.sdk.transport.domain.Error;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;

public class ApacheHttpTransport implements Transport {

    private static final Base64 BASE_64 = new Base64(0);
    private static final String IOV_JWT_HEADER = "X-IOV-JWT";
    private static int serverTimeOffset = 0;
    private static Date serverTimeOffsetExpires = null;
    private final Log logger;
    private final EntityKeyMap entityKeyMap;
    private PublicKeyData currentPublicKeyData = null;
    private Date currentPublicKeyDataExpires = null;

    private final ApiRequestBuilderFactory rbf;
    private final ObjectMapper objectMapper;
    private final Crypto crypto;
    private final HttpClient httpClient;
    private final JWTService jwtService;
    private final JWEService jweService;
    private final Cache publicKeyCache;
    private final int offsetTTL;
    private final int currentPublicKeyTTL;
    private final EntityIdentifier issuer;


    public ApacheHttpTransport(HttpClient httpClient, Crypto crypto, ObjectMapper objectMapper,
                               Cache publicKeyCache, String baseUrl, EntityIdentifier issuer,
                               JWTService jwtService, JWEService jweService,
                               int offsetTTL, int currentPublicKeyTTL, EntityKeyMap entityKeyMap
    ) {
        this.objectMapper = objectMapper;
        this.crypto = crypto;
        this.httpClient = httpClient;
        this.jwtService = jwtService;
        this.jweService = jweService;
        this.publicKeyCache = publicKeyCache;
        this.entityKeyMap = entityKeyMap;
        this.offsetTTL = offsetTTL;
        this.currentPublicKeyTTL = currentPublicKeyTTL;
        this.issuer = issuer;
        logger = LogFactory.getLog(getClass());
        rbf = new ApiRequestBuilderFactory(issuer.toString(), baseUrl, objectMapper, crypto, jwtService, jweService);
    }

    @Override
    public PublicV3PingGetResponse publicV3PingGet()
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        HttpResponse response = getHttpResponse("GET", "/public/v3/ping", null, null, false, null);
        return parseJsonResponse(response.getEntity(), PublicV3PingGetResponse.class);
    }

    @Override
    public PublicV3PublicKeyGetResponse publicV3PublicKeyGet(String publicKeyFingerprint)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {

        String path = "/public/v3/public-key";
        if (publicKeyFingerprint != null) {
            path = path + "/" + publicKeyFingerprint;
        }
        HttpResponse response = getHttpResponse("GET", path, null, null, false, null);
        Header keyHeader = response.getFirstHeader("X-IOV-KEY-ID");
        if (keyHeader == null) {
            throw new InvalidResponseException("Public Key ID header X-IOV-KEY-ID not found in response", null, null);
        }
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new InvalidResponseException("No public key in response", null, null);
            }
            String publicKey = EntityUtils.toString(entity);
            return new PublicV3PublicKeyGetResponse(publicKey, keyHeader.getValue());
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseException("Invalid public key in response.", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("Error reading response", e, null);
        }
    }

    @Override
    public ServiceV3AuthsPostResponse serviceV3AuthsPost(ServiceV3AuthsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/service/v3/auths", subject, request, true, null);
        return decryptResponse(response, ServiceV3AuthsPostResponse.class);
    }

    @Override
    public ServiceV3AuthsGetResponse serviceV3AuthsGet(UUID authRequestId, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException, AuthorizationRequestTimedOutError, NoKeyFoundException {
        ServiceV3AuthsGetResponse response;
        String path = "/service/v3/auths/" + authRequestId.toString();
        HttpResponse httpResponse = getHttpResponse("GET", path, subject, null, true, Arrays.asList(408));
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 204) { // User has not responded
            response = null;
        } else if (statusCode == 408) { // User did not respond before request timed out
            throw new AuthorizationRequestTimedOutError();
        } else { // Users responded
            ServiceV3AuthsGetResponseCore apiResponse = decryptResponse(httpResponse, ServiceV3AuthsGetResponseCore.class);
            try {
                JWTData jwtData = jwtService.getJWTData(getJWT(httpResponse));
                EntityIdentifier audience = EntityIdentifier.fromString(jwtData.getAudience());
                RSAPrivateKey key = entityKeyMap.getKey(audience, apiResponse.getPublicKeyId());
                byte[] decrypted;
                try {
                    byte[] encrypted = Base64.decodeBase64(apiResponse.getEncryptedDeviceResponse().getBytes());
                    decrypted = crypto.decryptRSA(encrypted, key);
                } catch (Exception e) {
                    throw new CryptographyError("Unable to decrypt device response!", e);
                }
                ServiceV3AuthsGetResponseDevice deviceResponse =
                        objectMapper.readValue(decrypted, ServiceV3AuthsGetResponseDevice.class);
                response = new ServiceV3AuthsGetResponse(
                        audience,
                        subject.getId(),
                        apiResponse.getServiceUserHash(),
                        apiResponse.getOrgUserHash(),
                        apiResponse.getUserPushId(),
                        deviceResponse.getAuthorizationRequestId(),
                        deviceResponse.getResponse(),
                        deviceResponse.getDeviceId(),
                        deviceResponse.getServicePins()
                );
            } catch (JWTError jwtError) {
                throw new CryptographyError("Unable to parse JWT to get key info!", jwtError);
            } catch (JsonParseException e) {
                throw new MarshallingError("Unable to parse the decrypted device response!", e);
            } catch (JsonMappingException e) {
                throw new MarshallingError("Unable to map the decrypted device response data!", e);
            } catch (IOException e) {
                throw new CommunicationErrorException("An I/O error occurred!", e, null);
            }
        }
        return response;
    }

    @Override
    public void serviceV3SessionsPost(ServiceV3SessionsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError,
            CryptographyError, InvalidRequestException, InvalidCredentialsException {
        getHttpResponse("POST", "/service/v3/sessions", subject, request, true, null);
    }

    @Override
    public void serviceV3SessionsDelete(ServiceV3SessionsDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        getHttpResponse("DELETE", "/service/v3/sessions", subject, request, true, null);
    }

    @Override
    public DirectoryV3DevicesPostResponse directoryV3DevicesPost(DirectoryV3DevicesPostRequest request, EntityIdentifier subject) throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError, InvalidRequestException, InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/directory/v3/devices", subject, request, true, null);
        return decryptResponse(response, DirectoryV3DevicesPostResponse.class);
    }

    @Override
    public DirectoryV3DevicesListPostResponse directoryV3DevicesListPost(DirectoryV3DevicesListPostRequest request, EntityIdentifier subject) throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError, InvalidRequestException, InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/directory/v3/devices/list", subject, request, true, null);
        DirectoryV3DevicesListPostResponseDevice[] devices =
                decryptResponse(response, DirectoryV3DevicesListPostResponseDevice[].class);
        return new DirectoryV3DevicesListPostResponse(Arrays.asList(devices));
    }

    @Override
    public void directoryV3devicesDelete(DirectoryV3DevicesDeleteRequest request, EntityIdentifier subject) throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError, InvalidRequestException, InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/devices", subject, request, true, null);
    }

    @Override
    public void directoryV3SessionsDelete(DirectoryV3SessionsDeleteRequest request, EntityIdentifier subject) throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError, InvalidRequestException, InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/sessions", subject, request, true, null);
    }

    @Override
    public ServerSentEvent handleServerSentEvent(Map<String, List<String>> headers, String body) throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException, InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        ServerSentEvent response;
        HeaderGroup headerGroup = new HeaderGroup();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String item : entry.getValue()) {
                headerGroup.addHeader(new BasicHeader(entry.getKey(), item));
            }
        }
        String jwt = headerGroup.getFirstHeader(IOV_JWT_HEADER).getValue();
        try {
            validateJWT(null, jwt);
            JWTData jwtData = jwtService.getJWTData(jwt);
            if (headerGroup.getFirstHeader("Content-Type").getValue().startsWith("application/jose")) {
                // Auths response is encrypted
                final EntityIdentifier requestingEntity = EntityIdentifier.fromString(jwtData.getAudience());
                final String encryptionKeyId = jweService.getHeaders(body).get("kid");
                final RSAPrivateKey privateKey = entityKeyMap.getKey(requestingEntity, encryptionKeyId);
                final String decrypted = jweService.decrypt(body, privateKey);
                final ServerSentEventAuthorizationResponseCore core = objectMapper.readValue(decrypted, ServerSentEventAuthorizationResponseCore.class);
                final byte[] decryptedDeviceResponse = crypto.decryptRSA(BASE_64.decode(core.getAuth().getBytes()), privateKey);
                final ServiceV3AuthsGetResponseDevice deviceResponse = objectMapper.readValue(decryptedDeviceResponse, ServiceV3AuthsGetResponseDevice.class);
                response = new ServerSentEventAuthorizationResponse(
                        requestingEntity,
                        EntityIdentifier.fromString(jwtData.getSubject()).getId(),
                        core.getServiceUserHash(),
                        core.getOrgUserHash(),
                        core.getUserPushId(),
                        deviceResponse.getAuthorizationRequestId(),
                        deviceResponse.getResponse(),
                        deviceResponse.getDeviceId(),
                        deviceResponse.getServicePins()
                );
            } else {
                // Session end is not encrypted
                response = objectMapper.readValue(body, ServerSentEventUserServiceSessionEnd.class);
            }
        } catch (JWTError jwtError) {
            throw new InvalidRequestException("Invalid JWT in the headers!", jwtError, null);
        } catch (JWEFailure jweFailure) {
            throw new InvalidRequestException("Unable to decrypt the body!", jweFailure, null);
        } catch (JsonParseException e) {
            throw new InvalidRequestException("Unable to parse the decrypted body as JSON!", e, null);
        } catch (JsonMappingException e) {
            throw new InvalidRequestException("Unable to map the decrypted body JSON to a Map<String, Object>!", e, null);
        } catch (IOException e) {
            throw new InvalidRequestException("Unable to read the body due to an I/O error!", e, null);
        }
        return response;
    }

    private HttpResponse getHttpResponse(
            String method, String path, EntityIdentifier subjectEntity, Object transportObject, boolean signRequest, List<Integer> httpStatusCodeWhiteList)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {

        PublicKey publicKey;
        String publicKeyFingerprint;
        Date currentDate;
        String subject;
        if (signRequest) {
            PublicKeyData publicKeyData = getCurrentPublicKeyData();
            publicKey = publicKeyData.getKey();
            publicKeyFingerprint = publicKeyData.getFingerprint();
            currentDate = getCurrentDate();
            subject = subjectEntity.toString();
        } else {
            publicKey = null;
            publicKeyFingerprint = null;
            currentDate = null;
            subject = null;
        }

        String requestId = UUID.randomUUID().toString();
        HttpUriRequest request = rbf.create(publicKey, publicKeyFingerprint, currentDate)
                .setMethod(method)
                .setPath(path)
                .setSubject(subject)
                .setTransportObject(transportObject)
                .build(requestId);
        try {
            HttpResponse response = httpClient.execute(request);
            throwForStatus(response, requestId, httpStatusCodeWhiteList == null ? new ArrayList<Integer>() : httpStatusCodeWhiteList);
            if (signRequest) {
                validateResponseJWT(response, requestId);
            }
            return response;
        } catch (IOException e) {
            throw new CommunicationErrorException("An I/O Error Occurred", e, null);
        }
    }

    private void throwForStatus(HttpResponse response, String requestId, List<Integer> httpStatusCodeWhiteList)
            throws CommunicationErrorException, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, MarshallingError, CryptographyError {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (httpStatusCodeWhiteList.contains(statusCode)) {
            // Do nothing
        } else if (statusCode == 400) {
            final HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null || httpEntity.getContentLength() == 0) {
                throw new InvalidRequestException(
                        response.getStatusLine().getReasonPhrase(),
                        null,
                        "HTTP-" + String.valueOf(statusCode)
                );
            } else {
                try {
                    validateResponseJWT(response, requestId);
                    Error error = decryptResponse(response, Error.class);
                    Object detail = error.getErrorDetail();
                    throw new InvalidResponseException(
                            objectMapper.writeValueAsString(detail), null, error.getErrorCode());
                } catch (IOException e) {
                    throw new InvalidResponseException("Unable to parse error in response", e, null);
                }
            }
        } else if (statusCode == 401) {
            throw new InvalidRequestException(
                    response.getStatusLine().getReasonPhrase(),
                    null,
                    "HTTP-" + String.valueOf(statusCode)
            );
        } else if (statusCode == 403) {
            throw new InvalidCredentialsException(
                    response.getStatusLine().getReasonPhrase(),
                    null,
                    "HTTP-" + String.valueOf(statusCode)
            );
        } else if (!(statusCode >= 200 && statusCode < 300)) {
            throw new CommunicationErrorException(
                    "HTTP Error: [" + String.valueOf(statusCode)
                            + "] " + response.getStatusLine().getReasonPhrase()
                    , null, null);

        }
    }

    private <T> T decryptResponse(HttpResponse response, Class<T> type)
            throws InvalidResponseException, CommunicationErrorException, CryptographyError {
        ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
        try {
            response.getEntity().writeTo(encrypted);
            String json = jweService.decrypt(encrypted.toString());
            return objectMapper.readValue(json, type);
        } catch (JsonParseException e) {
            throw new InvalidResponseException("Unable to parse response as JSON", e, null);
        } catch (JsonMappingException e) {
            throw new InvalidResponseException("Unable to parse response as JSON", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("AN IO Error Occurred", e, null);
        } catch (JWEFailure jweFailure) {
            throw new CryptographyError("Unable to decrypt response!", jweFailure);
        }
    }

    private <T> T parseJsonResponse(HttpEntity entity, Class<T> valueType)
            throws InvalidResponseException, CommunicationErrorException {
        try {
            return objectMapper.readValue(entity.getContent(), valueType);
        } catch (JsonParseException e) {
            throw new InvalidResponseException("Unable to parse response as JSON", e, null);
        } catch (JsonMappingException e) {
            throw new InvalidResponseException("Unable to parse response as JSON", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("AN IO Error Occurred", e, null);
        }
    }

    private void validateResponseJWT(HttpResponse response, String expectedTokenId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        try {

            final String jwt = getJWT(response);
            validateJWT(expectedTokenId, jwt);
        } catch (JWTError jwtError) {
            throw new InvalidResponseException("Invalid JWT in response!", jwtError, null);
        }
    }

    private void validateJWT(String expectedTokenId, String jwt) throws JWTError, MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError, InvalidRequestException, InvalidCredentialsException {
        String keyId = jwtService.getJWTData(jwt).getKeyId();
        jwtService.decode(
                getPublicKeyData(keyId).getKey(), issuer.toString(), expectedTokenId, getCurrentDate(), jwt);
    }

    private String getJWT(HttpResponse response) {
        return response.getFirstHeader(IOV_JWT_HEADER).getValue();
    }


    private PublicKeyData getCurrentPublicKeyData()
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        if (currentPublicKeyDataExpires == null || currentPublicKeyDataExpires.before(new Date())) {
            setCurrentPublicKeyData();
        }
        return currentPublicKeyData;

    }

    private synchronized void setCurrentPublicKeyData()
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        currentPublicKeyData = getPublicKeyData(null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, currentPublicKeyTTL);
        currentPublicKeyDataExpires = calendar.getTime();
    }

    private PublicKeyData getPublicKeyData(String fingerprint)
            throws MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        PublicKeyData publicKeyData = null;

        if (fingerprint != null) {
            String cacheKey = "LaunchKeyPublicKey:" + fingerprint;
            String publicKey = null;
            try {
                publicKey = publicKeyCache.get(cacheKey);
            } catch (CacheException e) {
                logger.error("Unable to retrieve public key from cache. This will degrade performance.", e);
            }
            if (publicKey != null) {
                try {
                    publicKeyData = new PublicKeyData(
                            crypto.getRSAPublicKeyFromPEM(publicKey),
                            fingerprint
                    );
                } catch (IllegalArgumentException e) {
                    // The stored key cannot be parsed. Do nothing, the next step will get a new key.
                    // If the exception was raised due to a missing algorithm, it will fail in that step as well.
                }
            }
        }

        // If the key was not in cache or the cached version was not valid,
        // get it from the API and store it in the cache
        if (publicKeyData == null) {
            PublicV3PublicKeyGetResponse apiKey = publicV3PublicKeyGet(fingerprint);
            publicKeyData = new PublicKeyData(
                    crypto.getRSAPublicKeyFromPEM(apiKey.getPublicKey()), apiKey.getPublicKeyFingerprint());
            try {
                publicKeyCache.put(apiKey.getPublicKeyFingerprint(), apiKey.getPublicKey());
            } catch (CacheException e) {
                logger.error("Unable to cache public key. This will degrade performance.", e);
            }
        }
        return publicKeyData;
    }

    private Date getCurrentDate()
            throws MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        if (serverTimeOffsetExpires == null || serverTimeOffsetExpires.before(new Date())) {
            setServerTimeOffset();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MILLISECOND, serverTimeOffset);
        return calendar.getTime();
    }

    private synchronized void setServerTimeOffset()
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException {
        Date now = new Date();
        PublicV3PingGetResponse response = publicV3PingGet();
        serverTimeOffset = new Long(response.getApiTime().getTime() - now.getTime()).intValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, offsetTTL);
        serverTimeOffsetExpires = calendar.getTime();
    }

    private class PublicKeyData {
        private final PublicKey key;
        private final String fingerprint;

        private PublicKeyData(PublicKey key, String fingerprint) {
            this.key = key;
            this.fingerprint = fingerprint;
        }

        public PublicKey getKey() {
            return key;
        }

        public String getFingerprint() {
            return fingerprint;
        }
    }
}
