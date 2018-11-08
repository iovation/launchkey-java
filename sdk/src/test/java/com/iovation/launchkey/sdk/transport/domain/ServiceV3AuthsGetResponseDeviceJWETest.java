package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceV3AuthsGetResponseDeviceJWETest {

    @Test
    public void getType() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"});
        assertEquals("Type", actual.getType());
    }

    @Test
    public void getReason() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"});
        assertEquals("Reason", actual.getReason());
    }

    @Test
    public void getDenialReason() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"});
        assertEquals("Denial Reason", actual.getDenialReason());
    }

    @Test
    public void getAuthorizationRequestId() {
        UUID expected = UUID.randomUUID();
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", expected,"Device ID", new String[]{"1", "2", "3"});
        assertEquals(expected, actual.getAuthorizationRequestId());
    }

    @Test
    public void getDeviceId() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"});
        assertEquals("Device ID", actual.getDeviceId());
    }

    @Test
    public void getServicePins() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"});
        assertArrayEquals(new String[]{"1", "2", "3"}, actual.getServicePins());
    }

    @Test
    public void verifyJsonParse() throws Exception {
        String json = "{" +
                "    \"type\": \"DENIED\"," +
                "    \"reason\": \"FRAUDULENT\"," +
                "    \"denial_reason\": \"DEN2\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        ServiceV3AuthsGetResponseDeviceJWE expected = new ServiceV3AuthsGetResponseDeviceJWE("DENIED", "FRAUDULENT",
                "DEN2", UUID.fromString("5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5"), "c07c4907-dc67-11e7-bb14-0469f8dc10a5",
                new String[]{"2648", "2046", "0583", "2963", "2046"});
        assertEquals(expected, actual);
    }

    @Test
    public void verifyJsonParseWithoutDenialReason() throws Exception {
        String json = "{" +
                "    \"type\": \"FAILED\"," +
                "    \"reason\": \"CONFIGURATION\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        ServiceV3AuthsGetResponseDeviceJWE expected = new ServiceV3AuthsGetResponseDeviceJWE("FAILED", "CONFIGURATION",
                null, UUID.fromString("5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5"), "c07c4907-dc67-11e7-bb14-0469f8dc10a5",
                new String[]{"2648", "2046", "0583", "2963", "2046"});
        assertEquals(expected, actual);
    }
}