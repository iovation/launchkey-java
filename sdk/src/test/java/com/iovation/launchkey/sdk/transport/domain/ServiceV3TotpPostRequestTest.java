package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceV3TotpPostRequestTest {

    private ServiceV3TotpPostRequest transport;

    @Before
    public void setUp() throws Exception {
        transport = new ServiceV3TotpPostRequest("identifier", "otp");
    }

    @Test
    public void getUser() {
        assertEquals("identifier", transport.getIdentifier());
    }

    @Test
    public void getTotp() {
        assertEquals("otp", transport.getOtp());
    }

    @Test
    public void equalObjectAreEqual() {
        assertEquals(transport, new ServiceV3TotpPostRequest("identifier", "otp"));
    }

    @Test
    public void differentIdentifiersAreNotEqual() {
        assertNotEquals(transport, new ServiceV3TotpPostRequest("otheridentifier", "otp"));
    }

    @Test
    public void differentOtpAreNotEqual() {
        assertNotEquals(transport, new ServiceV3TotpPostRequest("identifier", "otherotp"));
    }

    @Test
    public void equalObjectAreHashEqual() {
        assertEquals(transport.hashCode(), new ServiceV3TotpPostRequest("identifier", "otp").hashCode());
    }

    @Test
    public void differentIdentifiersAreNotHashEqual() {
        assertNotEquals(transport.hashCode(), new ServiceV3TotpPostRequest("otheridentifier", "otp").hashCode());
    }

    @Test
    public void differentOtpAreNotHashEqual() {
        assertNotEquals(transport.hashCode(), new ServiceV3TotpPostRequest("identifier", "otherotp").hashCode());
    }

    @Test
    public void toStringHasClassName() {
        assertTrue(transport.toString().contains(transport.getClass().getSimpleName()));
    }

    @Test
    public void canMarshallJson() throws JsonProcessingException {
        String expectedJson = "{\"identifier\":\"identifier\",\"otp\":\"otp\"}";
        String actualJson = (new ObjectMapper()).writeValueAsString(transport);
        assertEquals(expectedJson, actualJson);
    }
}