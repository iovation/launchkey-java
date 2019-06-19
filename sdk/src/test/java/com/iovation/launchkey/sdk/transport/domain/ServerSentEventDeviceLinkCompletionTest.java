package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ServerSentEventDeviceLinkCompletionTest {

    private ServerSentEventDeviceLinkCompletion serverSentEventDeviceLinkCompletion = null;

    @Before
    public void setUp() throws Exception {
        serverSentEventDeviceLinkCompletion = new ServerSentEventDeviceLinkCompletion(
                UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64"), "Key ID", "Public Key");
    }

    @Test
    public void getDeviceId() {
        assertEquals(UUID.fromString("f5de8190-8256-11e9-bc42-526af7764f64"), serverSentEventDeviceLinkCompletion.getDeviceId());
    }

    @Test
    public void getPublicKeyId() {
        assertEquals("Key ID", serverSentEventDeviceLinkCompletion.getPublicKeyId());
    }

    @Test
    public void getPublicKey() {
        assertEquals("Public Key", serverSentEventDeviceLinkCompletion.getPublicKey());
    }

    @Test
    public void canParse() throws Exception {
        String json = "{\"type\":\"DEVICE_LINK_COMPLETION\"," +
                "\"device_id\":\"f5de8190-8256-11e9-bc42-526af7764f64\"," +
                "\"device_public_key\":\"Public Key\"," +
                "\"device_public_key_id\":\"Key ID\"}";
        ServerSentEventDeviceLinkCompletion actual = new ObjectMapper().readValue(json, ServerSentEventDeviceLinkCompletion.class);
        assertEquals(serverSentEventDeviceLinkCompletion, actual);
    }
}