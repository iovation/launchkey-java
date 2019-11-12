package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.InvalidRequestException;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEvent;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEventAuthorizationResponse;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEventUserServiceSessionEnd;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicServiceClientHandleWebhookTest extends TestCase {

    @Mock
    public Transport transport;

    @Mock
    private ServerSentEventAuthorizationResponse serverSentEventAuthorizationResponse;

    @Mock
    private ServerSentEventUserServiceSessionEnd serverSentEventUserServiceSessionEnd;

    private final static UUID serviceId = UUID.randomUUID();
    private BasicServiceClient client;


    @Before
    public void setUp() throws Exception {
        client = new BasicServiceClient(serviceId, transport);
        when(serverSentEventAuthorizationResponse.getAuthorizationRequestId()).thenReturn(UUID.randomUUID());
        when(serverSentEventAuthorizationResponse.getServicePins()).thenReturn(new String[]{});
    }

    @Test
    public void handleWebhookWithoutMethodAndPathPassesNullMethodAndPath() throws Exception {
        client.handleWebhook(new HashMap<String, List<String>>(), "Body");
        verify(transport).handleServerSentEvent(ArgumentMatchers.<String, List<String>>anyMap(), (String) isNull(), (String) isNull(), anyString());
    }

    @Test
    public void handleWebhookPassesPathToTransport() throws Exception {
        client.handleWebhook(new HashMap<String, List<String>>(), "body", "method", "path");
        verify(transport).handleServerSentEvent(ArgumentMatchers.<String, List<String>>anyMap(), anyString(), eq("path"), anyString());
    }

    @Test
    public void handleWebhookPassesMethodToTransport() throws Exception {
        client.handleWebhook(new HashMap<String, List<String>>(), "body", "method", "path");
        verify(transport).handleServerSentEvent(ArgumentMatchers.<String, List<String>>anyMap(), eq("method"), anyString(), anyString());
    }

    @Test
    public void handleWebhookPassesBodyToTransport() throws Exception {
        client.handleWebhook(new HashMap<String, List<String>>(), "body", "method", "path");
        verify(transport).handleServerSentEvent(ArgumentMatchers.<String, List<String>>anyMap(), anyString(), anyString(), eq("body"));
    }

    @Test
    public void handleWebhookPassesHeaders() throws Exception {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("key", Arrays.asList("value", "value"));
        client.handleWebhook(expected, "body", "method", "path");
        verify(transport).handleServerSentEvent(eq(expected), anyString(), eq("path"), anyString());
    }

    @Test
    public void whenNullReturnedByTransportNullIsReturned() throws Exception{
        WebhookPackage actual = client.handleWebhook(null, null, null, null);
        assertNull(actual);
    }

    @Test(expected = InvalidRequestException.class)
    public void whenAnUnknownWebPackageIsReturnedInvalidRequestIsThrown() throws Exception {
        ServerSentEvent webhookPackage = new ServerSentEvent() {};
        when(transport.handleServerSentEvent(ArgumentMatchers.<String, List<String>>anyMap(), anyString(), anyString(), anyString())).thenReturn(webhookPackage);
        client.handleWebhook(new HashMap<String, List<String>>(), "body", "method", "path");
    }

    @Test
    public void handleWebhookPassesAuthorizationRequestIdWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        UUID expected = UUID.randomUUID();
        when(serverSentEventAuthorizationResponse.getAuthorizationRequestId()).thenReturn(expected);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected.toString(), actual.getAuthorizationResponse().getAuthorizationRequestId());
    }

    @Test
    public void handleWebhookPassesAuthorizationResponseWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        when(serverSentEventAuthorizationResponse.getResponse()).thenReturn(true);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertTrue(actual.getAuthorizationResponse().isAuthorized());
    }

    @Test
    public void handleWebhookPassesServiceUserHashWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        String expected = "Service User Hash";
        when(serverSentEventAuthorizationResponse.getServiceUserHash()).thenReturn(expected);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getAuthorizationResponse().getServiceUserHash());
    }

    @Test
    public void handleWebhookPassesOrganizationUserHashWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        String expected = "Organization User Hash";
        when(serverSentEventAuthorizationResponse.getOrganizationUserHash()).thenReturn(expected);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getAuthorizationResponse().getOrganizationUserHash());
    }

    @Test
    public void handleWebhookPassesUserPushIdWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        String expected = "User Push ID";
        when(serverSentEventAuthorizationResponse.getUserPushId()).thenReturn(expected);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getAuthorizationResponse().getUserPushId());
    }

    @Test
    public void handleWebhookPassesDeviceIdWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        String expected = "User Push ID";
        when(serverSentEventAuthorizationResponse.getDeviceId()).thenReturn(expected);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getAuthorizationResponse().getDeviceId());
    }

    @Test
    public void handleWebhookPassesServicePinsWhenAuthResponseWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventAuthorizationResponse);
        String[] pins = {"pin1", "pin2"};
        List<String> expected = Arrays.asList(pins);
        when(serverSentEventAuthorizationResponse.getServicePins()).thenReturn(pins);
        AuthorizationResponseWebhookPackage actual = (AuthorizationResponseWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getAuthorizationResponse().getServicePins());
    }

    @Test
    public void handleWebhookPassesApiTimeWhenEndSessionWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventUserServiceSessionEnd);
        Date expected = new Date();
        when(serverSentEventUserServiceSessionEnd.getApiTime()).thenReturn(expected);
        ServiceUserSessionEndWebhookPackage actual = (ServiceUserSessionEndWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getLogoutRequested());
    }

    @Test
    public void handleWebhookPassesUserHashWhenEndSessionWebhook() throws Exception {
        when(transport.handleServerSentEvent(null, null, null, null)).thenReturn(serverSentEventUserServiceSessionEnd);
        String expected = "User hash";
        when(serverSentEventUserServiceSessionEnd.getUserHash()).thenReturn(expected);
        ServiceUserSessionEndWebhookPackage actual = (ServiceUserSessionEndWebhookPackage) client.handleWebhook(null, null, null, null);
        assertEquals(expected, actual.getServiceUserHash());
    }
}