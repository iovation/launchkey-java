package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ServerSentEventAuthorizationResponseCoreTest {
    @Test
    public void testAuth() {
        assertEquals("Auth", new ServerSentEventAuthorizationResponseCore("Auth", null, null, null, null).getAuth());
    }

    @Test
    public void testUserPushId() {
        assertEquals("UPID", new ServerSentEventAuthorizationResponseCore(null, "UPID", null, null, null).getUserPushId());
    }

    @Test
    public void testServiceUserHash() {
        assertEquals("SUH", new ServerSentEventAuthorizationResponseCore(null, null, "SUH", null, null).getServiceUserHash());
    }

    @Test
    public void testOrgUserHash() {
        assertEquals("OUH", new ServerSentEventAuthorizationResponseCore(null, null, null, null, "OUH").getOrgUserHash());
    }

    @Test
    public void testPublicKeyId() {
        assertEquals("PKID", new ServerSentEventAuthorizationResponseCore(null, null, null, "PKID", null).getPublicKeyId());
    }

    @Test
    public void testParseWithAllValuesPasses() throws IOException {
        String json = "{\"auth\":\"Auth\", \"user_push_id\":\"UPID\",\"service_user_hash\":\"SUH\"," +
                "\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"}";
        ServerSentEventAuthorizationResponseCore expected = new ServerSentEventAuthorizationResponseCore("" +
                "Auth", "UPID", "SUH", "PKID", "OUH");
        ServerSentEventAuthorizationResponseCore actual = new ObjectMapper().readValue(
                json, ServerSentEventAuthorizationResponseCore.class);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseWithoutAuthRaisesException() throws IOException {
        String json = "{\"user_push_id\":\"UPID\",\"service_user_hash\":\"SUH\"," +
                "\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"}";
        new ObjectMapper().readValue(json, ServerSentEventAuthorizationResponseCore.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseWithoutUsePushIdhRaisesException() throws IOException {
        String json = "{\"auth\":\"Auth\",\"service_user_hash\":\"SUH\"," +
                "\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"}";
        new ObjectMapper().readValue(json, ServerSentEventAuthorizationResponseCore.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseWithoutServiceUserHashRaisesException() throws IOException {
        String json = "{\"auth\":\"Auth\", \"user_push_id\":\"UPID\"," +
                "\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"}";
        new ObjectMapper().readValue(json, ServerSentEventAuthorizationResponseCore.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseWithoutPublicKeyIdRaisesException() throws IOException {
        String json = "{\"auth\":\"Auth\", \"user_push_id\":\"UPID\",\"service_user_hash\":\"SUH\"," +
                "\"org_user_hash\":\"OUH\"}";
        new ObjectMapper().readValue(json, ServerSentEventAuthorizationResponseCore.class);
    }

    @Test
    public void testParseWithoutOrgUserHashIsNull() throws IOException {
        String json = "{\"auth\":\"Auth\", \"user_push_id\":\"UPID\",\"service_user_hash\":\"SUH\"," +
                "\"public_key_id\":\"PKID\"}";
        assertNull(new ObjectMapper().readValue(json, ServerSentEventAuthorizationResponseCore.class).getOrgUserHash());
    }

    @Test
    public void testParseWithAdditionalFieldDoesEffectParsing() throws IOException {
        String json = "{\"auth\":\"Auth\", \"user_push_id\":\"UPID\",\"service_user_hash\":\"SUH\"," +
                "\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"," +
                "\"UNKNOWN_FIELD\":null}";
        ServerSentEventAuthorizationResponseCore expected = new ServerSentEventAuthorizationResponseCore("" +
                "Auth", "UPID", "SUH", "PKID", "OUH");
        ServerSentEventAuthorizationResponseCore actual = new ObjectMapper().readValue(
                json, ServerSentEventAuthorizationResponseCore.class);
        assertEquals(expected, actual);
    }
}