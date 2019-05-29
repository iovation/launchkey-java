package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.directory.DeviceLinkCompletion;
import com.iovation.launchkey.sdk.domain.webhook.SuccessfulDeviceLinkCompletionWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.InvalidRequestException;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEvent;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEventSuccessfulDeviceLinkCompletion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientHandleWebhookTest {

    @Mock
    private Transport transport;
    private static final UUID directoryId = UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64");
    private final static Map<String, List<String>> emptyHeaders = Collections.unmodifiableMap(new HashMap<String, List<String>>());
    private DirectoryClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
    }

    @After
    public void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void handleWebhookWithoutMethodAndPathPassesNullForMethodAndPathToTransport() throws Exception {
        client.handleWebhook(emptyHeaders, "body");
        verify(transport).handleServerSentEvent(any(Map.class), (String) eq(null), (String) eq(null), anyString());
    }

    @Test
    public void handleWebhookSendsExpectedHeadersToTransport() throws Exception {
        Map<String, List<String>> headers = Collections.unmodifiableMap(new HashMap() {{
            put("One", Collections.unmodifiableList(new ArrayList() {{
                add("A");
                add("B");
            }}));
        }});
        client.handleWebhook(headers, "body", "method", "path");
        verify(transport).handleServerSentEvent(eq(headers), anyString(), anyString(), anyString());
    }

    @Test
    public void handleWebhookSendsExpectedBodyToTransport() throws Exception {
        client.handleWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), anyString(), anyString(), eq("body"));
    }

    @Test
    public void handleWebhookSendsExpectedMethodToTransport() throws Exception {
        client.handleWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), eq("method"), anyString(), anyString());
    }

    @Test
    public void handleWebhookSendsExpectedPathToTransport() throws Exception {
        client.handleWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), anyString(), eq("path"), anyString());
    }

    @Test
    public void handleWebhookReturnsNullWhenTransportReturnsNull() throws Exception {
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(null);
        WebhookPackage actual = client.handleWebhook(emptyHeaders, "body", "method", "path");
        assertNull(actual);
    }

    @Test
    public void handleWebhookReturnsExpectedDeviceLinkingResponseWhenWebhookReturnsSuccessfulDeviceLinkCompletionPackage() throws Exception {
        UUID deviceId = UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64");
        String publicKey = "Public Key";
        String publicKeyId = "Public Key ID";
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(
                new ServerSentEventSuccessfulDeviceLinkCompletion(deviceId, publicKeyId, publicKey));
        DeviceLinkCompletion expected = new DeviceLinkCompletion(deviceId, publicKey, publicKeyId);
        DeviceLinkCompletion actual = ((SuccessfulDeviceLinkCompletionWebhookPackage) client.handleWebhook(
                emptyHeaders, "body", "method", "path")).getDeviceLinkCompletion();
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidRequestException.class)
    public void handleWebhookRaisesInvalidRequestExceptionWhenWebhookReturnsOtherSSEPackage() throws Exception {
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(mock(ServerSentEvent.class));
        client.handleWebhook(emptyHeaders, "body", "method", "path");
    }
}