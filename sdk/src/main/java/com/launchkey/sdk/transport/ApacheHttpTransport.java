/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.transport.domain.PublicPingGetResponse;
import com.launchkey.sdk.transport.domain.PublicPublicKeyGetResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.PublicKey;

public class ApacheHttpTransport implements Transport {
    private final HttpClient httpClient;
    private final Provider provider;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String audience;

    public ApacheHttpTransport(HttpClient httpClient, Provider provider, ObjectMapper objectMapper, String baseUrl, String audience) {

        this.httpClient = httpClient;
        this.provider = provider;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.audience = audience;
    }

    @Override
    public PublicPingGetResponse publicPingGet() throws CommunicationErrorException, InvalidResponseException {

        HttpResponse response = getHttpResponse("GET", "/public/v3/ping");
        return parseJsonResponse(response.getEntity(), PublicPingGetResponse.class);
    }

    @Override
    public PublicPublicKeyGetResponse publicPublicKeyGet(String publicKeyFingerprint) throws CommunicationErrorException, InvalidResponseException {
        String path = "/public/v3/public-key";
        if (publicKeyFingerprint != null) {
            path = path + "/" + publicKeyFingerprint;
        }
        HttpResponse response = getHttpResponse("GET", path);
        Header keyHeader = response.getFirstHeader("X-IOV-KEY-ID");
        if (keyHeader == null) {
            throw new InvalidResponseException("Public Key ID header X-IOV-KEY-ID not found in response", null, null);
        }
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new InvalidResponseException("No public key in response", null, null);
            }
            PublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(provider, EntityUtils.toString(entity));
            return new PublicPublicKeyGetResponse(publicKey, keyHeader.getValue());
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseException("Invalid public key in response.", e, null);
        } catch (IOException e) {
            throw new CommunicationErrorException("Error reading response", e, null);
        }
    }

    private HttpResponse getHttpResponse(String method, String path) throws CommunicationErrorException {
        try {
            HttpResponse response = httpClient.execute(
                    RequestBuilder.create(method).setUri(baseUrl.concat(path)).build()
            );
            throwForStatus(response);
            return response;
        } catch (IOException e) {
            throw new CommunicationErrorException("AN IO Error Occurred", e, null);
        }
    }

    private void throwForStatus(HttpResponse response) throws CommunicationErrorException {
        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
            throw new CommunicationErrorException(
                    "HTTP Error: [" + String.valueOf(response.getStatusLine().getStatusCode())
                            + "] " + response.getStatusLine().getReasonPhrase()
                    , null, null);
        }
    }

    private <T> T parseJsonResponse(HttpEntity entity, Class<T> valueType) throws InvalidResponseException, CommunicationErrorException {
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
}
