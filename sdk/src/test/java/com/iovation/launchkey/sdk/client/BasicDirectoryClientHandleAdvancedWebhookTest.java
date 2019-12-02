package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.directory.DeviceLinkCompletionResponse;
import com.iovation.launchkey.sdk.domain.webhook.DirectoryUserDeviceLinkCompletionWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.InvalidRequestException;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEvent;
import com.iovation.launchkey.sdk.transport.domain.ServerSentEventDeviceLinkCompletion;
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


@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientHandleAdvancedWebhookTest {

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
    public void handleWebhookSendsExpectedHeadersToTransport() throws Exception {
        Map<String, List<String>> headers = Collections.unmodifiableMap(new HashMap<String, List<String>>() {{
            put("One", Collections.unmodifiableList(new ArrayList<String>() {{
                add("A");
                add("B");
            }}));
        }});
        client.handleAdvancedWebhook(headers, "body", "method", "path");
        verify(transport).handleServerSentEvent(eq(headers), anyString(), anyString(), anyString());
    }

    @Test
    public void handleWebhookSendsExpectedBodyToTransport() throws Exception {
        client.handleAdvancedWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), anyString(), anyString(), eq("body"));
    }

    @Test
    public void handleWebhookSendsExpectedMethodToTransport() throws Exception {
        client.handleAdvancedWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), eq("method"), anyString(), anyString());
    }

    @Test
    public void handleWebhookSendsExpectedPathToTransport() throws Exception {
        client.handleAdvancedWebhook(emptyHeaders, "body", "method", "path");
        verify(transport).handleServerSentEvent(any(Map.class), anyString(), eq("path"), anyString());
    }

    @Test
    public void handleWebhookReturnsNullWhenTransportReturnsNull() throws Exception {
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(null);
        WebhookPackage actual = client.handleAdvancedWebhook(emptyHeaders, "body", "method", "path");
        assertNull(actual);
    }

    @Test
    public void handleWebhookReturnsExpectedDeviceLinkingResponseWhenWebhookReturnsSuccessfulDeviceLinkCompletionPackage() throws Exception {
        UUID deviceId = UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64");
        String publicKey = "Public Key";
        String publicKeyId = "Public Key ID";
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(
                new ServerSentEventDeviceLinkCompletion(deviceId, publicKeyId, publicKey));
        DeviceLinkCompletionResponse expected = new DeviceLinkCompletionResponse(deviceId, publicKey, publicKeyId);
        DeviceLinkCompletionResponse actual = ((DirectoryUserDeviceLinkCompletionWebhookPackage) client.handleAdvancedWebhook(
                emptyHeaders, "body", "method", "path")).getDeviceLinkCompletionResponse();
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidRequestException.class)
    public void handleWebhookRaisesInvalidRequestExceptionWhenWebhookReturnsOtherSSEPackage() throws Exception {
        when(transport.handleServerSentEvent(any(Map.class), anyString(), anyString(), anyString())).thenReturn(mock(ServerSentEvent.class));
        client.handleAdvancedWebhook(emptyHeaders, "body", "method", "path");
    }
}