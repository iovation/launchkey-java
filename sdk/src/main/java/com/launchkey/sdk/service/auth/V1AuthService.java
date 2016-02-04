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

package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.V1ServiceAbstract;
import com.launchkey.sdk.service.error.InvalidCallbackException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.InvalidSignatureException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Auth service based on an API V1 transport
 */
public class V1AuthService extends V1ServiceAbstract implements AuthService {

    /**
     * Constructor
     *
     * @param transport Transport service
     * @param crypto Srypto service
     * @param pingResponseCache Ping Response Cahce service
     * @param rocketKey Rocket key for requests
     * @param secretKey Secret key for requests
     */
    public V1AuthService(
            Transport transport, Crypto crypto, PingResponseCache pingResponseCache, long rocketKey, String secretKey
    ) {
        super(transport, crypto, pingResponseCache, rocketKey, secretKey);
    }

    @Override
    public String authorize(String username) throws LaunchKeyException {
        return auth(username, false);
    }

    @Override
    public String login(String username) throws LaunchKeyException {
        return auth(username, true);
    }

    @Override
    public void logout(String authRequestId) throws LaunchKeyException {
        byte[] secret = getSecret();
        transport.logs(
                new LogsRequest(
                        "Revoke",
                        true,
                        authRequestId,
                        rocketKey,
                        base64.encodeAsString(secret),
                        base64.encodeAsString(crypto.sign(secret))
                )
        );
    }

    @Override
    public AuthResponse getAuthResponse(String authRequestId) throws LaunchKeyException {
        AuthResponse authResponse = null;
        byte[] secret = getSecret();
        PollResponse response = transport.poll(
                new PollRequest(
                        authRequestId,
                        rocketKey,
                        base64.encodeAsString(secret),
                        base64.encodeAsString(crypto.sign(secret))
                )
        );
        if (response != null) {
            byte[] authJSON = crypto.decryptRSA(base64.decode(response.getAuth()));
            try {
                Auth auth = objectMapper.readValue(authJSON, Auth.class);
                if (!authRequestId.equals(auth.getAuthRequestId())) {
                    throw new InvalidResponseException(
                            "Requested auth request ID " + authRequestId + " did not match responses auth request ID " +
                                    auth.getAuthRequestId(),
                            0
                    );
                }

                byte[] logsSecret = getSecret();
                transport.logs(
                        new LogsRequest(
                                "Authenticate",
                                auth.isAuthorized(),
                                authRequestId,
                                rocketKey,
                                base64.encodeAsString(logsSecret),
                                base64.encodeAsString(crypto.sign(logsSecret))
                        )
                );

                authResponse = new AuthResponse(
                        auth.getAuthRequestId(),
                        auth.isAuthorized(),
                        response.getUserHash(),
                        response.getOrganizationUser(),
                        response.getUserPushId(),
                        auth.getDeviceId()
                );
            } catch (IOException e) {
                throw new InvalidResponseException("Error reading auth from poll response", e);
            }
        }
        return authResponse;
    }

    @Override
    public CallbackResponse handleCallback(
            Map<String, String> callbackData, int signatureTimeThreshold
    ) throws LaunchKeyException {
        CallbackResponse callbackResponse;
        if (callbackData.containsKey("auth") &&
                callbackData.containsKey("user_hash") &&
                callbackData.containsKey("auth_request")
                ) {
            try {
                byte[] authJSON = crypto.decryptRSA(base64.decode(callbackData.get("auth")));
                Auth auth = objectMapper.readValue(authJSON, Auth.class);
                String authRequestId = callbackData.get("auth_request");
                if (!authRequestId.equals(auth.getAuthRequestId())) {
                    throw new InvalidResponseException(
                            "Requested auth request ID " + authRequestId + " did not match responses auth request ID " +
                                    auth.getAuthRequestId(),
                            0
                    );
                }

                byte[] logsSecret = getSecret();
                transport.logs(
                        new LogsRequest(
                                "Authenticate",
                                auth.isAuthorized(),
                                authRequestId,
                                rocketKey,
                                base64.encodeAsString(logsSecret),
                                base64.encodeAsString(crypto.sign(logsSecret))
                        )
                );

                callbackResponse = new AuthResponseCallbackResponse(
                        new AuthResponse(
                                auth.getAuthRequestId(),
                                auth.isAuthorized(),
                                callbackData.get("user_hash"),
                                callbackData.get("organization_user"),
                                callbackData.get("user_push_id"),
                                auth.getDeviceId()
                        )
                );
            } catch (IOException e) {
                throw new InvalidCallbackException("Unable to parse auth data", e);
            }
        } else if (callbackData.containsKey("deorbit") && callbackData.containsKey("signature")) {
            if (!crypto.verifySignature(
                    base64.decode(callbackData.get("signature")),
                    callbackData.get("deorbit").getBytes(),
                    getLaunchKeyPublicKey()
            )
                    ) {
                throw new InvalidSignatureException("Invalid signature", 0);
            }

            try {
                DeOrbitCallbackResponse cbr = objectMapper.readValue(
                        callbackData.get("deorbit"),
                        DeOrbitCallbackResponse.class
                );
                // Reduce current time to the second as seconds are hte precision of "LaunchKey Time"
                long currentTime = new Date().getTime() / 1000 * 1000;
                if (cbr.getDeOrbitTime().getTime() + (signatureTimeThreshold * 1000) < currentTime) {
                    throw new InvalidCallbackException("Message has expired");
                }
                callbackResponse = cbr;
            } catch (IOException e) {
                throw new InvalidCallbackException("Unable to parse deorbit data", e);
            }
        } else {
            throw new InvalidCallbackException("Callback data was insufficient to process");
        }
        return callbackResponse;
    }

    /**
     * Handle a callback request
     * @param callbackData Callback data received
     * @return parsed callback
     * @throws LaunchKeyException when an error occurs handling the callback
     */
    public CallbackResponse handleCallback(Map<String, String> callbackData) throws LaunchKeyException {
        return handleCallback(callbackData, 300);
    }

    private String auth(String username, boolean session) throws LaunchKeyException {
        byte[] secret = getSecret();
        AuthsRequest request = new AuthsRequest(
                username,
                rocketKey,
                base64.encodeAsString(secret),
                base64.encodeAsString(crypto.sign(secret)),
                session ? 1 : 0,
                1
        );

        AuthsResponse response = transport.auths(request);
        return response.getAuthRequestId();
    }


    /**
     * Transport object for de-serializing V1 auth responses
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Auth {
        /**
         * Unique identifier for the authentication request generated by the LaunchKey Engine API.
         */
        private final String authRequestId;

        /**
         * Identifier that is unique for a single user identifying the device the user utilized to respond to the
         * auth request.
         */
        private final String deviceId;

        /**
         * Has the request been authorized by the user.
         */
        private final boolean authorized;

        /**
         * @param authorized    The users response to the auth request.  True and False are authorized and declined
         *                      respectively.
         * @param authRequestId Unique identifier for the authentication request for which this is the response.
         * @param deviceId      Identifier for the device the User utilized to respond to the authentication request.
         *                      This value is unique to the User that responded to the authentication request and may
         *                      be duplicated in another User.
         */
        @JsonCreator
        public Auth(
                @JsonProperty("auth_request") String authRequestId,
                @JsonProperty("response") boolean authorized,
                @JsonProperty("device_id") String deviceId
        ) {
            this.authRequestId = authRequestId;
            this.deviceId = deviceId;
            this.authorized = authorized;
        }

        /**
         * Get the unique identifier for the authentication request for which this is the response.
         *
         * @return Authentication request ID
         */
        public String getAuthRequestId() {
            return authRequestId;
        }

        /**
         * Get the users response to the auth request.  True and False are authorized and declined respectively.
         *
         * @return Authorized response value
         */
        public boolean isAuthorized() {
            return authorized;
        }

        /**
         * Get the identifier for the device the User utilized to respond to the authentication request. This
         * value is unique to the User that responded to the authentication request and may be duplicated in another User.
         *
         * @return Device ID
         */
        public String getDeviceId() {
            return deviceId;
        }
    }
}
