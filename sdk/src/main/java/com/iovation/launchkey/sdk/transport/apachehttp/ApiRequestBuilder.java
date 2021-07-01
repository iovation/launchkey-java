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

package com.iovation.launchkey.sdk.transport.apachehttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.jwe.JWEFailure;
import com.iovation.launchkey.sdk.crypto.jwe.JWEService;
import com.iovation.launchkey.sdk.crypto.jwt.JWTError;
import com.iovation.launchkey.sdk.crypto.jwt.JWTService;
import com.iovation.launchkey.sdk.error.CryptographyError;
import com.iovation.launchkey.sdk.error.MarshallingError;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;

class ApiRequestBuilder {
    private final PublicKey publicKey;
    private final String publicKeyFingerprint;
    private final String issuer;
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    private final JWTService jwtService;
    private final JWEService jweService;
    private final Crypto crypto;
    private final Date currentDate;
    private final String userAgentHeader;
    private String method = "GET";
    private String path = "/";
    private Object transportObject = null;
    private String subject = null;

    ApiRequestBuilder(PublicKey publicKey, String publicKeyFingerprint, Date currentDate, String issuer, String baseUrl, ObjectMapper objectMapper, JWTService jwtService, JWEService jweService, Crypto crypto) {
        this.publicKey = publicKey;
        this.publicKeyFingerprint = publicKeyFingerprint;
        this.currentDate = currentDate;
        this.issuer = issuer;
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.jweService = jweService;
        this.crypto = crypto;
        this.userAgentHeader = "JavaServiceSDK/" + getClass().getPackage().getImplementationVersion();
    }

    public ApiRequestBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public ApiRequestBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ApiRequestBuilder setTransportObject(Object transportObject) {
        this.transportObject = transportObject;
        return this;
    }

    public ApiRequestBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public HttpUriRequest build(String requestId) throws MarshallingError, CryptographyError {
        try {
            RequestBuilder rb = RequestBuilder.create(this.method)
                    .setUri(this.baseUrl + this.path);
            rb.addHeader("User-Agent", this.userAgentHeader);
            if (subject != null) {
               processRequestJOSE(rb, path, requestId);
            }
            return rb.build();
        } catch (JsonProcessingException e) {
            throw new MarshallingError("An error occurred attempting to marshall the transport object!", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyError("Could not create message hash for JWT!", e);
        } catch (IOException e) {
            throw new MarshallingError("Unable to create a request!", e);
        } catch (JWTError jwtError) {
            throw new MarshallingError("Unable to build a JWT!", jwtError);
        }
    }

    private void processRequestJOSE(RequestBuilder rb, String path, String requestId) throws CryptographyError, IOException, NoSuchAlgorithmException, JWTError {
        String hash;
        String func;
        if (transportObject != null) {
            HttpEntity entity = getEntity(transportObject);
            rb.setEntity(entity);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            entity.writeTo(stream);
            hash = Hex.encodeHexString(crypto.sha256(stream.toByteArray()));
            func = "S256";
        } else {
            hash = null;
            func = null;
        }
        String jwt = jwtService.encode(requestId, issuer, subject, currentDate, rb.getMethod(), path, func, hash);
        rb.setHeader("Authorization", "IOV-JWT " + jwt);
    }

    private HttpEntity getEntity(Object transportObject) throws JsonProcessingException, CryptographyError {
        String json = objectMapper.writeValueAsString(transportObject);
        byte[] encrypted;
        try {
            encrypted = jweService.encrypt(json, publicKey, publicKeyFingerprint, "application/json").getBytes();
        } catch (JWEFailure jweFailure) {
            throw new CryptographyError("An error occurred encrypting the request entity with JWE", jweFailure);
        }
        ByteArrayEntity entity = new ByteArrayEntity(encrypted);
        entity.setContentType("application/jose");
        return entity;
    }
}
