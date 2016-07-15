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

package com.launchkey.sdk.service.application.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.domain.auth.*;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.error.NoLinkedDevicesException;
import com.launchkey.sdk.error.NoSuchUserException;
import com.launchkey.sdk.service.ping.PingService;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.*;
import com.launchkey.sdk.transport.v1.domain.Policy.Factor;
import com.launchkey.sdk.transport.v1.domain.Policy.Policy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Auth service based on an API V1 transport
 */
public class V1AuthService implements AuthService {

    protected final Transport transport;
    protected final Crypto crypto;
    protected final long appKey;
    protected final String secretKey;
    protected final Base64 base64 = new Base64(0);
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Log log;
    protected final PlatformDateFormat platformDateFormat = new PlatformDateFormat();
    protected final PingService pingService;

    /**
     * @param transport Transport service
     * @param crypto Crypto service
     * @param pingService Ping service
     * @param appKey Application key for the requests
     * @param secretKey Secret key for the requests
     */
    public V1AuthService(
            Transport transport,
            Crypto crypto,
            PingService pingService,
            long appKey,
            String secretKey
    ) {
        this.transport = transport;
        this.secretKey = secretKey;
        this.pingService = pingService;
        this.appKey = appKey;
        this.crypto = crypto;
        this.log = LogFactory.getLog(getClass());
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Override public String authorize(String username, String context) throws BaseException {
        return authorize(username, context, null);
    }

    @Override public String authorize(String username, String context, AuthPolicy policy) throws BaseException {
        return auth(username, context, policy, false);
    }

    @Override
    public String authorize(String username) throws BaseException {
        return authorize(username, null, null);
    }

    @Override public String login(String username, String context) throws BaseException {
        return login(username, context, null);
    }

    @Override public String login(String username, String context, AuthPolicy policy) throws BaseException {
        return auth(username, context, policy, true);
    }

    @Override
    public String login(String username) throws BaseException {
        return login(username, null, null);
    }

    @Override
    public void logout(String authRequestId) throws BaseException {
        byte[] secret = getSecret();
        try {
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
        } catch (ApiException e) {
            processLegacyException(e);
        }
    }

    @Override
    public AuthResponse getAuthResponse(String authRequestId) throws BaseException {
        AuthResponse authResponse = null;
        byte[] secret = getSecret();
        PollResponse response = null;
        try {
            response = transport.poll(
                    new PollRequest(
                            authRequestId,
                            appKey,
                            base64.encodeAsString(secret),
                            base64.encodeAsString(crypto.sign(secret))
                    )
            );
        } catch (ApiException e) {
            processLegacyException(e);
        }
        if (response != null) {
            byte[] authJSON = crypto.decryptRSA(base64.decode(response.getAuth()));
            try {
                Auth auth = objectMapper.readValue(authJSON, Auth.class);
                if (!authRequestId.equals(auth.getAuthRequestId())) {
                    throw new InvalidResponseException(
                            "Requested auth request ID " + authRequestId + " did not match responses auth request ID " +
                                    auth.getAuthRequestId(),
                            null,
                            null
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
                throw new InvalidResponseException("Error reading auth from poll response", e, null);
            } catch (ApiException e) {
                processLegacyException(e);
            }
        }
        return authResponse;
    }

    @Override
    public CallbackResponse handleCallback(
            Map<String, String> callbackData, int signatureTimeThreshold
    ) throws BaseException {
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
                            null,
                            null
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
                throw new InvalidCallbackException("Unable to parse auth data", e, null);
            } catch (ApiException e) {
                processLegacyException(e);
                throw new BaseException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
            }
        } else if (callbackData.containsKey("deorbit") && callbackData.containsKey("signature")) {
            if (!crypto.verifySignature(
                    base64.decode(callbackData.get("signature")),
                    callbackData.get("deorbit").getBytes(),
                    pingService.getPublicKey()
            )
                    ) {
                throw new InvalidSignatureException("Invalid signature", null, null);
            }

            try {
                LogoutCallbackResponse cbr = objectMapper.readValue(
                        callbackData.get("deorbit"),
                        LogoutCallbackResponse.class
                );
                // Reduce current time to the second as seconds are hte precision of "Platform Time"
                long currentTime = new Date().getTime() / 1000 * 1000;
                if (cbr.getLogoutRequested().getTime() + (signatureTimeThreshold * 1000) < currentTime) {
                    throw new InvalidCallbackException("Message has expired", null, null);
                }
                callbackResponse = cbr;
            } catch (IOException e) {
                throw new InvalidCallbackException("Unable to parse deorbit data", e, null);
            }
        } else {
            throw new InvalidCallbackException("Callback data was insufficient to process", null, null);
        }
        return callbackResponse;
    }

    /**
     * Handle a callback request
     *
     * @param callbackData Callback data received
     * @return parsed callback
     * @throws BaseException when an error occurs handling the callback
     */
    public CallbackResponse handleCallback(Map<String, String> callbackData) throws BaseException {
        return handleCallback(callbackData, 300);
    }

    private String auth(String username, String context, AuthPolicy authPolicy, boolean session) throws BaseException {
        if (context != null && context.length() > 400) {
            throw new IllegalArgumentException("Context value cannot be more than 400 characters");
        }

        Policy policy = getPolicy(authPolicy);

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

        AuthsResponse response;
        try {
            response = transport.auths(request);
        } catch (ApiException e) {
            processLegacyException(e);
            throw new BaseException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        }
        return response.getAuthRequestId();
    }

    private Policy getPolicy(AuthPolicy authPolicy) {
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
        return policy;
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

    private byte[] getSecret() throws BaseException {

        try {
            String json = objectMapper.writeValueAsString(new Object() {
                public final String secret = secretKey;
                public final String stamped = platformDateFormat.format(new Date());
            });
            return crypto.encryptRSA(json.getBytes(), pingService.getPublicKey());
        } catch (JsonProcessingException e) {
            throw new BaseException("Unable to create JSON from secret key", e, null);
        }
    }

    private static void processLegacyException(ApiException e) throws BaseException {
        if (e instanceof com.launchkey.sdk.service.error.InvalidRequestException) {
            throw new com.launchkey.sdk.error.InvalidRequestException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } else if (e instanceof com.launchkey.sdk.service.error.InvalidCredentialsException) {
            throw new InvalidCredentialsException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } else if (e instanceof com.launchkey.sdk.service.error.NoSuchUserException) {
            throw new NoSuchUserException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } else if (e instanceof com.launchkey.sdk.service.error.NoPairedDevicesException) {
            throw new NoLinkedDevicesException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } else if (e instanceof com.launchkey.sdk.service.error.InvalidSignatureException) {
            throw new InvalidSignatureException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } else if (e instanceof com.launchkey.sdk.service.error.RateLimitExceededException) {
            throw new RateLimitExceededException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        }
    }
}
