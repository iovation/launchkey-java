package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.AuthorizationRequest;
import com.iovation.launchkey.sdk.domain.service.DenialReason;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceV3AuthsPostRequest;
import com.iovation.launchkey.sdk.transport.domain.ServiceV3AuthsPostResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicServiceClientAuthorizationRequestTest {
    private final static UUID serviceId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private final static UUID authRequestId = UUID.fromString("3e3dd6f8-a24e-11e8-b600-8c85909ffbde");
    private final static String user = "User Name";
    private final static String pushPackage = "Push Package";

    @Mock
    public Transport transport;

    @Mock
    public ServiceV3AuthsPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServiceV3AuthsPostRequest> requestCaptor;

    private ServiceClient client;

    private AuthPolicy requiredFactorsPolicy;

    @Before
    public void setUp() throws Exception {
        client = new BasicServiceClient(serviceId, transport);
        when(response.getAuthRequest()).thenReturn(authRequestId);
        when(response.getPushPackage()).thenReturn(pushPackage);
        when(transport.serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
        requiredFactorsPolicy = new AuthPolicy(99, true, Arrays.asList(
                new AuthPolicy.Location(1, 1.1, 1.2),
                new AuthPolicy.Location(2, 2.1, 2.2)
        ));
    }


    @Test
    public void sendsSubjectEntityTypeWithUser() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }


    @Test
    public void sendsSubjectEntityTypeWithUserContext() throws Exception {
        client.createAuthorizationRequest(user, null);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }


    @Test
    public void sendsSubjectEntityTypeWithUserContextPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, null);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityTypeWithUserContextPolicyTitle() throws Exception {
        client.createAuthorizationRequest(user, null, null, null, null);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityIdWithUser() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsSubjectEntityIdWithUserContext() throws Exception {
        client.createAuthorizationRequest(user, null);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsSubjectEntityIdWithUserContextPolicyTitle() throws Exception {
        client.createAuthorizationRequest(user, null, null, null, null);
        verify(transport).serviceV3AuthsPost(any(ServiceV3AuthsPostRequest.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsUserAsUsernameWithUser() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getUsername());
    }

    @Test
    public void sendsUserAsUsernameWithUserContext() throws Exception {
        client.createAuthorizationRequest(user, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getUsername());
    }

    @Test
    public void sendsUserAsUsernameWithUserContextPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getUsername());
    }

    @Test
    public void sendsUserAsUsernameWithUserContextPolicyTitle() throws Exception {
        client.createAuthorizationRequest(user, null, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getUsername());
    }

    @Test
    public void sendsNullContextWithUser() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getContext());
    }

    @Test
    public void sendsContextWithUserContext() throws Exception {
        client.createAuthorizationRequest(user, "Expected Context");
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Context", requestCaptor.getValue().getContext());
    }

    @Test
    public void sendsContextWithUserContextPolicy() throws Exception {
        client.createAuthorizationRequest(user, "Expected Context", null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Context", requestCaptor.getValue().getContext());
    }

    @Test
    public void sendsContextWithUserContextPolicyTitleTtl() throws Exception {
        client.createAuthorizationRequest(user, "Expected Context", null, null, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Context", requestCaptor.getValue().getContext());
    }

    @Test
    public void sendsNullPolicyWithUser() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy());
    }

    @Test
    public void sendsNullPolicyWithUserContext() throws Exception {
        client.createAuthorizationRequest(user, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy());
    }

    @Test
    public void sendsNullPolicyRequiredFactorsWithIndividualFactorsPolicy() throws Exception {
        AuthPolicy policy = new AuthPolicy(false, false, false, false, new ArrayList<AuthPolicy.Location>());
        client.createAuthorizationRequest(user, null, policy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getAny());
    }

    @Test
    public void sendsExpectedPolicyKnowledgeFactorWithIndividualFactorsPolicy() throws Exception {
        AuthPolicy policy = new AuthPolicy(true, false, false, false, new ArrayList<AuthPolicy.Location>());
        client.createAuthorizationRequest(user, null, policy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        ServiceV3AuthsPostRequest request = requestCaptor.getValue();
        assertEquals(1, request.getPolicy().getMinimumRequirements().get(0).getKnowledge().intValue());
    }

    @Test
    public void sendsExpectedPolicyInherenceFactorWithIndividualFactorsPolicy() throws Exception {
        AuthPolicy policy = new AuthPolicy(false, true, false, false, new ArrayList<AuthPolicy.Location>());
        client.createAuthorizationRequest(user, null, policy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        ServiceV3AuthsPostRequest request = requestCaptor.getValue();
        assertEquals(1, request.getPolicy().getMinimumRequirements().get(0).getInherence().intValue());
    }

    @Test
    public void sendExpectedPolicyPossessionFactorWithIndividualFactorsPolicy() throws Exception {
        AuthPolicy policy = new AuthPolicy(false, false, true, false, new ArrayList<AuthPolicy.Location>());
        client.createAuthorizationRequest(user, null, policy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        ServiceV3AuthsPostRequest request = requestCaptor.getValue();
        assertEquals(1, request.getPolicy().getMinimumRequirements().get(0).getPossession().intValue());
    }

    @Test
    public void sendsExpectedPolicyRequiredFactorsWithRequiredFactorsPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, requiredFactorsPolicy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(99, requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getAny().intValue());
    }

    @Test
    public void sendsNullPolicyKnowledgeFactorWithRequiredFactorsPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, requiredFactorsPolicy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getKnowledge());
    }

    @Test
    public void sendsNullPolicyInherenceFactorWithRequiredFactorsPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, requiredFactorsPolicy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getInherence());
    }

    @Test
    public void sendsNullPolicyPossessionFactorWithRequiredFactorsPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, requiredFactorsPolicy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getPossession());
    }

    @Test
    public void sendsExpectedPolicyDeviceIntegrityWithPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, new AuthPolicy(99, null, new ArrayList<AuthPolicy.Location>()));
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(99, requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getAny().intValue());
    }

    @Test
    public void sendsExpectedPolicyGeoFencesWithPolicy() throws Exception {
        client.createAuthorizationRequest(user, null, requiredFactorsPolicy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        List<com.iovation.launchkey.sdk.transport.domain.AuthPolicy.Location> expected = Arrays.asList(
                new com.iovation.launchkey.sdk.transport.domain.AuthPolicy.Location(null, 1, 1.1, 1.2),
                new com.iovation.launchkey.sdk.transport.domain.AuthPolicy.Location(null, 2, 2.1, 2.2)
        );
        assertEquals(expected, requestCaptor.getValue().getPolicy().getGeoFences());
    }

    @Test
    public void sendsExpectedDeviceIntegraitywithPolicy() throws Exception {
        AuthPolicy policy = new AuthPolicy(0, true, new ArrayList<AuthPolicy.Location>());
        client.createAuthorizationRequest(user, null, policy);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(true, requestCaptor.getValue().getPolicy().getDeviceIntegrity());
    }

    @Test
    public void sendsNoPolicyWithNoPolicy() throws Exception {
        client.createAuthorizationRequest(user);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy());
    }

    @Test
    public void sendsTitleWithUserContextPolicyTitleTtl() throws Exception {
        client.createAuthorizationRequest(user, null, null, "Expected Title", null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Title", requestCaptor.getValue().getTitle());
    }

    @Test
    public void sendsTtlWithUserContextPolicyTitleTtl() throws Exception {
        client.createAuthorizationRequest(user, null, null, null, 999);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(999), requestCaptor.getValue().getTTL());
    }

    @Test
    public void sendsPushTitleWithUserContextPolicyTitleTtlPushTitleBody() throws Exception {
        client.createAuthorizationRequest(user, null, null, null, null, "Expected Push Title", null, null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Push Title", requestCaptor.getValue().getPushTitle());
    }

    @Test
    public void sendsPushBodyWithUserContextPolicyTitleTtlPushTitleBody() throws Exception {
        client.createAuthorizationRequest(user, null, null, null, null, null, "Expected Push Body", null);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Push Body", requestCaptor.getValue().getPushBody());
    }

    @Test
    public void sendsDenialReasonsWithUserContextPolicyTitleTtlPushTitleBody() throws Exception {
        List<DenialReason> reasons = new ArrayList<DenialReason>() {{
            add(new DenialReason("1", "a", true));
            add(new DenialReason("2", "b", false));
        }};
        client.createAuthorizationRequest(user, null, null, null, null, null, null, reasons);
        verify(transport).serviceV3AuthsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        List<com.iovation.launchkey.sdk.transport.domain.DenialReason> expectedReasons = new ArrayList<com.iovation.launchkey.sdk.transport.domain.DenialReason>() {{
            add(new com.iovation.launchkey.sdk.transport.domain.DenialReason("1", "a", true));
            add(new com.iovation.launchkey.sdk.transport.domain.DenialReason("2", "b", false));
        }};
        assertEquals(expectedReasons, requestCaptor.getValue().getDenialReasons());
    }

    @Test
    public void returnsAuthRequestIdWithUser() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user);
        assertEquals(authRequestId.toString(), actual.getId());
    }

    @Test
    public void returnsAuthRequestIdWithUserContext() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null);
        assertEquals(authRequestId.toString(), actual.getId());
    }

    @Test
    public void returnsAuthRequestIdWithUserContextPolicy() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null);
        assertEquals(authRequestId.toString(), actual.getId());
    }

    @Test
    public void returnsAuthRequestIdWithUserContextPolicyTitleTtl() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null, null, null);
        assertEquals(authRequestId.toString(), actual.getId());
    }

    @Test
    public void returnsAuthRequestIdWithUserContextPolicyTitleTtlPushTitleBody() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null, null, null, null, null, null);
        assertEquals(authRequestId.toString(), actual.getId());
    }

    @Test
    public void returnsPushPackageWithUser() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user);
        assertEquals(pushPackage, actual.getPushPackage());
    }

    @Test
    public void returnsPushPackageWithUserContext() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null);
        assertEquals(pushPackage, actual.getPushPackage());
    }

    @Test
    public void returnsPushPackageWithUserContextPolicy() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null);
        assertEquals(pushPackage, actual.getPushPackage());
    }

    @Test
    public void returnsPushPackageWithUserContextPolicyPushTitleBody() throws Exception {
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null, null, null);
        assertEquals(pushPackage, actual.getPushPackage());
    }

    @Test
    public void returnsDeviceIds() throws Exception {
        List<String> expected = new ArrayList<String>() {{
            add("Device A");
            add("Device B");
        }};
        when(response.getDeviceIds()).thenReturn(expected);
        AuthorizationRequest actual = client.createAuthorizationRequest(user, null, null, null, null, null, null, null);
        assertEquals(expected, actual.getDeviceIds());
    }
}