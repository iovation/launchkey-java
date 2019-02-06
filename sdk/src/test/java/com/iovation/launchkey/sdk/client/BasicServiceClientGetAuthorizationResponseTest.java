package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.InvalidRequestException;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
                    "denial-reason"
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
                    "denial-reason"
            ));
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
                    "denial-reason"
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
                    "denial-reason"
            ));
            ServiceClient client = new BasicServiceClient(UUID.randomUUID(), transport);
            AuthorizationResponse response = client.getAuthorizationResponse(UUID.randomUUID().toString());
            assertEquals(this.expectedOutput, response.isFraud());
        }
    }

}