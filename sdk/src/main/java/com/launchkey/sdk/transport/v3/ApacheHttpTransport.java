/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v3;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.jwe.JWEFailure;
import com.launchkey.sdk.crypto.jwe.JWEService;
import com.launchkey.sdk.crypto.jwt.JWTClaims;
import com.launchkey.sdk.crypto.jwt.JWTError;
import com.launchkey.sdk.crypto.jwt.JWTService;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.service.ping.PingService;
import com.launchkey.sdk.service.token.TokenIdService;
import com.launchkey.sdk.transport.v3.domain.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApacheHttpTransport implements Transport {
    public static final String AUTH_HEADER_KEY = "Authorization";
    private final HttpClient client;
    private final String platformId;
    private final JWTService jwtService;
    private final JWEService jweService;
    private final PingService pingService;
    private final ObjectMapper objectMapper;
    private final Crypto crypto;
    private final URIBuilder uriBuilder;
    private final String baseUrl;
    private final TokenIdService tokenIdService;

    public ApacheHttpTransport(
            HttpClient client, String baseUrl, String platformId, JWTService jwtService,
            JWEService jweService, Crypto crypto, PingService pingService, TokenIdService tokenIdService
    ) {
        try {
            this.uriBuilder = new URIBuilder(baseUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid Platform Base URL specified", e);
        }
        this.client = client;
        this.baseUrl = baseUrl;
        this.platformId = platformId;
        this.jweService = jweService;
        this.jwtService = jwtService;
        this.crypto = crypto;
        this.pingService = pingService;
        this.tokenIdService = tokenIdService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WhiteLabelDeviceAddResponse whiteLabelUserDeviceAdd(
            WhiteLabelDeviceAddRequest request
    ) throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException, InvalidResponseException, CommunicationErrorException, InvalidCredentialsException {
        return processRequest(
                request,
                HttpPost.METHOD_NAME,
                "/organization/v3/whitelabel/user/devices",
                WhiteLabelDeviceAddResponse.class
        );
    }

    @Override public void whiteLabelUserDeviceDelete(WhiteLabelDeviceDeleteRequest request) throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException, InvalidResponseException, CommunicationErrorException, InvalidCredentialsException {
        processRequest(
                request,
                HttpDelete.METHOD_NAME,
                "/organization/v3/whitelabel/user/devices",
                null
        );
    }

    @Override public Device[] whiteLabelUserDeviceList(WhiteLabelDeviceListRequest request) throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException, InvalidResponseException, CommunicationErrorException, InvalidCredentialsException {
        return processRequest(
                request,
                HttpPost.METHOD_NAME,
                "/organization/v3/whitelabel/user/devices/list",
                Device[].class
        );
    }


    protected <T extends Object> T processRequest(
            Request request, String method, String path, Class<T> responseType
    ) throws InvalidStateException, PlatformErrorException, InvalidResponseException, InvalidRequestException, UnknownEntityException, CommunicationErrorException, InvalidCredentialsException {
        HttpResponse response;
        T result;
        String nonce = tokenIdService.getTokenId();
        try {
            String data = objectMapper.writeValueAsString(request);
            String jwe = jweService.encrypt(data);
            HttpEntity entity = getRequestEntity(jwe);

            String contentHashAlgorithm;
            String contentHash;
            if (data == null || data.trim().isEmpty()) {
                contentHashAlgorithm = null;
                contentHash = null;
            } else {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                entity.writeTo(os);
                contentHash = Hex.encodeHexString(crypto.sha256(os.toByteArray()));
                contentHashAlgorithm = "SHA256";
            }
            String jwt = jwtService.encode(nonce, method, path, contentHashAlgorithm, contentHash);
            String authHeader = "Bearer " + jwt;
            URI uri = uriBuilder.setPath(path).build();
            HttpUriRequest httpRequest = RequestBuilder.create(method)
                    .addHeader(AUTH_HEADER_KEY, authHeader)
                    .setUri(uri)
                    .setEntity(entity)
                    .build();

            response = client.execute(httpRequest);
            result = getResponse(response, nonce, responseType);
        } catch (ClientProtocolException e) {
            throw new CommunicationErrorException("A protocol error occurred", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("An I/O error occurred", e, null);
        } catch (JWTError e) {
            throw new InvalidStateException("Unable to encode JWT", e, null);
        } catch (URISyntaxException e) {
            throw new InvalidStateException(
                    "Unable to construct a valid URI with the Platform base URL: ".concat(baseUrl),
                    e, null
            );
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidStateException("Required hashing algorithm is not supported!", e, null);
        } catch (JWEFailure e) {
            throw new InvalidStateException("Unable to encrypt JWE", e, null);
        }
        return result;
    }

    private <T extends Object> T getResponse(
            HttpResponse response, String nonce, Class<T> responseType
    ) throws IOException, InvalidResponseException, PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidCredentialsException, JWEFailure {

        // Check the response and handle the error if it is an erro response
        throwExceptionForError(response);
        JWTClaims claims = getJwtClaims(response);
        validateClaims(nonce, claims, response);
        T responseEntity = generateResponseEntity(response, responseType);
        return responseEntity;
    }

    private <T extends Object> T generateResponseEntity(
            HttpResponse response, Class<T> responseType
    ) throws IOException, InvalidResponseException, JWEFailure {
        T returnValue;
        try {
            returnValue = parseBody(response.getEntity(), responseType, true);
        } catch (JsonParseException e) {
            throw new InvalidResponseException("Unable to parse response", e, null);
        } catch (JsonMappingException e) {
            throw new InvalidResponseException("Unable to map attributes of response", e, null);
        }
        return returnValue;
    }

    private void validateClaims(String nonce, JWTClaims claims, HttpResponse response) throws InvalidResponseException {
        // "tid" is the nonce, if it does not match from the request, invalid credentials
        if (claims.getTokenId() == null || !claims.getTokenId().equals(nonce)) {
            throw new InvalidResponseException("Response token ID (tid) does not match request value", null, null);
            // If the issuer does not match header, invalid credentials
        }
    }

    private JWTClaims getJwtClaims(HttpResponse response) throws InvalidResponseException {
        // Get the auth header from the response
        Header authHeader = response.getFirstHeader(AUTH_HEADER_KEY);
        if (authHeader == null) {
            throw new InvalidResponseException("No Authorization header in response", null, null);
        }

        // Parse the header into segments
        String jwtText;
        try {
            Matcher authHeaderMatcher = Pattern.compile("^Bearer (.*)$").matcher(authHeader.getValue());
            authHeaderMatcher.find();
            jwtText = authHeaderMatcher.group(1);
        } catch (RuntimeException e) {
            throw new InvalidResponseException("Invalid authorization header format", null, null);
        }
        // Get the issuer from the first two segments
        // Decode the JWT (3rd segment)
        JWTClaims claims;
        try {
            claims = jwtService.decode(jwtText);
        } catch (JWTError jwtError) {
            // If the JWT fails validation, throw invalid credentials
            throw new InvalidResponseException("Validation of the JWT failed", jwtError, null);
        }
        return claims;
    }

    private void throwExceptionForError(
            HttpResponse response
    ) throws InvalidRequestException, InvalidCredentialsException, UnknownEntityException, PlatformErrorException, InvalidResponseException {
        String errorCode = null;
        String errorMessage = null;
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        try {
            ErrorResponse errorResponse = parseBody(response.getEntity(), ErrorResponse.class, false);
            if (errorResponse != null) {
                errorCode = errorResponse.getMessageCode();
                errorMessage = errorResponse.getMessage();
            }
        } catch (IOException e) {
            // Intentionally left blank
        }

        if (errorCode == null) {
            errorCode = "HTTP-".concat(String.valueOf(statusCode));
            errorMessage = response.getStatusLine().getReasonPhrase();
        }

        if (statusCode == 400) {
            throw new InvalidRequestException(errorMessage, null, errorCode);
        } else if (statusCode == 401 || statusCode == 403) {
            throw new InvalidCredentialsException(errorMessage, null, errorCode);
        } else if (statusCode == 404) {
            throw new UnknownEntityException(errorMessage, null, errorCode);
        } else {
            throw new PlatformErrorException(errorMessage, null, errorCode);
        }
    }

    private <T extends Object> T parseBody(HttpEntity entity, Class<T> type, boolean isJWE) throws IOException, InvalidResponseException {
        T returnValue;
        if (type == null || entity.getContentLength() == 0) {
            returnValue = null;
        } else {
            InputStream content = entity.getContent();
            final char[] buffer = new char[2048];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(content);
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            String json;
            if (isJWE) {
                try {
                    json = jweService.decrypt(out.toString());
                } catch (JWEFailure jweFailure) {
                    throw new InvalidResponseException("Unable to parse JWE", jweFailure, null);
                }
            } else {
                json = out.toString();
            }
            returnValue = objectMapper.readValue(json, type);
        }
        return returnValue;

    }

    private HttpEntity getRequestEntity(String body) {
        return new StringEntity(
                body, ContentType.create("application/jwe")
        );
    }
}
