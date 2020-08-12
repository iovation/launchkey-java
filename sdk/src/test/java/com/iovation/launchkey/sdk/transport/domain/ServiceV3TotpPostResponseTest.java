package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceV3TotpPostResponseTest {

    private ServiceV3TotpPostResponse transport;

    @Before
    public void setUp() throws Exception {
        transport = new ServiceV3TotpPostResponse(true);
    }

    @Test
    public void isValid() {
        assertTrue(transport.isValid());
    }

    @Test
    public void equalObjectsAreEqual() {
        assertEquals(transport, new ServiceV3TotpPostResponse(true));
    }

    @Test
    public void unequalObjectsAreNotEqual() {
        assertNotEquals(transport, new ServiceV3TotpPostResponse(false));
    }

    @Test
    public void equalObjectsAreHashEqual() {
        assertEquals(transport.hashCode(), new ServiceV3TotpPostResponse(true).hashCode());
    }

    @Test
    public void unequalObjectsAreNotHashEqual() {
        assertNotEquals(transport.hashCode(), new ServiceV3TotpPostResponse(false).hashCode());
    }

    @Test
    public void toStringHasClassName() {
        assertTrue(transport.toString().contains(transport.getClass().getSimpleName()));
    }

    @Test
    public void testJsonCanMarshallToObject() throws JsonProcessingException {
        String json = "{\"valid\": true}";
        ServiceV3TotpPostResponse expected = new ServiceV3TotpPostResponse(true);
        ServiceV3TotpPostResponse actual = (new ObjectMapper()).readValue(json, ServiceV3TotpPostResponse.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonCanMarshallWithNewdata() throws JsonProcessingException {
        String json = "{\"valid\": true, \"extra\": null}";
        ServiceV3TotpPostResponse expected = new ServiceV3TotpPostResponse(true);
        ServiceV3TotpPostResponse actual = (new ObjectMapper()).readValue(json, ServiceV3TotpPostResponse.class);
        assertEquals(expected, actual);
    }
}