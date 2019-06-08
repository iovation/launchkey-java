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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.jwe.JWEService;
import com.iovation.launchkey.sdk.crypto.jwt.JWTService;

import java.security.PublicKey;
import java.util.Date;

class ApiRequestBuilderFactory {
    private final String issuer;
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    private final Crypto crypto;
    private final JWTService jwtService;
    private final JWEService jweService;

    ApiRequestBuilderFactory(String issuer, String baseUrl, ObjectMapper objectMapper, Crypto crypto,
                             JWTService jwtService, JWEService jweService) {
        this.issuer = issuer;
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.crypto = crypto;
        this.jwtService = jwtService;
        this.jweService = jweService;
    }

    public ApiRequestBuilder create(PublicKey publicKey, String publicKeyFingerprint, Date currentDate) {
        return new ApiRequestBuilder(publicKey, publicKeyFingerprint, currentDate, issuer, baseUrl, objectMapper, jwtService, jweService, crypto);
    }
}

