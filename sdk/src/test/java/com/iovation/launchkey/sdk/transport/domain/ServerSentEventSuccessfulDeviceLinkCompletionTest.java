package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ServerSentEventSuccessfulDeviceLinkCompletionTest {

    private ServerSentEventSuccessfulDeviceLinkCompletion event = null;

    @Before
    public void setUp() throws Exception {
        event = new ServerSentEventSuccessfulDeviceLinkCompletion(
                UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64"), "Key ID", "Public Key");
    }

    @Test
    public void getDeviceId() {
        assertEquals(UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64"), event.getDeviceId());
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
        String json = "{\"device_id\":\"f5de8190-8256-11e9-bc42-526af7764f64\",\"public_key_id\":\"Key ID\",\"public_key\":\"Public Key\"}";
        ServerSentEventSuccessfulDeviceLinkCompletion actual = new ObjectMapper().readValue(json, ServerSentEventSuccessfulDeviceLinkCompletion.class);
        assertEquals(event, actual);
    }
}