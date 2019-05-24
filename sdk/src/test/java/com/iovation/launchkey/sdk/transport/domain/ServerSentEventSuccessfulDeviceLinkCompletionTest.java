package com.iovation.launchkey.sdk.transport.domain;

import com.apple.eawt.AppEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class ServerSentEventSuccessfulDeviceLinkCompletionTest {

    private UUID deviceId = null;
    private ServerSentEventSuccessfulDeviceLinkCompletion event = null;

    @Before
    public void setUp() throws Exception {
        event = new ServerSentEventSuccessfulDeviceLinkCompletion("Device ID", "Key ID", "Public Key");
    }

    @Test
    public void getDeviceId() {
        assertEquals("Device ID", event.getDeviceId());
    }

    @Test
    public void getPublicKeyId() {
        assertEquals("Key ID", event.getPublicKeyId());
    }

    @Test
    public void getPublicKey() {
        assertEquals("Public Key", event.getPublicKey());
    }

    @Test
    public void canParse() throws Exception {
        String json = "{\"device_id\":\"Device ID\",\"public_key_id\":\"Key ID\",\"public_key\":\"Public Key\"}";
        ServerSentEventSuccessfulDeviceLinkCompletion actual = new ObjectMapper().readValue(json, ServerSentEventSuccessfulDeviceLinkCompletion.class);
        assertEquals(event, actual);
    }
}