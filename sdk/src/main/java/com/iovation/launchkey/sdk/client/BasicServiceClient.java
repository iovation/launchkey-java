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

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.service.AuthorizationRequest;
import com.iovation.launchkey.sdk.domain.service.DenialReason;
import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("DuplicateThrows")
public class BasicServiceClient implements ServiceClient {
    private final EntityIdentifier serviceEntity;
    private final Transport transport;

    public BasicServiceClient(UUID serviceId, Transport transport) {
        this.serviceEntity = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, serviceId);
        this.transport = transport;
    }

    @Override
    public String authorize(String user, String context, AuthPolicy policy)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(user, context, policy, null, null).getId();
    }

    @Override
    public String authorize(String user, String context)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(user, context, null, null, null).getId();
    }

    @Override
    public String authorize(String user)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(user, null, null, null, null).getId();
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy,
                                                           String title, Integer ttl, String pushTitle, String pushBody,
                                                           List<DenialReason> denialReasons)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        com.iovation.launchkey.sdk.transport.domain.AuthPolicy requestPolicy;
        if (policy == null) {
            requestPolicy = null;
        } else {
            requestPolicy = new com.iovation.launchkey.sdk.transport.domain.AuthPolicy(
                    policy.getRequiredFactors(),
                    policy.isInherenceFactorRequired(),
                    policy.isKnowledgeFactorRequired(),
                    policy.isPossessionFactorRequired(),
                    policy.isJailbreakProtectionEnabled()
            );
            for (AuthPolicy.Location location : policy.getLocations()) {
                requestPolicy.addGeoFence(
                        location.getRadius(),
                        location.getLatitude(),
                        location.getLongitude()
                );
            }
        }

        List<com.iovation.launchkey.sdk.transport.domain.DenialReason> requestDenialReasons;
        if (denialReasons == null) {
            requestDenialReasons = null;
        } else {
            requestDenialReasons = new ArrayList<>();
            for (DenialReason denialreason : denialReasons) {
                requestDenialReasons.add(new com.iovation.launchkey.sdk.transport.domain.DenialReason(
                        denialreason.getId(), denialreason.getReason(), denialreason.isFraud()
                ));
            }
        }

        ServiceV3AuthsPostRequest request = new ServiceV3AuthsPostRequest(
                userIdentifier, requestPolicy, context, title, ttl, pushTitle, pushBody, requestDenialReasons);
        ServiceV3AuthsPostResponse response = transport.serviceV3AuthsPost(request, serviceEntity);
        return new AuthorizationRequest(
                response.getAuthRequest().toString(),
                response.getPushPackage());

    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy, String title, Integer ttl) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, policy, title, ttl, null, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, policy, null, null, null, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, null, null, null, null, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, null, null, null, null, null, null, null);
    }

    @Override
    public void cancelAuthorizationRequest(String authorizationRequestId)
            throws EntityNotFound, AuthorizationRequestCanceled, AuthorizationResponseExists,
            CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException,
            CryptographyError {
        transport.serviceV3AuthsDelete(UUID.fromString(authorizationRequestId), serviceEntity);
    }

    @Override
    public AuthorizationResponse getAuthorizationResponse(String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException {
        UUID authorizationRequestUUID;
        authorizationRequestUUID = getAuthRequestIdFromString(authorizationRequestId);
        AuthorizationResponse response;
        ServiceV3AuthsGetResponse transportResponse = transport.serviceV3AuthsGet(
                authorizationRequestUUID, serviceEntity
        );
        if (transportResponse == null) {
            response = null;
        } else {
            response = getAuthorizationResponse(transportResponse);
        }
        return response;
    }

    private AuthorizationResponse getAuthorizationResponse(AuthsResponse authsResponse) {
        AuthorizationResponse response;
        AuthorizationResponse.Type type;
        if (authsResponse.getType() == null) {
            type = null;
        } else if (authsResponse.getType().equals("AUTHORIZED")) {
            type = AuthorizationResponse.Type.AUTHORIZED;
        } else if (authsResponse.getType().equals("DENIED")) {
            type = AuthorizationResponse.Type.DENIED;
        } else if (authsResponse.getType().equals("FAILED")) {
            type = AuthorizationResponse.Type.FAILED;
        } else {
            type = AuthorizationResponse.Type.OTHER;
        }
        AuthorizationResponse.Reason reason;
        if (authsResponse.getReason() == null) {
            reason = null;
        } else if (authsResponse.getReason().equals("APPROVED")) {
            reason = AuthorizationResponse.Reason.APPROVED;
        } else if (authsResponse.getReason().equals("DISAPPROVED")) {
            reason = AuthorizationResponse.Reason.DISAPPROVED;
        } else if (authsResponse.getReason().equals("FRAUDULENT")) {
            reason = AuthorizationResponse.Reason.FRAUDULENT;
        } else if (authsResponse.getReason().equals("POLICY")) {
            reason = AuthorizationResponse.Reason.POLICY;
        } else if (authsResponse.getReason().equals("PERMISSION")) {
            reason = AuthorizationResponse.Reason.PERMISSION;
        } else if (authsResponse.getReason().equals("AUTHENTICATION")) {
            reason = AuthorizationResponse.Reason.AUTHENTICATION;
        } else if (authsResponse.getReason().equals("CONFIGURATION")) {
            reason = AuthorizationResponse.Reason.CONFIGURATION;
        } else if (authsResponse.getReason().equals("BUSY_LOCAL")) {
            reason = AuthorizationResponse.Reason.BUSY_LOCAL;
        } else {
            reason = AuthorizationResponse.Reason.OTHER;
        }
        response = new AuthorizationResponse(
                authsResponse.getAuthorizationRequestId().toString(),
                authsResponse.getResponse(),
                authsResponse.getServiceUserHash(),
                authsResponse.getOrganizationUserHash(),
                authsResponse.getUserPushId(),
                authsResponse.getDeviceId(),
                Arrays.asList(authsResponse.getServicePins()),
                type,
                reason,
                authsResponse.getDenialReason(),
                reason == AuthorizationResponse.Reason.FRAUDULENT);
        return response;
    }

    private UUID getAuthRequestIdFromString(String uuid) {
        UUID authorizationRequestUUID;
        try {
            authorizationRequestUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("authorizationRequestId must be a valid UUID!", e);
        }
        return authorizationRequestUUID;
    }

    @Override
    public void sessionStart(String user, String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        UUID authorizationRequestUUID = authorizationRequestId == null ? null : getAuthRequestIdFromString(authorizationRequestId);
        ServiceV3SessionsPostRequest request = new ServiceV3SessionsPostRequest(user, authorizationRequestUUID);
        transport.serviceV3SessionsPost(request, serviceEntity);
    }

    @Override
    public void sessionStart(String user)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        sessionStart(user, null);
    }

    @Override
    public void sessionEnd(String user)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        ServiceV3SessionsDeleteRequest request = new ServiceV3SessionsDeleteRequest(user);
        transport.serviceV3SessionsDelete(request, serviceEntity);
    }

    @Override
    public WebhookPackage handleWebhook(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        return handleWebhook(headers, body, null, null);
    }

    @Override
    public WebhookPackage handleWebhook(Map<String, List<String>> headers, String body, String method, String path)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        ServerSentEvent transportResponse = transport.handleServerSentEvent(headers, method, path, body);
        WebhookPackage response;
        if (transportResponse == null) {
            response = null;
        } else if (transportResponse instanceof ServerSentEventAuthorizationResponse) {
            response = new AuthorizationResponseWebhookPackage(
                    getAuthorizationResponse((ServerSentEventAuthorizationResponse) transportResponse)
            );
        } else if (transportResponse instanceof ServerSentEventUserServiceSessionEnd) {
            response = new ServiceUserSessionEndWebhookPackage(
                    ((ServerSentEventUserServiceSessionEnd) transportResponse).getApiTime(),
                    ((ServerSentEventUserServiceSessionEnd) transportResponse).getUserHash()
            );
        } else {
            throw new InvalidRequestException("Unknown response type was returned by the transport", null, null);
        }
        return response;
    }
}
