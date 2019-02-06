package com.iovation.launchkey.sdk.transport.apachehttp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.cache.CacheException;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.jwe.JWEFailure;
import com.iovation.launchkey.sdk.crypto.jwe.JWEService;
import com.iovation.launchkey.sdk.crypto.jwt.JWTClaims;
import com.iovation.launchkey.sdk.crypto.jwt.JWTData;
import com.iovation.launchkey.sdk.crypto.jwt.JWTError;
import com.iovation.launchkey.sdk.crypto.jwt.JWTService;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import com.iovation.launchkey.sdk.transport.domain.Error;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
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
import java.security.NoSuchAlgorithmException;
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
        this.objectMapper.setDateFormat(new StdDateFormat());
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
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("GET", "/public/v3/ping", null, null, false, null);
        return parseJsonResponse(response.getEntity(), PublicV3PingGetResponse.class);
    }

    @Override
    public PublicV3PublicKeyGetResponse publicV3PublicKeyGet(String publicKeyFingerprint)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidCredentialsException {

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
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/service/v3/auths", subject, request, true, null);
        return decryptResponse(response, ServiceV3AuthsPostResponse.class);
    }

    @Override
    public ServiceV3AuthsGetResponse serviceV3AuthsGet(UUID authRequestId, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException, AuthorizationRequestTimedOutError,
            NoKeyFoundException {
        ServiceV3AuthsGetResponse response;
        HttpResponse httpResponse;

        String path = "/service/v3/auths/" + authRequestId.toString();
        try {
            httpResponse = getHttpResponse("GET", path, subject, null, true, null);
        } catch (RequestTimedOut e) {
            throw new AuthorizationRequestTimedOutError();
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 204) { // User has not responded
            response = null;
        } else { // Users responded
            ServiceV3AuthsGetResponseCore apiResponse =
                    decryptResponse(httpResponse, ServiceV3AuthsGetResponseCore.class);

            EntityIdentifier audience;
            try {
                JWTData jwtData = jwtService.getJWTData(getJWT(httpResponse));
                audience = EntityIdentifier.fromString(jwtData.getAudience());
            } catch (JWTError jwtError){
                    throw new CryptographyError("Unable to parse JWT to get key info!", jwtError);
            }
            try {
                if (apiResponse.getJweEncryptedDeviceResponse() != null) {
                    String decrypted = jweService.decrypt(apiResponse.getJweEncryptedDeviceResponse());
                    ServiceV3AuthsGetResponseDeviceJWE deviceResponse =
                            objectMapper.readValue(decrypted, ServiceV3AuthsGetResponseDeviceJWE.class);
                    response =  new ServiceV3AuthsGetResponse(
                            audience,
                            subject.getId(),
                            apiResponse.getServiceUserHash(),
                            apiResponse.getOrgUserHash(),
                            apiResponse.getUserPushId(),
                            deviceResponse.getAuthorizationRequestId(),
                            "AUTHORIZED".equals(deviceResponse.getType()),
                            deviceResponse.getDeviceId(),
                            deviceResponse.getServicePins(),
                            deviceResponse.getType(),
                            deviceResponse.getReason(),
                            deviceResponse.getDenialReason());

                } else {
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
                            deviceResponse.getServicePins(),
                            null,
                            null,
                            null

                    );
                }
            } catch (JsonParseException e) {
                throw new MarshallingError("Unable to parse the decrypted device response!", e);
            } catch (JsonMappingException e) {
                throw new MarshallingError("Unable to map the decrypted device response data!", e);
            } catch (IOException e) {
                throw new CommunicationErrorException("An I/O error occurred!", e, null);
            } catch (JWEFailure jweFailure) {
                throw new CryptographyError("Unable to decrypt auth_jwe in response!", jweFailure);
            }
        }
        return response;
    }

    @Override
    public void serviceV3AuthsDelete(UUID authRequestId, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError,
            CryptographyError, InvalidCredentialsException {
        String path = "/service/v3/auths/" + authRequestId.toString();
        getHttpResponse("DELETE", path, subject, null, true, null);
    }

    @Override
    public void serviceV3SessionsPost(ServiceV3SessionsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError,
            CryptographyError, InvalidCredentialsException {
        getHttpResponse("POST", "/service/v3/sessions", subject, request, true, null);
    }

    @Override
    public void serviceV3SessionsDelete(ServiceV3SessionsDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/service/v3/sessions", subject, request, true, null);
    }

    @Override
    public DirectoryV3DevicesPostResponse directoryV3DevicesPost(DirectoryV3DevicesPostRequest request,
                                                                 EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/directory/v3/devices", subject, request, true, null);
        return decryptResponse(response, DirectoryV3DevicesPostResponse.class);
    }

    @Override
    public DirectoryV3DevicesListPostResponse directoryV3DevicesListPost(DirectoryV3DevicesListPostRequest request,
                                                                         EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/directory/v3/devices/list", subject, request, true, null);
        DirectoryV3DevicesListPostResponseDevice[] devices =
                decryptResponse(response, DirectoryV3DevicesListPostResponseDevice[].class);
        return new DirectoryV3DevicesListPostResponse(Arrays.asList(devices));
    }

    @Override
    public void directoryV3devicesDelete(DirectoryV3DevicesDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/devices", subject, request, true, null);
    }

    @Override
    public DirectoryV3SessionsListPostResponse directoryV3SessionsListPost(DirectoryV3SessionsListPostRequest request,
                                                                           EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("POST", "/directory/v3/sessions/list", subject, request, true, null);
        DirectoryV3SessionsListPostResponseSession[] sessions =
                decryptResponse(response, DirectoryV3SessionsListPostResponseSession[].class);
        return new DirectoryV3SessionsListPostResponse(Arrays.asList(sessions));
    }

    @Override
    public void directoryV3SessionsDelete(DirectoryV3SessionsDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/sessions", subject, request, true, null);
    }

    @Override
    @Deprecated
    public ServerSentEvent handleServerSentEvent(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        return handleServerSentEvent(headers, null, null, body);
    }

    @Override
    public ServerSentEvent handleServerSentEvent(Map<String, List<String>> headers, String method, String path, String body)
        throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException,
            CryptographyError, NoKeyFoundException {

        ServerSentEvent response;
        HeaderGroup headerGroup = new HeaderGroup();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String item : entry.getValue()) {
                headerGroup.addHeader(new BasicHeader(entry.getKey(), item));
            }
        }
        String jwt = headerGroup.getFirstHeader(IOV_JWT_HEADER).getValue();
        try {
            JWTClaims jwtClaims = validateJWT(null, jwt);
            if (method != null && !method.equals(jwtClaims.getMethod())) {
                throw new JWTError("JWT request method does not match the method provided", null);
            }
            if (path != null && !path.equals(jwtClaims.getPath())) {
                throw new JWTError("JWT request path does not match the path provided", null);
            }

            ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
            bodyStream.write(body.getBytes());
            verifyContentHash(jwtClaims, bodyStream, "request");

            if (headerGroup.getFirstHeader("Content-Type").getValue().startsWith("application/jose")) {
                // Auths response is encrypted
                final EntityIdentifier requestingEntity = EntityIdentifier.fromString(jwtClaims.getAudience());
                final String encryptionKeyId = jweService.getHeaders(body).get("kid");
                final RSAPrivateKey privateKey = entityKeyMap.getKey(requestingEntity, encryptionKeyId);
                final String decrypted = jweService.decrypt(body, privateKey);
                final ServerSentEventAuthorizationResponseCore core =
                        objectMapper.readValue(decrypted, ServerSentEventAuthorizationResponseCore.class);
                if (core.getAuthJwe() != null) {
                    final String decryptedDeviceResponse =
                            jweService.decrypt(core.getAuthJwe(), privateKey);
                    final ServiceV3AuthsGetResponseDeviceJWE deviceResponse =
                            objectMapper.readValue(decryptedDeviceResponse, ServiceV3AuthsGetResponseDeviceJWE.class);
                    response = new ServerSentEventAuthorizationResponse(
                            requestingEntity,
                            EntityIdentifier.fromString(jwtClaims.getSubject()).getId(),
                            core.getServiceUserHash(),
                            core.getOrgUserHash(),
                            core.getUserPushId(),
                            deviceResponse.getAuthorizationRequestId(),
                            "AUTHORIZED".equals(deviceResponse.getType()),
                            deviceResponse.getDeviceId(),
                            deviceResponse.getServicePins(),
                            deviceResponse.getType(),
                            deviceResponse.getReason(),
                            deviceResponse.getDenialReason()
                    );
                } else {
                    final byte[] decryptedDeviceResponse =
                            crypto.decryptRSA(BASE_64.decode(core.getAuth().getBytes()), privateKey);
                    final ServiceV3AuthsGetResponseDevice deviceResponse =
                            objectMapper.readValue(decryptedDeviceResponse, ServiceV3AuthsGetResponseDevice.class);
                    response = new ServerSentEventAuthorizationResponse(
                            requestingEntity,
                            EntityIdentifier.fromString(jwtClaims.getSubject()).getId(),
                            core.getServiceUserHash(),
                            core.getOrgUserHash(),
                            core.getUserPushId(),
                            deviceResponse.getAuthorizationRequestId(),
                            deviceResponse.getResponse(),
                            deviceResponse.getDeviceId(),
                            deviceResponse.getServicePins(),
                            null,
                            null,
                            null
                    );
                }
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
            throw new InvalidRequestException("Unable to map the decrypted body JSON to a Map<String, Object>!", e,
                    null);
        } catch (IOException e) {
            throw new InvalidRequestException("Unable to read the body due to an I/O error!", e, null);
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidRequestException("Invalid hash algorithm", e, null);
        }
        return response;
    }

    @Override
    public OrganizationV3DirectoriesPostResponse organizationV3DirectoriesPost(
            OrganizationV3DirectoriesPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        HttpResponse response = getHttpResponse("POST", "/organization/v3/directories", subject, request, true, null);
        return decryptResponse(response, OrganizationV3DirectoriesPostResponse.class);
    }

    @Override
    public void organizationV3DirectoriesPatch(OrganizationV3DirectoriesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/organization/v3/directories", subject, request, true, null);
    }

    @Override
    public OrganizationV3DirectoriesGetResponse organizationV3DirectoriesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        HttpResponse response = getHttpResponse("GET", "/organization/v3/directories", subject, null, true, null);
        OrganizationV3DirectoriesGetResponseDirectory[] directories =
                decryptResponse(response, OrganizationV3DirectoriesGetResponseDirectory[].class);
        return new OrganizationV3DirectoriesGetResponse(Arrays.asList(directories));
    }

    @Override
    public OrganizationV3DirectoriesListPostResponse organizationV3DirectoriesListPost(
            OrganizationV3DirectoriesListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/directories/list", subject, request, true, null);
        return new OrganizationV3DirectoriesListPostResponse(Arrays.asList(
                decryptResponse(httpResponse, OrganizationV3DirectoriesListPostResponseDirectory[].class)));
    }

    @Override
    public KeysPostResponse organizationV3DirectoryKeysPost(OrganizationV3DirectoryKeysPostRequest request,
                                                            EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/directory/keys", subject, request, true, null);
        return decryptResponse(httpResponse, KeysPostResponse.class);
    }

    @Override
    public KeysListPostResponse organizationV3DirectoryKeysListPost(OrganizationV3DirectoryKeysListPostRequest request,
                                                                    EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/directory/keys/list", subject, request, true, null);
        return new KeysListPostResponse(
                Arrays.asList(decryptResponse(httpResponse, KeysListPostResponsePublicKey[].class)));
    }

    @Override
    public void organizationV3DirectoryKeysPatch(OrganizationV3DirectoryKeysPatchRequest request,
                                                 EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/organization/v3/directory/keys", subject, request, true, null);
    }

    @Override
    public void organizationV3DirectoryKeysDelete(OrganizationV3DirectoryKeysDeleteRequest request,
                                                  EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/organization/v3/directory/keys", subject, request, true, null);
    }

    @Override
    public OrganizationV3DirectorySdkKeysPostResponse organizationV3DirectorySdkKeysPost(
            OrganizationV3DirectorySdkKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/directory/sdk-keys", subject, request, true, null);
        return decryptResponse(httpResponse, OrganizationV3DirectorySdkKeysPostResponse.class);
    }

    @Override
    public void organizationV3DirectorySdkKeysDelete(OrganizationV3DirectorySdkKeysDeleteRequest request,
                                                     EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/organization/v3/directory/sdk-keys", subject, request, true, null);
    }

    @Override
    public OrganizationV3DirectorySdkKeysListPostResponse organizationV3DirectorySdkKeysListPost(
            OrganizationV3DirectorySdkKeysListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/directory/sdk-keys/list", subject, request, true, null);
        final UUID[] sdkKeys = decryptResponse(httpResponse, UUID[].class);

        return new OrganizationV3DirectorySdkKeysListPostResponse(Arrays.asList(sdkKeys));
    }

    @Override
    public ServicesPostResponse organizationV3ServicesPost(ServicesPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/services", subject, request, true, null);
        return decryptResponse(httpResponse, ServicesPostResponse.class);
    }

    @Override
    public void organizationV3ServicesPatch(ServicesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/organization/v3/services", subject, request, true, null);
    }

    @Override
    public ServicesListPostResponse organizationV3ServicesListPost(ServicesListPostRequest request,
                                                                   EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/services/list", subject, request, true, null);
        return new ServicesListPostResponse(
                Arrays.asList(decryptResponse(httpResponse, ServicesListPostResponseService[].class)));
    }

    @Override
    public ServicesGetResponse organizationV3ServicesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("GET", "/organization/v3/services", subject, null, true, null);
        return new ServicesGetResponse(
                Arrays.asList(decryptResponse(httpResponse, ServicesGetResponseService[].class)));
    }

    @Override
    public KeysListPostResponse organizationV3ServiceKeysListPost(ServiceKeysListPostRequest request,
                                                                  EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/service/keys/list", subject, request, true, null);
        return new KeysListPostResponse(
                Arrays.asList(decryptResponse(httpResponse, KeysListPostResponsePublicKey[].class)));
    }

    @Override
    public KeysPostResponse organizationV3ServiceKeysPost(ServiceKeysPostRequest request,
                                                          EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/organization/v3/service/keys", subject, request, true, null);
        return decryptResponse(httpResponse, KeysPostResponse.class);
    }

    @Override
    public void organizationV3ServiceKeysPatch(ServiceKeysPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/organization/v3/service/keys", subject, request, true, null);
    }

    @Override
    public void organizationV3ServiceKeysDelete(ServiceKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/organization/v3/service/keys", subject, request, true, null);
    }

    @Override
    public void organizationV3ServicePolicyPut(ServicePolicyPutRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PUT", "/organization/v3/service/policy", subject, request, true, null);
    }

    @Override
    public ServicePolicy organizationV3ServicePolicyItemPost(ServicePolicyItemPostRequest request,
                                                             EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse response =
                getHttpResponse("POST", "/organization/v3/service/policy/item", subject, request, true, null);
        return decryptResponse(response, ServicePolicy.class);
    }

    @Override
    public void organizationV3ServicePolicyDelete(ServicePolicyDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/organization/v3/service/policy", subject, request, true, null);
    }

    @Override
    public ServicesPostResponse directoryV3ServicesPost(ServicesPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/directory/v3/services", subject, request, true, null);
        return decryptResponse(httpResponse, ServicesPostResponse.class);
    }

    @Override
    public void directoryV3ServicesPatch(ServicesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/directory/v3/services", subject, request, true, null);
    }

    @Override
    public ServicesListPostResponse directoryV3ServicesListPost(ServicesListPostRequest request,
                                                                EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/directory/v3/services/list", subject, request, true, null);
        return new ServicesListPostResponse(
                Arrays.asList(decryptResponse(httpResponse, ServicesListPostResponseService[].class)));
    }

    @Override
    public ServicesGetResponse directoryV3ServicesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("GET", "/directory/v3/services", subject, null, true, null);
        return new ServicesGetResponse(
                Arrays.asList(decryptResponse(httpResponse, ServicesGetResponseService[].class)));
    }

    @Override
    public KeysPostResponse directoryV3ServiceKeysPost(ServiceKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/directory/v3/service/keys", subject, request, true, null);
        return decryptResponse(httpResponse, KeysPostResponse.class);
    }

    @Override
    public KeysListPostResponse directoryV3ServiceKeysListPost(ServiceKeysListPostRequest request,
                                                               EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        final HttpResponse httpResponse =
                getHttpResponse("POST", "/directory/v3/service/keys/list", subject, request, true, null);
        return new KeysListPostResponse(
                Arrays.asList(decryptResponse(httpResponse, KeysListPostResponsePublicKey[].class)));
    }

    @Override
    public void directoryV3ServiceKeysDelete(ServiceKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/service/keys", subject, request, true, null);
    }

    @Override
    public void directoryV3ServiceKeysPatch(ServiceKeysPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PATCH", "/directory/v3/service/keys", subject, request, true, null);
    }

    @Override
    public void directoryV3ServicePolicyPut(ServicePolicyPutRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("PUT", "/directory/v3/service/policy", subject, request, true, null);
    }

    @Override
    public ServicePolicy directoryV3ServicePolicyItemPost(ServicePolicyItemPostRequest request,
                                                          EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        HttpResponse response =
                getHttpResponse("POST", "/directory/v3/service/policy/item", subject, request, true, null);
        return decryptResponse(response, ServicePolicy.class);
    }

    @Override
    public void directoryV3ServicePolicyDelete(ServicePolicyDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException {
        getHttpResponse("DELETE", "/directory/v3/service/policy", subject, request, true, null);
    }

    protected HttpResponse getHttpResponse(
            String method, String path, EntityIdentifier subjectEntity, Object transportObject, boolean signRequest,
            List<Integer> httpStatusCodeWhiteList)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidCredentialsException {

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
            if (response == null) {
                throw new InvalidResponseException("No response returned from HTTP client", null, null);
            }
            response = new ReplayHttpResponse(response);
            if (signRequest) {
                validateResponseJWT(response, requestId);
            }
            throwForStatus(response, httpStatusCodeWhiteList == null ? new ArrayList<Integer>() : httpStatusCodeWhiteList);
            return response;
        } catch (IOException e) {
            throw new CommunicationErrorException("An I/O Error Occurred", e, null);
        }
    }

    private void throwForStatus(HttpResponse response, List<Integer> httpStatusCodeWhiteList)
            throws CommunicationErrorException, InvalidResponseException, CryptographyError {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (httpStatusCodeWhiteList.contains(statusCode)) {
            logger.debug("Did not throw for status as it was in white list");
        } else if (statusCode == 400 || statusCode == 409) {
            final HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null || httpEntity.getContentLength() == 0) {
                throw new InvalidRequestException(
                        response.getStatusLine().getReasonPhrase(),
                        null,
                        "HTTP-" + statusCode
                );
            } else {
                com.iovation.launchkey.sdk.transport.domain.Error error = decryptResponse(response, Error.class);
                throw InvalidRequestException.fromError(error);
            }
        } else if (!(statusCode >= 200 && statusCode < 300)) {
            String message = "HTTP Error: [" + String.valueOf(statusCode)
                    + "] " + response.getStatusLine().getReasonPhrase();
            throw CommunicationErrorException.fromStatusCode(statusCode, message);
        }
    }

    protected <T> T decryptResponse(HttpResponse response, Class<T> type)
            throws InvalidResponseException, CommunicationErrorException, CryptographyError {
        ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
        try {
            response.getEntity().writeTo(encrypted);
            String json = jweService.decrypt(encrypted.toString());
            return objectMapper.readValue(json, type);
        } catch (JsonParseException | JsonMappingException e) {
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
        } catch (JsonParseException | JsonMappingException e) {
            throw new InvalidResponseException("Unable to parse response as JSON", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("AN IO Error Occurred", e, null);
        }
    }

    private void validateResponseJWT(HttpResponse response, String expectedTokenId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidCredentialsException {
        try {

            final String jwt = getJWT(response);
            final JWTClaims claims = validateJWT(expectedTokenId, jwt);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (entity != null) entity.writeTo(stream);

            if (claims.getStatusCode() != response.getStatusLine().getStatusCode())
                throw new JWTError("Status code of response content does not match JWT response status code", null);

            verifyContentHash(claims, stream, "response");

            if ((response.containsHeader("Location") &&
                            !response.getFirstHeader("Location").getValue().equals(claims.getLocationHeader()))
                    || (!response.containsHeader("Location") && claims.getLocationHeader() != null)
            ) throw new JWTError("Location header of response content does not match JWT response location", null);

            if ((response.containsHeader("Cache-Control") &&
                    !response.getFirstHeader("Cache-Control").getValue().equals(claims.getCacheControlHeader()))
                || (!response.containsHeader("Cache-Control") && claims.getCacheControlHeader() != null)
            ) throw new JWTError("Cache-Control header of response content does not match JWT response cache", null);


        } catch (JWTError jwtError) {
            throw new InvalidResponseException("Invalid JWT in response!", jwtError, null);
        } catch (NoSuchAlgorithmException | IOException e ) {
            throw new CryptographyError("An error occurred validating the body hash", e);
        }
    }

    private void verifyContentHash(JWTClaims claims, ByteArrayOutputStream stream, String type) throws NoSuchAlgorithmException, JWTError {
        String contentHashAlgorithm = claims.getContentHashAlgorithm();
        if (stream.size() > 0 && contentHashAlgorithm != null) {
            String hash;
            if (claims.getContentHashAlgorithm().equals("S256")) {
                hash = Hex.encodeHexString(crypto.sha256(stream.toByteArray()));
            } else if (claims.getContentHashAlgorithm().equals("S384")) {
                hash = Hex.encodeHexString(crypto.sha384(stream.toByteArray()));
            } else if (claims.getContentHashAlgorithm().equals("S512")) {
                hash = Hex.encodeHexString(crypto.sha512(stream.toByteArray()));
            } else {
                throw new JWTError("Hash of " + type + " content uses unsupported algorithm of " +
                        claims.getContentHashAlgorithm(), null);
            }
            if (claims.getContentHash() == null || !hash.equals(claims.getContentHash()))
                throw new JWTError("Hash of " + type + " content does not match JWT " + type + " hash", null);
        } else if (stream.size() > 0 && claims.getContentHashAlgorithm() == null) {
            throw new JWTError("No content hash algorithm found in JWT and there was content!", null);
        } else if (stream.size() == 0 && claims.getContentHashAlgorithm() != null) {
            throw new JWTError("Content hash algorithm found in JWT and there was no content!", null);
        } else if (stream.size() == 0 && claims.getContentHash() != null) {
            throw new JWTError("Content hash found in JWT and there was no content!", null);
        }
    }

    private JWTClaims validateJWT(String expectedTokenId, String jwt)
            throws JWTError, MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError,
            InvalidCredentialsException {
        String keyId = jwtService.getJWTData(jwt).getKeyId();
        return jwtService.decode(
                getPublicKeyData(keyId).getKey(), issuer.toString(), expectedTokenId, getCurrentDate(), jwt);
    }

    private String getJWT(HttpResponse response) {
        return response.getFirstHeader(IOV_JWT_HEADER).getValue();
    }


    private PublicKeyData getCurrentPublicKeyData()
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidCredentialsException {
        if (currentPublicKeyDataExpires == null || currentPublicKeyDataExpires.before(new Date())) {
            setCurrentPublicKeyData();
        }
        return currentPublicKeyData;

    }

    private synchronized void setCurrentPublicKeyData()
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, CryptographyError,
            InvalidCredentialsException {
        currentPublicKeyData = getPublicKeyData(null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, currentPublicKeyTTL);
        currentPublicKeyDataExpires = calendar.getTime();
    }

    private PublicKeyData getPublicKeyData(String fingerprint)
            throws MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError,
            InvalidCredentialsException {
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
                publicKeyCache.put("LaunchKeyPublicKey:" + apiKey.getPublicKeyFingerprint(), apiKey.getPublicKey());
            } catch (CacheException e) {
                logger.error("Unable to cache public key. This will degrade performance.", e);
            }
        }
        return publicKeyData;
    }

    private Date getCurrentDate()
            throws MarshallingError, InvalidResponseException, CommunicationErrorException, CryptographyError,
            InvalidCredentialsException {
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
            InvalidCredentialsException {
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
