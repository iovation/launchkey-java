/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.service.error.InvalidCallbackException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.InvalidSignatureException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.*;
import com.launchkey.sdk.transport.v1.domain.Policy.Factor;
import com.launchkey.sdk.transport.v1.domain.Policy.Policy;

import java.io.IOException;
import java.util.*;

/**
 * Auth service based on an API V1 transport
 */
public class V1AuthService extends V1ServiceAbstract implements AuthService {

    /**
     * Constructor
     *
     * @param transport         Transport service
     * @param crypto            Crypto service
     * @param pingResponseCache Ping Response Cache service
     * @param appKey            Application key for requests
     * @param secretKey         Secret key for requests
     */
    public V1AuthService(
            Transport transport, Crypto crypto, PingResponseCache pingResponseCache, long appKey, String secretKey
    ) {
        super(transport, crypto, pingResponseCache, appKey, secretKey);
    }

    @Override public String authorize(String username, String context) throws ApiException {
        return authorize(username, context, null);
    }

    @Override public String authorize(String username, String context, AuthPolicy policy) throws ApiException {
        return auth(username, context, policy, false);
    }

    @Override
    public String authorize(String username) throws ApiException {
        return authorize(username, null, null);
    }

    @Override public String login(String username, String context) throws ApiException {
        return login(username, context, null);
    }

    @Override public String login(String username, String context, AuthPolicy policy) throws ApiException {
        return auth(username, context, policy, true);
    }

    @Override
    public String login(String username) throws ApiException {
        return login(username, null, null);
    }

    @Override
    public void logout(String authRequestId) throws ApiException {
        byte[] secret = getSecret();
        transport.logs(
                new LogsRequest(
                        "Revoke",
                        true,
                        authRequestId,
                        appKey,
                        base64.encodeAsString(secret),
                        base64.encodeAsString(crypto.sign(secret))
                )
        );
    }

    @Override
    public AuthResponse getAuthResponse(String authRequestId) throws ApiException {
        AuthResponse authResponse = null;
        byte[] secret = getSecret();
        PollResponse response = transport.poll(
                new PollRequest(
                        authRequestId,
                        appKey,
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
                                appKey,
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
    ) throws ApiException {
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
                                appKey,
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
                    getPublicKey()
            )
                    ) {
                throw new InvalidSignatureException("Invalid signature", 0);
            }

            try {
                LogoutCallbackResponse cbr = objectMapper.readValue(
                        callbackData.get("deorbit"),
                        LogoutCallbackResponse.class
                );
                // Reduce current time to the second as seconds are hte precision of "Platform Time"
                long currentTime = new Date().getTime() / 1000 * 1000;
                if (cbr.getLogoutRequested().getTime() + (signatureTimeThreshold * 1000) < currentTime) {
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
     *
     * @param callbackData Callback data received
     * @return parsed callback
     * @throws ApiException when an error occurs handling the callback
     */
    public CallbackResponse handleCallback(Map<String, String> callbackData) throws ApiException {
        return handleCallback(callbackData, 300);
    }

    private String auth(String username, String context, AuthPolicy authPolicy, boolean session) throws ApiException {
        if (context != null && context.length() > 400) {
            throw new IllegalArgumentException("Context value cannot be more than 400 characters");
        }

        Policy policy;
        if (authPolicy == null) {
            policy = null;
        } else {
            List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>();
            minimumRequirements.add(new Policy.MinimumRequirement(
                    Policy.MinimumRequirement.Type.AUTHENTICATED,
                    authPolicy.getRequiredFactors(),
                    authPolicy.isKnowledgeFactorRequired() ? 1 : 0,
                    authPolicy.isInherenceFactorRequired() ? 1 : 0,
                    authPolicy.isPossessionFactorRequired() ? 1 : 0
            ));
            List<Factor> factors;
            if (authPolicy.getLocations().isEmpty()) {
                factors = new ArrayList<Factor>();
            } else {
                List<Factor.Location> locations = new ArrayList<Factor.Location>();
                for (AuthPolicy.Location location : authPolicy.getLocations()) {
                    locations.add(new Factor.Location(
                            location.getRadius(),
                            location.getLatitude(),
                            location.getLongitude()
                    ));
                }

                factors = new ArrayList<Factor>();
                factors.add(new Factor(
                        Factor.Type.GEOFENCE,
                        true,
                        Factor.Requirement.FORCED,
                        1,
                        new Factor.Attributes(locations)
                ));
            }
            policy = new Policy(minimumRequirements, factors);
        }

        byte[] secret = getSecret();
        AuthsRequest request = new AuthsRequest(
                username,
                appKey,
                base64.encodeAsString(secret),
                base64.encodeAsString(crypto.sign(secret)),
                session ? 1 : 0,
                1,
                context,
                policy
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
         * Unique identifier for the authentication request generated by the Platform API.
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
         * @param authorized    The users response to the auth request. True and False are authorized and declined
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
