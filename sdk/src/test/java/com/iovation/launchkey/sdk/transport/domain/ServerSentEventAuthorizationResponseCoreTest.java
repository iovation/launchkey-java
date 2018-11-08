package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerSentEventAuthorizationResponseCoreTest {

    @Test
    public void getEncryptedDeviceResponse() {
        ServerSentEventAuthorizationResponseCore actual = new ServerSentEventAuthorizationResponseCore(
                "Auth Value", "JWE Auth Value", "User Push ID", "Service User Hash", "Public Key ID", "Org User Hash");
        assertEquals("Auth Value", actual.getAuth());
    }

    @Test
    public void getJweEncryptedDeviceResponse() {
        ServerSentEventAuthorizationResponseCore actual = new ServerSentEventAuthorizationResponseCore(
                "Auth Value", "JWE Auth Value", "User Push ID", "Service User Hash", "Public Key ID", "Org User Hash");
        assertEquals("JWE Auth Value", actual.getAuthJwe());
    }

    @Test
    public void verifyJsonParseWithoutJweResponse() throws Exception {
        String json = "{\"auth\":\"Auth Value\",\"user_push_id\":\"User Push ID\"," +
                "\"service_user_hash\":\"Service User Hash\",\"public_key_id\":\"Public Key ID\"," +
                "\"org_user_hash\":\"Org User Hash\"}";
        ServerSentEventAuthorizationResponseCore actual = new ObjectMapper().readValue(
                json, ServerSentEventAuthorizationResponseCore.class);
        ServerSentEventAuthorizationResponseCore expected = new ServerSentEventAuthorizationResponseCore(
                "Auth Value", null, "User Push ID", "Service User Hash", "Public Key ID", "Org User Hash");
        assertEquals(expected, actual);
    }

    @Test
    public void verifyJsonParseWithJweResponse() throws Exception {
        String json = "{\"auth\":\"Auth Value\",\"user_push_id\":\"User Push ID\"," +
                "\"service_user_hash\":\"Service User Hash\",\"public_key_id\":\"Public Key ID\"," +
                "\"org_user_hash\":\"Org User Hash\",\"auth_jwe\":\"JWE Auth Value\"}";
        ServerSentEventAuthorizationResponseCore actual = new ObjectMapper().readValue(
                json, ServerSentEventAuthorizationResponseCore.class);
        ServerSentEventAuthorizationResponseCore expected = new ServerSentEventAuthorizationResponseCore(
                "Auth Value", "JWE Auth Value", "User Push ID", "Service User Hash", "Public Key ID", "Org User Hash");
        assertEquals(expected, actual);
    }

}