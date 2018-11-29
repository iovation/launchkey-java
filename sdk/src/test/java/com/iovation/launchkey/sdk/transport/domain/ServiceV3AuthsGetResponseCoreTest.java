package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceV3AuthsGetResponseCoreTest {

    @Test
    public void getEncryptedDeviceResponse() {
        ServiceV3AuthsGetResponseCore actual = new ServiceV3AuthsGetResponseCore("Auth Value", "JWE Auth Value",
                "Service User Hash", "Org User Hash","User Push ID", "Public Key ID");
        assertEquals("Auth Value", actual.getEncryptedDeviceResponse());
    }

    @Test
    public void getJweEncryptedDeviceResponse() {
        ServiceV3AuthsGetResponseCore actual = new ServiceV3AuthsGetResponseCore("Auth Value", "JWE Auth Value",
                "Service User Hash", "Org User Hash","User Push ID", "Public Key ID");
        assertEquals("JWE Auth Value", actual.getJweEncryptedDeviceResponse());
    }

    @Test
    public void verifyJsonParseWithoutJweResponse() throws Exception {
        String json = "{\"auth\":\"Auth Value\",\"user_push_id\":\"User Push ID\"," +
                "\"service_user_hash\":\"Service User Hash\",\"public_key_id\":\"Public Key ID\"," +
                "\"org_user_hash\":\"Org User Hash\"}";
        ServiceV3AuthsGetResponseCore actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseCore.class);
        ServiceV3AuthsGetResponseCore expected = new ServiceV3AuthsGetResponseCore("Auth Value",
                null, "Service User Hash", "Org User Hash",
                "User Push ID", "Public Key ID");
        assertEquals(expected, actual);
    }

    @Test
    public void verifyJsonParseWithJweResponse() throws Exception {
        String json = "{\"auth\":\"Auth Value\",\"user_push_id\":\"User Push ID\"," +
                "\"service_user_hash\":\"Service User Hash\",\"public_key_id\":\"Public Key ID\"," +
                "\"org_user_hash\":\"Org User Hash\",\"auth_jwe\":\"JWE Auth Value\"}";
        ServiceV3AuthsGetResponseCore actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseCore.class);
        ServiceV3AuthsGetResponseCore expected = new ServiceV3AuthsGetResponseCore("Auth Value",
                "JWE Auth Value", "Service User Hash", "Org User Hash","User Push ID", "Public Key ID");
        assertEquals(expected, actual);
    }
}