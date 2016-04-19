/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.domain.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache HTTP client based LaunchKey API v1
 */
public class ApacheHttpClientTransport implements Transport {

    private final Log log = LogFactory.getLog(getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient client;
    private final String baseUrl;
    private final Crypto crypto;
    private Base64 base64 = new Base64(0);

    /**
     * @param client HTTP Client to use for requests
     * @param baseUrl Base URL for the v1 API
     * @param crypto Crypto service to encrypt, decrypt and sign data
     */
    public ApacheHttpClientTransport(HttpClient client, String baseUrl, Crypto crypto) {
        this.client = client;
        this.crypto = crypto;
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }

        // Test the URI for good measure
        try {
            new URIBuilder(this.baseUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("baseURL must be be a valid parsable URI with URIBuilder", e);
        }
    }

    /**
     * @see Transport#ping(PingRequest)
     */
    public PingResponse ping(PingRequest request) throws ApiException {
        log.trace("Beginning ping request");
        PingResponse pingResponse;
        try {
            URIBuilder uriBuilder = new URIBuilder(getUrlForPath("/ping"));
            String dateStamp = request.getDateStamp();
            if (dateStamp != null) {
                uriBuilder.setParameter("date_stamp", dateStamp);
            }
            HttpGet ping = new HttpGet(uriBuilder.build());
            HttpResponse httpResponse = client.execute(ping);
            pingResponse = getTransportObjectFromResponse(PingResponse.class, httpResponse);
        } catch (ApiException e) {
            log.trace("Error encountered in response from LaunchKey engine", e);
            throw e;
        } catch (Exception e) {
            log.trace("Exception caught processing ping request", e);
            throw new ApiException("Exception caught processing ping request", e, 0);
        }
        log.trace("Completed ping request");
        log.trace(pingResponse);
        return pingResponse;
    }

    /**
     * @see Transport#auths(AuthsRequest)
     */
    public AuthsResponse auths(AuthsRequest request) throws ApiException {
        log.trace("Beginning auths request");
        AuthsResponse authsResponse;
        try {
            URI uri = new URIBuilder(getUrlForPath("/auths")).build();
            HttpPost auths = new HttpPost(uri);
            List<NameValuePair> formData = new ArrayList<NameValuePair>();
            formData.add(new BasicNameValuePair("username", request.getUsername()));
            formData.add(new BasicNameValuePair("app_key", Long.toString(request.getAppKey())));
            formData.add(new BasicNameValuePair("secret_key", request.getSecretKey()));
            formData.add(new BasicNameValuePair("signature", request.getSignature()));
            formData.add(new BasicNameValuePair("session", Integer.toString(request.getSession())));
            formData.add(new BasicNameValuePair("user_push_id", Integer.toString(request.getUserPushID())));
            if (request.getContext() != null && request.getContext().length() > 0) {
                formData.add(new BasicNameValuePair("context", request.getContext()));
            }
            if (request.getPolicy() != null) {
                formData.add(new BasicNameValuePair("policy", objectMapper.writeValueAsString(request.getPolicy())));
            }
            auths.setEntity(new UrlEncodedFormEntity(formData));
            HttpResponse httpResponse = client.execute(auths);
            authsResponse = getTransportObjectFromResponse(AuthsResponse.class, httpResponse);
        } catch (ApiException e) {
            log.trace("Error encountered in response from LaunchKey engine", e);
            throw e;
        } catch (Exception e) {
            log.trace("Exception caught processing auths request", e);
            throw new CommunicationErrorException("Exception caught processing auths request", e, 0);
        }
        log.trace("Completed auths request");
        log.trace(authsResponse);
        return authsResponse;
    }

    /**
     * @see Transport#poll(PollRequest)
     */
    public PollResponse poll(PollRequest request) throws ApiException {
        log.trace("Beginning poll request");
        PollResponse pollResponse;
        try {
            URI uri = new URIBuilder(getUrlForPath("/poll"))
                    .setParameter("auth_request", request.getAuthRequest())
                    .setParameter("app_key", Long.toString(request.getAppKey()))
                    .setParameter("secret_key", request.getSecretKey())
                    .setParameter("signature", request.getSignature())
                    .build();

            HttpGet poll = new HttpGet(uri);
            HttpResponse httpResponse = client.execute(poll);
            pollResponse = getTransportObjectFromResponse(PollResponse.class, httpResponse);
        } catch (ApiException e) {
            if (e.getCode() == 70403) {
                log.trace("Pending response received", e);
                pollResponse = null;
            } else {
                log.trace("Error encountered in response from LaunchKey engine", e);
                throw e;
            }
        } catch (Exception e) {
            log.trace("Exception caught processing poll request", e);
            throw new CommunicationErrorException("Exception caught processing poll request", e, 0);
        }
        log.trace("Completed poll request");
        log.trace(pollResponse);
        return pollResponse;
    }

    /**
     * @see Transport#logs(LogsRequest)
     */
    public void logs(LogsRequest request) throws ApiException {
        log.trace("Beginning logs request");
        try {
            URI uri = new URIBuilder(getUrlForPath("/logs")).build();
            HttpPut logs = new HttpPut(uri);

            List<NameValuePair> formData = new ArrayList<NameValuePair>();
            formData.add(new BasicNameValuePair("action", request.getAction()));
            formData.add(new BasicNameValuePair("status", request.getStatus()));
            formData.add(new BasicNameValuePair("auth_request", request.getAuthRequest()));
            formData.add(new BasicNameValuePair("app_key", Long.toString(request.getAppKey())));
            formData.add(new BasicNameValuePair("secret_key", request.getSecretKey()));
            formData.add(new BasicNameValuePair("signature", request.getSignature()));

            logs.setEntity(new UrlEncodedFormEntity(formData));

            HttpResponse httpResponse = client.execute(logs);
            validateResponse(httpResponse);
        } catch (ApiException e) {
            log.trace("Error encountered in response from LaunchKey engine", e);
            throw e;
        } catch (Exception e) {
            log.trace("Exception caught processing logs request", e);
            throw new CommunicationErrorException("Exception caught processing logs request", e, 0);
        }
        log.trace("Completed logs request");
    }

    /**
     * @see Transport#users(UsersRequest)
     */
    public UsersResponse users(UsersRequest request) throws ApiException {
        log.trace("Beginning users request");
        UsersResponse usersResponse;
        try {
            byte[] data = objectMapper.writeValueAsBytes(request);
            byte[] signature = base64.encode(crypto.sign(data));
            URI uri = new URIBuilder(getUrlForPath("/users"))
                    .setParameter("signature", new String(signature))
                    .build();

            HttpPost logs = new HttpPost(uri);
            logs.setHeader("Content-Type", "application/json");
            logs.setHeader("Accept", "application/json");
            logs.setEntity(new ByteArrayEntity(data));
            HttpResponse httpResponse = client.execute(logs);
            usersResponse = getTransportObjectFromResponse(UsersResponse.class, httpResponse);
            if (! usersResponse.isSuccessful()) {
                throw ApiException.fromCode(usersResponse.getMessageCode(), usersResponse.getMessage());
            }
        } catch (ApiException e) {
            log.trace("Error encountered in response from LaunchKey engine", e);
            throw e;
        } catch (Exception e) {
            log.trace("Exception processing users request", e);
            throw new CommunicationErrorException("Exception processing users request", e, 0);
        }
        log.trace("Completed users request");
        return usersResponse;
    }

    private String getUrlForPath(String path) {
        return baseUrl + path;
    }

    private <T> T getTransportObjectFromResponse(Class<T> clazz, HttpResponse httpResponse)
            throws ApiException {
        T response;
        validateResponse(httpResponse);

        try {
            response = objectMapper.readValue(httpResponse.getEntity().getContent(), clazz);
        } catch (Exception e) {
            throw new InvalidResponseException("Error parsing response body", e);
        }
        return response;
    }

    private void validateResponse(HttpResponse httpResponse) throws ApiException {
        int code = httpResponse.getStatusLine().getStatusCode();
        String message;
        if (code < 200 || code >= 300) {
            if (code >= 400 && code < 404) {
                try {
                    ErrorResponse errorResponse = ErrorResponse.factory(objectMapper.readTree(
                            httpResponse.getEntity().getContent()
                    ));
                    if (errorResponse.getMessageCode() != 0) code = errorResponse.getMessageCode();
                    message = (errorResponse.getMessage() == null) ? httpResponse.getStatusLine().getReasonPhrase() : errorResponse.getMessage();
                    throw ApiException.fromCode(code, message);
                } catch (ApiException e) {
                    throw e;
                } catch (Exception e) {
                    message = httpResponse.getStatusLine().getReasonPhrase();
                }
            } else {
                message = httpResponse.getStatusLine().getReasonPhrase();
            }
            throw new CommunicationErrorException(message, code);
        }
    }
}
