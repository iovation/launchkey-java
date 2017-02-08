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

package com.launchkey.sdk.service;

import com.launchkey.sdk.domain.service.AuthPolicy;
import com.launchkey.sdk.domain.service.AuthorizationResponse;
import com.launchkey.sdk.domain.sse.AuthorizationResponseServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServiceUserSessionServerSentEventPackage;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.Transport;
import com.launchkey.sdk.transport.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BasicServiceService implements ServiceService {
    private final EntityIdentifier serviceEntity;
    private final Transport transport;

    public BasicServiceService(UUID serviceId, Transport transport) {
        this.serviceEntity = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, serviceId);
        this.transport = transport;
    }

    @Override
    public String authorize(String user, String context, AuthPolicy policy)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {

        ServiceV3AuthsPostRequestPolicy requestPolicy;
        if (policy == null) {
            requestPolicy = null;
        } else {
            requestPolicy = new ServiceV3AuthsPostRequestPolicy(
                    policy.getRequiredFactors(),
                    policy.isKnowledgeFactorRequired(),
                    policy.isInherenceFactorRequired(),
                    policy.isPossessionFactorRequired()
            );
            for (AuthPolicy.Location location : policy.getLocations()) {
                requestPolicy.addGeoFence(
                        location.getRadius(),
                        location.getLatitude(),
                        location.getLongitude()
                );
            }
        }
        ServiceV3AuthsPostRequest request = new ServiceV3AuthsPostRequest(user, requestPolicy, context);
        ServiceV3AuthsPostResponse response = transport.serviceV3AuthsPost(request, serviceEntity);
        return response.getAuthRequest().toString();
    }

    @Override
    public String authorize(String user, String context)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        return authorize(user, context, null);
    }

    @Override
    public String authorize(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        return authorize(user, null, null);
    }

    @Override
    public AuthorizationResponse getAuthorizationResponse(String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException {
        UUID authorizationRequestUUID;
        authorizationRequestUUID = getUUIDFromString(authorizationRequestId, "authorizationRequestId");
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

    private UUID getUUIDFromString(String uuid, String parameter) {
        UUID authorizationRequestUUID;
        try {
            authorizationRequestUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(parameter + " must be a valid UUID!", e);
        }
        return authorizationRequestUUID;
    }

    @Override
    public void sessionStart(String user, String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        UUID authorizationRequestUUID = authorizationRequestId == null ? null : getUUIDFromString(authorizationRequestId, "authorizationRequestId");
        ServiceV3SessionsPostRequest request = new ServiceV3SessionsPostRequest(user, authorizationRequestUUID);
        transport.serviceV3SessionsPost(request, serviceEntity);
    }

    @Override
    public void sessionStart(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        sessionStart(user, null);
    }

    @Override
    public void sessionEnd(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        ServiceV3SessionsDeleteRequest request = new ServiceV3SessionsDeleteRequest(user);
        transport.serviceV3SessionsDelete(request, serviceEntity);
    }

    @Override
    public ServerSentEventPackage handleServerSentEvent(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        ServerSentEvent transportResponse = transport.handleServerSentEvent(headers, body);
        ServerSentEventPackage response;
        if (transportResponse == null) {
            response = null;
        } else if (ServerSentEventAuthorizationResponse.class.isInstance(transportResponse)) {
            response = new AuthorizationResponseServerSentEventPackage(
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
            response = new ServiceUserSessionServerSentEventPackage(
                    ((ServerSentEventUserServiceSessionEnd) transportResponse).getApiTime(),
                    ((ServerSentEventUserServiceSessionEnd) transportResponse).getUserHash()
            );
        } else {
            throw new InvalidRequestException("Unknown response type was returned by the transport", null, null);
        }
        return response;
    }

    private Map<String, List<String>> normalizeHeaders(Map<String, List<String>> inputHeaders) {
        Map<String, List<String>> outputHeaders = new ConcurrentHashMap<String, List<String>>(inputHeaders.size());
        for (Map.Entry<String, List<String>> entry : inputHeaders.entrySet()) {
            outputHeaders.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        return outputHeaders;
    }
}
