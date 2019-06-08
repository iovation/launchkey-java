package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.service.AuthMethod.Type;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.AuthMethod;
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceV3AuthsGetResponse;
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
public class BasicServiceClientGetAuthorizationResponseTest extends TestCase {

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
                    new AuthPolicy(
                            1, true, true, true, null
                    ),
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
            client.getAuthorizationResponse(authRequestId.toString());
            verify(transport).serviceV3AuthsGet(any(UUID.class), eq(expected));
        }

        @Test
        public void sendsExpectedAuthRequestId() throws Exception {
            client.getAuthorizationResponse(authRequestId.toString());
            verify(transport).serviceV3AuthsGet(eq(authRequestId), any(EntityIdentifier.class));
        }

        @Test
        public void usesResponseAuthorized() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertTrue(response.isAuthorized());
        }

        @Test
        public void usesProvidedAuthRequestId() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(authResponse.getAuthorizationRequestId().toString(), response.getAuthorizationRequestId());
        }

        @Test
        public void usesServiceUserHash() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals("service-user-hash", response.getServiceUserHash());
        }

        @Test
        public void usesOrganizationUserHash() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals("org-user-hash", response.getOrganizationUserHash());
        }

        @Test
        public void usesUserPushId() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals("user-push-id", response.getUserPushId());
        }

        @Test
        public void usesDeviceId() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals("device-id", response.getDeviceId());
        }

        @Test
        public void usesServicePins() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(Arrays.asList("service", "pins"), response.getServicePins());
        }

        @Test
        public void usesDenialreason() throws Exception {
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals("denial-reason", response.getDenialReason());
        }

        @Test
        public void testNullTransportResponseReturnsNull() throws Exception {
            when(transport.serviceV3AuthsGet(any(UUID.class), any(EntityIdentifier.class))).thenReturn(null);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response);
        }


    }

    @RunWith(Parameterized.class)
    public static class ResponseTypeTests {
        private final String input;
        private final AuthorizationResponse.Type expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "AUTHORIZED", AuthorizationResponse.Type.AUTHORIZED },
                    { "DENIED", AuthorizationResponse.Type.DENIED },
                    { "FAILED", AuthorizationResponse.Type.FAILED },
                    { "UNKNOWN", AuthorizationResponse.Type.OTHER },
            });
        }

        public ResponseTypeTests(String input, AuthorizationResponse.Type expectedOutput) {
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
                    new AuthPolicy(
                            1, true, true, true, null
                    ),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.getType());
        }
    }

    @RunWith(Parameterized.class)
    public static class ResponseReasonTests {
        private final String input;
        private final AuthorizationResponse.Reason expectedOutput;

        @Parameterized.Parameters()
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "APPROVED", AuthorizationResponse.Reason.APPROVED },
                    { "DISAPPROVED", AuthorizationResponse.Reason.DISAPPROVED },
                    { "FRAUDULENT", AuthorizationResponse.Reason.FRAUDULENT },
                    { "POLICY", AuthorizationResponse.Reason.POLICY },
                    { "PERMISSION", AuthorizationResponse.Reason.PERMISSION },
                    { "AUTHENTICATION", AuthorizationResponse.Reason.AUTHENTICATION },
                    { "CONFIGURATION", AuthorizationResponse.Reason.CONFIGURATION },
                    { "BUSY_LOCAL", AuthorizationResponse.Reason.BUSY_LOCAL },
                    { "SENSOR", AuthorizationResponse.Reason.SENSOR },
                    { "UNKNOWN", AuthorizationResponse.Reason.OTHER },
            });
        }

        public ResponseReasonTests(String input, AuthorizationResponse.Reason expectedOutput) {
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
                    new AuthPolicy(
                            1, true, true, true, null
                    ),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }
            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
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
                    new AuthPolicy(
                            1, true, true, true, null
                    ),
                    new AuthMethod[]{
                            new AuthMethod("meth-true", true, true, true, true, true, true, true),
                            new AuthMethod("meth-false", false, false, false, false, false, false, false),
                            new AuthMethod("meth-null", null, null, null, null, null, null, null)

                    }
            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
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
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsSameValue() throws Exception {
            AuthPolicy policy = new AuthPolicy(1, null, null, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(Integer.valueOf(1), response.getPolicy().getRequiredFactors());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsNullForInherenceRequired() throws Exception {
            AuthPolicy policy = new AuthPolicy(1, null, null, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy().isInherenceFactorRequired());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsNullForKnowledgeRequired() throws Exception {
            AuthPolicy policy = new AuthPolicy(1, null, null, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy().isKnowledgeFactorRequired());
        }

        @Test
        public void transportResponsePolicyWithRequirementCountReturnsNullForPossessionRequired() throws Exception {
            AuthPolicy policy = new AuthPolicy(1, null, null, null, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy().isPossessionFactorRequired());
        }

        @Test
        public void transportResponsePolicyWithTypeRequiredHasNullForRequirementCount() throws Exception {
            AuthPolicy policy = new AuthPolicy(null, true, true, true, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertNull(response.getPolicy().getRequiredFactors());
        }

        @Test
        public void transportResponsePolicyWithInherenceRequiredReturnsInherenceRequiredWithSameValue() throws Exception {
            AuthPolicy policy = new AuthPolicy(null, true, false, false, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().isInherenceFactorRequired());
        }

        @Test
        public void transportResponsePolicyWithKnowledgeRequiredReturnsKnowledgeRequiredWithSameValue() throws Exception {
            AuthPolicy policy = new AuthPolicy(null, false, true, false, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().isKnowledgeFactorRequired());
        }

        @Test
        public void transportResponsePolicyWithPossessionRequiredReturnsPossessionRequiredWithSAmeValue() throws Exception {
            AuthPolicy policy = new AuthPolicy(null, false, false, true, null);
            when(transportResponse.getAuthPolicy()).thenReturn(policy);
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertTrue(response.getPolicy().isPossessionFactorRequired());
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
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
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
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getAuthMethods());
        }

        @Test
        public void testEmptyArrayReturnsEmptyList() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{});
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
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
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getSet());
        }

        @Test
        public void testIsActive() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, value, null, null, null, null, null)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getActive());
        }

        @Test
        public void testIsAllowed() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, value, null, null, null, null)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getAllowed());
        }

        @Test
        public void testIsSupported() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, value, null, null, null)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getSupported());
        }

        @Test
        public void testIsUserRequired() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, value, null, null)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getUserRequired());
        }

        @Test
        public void testIsPassed() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, null, value, null)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getPassed());
        }

        @Test
        public void testIsError() throws Exception {
            when(transportResponse.getAuthMethods()).thenReturn(new AuthMethod[]{
                    new AuthMethod(null, null, null, null, null, null, null, value)
            });
            AuthorizationResponse response = client.getAuthorizationResponse(authRequestId.toString());
            assertEquals(value, response.getAuthMethods().get(0).getError());
        }
    }


    @RunWith(MockitoJUnitRunner.class)
    public static class PolicyLocationsTests {

        @Mock
        private Transport transport;

        @Mock
        private ServiceV3AuthsGetResponse transportResponse;

        @Mock
        private AuthPolicy policy;

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
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertThat(response.getPolicy().getLocations(), is(empty()));
        }

        @Test
        public void testEmptyArrayReturnsEmptyList() throws Exception {
            when(policy.getGeoFences()).thenReturn(new ArrayList<AuthPolicy.Location>());
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertThat(response.getPolicy().getLocations(), is(empty()));
        }

        @Test
        public void testNullNameTransfers() throws Exception {
            when(policy.getGeoFences()).thenReturn(Arrays.asList(new AuthPolicy.Location(null, 1.0, 2.0, 3.0)));
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertNull(response.getPolicy().getLocations().get(0).getName());
        }

        @Test
        public void testNameTransfers() throws Exception {
            when(policy.getGeoFences()).thenReturn(Arrays.asList(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)));
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals("name", response.getPolicy().getLocations().get(0).getName());
        }

        @Test
        public void testRadiusTransfers() throws Exception {
            when(policy.getGeoFences()).thenReturn(Arrays.asList(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)));
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(1.0, response.getPolicy().getLocations().get(0).getRadius());
        }

        @Test
        public void testLatitudeTransfers() throws Exception {
            when(policy.getGeoFences()).thenReturn(Arrays.asList(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)));
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(2.0, response.getPolicy().getLocations().get(0).getLatitude());
        }

        @Test
        public void testLongitudeTransfers() throws Exception {
            when(policy.getGeoFences()).thenReturn(Arrays.asList(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)));
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(3.0, response.getPolicy().getLocations().get(0).getLongitude());
        }
    }
}