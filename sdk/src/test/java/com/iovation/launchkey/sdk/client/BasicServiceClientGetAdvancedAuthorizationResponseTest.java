package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import com.iovation.launchkey.sdk.domain.policy.TerritoryFence;
import com.iovation.launchkey.sdk.domain.service.AdvancedAuthorizationResponse;
import com.iovation.launchkey.sdk.domain.service.AuthMethod.Type;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.*;


@RunWith(Enclosed.class)
public class BasicServiceClientGetAdvancedAuthorizationResponseTest extends TestCase {

    @RunWith(MockitoJUnitRunner.class)
    public static class StandardTests {
        @Mock
        public Transport transport;

        private final static UUID serviceId = UUID.randomUUID();
        private final static UUID authRequestId = UUID.randomUUID();
        private BasicServiceClient client;
        private ServiceV3AuthsGetResponse authResponse;


        @Before
        public void setUp() throws Exception {
            client = new BasicServiceClient(serviceId, transport);
            authResponse = new ServiceV3AuthsGetResponse(
                    new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, serviceId),
                    UUID.randomUUID(),
                    "service-user-hash",
                    "org-user-hash",
                    "user-push-id",
                    authRequestId,
                    true,
                    "device-id",
                    new String[]{"service", "pins"},
                    "type",
                    "reason",
                    "denial-reason",
                    new AuthResponsePolicy("AMOUNT", 1, null, null),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }
            );
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(authResponse);
        }

        @Test
        public void sendsExpectedServiceEntity() throws Exception {
            EntityIdentifier expected = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, serviceId);
            client.getAdvancedAuthorizationResponse(authRequestId.toString());
            verify(transport).serviceV3AuthsGet(any(UUID.class), eq(expected));
        }

        @Test
        public void sendsExpectedAuthRequestId() throws Exception {
            client.getAdvancedAuthorizationResponse(authRequestId.toString());
            verify(transport).serviceV3AuthsGet(eq(authRequestId), any(EntityIdentifier.class));
        }

        @Test
        public void usesResponseAuthorized() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertTrue(response.isAuthorized());
        }

        @Test
        public void usesProvidedAuthRequestId() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(authResponse.getAuthorizationRequestId().toString(), response.getAuthorizationRequestId());
        }

        @Test
        public void usesServiceUserHash() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals("service-user-hash", response.getServiceUserHash());
        }

        @Test
        public void usesOrganizationUserHash() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals("org-user-hash", response.getOrganizationUserHash());
        }

        @Test
        public void usesUserPushId() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals("user-push-id", response.getUserPushId());
        }

        @Test
        public void usesDeviceId() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals("device-id", response.getDeviceId());
        }

        @Test
        public void usesServicePins() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(Arrays.asList("service", "pins"), response.getServicePins());
        }

        @Test
        public void usesDenialreason() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals("denial-reason", response.getDenialReason());
        }

        @Test
        public void testNullTransportResponseReturnsNull() throws Exception {
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(null);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertNull(response);
        }


    }

    @RunWith(Parameterized.class)
    public static class ResponseTypeTests {
        private final String input;
        private final AdvancedAuthorizationResponse.Type expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "AUTHORIZED", AdvancedAuthorizationResponse.Type.AUTHORIZED },
                    { "DENIED", AdvancedAuthorizationResponse.Type.DENIED },
                    { "FAILED", AdvancedAuthorizationResponse.Type.FAILED },
                    { "UNKNOWN", AdvancedAuthorizationResponse.Type.OTHER },
            });
        }

        public ResponseTypeTests(String input, AdvancedAuthorizationResponse.Type expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Test
        public void properlyMapsType() throws Exception {
            Transport transport = mock(Transport.class);
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(new ServiceV3AuthsGetResponse(
                    new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID()),
                    UUID.randomUUID(),
                    "service-user-hash",
                    "org-user-hash",
                    "user-push-id",
                    UUID.randomUUID(),
                    true,
                    "device-id",
                    new String[]{"service", "pins"},
                    this.input,
                    "reason",
                    "denial-reason",
                    new AuthResponsePolicy("AMOUNT", 1, null, null),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.getType());
        }
    }

    @RunWith(Parameterized.class)
    public static class ResponseReasonTests {
        private final String input;
        private final AdvancedAuthorizationResponse.Reason expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "APPROVED", AdvancedAuthorizationResponse.Reason.APPROVED },
                    { "DISAPPROVED", AdvancedAuthorizationResponse.Reason.DISAPPROVED },
                    { "FRAUDULENT", AdvancedAuthorizationResponse.Reason.FRAUDULENT },
                    { "POLICY", AdvancedAuthorizationResponse.Reason.POLICY },
                    { "PERMISSION", AdvancedAuthorizationResponse.Reason.PERMISSION },
                    { "AUTHENTICATION", AdvancedAuthorizationResponse.Reason.AUTHENTICATION },
                    { "CONFIGURATION", AdvancedAuthorizationResponse.Reason.CONFIGURATION },
                    { "BUSY_LOCAL", AdvancedAuthorizationResponse.Reason.BUSY_LOCAL },
                    { "SENSOR", AdvancedAuthorizationResponse.Reason.SENSOR },
                    { "UNKNOWN", AdvancedAuthorizationResponse.Reason.OTHER },
            });
        }

        public ResponseReasonTests(String input, AdvancedAuthorizationResponse.Reason expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Test
        public void properlyMapsReason() throws Exception {
            Transport transport = mock(Transport.class);
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(new ServiceV3AuthsGetResponse(
                    new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID()),
                    UUID.randomUUID(),
                    "service-user-hash",
                    "org-user-hash",
                    "user-push-id",
                    UUID.randomUUID(),
                    true,
                    "device-id",
                    new String[]{"service", "pins"},
                    "type",
                    this.input,
                    "denial-reason",
                    new AuthResponsePolicy("AMOUNT", 1, null, null),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }
            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.getReason());
        }
    }

    @RunWith(Parameterized.class)
    public static class FraudTests {
        private final String input;
        private final Boolean expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "APPROVED", false },
                    { "DISAPPROVED", false },
                    { "FRAUDULENT", true },
                    { "POLICY", false },
                    { "PERMISSION", false },
                    { "AUTHENTICATION", false },
                    { "CONFIGURATION", false },
                    { "UNKNOWN", false },
            });
        }

        public FraudTests(String input, boolean expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Test
        public void properlyMapsFraud() throws Exception {
            Transport transport = mock(Transport.class);
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(new ServiceV3AuthsGetResponse(
                    new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID()),
                    UUID.randomUUID(),
                    "service-user-hash",
                    "org-user-hash",
                    "user-push-id",
                    UUID.randomUUID(),
                    true,
                    "device-id",
                    new String[]{"service", "pins"},
                    "type",
                    this.input,
                    "denial-reason",
                    new AuthResponsePolicy("AMOUNT", 1, null, null),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }
            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.isFraud());
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class PolicyTests {

        @Mock
        private Transport transport;

        @Mock
        private ServiceV3AuthsGetResponse transportResponse;

        private BasicServiceClient client;

        private UUID serviceId = UUID.randomUUID();

        private UUID authRequestId = UUID.randomUUID();

        @Before
        public void setUp() throws Exception {
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(transportResponse);
            when(transportResponse.getAuthorizationRequestId()).thenReturn(authRequestId);
            when(transportResponse.getServicePins()).thenReturn(new String[]{});
            client = new BasicServiceClient(serviceId, transport);
        }

        @Test
        public void noPolicyReturnsNull() throws Exception {
            when(transportResponse.getAuthPolicy()).thenReturn(null);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsSameValue() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("amount", 1, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(1, response.getPolicy().getAmount());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsFalseForInherenceRequired() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("amount", 1, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertFalse(response.getPolicy().wasInherenceRequired());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsFalseForKnowledgeRequired() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("amount", 1, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertFalse(response.getPolicy().wasInherenceRequired());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsFalseForPossessionRequired() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("amount", 1, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertFalse(response.getPolicy().wasPossessionRequired());
        }

        @Test
        public void transportResponsePolicyWithTypeRequiredHas0ForRequirementCount() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("types", 0, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(0, response.getPolicy().getAmount());
        }

        @Test
        public void transportResponsePolicyWithInherenceRequiredReturnsInherenceRequiredWithSameValue() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("types", 0, new ArrayList<String>(){{
                add("inherence");
            }}, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().wasInherenceRequired());
        }

        @Test
        public void transportResponsePolicyWithKnowledgeRequiredReturnsKnowledgeRequiredWithSameValue() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("types", 0, new ArrayList<String>(){{
                add("knowledge");
            }}, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().wasKnowledgeRequired());
        }

        @Test
        public void transportResponsePolicyWithPossessionRequiredReturnsPossessionRequiredWithSAmeValue() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy("types", 0, new ArrayList<String>(){{
                add("possession");
            }}, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().wasPossessionRequired());
        }

        @Test
        public void transportResponsePolicyWithMNullRequirementReturnsNullRequirement() throws Exception {
            AuthResponsePolicy policy = new AuthResponsePolicy(null, 0, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy().getRequirement());
        }
    }

    @RunWith(Parameterized.class)
    public static class AuthMethodsTypeTests {
        private final String input;
        private final Type expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "FACE", Type.FACE },
                    { "CIRCLE_CODE", Type.CIRCLE_CODE },
                    { "FINGERPRINT", Type.FINGERPRINT },
                    { "GEOFENCING", Type.GEOFENCING },
                    { "LOCATIONS", Type.LOCATIONS },
                    { "PIN_CODE", Type.PIN_CODE },
                    { "WEARABLES", Type.WEARABLES },
                    { "UNKNOWN", Type.OTHER },
            });
        }

        public AuthMethodsTypeTests(String input, Type expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Test
        public void properlyMapsType() throws Exception {
            UUID authRequestId = UUID.randomUUID();
            UUID serviceId = UUID.randomUUID();
            Transport transport = mock(Transport.class);
            ServiceV3AuthsGetResponse transportResponse = mock(ServiceV3AuthsGetResponse.class);
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(transportResponse);
            when(transportResponse.getAuthorizationRequestId()).thenReturn(authRequestId);
            when(transportResponse.getServicePins()).thenReturn(new String[]{});
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[] {
                    new AuthMethod(this.input, null, null, null, null, null, null, null)
            });
            BasicServiceClient client = new BasicServiceClient(serviceId, transport);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.getAuthMethods().get(0).getMethod());
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class AuthMethodsTests {

        @Mock
        private Transport transport;

        @Mock
        private ServiceV3AuthsGetResponse transportResponse;

        private BasicServiceClient client;

        private UUID serviceId = UUID.randomUUID();

        private UUID authRequestId = UUID.randomUUID();

        @Before
        public void setUp() throws Exception {
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(transportResponse);
            when(transportResponse.getAuthorizationRequestId()).thenReturn(authRequestId);
            when(transportResponse.getServicePins()).thenReturn(new String[]{});
            client = new BasicServiceClient(serviceId, transport);
        }

        @Test
        public void testNullReturnsNull() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getAuthMethods());
        }

        @Test
        public void testEmptyArrayReturnsEmptyList() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{});
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertNotNull(response.getAuthMethods());
            assertThat(response.getAuthMethods(), is(empty()));
        }
    }

    @RunWith(Parameterized.class)
    public static class AuthMethodsAttributeValueTests {
        private final Boolean value;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { null },
                    { Boolean.TRUE },
                    { Boolean.TRUE },
            });
        }

        private Transport transport;

        private ServiceV3AuthsGetResponse transportResponse;

        private BasicServiceClient client;

        private UUID serviceId = UUID.randomUUID();

        private UUID authRequestId = UUID.randomUUID();

        public AuthMethodsAttributeValueTests(Boolean vaue) {
            this.value = vaue;
        }

        @Before
        public void setUp() throws Exception {
            transportResponse = mock(ServiceV3AuthsGetResponse.class);
            when(transportResponse.getAuthorizationRequestId()).thenReturn(authRequestId);
            when(transportResponse.getServicePins()).thenReturn(new String[]{});
            transport = mock(Transport.class);
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(transportResponse);
            client = new BasicServiceClient(serviceId, transport);
        }

        @Test
        public void testIsSet() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, value, null, null, null, null, null, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getSet());
        }

        @Test
        public void testIsActive() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, value, null, null, null, null, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getActive());
        }

        @Test
        public void testIsAllowed() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, value, null, null, null, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getAllowed());
        }

        @Test
        public void testIsSupported() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, value, null, null, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getSupported());
        }

        @Test
        public void testIsUserRequired() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, value, null, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getUserRequired());
        }

        @Test
        public void testIsPassed() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, null, value, null)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getPassed());
        }

        @Test
        public void testIsError() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, null, null, value)
            });
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getError());
        }
    }


    @RunWith(MockitoJUnitRunner.class)
    public static class PolicyFencesTests {

        @Mock
        private Transport transport;

        @Mock
        private ServiceV3AuthsGetResponse transportResponse;

        @Mock
        private AuthResponsePolicy policy;

        private BasicServiceClient client;

        private UUID serviceId = UUID.randomUUID();

        private UUID authRequestId = UUID.randomUUID();

        @Before
        public void setUp() throws Exception {
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(transportResponse);
            when(transportResponse.getAuthorizationRequestId()).thenReturn(authRequestId);
            when(transportResponse.getServicePins()).thenReturn(new String[]{});
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            client = new BasicServiceClient(serviceId, transport);
        }

        @Test
        public void testNullReturnsEmptyList() throws Exception {
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertThat(response.getPolicy().getFences(), is(empty()));
        }

        @Test
        public void testEmptyArrayReturnsEmptyList() throws Exception {
            when(policy.getFences()).thenReturn(new ArrayList<Fence>());
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertThat(response.getPolicy().getFences(), is(empty()));
        }

        @Test
        public void testGeoCircleNullNameTransfers() throws Exception {
            when(policy.getFences()).thenReturn(Arrays.asList(new Fence(null, "GEO_CIRCLE", 1.0, 2.0, 3.0, null, null, null)));
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getPolicy().getFences().get(0).getName());
        }

        @Test
        public void testGeoCircleFenceTransfers() throws Exception {
            when(policy.getFences()).thenReturn(Arrays.asList(new Fence("name", "GEO_CIRCLE", 1.0, 2.0, 3.0, null, null, null)));
            com.iovation.launchkey.sdk.domain.policy.Fence expected = new GeoCircleFence("name", 1.0, 2.0, 3.0);
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(expected, response.getPolicy().getFences().get(0));
        }

        @Test
        public void testTerritoryFenceNullNameTransfers() throws Exception {
            when(policy.getFences()).thenReturn(Arrays.asList(new Fence(null, "GEO_CIRCLE", 1.0, 2.0, 3.0, null, null, null)));
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getPolicy().getFences().get(0).getName());
        }

        @Test
        public void testUnknownFenceNullNameTransfersAsGeoCircle() throws Exception {
            when(policy.getFences()).thenReturn(Arrays.asList(new Fence(null, null, 1.0, 2.0, 3.0, null, null, null)));
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getPolicy().getFences().get(0).getName());
        }

        @Test
        public void testTerritoryFenceTransfers() throws Exception {
            when(policy.getFences()).thenReturn(Arrays.asList(new Fence("name", "TERRITORY", null, null, null, "country", "admin", "postal")));
            com.iovation.launchkey.sdk.domain.policy.Fence expected = new TerritoryFence("name", "country", "admin", "postal");
            AdvancedAuthorizationResponse response = client.getAdvancedAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(expected, response.getPolicy().getFences().get(0));
        }
    }
}