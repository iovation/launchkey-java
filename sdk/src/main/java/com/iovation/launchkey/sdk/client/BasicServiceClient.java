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

import com.iovation.launchkey.sdk.domain.policy.Fence;
import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import com.iovation.launchkey.sdk.domain.policy.TerritoryFence;
import com.iovation.launchkey.sdk.domain.service.AuthMethod;
import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.DenialReason;
import com.iovation.launchkey.sdk.domain.service.*;
import com.iovation.launchkey.sdk.domain.webhook.AdvancedAuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;

import java.util.*;

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
                        location.getName(),
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
        AdvancedAuthorizationResponse advancedResponse = getAdvancedAuthorizationResponse(authorizationRequestId);

        return getAuthorizationResponse(advancedResponse);
    }

    @Override
    public AdvancedAuthorizationResponse getAdvancedAuthorizationResponse(String authorizationRequestId) throws CommunicationErrorException, MarshallingError, InvalidResponseException, InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException, AuthorizationRequestCanceled {
        UUID authorizationRequestUUID = getAuthRequestIdFromString(authorizationRequestId);
        AuthsResponse transportResponse = transport.serviceV3AuthsGet(authorizationRequestUUID, serviceEntity);
        AdvancedAuthorizationResponse response;
        if (transportResponse == null) {
            response = null;
        } else {
            response = getAdvancedAuthorizationResponse(transportResponse);
        }
        return response;
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
        WebhookPackage response = handleAdvancedWebhook(headers, body, method, path);
        if (response instanceof AdvancedAuthorizationResponseWebhookPackage) {
            //noinspection deprecation
            response = new AuthorizationResponseWebhookPackage(getAuthorizationResponse(
                    ((AdvancedAuthorizationResponseWebhookPackage) response).getAuthorizationResponse()));
        }
        return response;
    }

    @Override
    public WebhookPackage handleAdvancedWebhook(
            Map<String, List<String>> headers, String body, String method, String path
    ) throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        ServerSentEvent transportResponse = transport.handleServerSentEvent(headers, method, path, body);
        WebhookPackage response;
        if (transportResponse == null) {
            response = null;
        } else if (transportResponse instanceof ServerSentEventAuthorizationResponse) {
            response = new AdvancedAuthorizationResponseWebhookPackage(
                    getAdvancedAuthorizationResponse((ServerSentEventAuthorizationResponse) transportResponse)
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

    private AuthorizationResponse getAuthorizationResponse(AdvancedAuthorizationResponse advancedResponse) {
        AuthorizationResponse response;
        if (advancedResponse == null) {
            response = null;
        } else {
            response = new AuthorizationResponse(
                    advancedResponse.getAuthorizationRequestId(),
                    advancedResponse.isAuthorized(),
                    advancedResponse.getServiceUserHash(),
                    advancedResponse.getOrganizationUserHash(),
                    advancedResponse.getUserPushId(),
                    advancedResponse.getDeviceId(),
                    advancedResponse.getServicePins(),
                    getLegacyTypeFromAdvancedType(advancedResponse.getType()),
                    getLegacyReasonFromAdvancedReason(advancedResponse.getReason()),
                    advancedResponse.getDenialReason(),
                    advancedResponse.isFraud(),
                    getLegacyPolicyFromAdvancedPolicy(advancedResponse.getPolicy()),
                    advancedResponse.getAuthMethods()
            );
        }
        return response;
    }

    private AuthPolicy getLegacyPolicyFromAdvancedPolicy(AuthorizationResponsePolicy advancedPolicy) {
        if (advancedPolicy == null) {
            return null;
        }
        List<AuthPolicy.Location> locations = new ArrayList<>();
        if (advancedPolicy.getFences() != null) {
            for (Fence fence : advancedPolicy.getFences()) {
                if (fence instanceof GeoCircleFence) {
                    GeoCircleFence circleFence = (GeoCircleFence) fence;
                    locations.add(new AuthPolicy.Location(circleFence.getName(), circleFence.getRadius(),
                            circleFence.getLatitude(), circleFence.getLongitude()));
                }
            }
        }
        AuthPolicy authPolicy;
        if (Requirement.TYPES.equals(advancedPolicy.getRequirement())) {
            authPolicy = new AuthPolicy(advancedPolicy.wasKnowledgeRequired(), advancedPolicy.wasInherenceRequired(), advancedPolicy.wasPossessionRequired(), null, locations);
        } else if (Requirement.AMOUNT.equals(advancedPolicy.getRequirement())) {
            authPolicy = new AuthPolicy(advancedPolicy.getAmount(), null, locations);
        } else if (null == advancedPolicy.getRequirement()) {
            authPolicy = new AuthPolicy(locations);
        } else {
            authPolicy = null;
        }
        return authPolicy;
    }

    private AuthorizationResponse.Reason getLegacyReasonFromAdvancedReason(AdvancedAuthorizationResponse.Reason advancedreason) {
        AuthorizationResponse.Reason reason;
        if (advancedreason == null) {
            reason = null;
        } else {
            switch (advancedreason) {
                case APPROVED:
                    reason = AuthorizationResponse.Reason.APPROVED;
                    break;
                case DISAPPROVED:
                    reason = AuthorizationResponse.Reason.DISAPPROVED;
                    break;
                case FRAUDULENT:
                    reason = AuthorizationResponse.Reason.FRAUDULENT;
                    break;
                case POLICY:
                    reason = AuthorizationResponse.Reason.POLICY;
                    break;
                case PERMISSION:
                    reason = AuthorizationResponse.Reason.PERMISSION;
                    break;
                case AUTHENTICATION:
                    reason = AuthorizationResponse.Reason.AUTHENTICATION;
                    break;
                case CONFIGURATION:
                    reason = AuthorizationResponse.Reason.CONFIGURATION;
                    break;
                case BUSY_LOCAL:
                    reason = AuthorizationResponse.Reason.BUSY_LOCAL;
                    break;
                case SENSOR:
                    reason = AuthorizationResponse.Reason.SENSOR;
                    break;
                case OTHER:
                    reason = AuthorizationResponse.Reason.OTHER;
                    break;
                default:
                    reason = null;
            }
        }
        return reason;
    }

    private AuthorizationResponse.Type getLegacyTypeFromAdvancedType(AdvancedAuthorizationResponse.Type advancedType) {
        AuthorizationResponse.Type type;
        if (advancedType == null) {
            type = null;
        } else {
            switch (advancedType) {
                case AUTHORIZED:
                    type = AuthorizationResponse.Type.AUTHORIZED;
                    break;
                case DENIED:
                    type = AuthorizationResponse.Type.DENIED;
                    break;
                case FAILED:
                    type = AuthorizationResponse.Type.FAILED;
                    break;
                case OTHER:
                    type = AuthorizationResponse.Type.OTHER;
                    break;
                default:
                    type = null;
            }
        }
        return type;
    }

    private AdvancedAuthorizationResponse getAdvancedAuthorizationResponse(AuthsResponse authsResponse) throws InvalidResponseException {
        AdvancedAuthorizationResponse.Type type = getAdvancedType(authsResponse);
        AdvancedAuthorizationResponse.Reason reason = getAdvancedReason(authsResponse);
        AuthorizationResponsePolicy policy;
        try {
            policy = getAuthorizationResponsePolicy(authsResponse);
        } catch (UnknownPolicyException e) {
            throw new InvalidResponseException("Error parsing policy", e, null);
        }
        List<AuthMethod> authMethods = getMethods(authsResponse);

        return new AdvancedAuthorizationResponse(
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
                reason == AdvancedAuthorizationResponse.Reason.FRAUDULENT,
                policy,
                authMethods);
    }

    private AuthorizationResponsePolicy getAuthorizationResponsePolicy(AuthsResponse authsResponse) throws UnknownPolicyException {
        AuthorizationResponsePolicy policy;

        if (authsResponse.getAuthPolicy() == null) {
            policy = null;
        } else {
            policy = new AuthorizationResponsePolicy(
                    getRequirement(authsResponse.getAuthPolicy()),
                    authsResponse.getAuthPolicy().getAmount(),
                    getFences(authsResponse.getAuthPolicy()),
                    authsResponse.getAuthPolicy().getTypes() != null && authsResponse.getAuthPolicy().getTypes().contains("inherence"),
                    authsResponse.getAuthPolicy().getTypes() != null && authsResponse.getAuthPolicy().getTypes().contains("knowledge"),
                    authsResponse.getAuthPolicy().getTypes() != null && authsResponse.getAuthPolicy().getTypes().contains("possession")
            );
        }
        return policy;
    }

    private Requirement getRequirement(AuthResponsePolicy authPolicy) {
        Requirement requirement;
        if (null == authPolicy.getRequirement()) {
            requirement = null;
        } else if ("types".equals(authPolicy.getRequirement())) {
            requirement = Requirement.TYPES;
        } else if ("amount".equals(authPolicy.getRequirement())) {
            requirement = Requirement.AMOUNT;
        } else if ("cond_geo".equals(authPolicy.getRequirement())) {
            requirement = Requirement.COND_GEO;
        } else {
            requirement = Requirement.OTHER;
        }
        return requirement;
    }

    private AdvancedAuthorizationResponse.Reason getAdvancedReason(AuthsResponse authsResponse) {
        AdvancedAuthorizationResponse.Reason reason;
        if (authsResponse.getReason() == null) {
            reason = null;
        } else if (authsResponse.getReason().equals("APPROVED")) {
            reason = AdvancedAuthorizationResponse.Reason.APPROVED;
        } else if (authsResponse.getReason().equals("DISAPPROVED")) {
            reason = AdvancedAuthorizationResponse.Reason.DISAPPROVED;
        } else if (authsResponse.getReason().equals("FRAUDULENT")) {
            reason = AdvancedAuthorizationResponse.Reason.FRAUDULENT;
        } else if (authsResponse.getReason().equals("POLICY")) {
            reason = AdvancedAuthorizationResponse.Reason.POLICY;
        } else if (authsResponse.getReason().equals("PERMISSION")) {
            reason = AdvancedAuthorizationResponse.Reason.PERMISSION;
        } else if (authsResponse.getReason().equals("AUTHENTICATION")) {
            reason = AdvancedAuthorizationResponse.Reason.AUTHENTICATION;
        } else if (authsResponse.getReason().equals("CONFIGURATION")) {
            reason = AdvancedAuthorizationResponse.Reason.CONFIGURATION;
        } else if (authsResponse.getReason().equals("BUSY_LOCAL")) {
            reason = AdvancedAuthorizationResponse.Reason.BUSY_LOCAL;
        } else if (authsResponse.getReason().equals("SENSOR")) {
            reason = AdvancedAuthorizationResponse.Reason.SENSOR;
        } else {
            reason = AdvancedAuthorizationResponse.Reason.OTHER;
        }
        return reason;
    }

    private AdvancedAuthorizationResponse.Type getAdvancedType(AuthsResponse authsResponse) {
        AdvancedAuthorizationResponse.Type type;
        if (authsResponse.getType() == null) {
            type = null;
        } else if (authsResponse.getType().equals("AUTHORIZED")) {
            type = AdvancedAuthorizationResponse.Type.AUTHORIZED;
        } else if (authsResponse.getType().equals("DENIED")) {
            type = AdvancedAuthorizationResponse.Type.DENIED;
        } else if (authsResponse.getType().equals("FAILED")) {
            type = AdvancedAuthorizationResponse.Type.FAILED;
        } else {
            type = AdvancedAuthorizationResponse.Type.OTHER;
        }
        return type;
    }

    private List<Fence> getFences(AuthResponsePolicy transportPolicy) throws UnknownPolicyException {
        List<Fence> fences;
        if (transportPolicy.getFences() == null) {
            fences = null;
        } else {
            fences = new ArrayList<>();
            for (com.iovation.launchkey.sdk.transport.domain.Fence policyFence : transportPolicy.getFences()) {
                Fence fence;
                if (policyFence.getType() == null || "GEO_CIRCLE".equals(policyFence.getType())) {
                    fence = new GeoCircleFence(policyFence.getName(), policyFence.getLatitude(),
                            policyFence.getLongitude(), policyFence.getRadius());
                } else if ("TERRITORY".equals(policyFence.getType())) {
                    fence = new TerritoryFence(policyFence.getName(), policyFence.getCountry(),
                            policyFence.getAdministrativeArea(), policyFence.getPostalCode());
                } else {
                    throw new UnknownPolicyException("Policy has unknown fence type: " + policyFence.getType(),
                            null, null);
                }
                fences.add(fence);
            }
        }
        return fences;
    }

    private List<AuthMethod> getMethods(AuthsResponse authsResponse) {
        List<AuthMethod> authMethods;
        if (authsResponse.getAuthMethods() == null) {
            authMethods = null;
        } else {
            authMethods = new ArrayList<>();
            for (com.iovation.launchkey.sdk.transport.domain.AuthMethod authMethod : authsResponse.getAuthMethods()) {
                AuthMethod.Type authMethodType;
                if ("PIN_CODE".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.PIN_CODE;
                } else if ("CIRCLE_CODE".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.CIRCLE_CODE;
                } else if ("GEOFENCING".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.GEOFENCING;
                } else if ("LOCATIONS".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.LOCATIONS;
                } else if ("WEARABLES".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.WEARABLES;
                } else if ("FINGERPRINT".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.FINGERPRINT;
                } else if ("FACE".equalsIgnoreCase(authMethod.getMethod())) {
                    authMethodType = AuthMethod.Type.FACE;
                } else {
                    authMethodType = AuthMethod.Type.OTHER;
                }
                authMethods.add(new AuthMethod(authMethodType, authMethod.getSet(), authMethod.getActive(),
                        authMethod.getAllowed(), authMethod.getSupported(), authMethod.getUserRequired(),
                        authMethod.getPassed(), authMethod.getError()));
            }
        }
        return authMethods;
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
}
