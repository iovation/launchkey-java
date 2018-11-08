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
import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy, String title, Integer ttl, String pushTitle, String pushBody) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
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
        ServiceV3AuthsPostRequest request = new ServiceV3AuthsPostRequest(
                userIdentifier, requestPolicy, context, title, ttl, pushTitle, pushBody);
        ServiceV3AuthsPostResponse response = transport.serviceV3AuthsPost(request, serviceEntity);
        return new AuthorizationRequest(
                response.getAuthRequest().toString(),
                response.getPushPackage());

    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy, String title, Integer ttl) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, policy, title, ttl, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, policy, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, context, null, null, null);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(String userIdentifier) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError {
        return createAuthorizationRequest(userIdentifier, null, null, null, null);
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
            response = new AuthorizationResponse(
                    transportResponse.getAuthorizationRequestId().toString(),
                    transportResponse.getResponse(),
                    transportResponse.getServiceUserHash(),
                    transportResponse.getOrganizationUserHash(),
                    transportResponse.getUserPushId(),
                    transportResponse.getDeviceId(),
                    Arrays.asList(transportResponse.getServicePins()));
        }
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
        } else if (ServerSentEventAuthorizationResponse.class.isInstance(transportResponse)) {
            response = new AuthorizationResponseWebhookPackage(
                    new AuthorizationResponse(
                            ((ServerSentEventAuthorizationResponse) transportResponse).getAuthorizationRequestId().toString(),
                            ((ServerSentEventAuthorizationResponse) transportResponse).getResponse(),
                            ((ServerSentEventAuthorizationResponse) transportResponse).getServiceUserHash(),
                            ((ServerSentEventAuthorizationResponse) transportResponse).getOrganizationUserHash(),
                            ((ServerSentEventAuthorizationResponse) transportResponse).getUserPushId(),
                            ((ServerSentEventAuthorizationResponse) transportResponse).getDeviceId(),
                            Arrays.asList(((ServerSentEventAuthorizationResponse) transportResponse).getServicePins())
                    )
            );
        } else if (ServerSentEventUserServiceSessionEnd.class.isInstance(transportResponse)) {
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
