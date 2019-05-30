package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceV3AuthsGetResponseDeviceJWETest {

    @Test
    public void getType() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertEquals("Type", actual.getType());
    }

    @Test
    public void getReason() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertEquals("Reason", actual.getReason());
    }

    @Test
    public void getDenialReason() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertEquals("Denial Reason", actual.getDenialReason());
    }

    @Test
    public void getAuthorizationRequestId() {
        UUID expected = UUID.randomUUID();
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", expected,"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertEquals(expected, actual.getAuthorizationRequestId());
    }

    @Test
    public void getDeviceId() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertEquals("Device ID", actual.getDeviceId());
    }

    @Test
    public void getServicePins() {
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, null);
        assertArrayEquals(new String[]{"1", "2", "3"}, actual.getServicePins());
    }

    @Test
    public void getAuthPolicy() {
        AuthPolicy expected = new AuthPolicy(2, true, false, true, null);
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, expected, null);
        assertEquals(expected, actual.getAuthPolicy());
    }

    @Test
    public void getAuthMethods() {
        AuthMethod[] expected = new AuthMethod[]{
                new AuthMethod("Method True", true, true, true, true, true, true, true),
                new AuthMethod("Method False", false, false, false, false, false, false, false),
                new AuthMethod("Method Null", null, null, null, null, null, null, null),
        };
        ServiceV3AuthsGetResponseDeviceJWE actual = new ServiceV3AuthsGetResponseDeviceJWE("Type", "Reason",
                "Denial Reason", UUID.randomUUID(),"Device ID", new String[]{"1", "2", "3"}, null, expected);
        assertArrayEquals(expected, actual.getAuthMethods());
    }

    @Test
    public void verifyJsonParseAuthPolicyRequirementIsTypes() throws Exception {
        String json = "{" +
                "    \"type\": \"DENIED\"," +
                "    \"reason\": \"FRAUDULENT\"," +
                "    \"denial_reason\": \"DEN2\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"," +
                "    \"auth_policy\": {" +
                "        \"requirement\": \"types\"," +
                "        \"types\": [\"knowledge\", \"inherence\", \"possession\"]," +
                "        \"geofences\": [" +
                "               {" +
                "                   \"name\": null," +
                "                   \"latitude\": 36.120825," +
                "                   \"longitude\": -115.157216," +
                "                   \"radius\": 200" +
                "               }," +
                "               {" +
                "                   \"name\": \"HQ North\"," +
                "                   \"latitude\": 36.121020," +
                "                   \"longitude\": -115.156460," +
                "                   \"radius\": 550" +
                "               }" +
                "        ]" +
                "    }," +
                "    \"auth_methods\": [" +
                "        {" +
                "             \"method\": \"wearables\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        },  " +
                "        {" +
                "             \"method\": \"geofencing\"," +
                "             \"set\": null," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"locations\"," +
                "             \"set\": true," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": true," +
                "             \"passed\": true," +
                "             \"error\": false" +
                "        }," +
                "        {    " +
                "             \"method\": \"pin_code\"," +
                "             \"set\": true," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": true," +
                "             \"passed\": true," +
                "             \"error\": false" +
                "        }," +
                "        {" +
                "             \"method\": \"circle_code\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"face\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"fingerprint\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }" +
                "    ]" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        ServiceV3AuthsGetResponseDeviceJWE expected = new ServiceV3AuthsGetResponseDeviceJWE("DENIED", "FRAUDULENT",
                "DEN2", UUID.fromString("5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5"), "c07c4907-dc67-11e7-bb14-0469f8dc10a5",
                new String[]{"2648", "2046", "0583", "2963", "2046"},
                new AuthPolicy(null, null, "types", Arrays.asList("knowledge", "inherence", "possession"), null,
                        Arrays.asList(
                                new AuthPolicy.Location(null, 200, 36.120825, -115.157216),
                                new AuthPolicy.Location("HQ North", 550, 36.121020, -115.156460)
                        )
                ),
                new AuthMethod[]{
                        new AuthMethod("wearables", false, false, true, true, null, null, null),
                        new AuthMethod("geofencing", null, true, true, true, null, null, null),
                        new AuthMethod("locations", true, true, true, true, true, true, false),
                        new AuthMethod("pin_code", true, true, true, true, true, true, false),
                        new AuthMethod("circle_code", false, false, true, true, null, null, null),
                        new AuthMethod("face", false, false, true, true, null, null, null),
                        new AuthMethod("fingerprint", false, false, true, true, null, null, null)
                }
        );
        assertEquals(expected, actual);
    }

    @Test
    public void verifyJsonParseAuthPolicyRequirement() throws Exception {
        String json = "{" +
                "    \"type\": \"DENIED\"," +
                "    \"reason\": \"FRAUDULENT\"," +
                "    \"denial_reason\": \"DEN2\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"," +
                "    \"auth_policy\": {" +
                "        \"requirement\": \"amount\"," +
                "        \"amount\": 2," +
                "        \"geofences\": [" +
                "               {" +
                "                   \"name\": null," +
                "                   \"latitude\": 36.120825," +
                "                   \"longitude\": -115.157216," +
                "                   \"radius\": 200" +
                "               }," +
                "               {" +
                "                   \"name\": \"HQ North\"," +
                "                   \"latitude\": 36.121020," +
                "                   \"longitude\": -115.156460," +
                "                   \"radius\": 550" +
                "               }" +
                "        ]" +
                "    }," +
                "    \"auth_methods\": [" +
                "        {" +
                "             \"method\": \"wearables\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        },  " +
                "        {" +
                "             \"method\": \"geofencing\"," +
                "             \"set\": null," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"locations\"," +
                "             \"set\": true," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": true," +
                "             \"passed\": true," +
                "             \"error\": false" +
                "        }," +
                "        {    " +
                "             \"method\": \"pin_code\"," +
                "             \"set\": true," +
                "             \"active\": true," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": true," +
                "             \"passed\": true," +
                "             \"error\": false" +
                "        }," +
                "        {" +
                "             \"method\": \"circle_code\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"face\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }," +
                "        {" +
                "             \"method\": \"fingerprint\"," +
                "             \"set\": false," +
                "             \"active\": false," +
                "             \"allowed\": true," +
                "             \"supported\": true," +
                "             \"user_required\": null," +
                "             \"passed\": null," +
                "             \"error\": null" +
                "        }" +
                "    ]" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        ServiceV3AuthsGetResponseDeviceJWE expected = new ServiceV3AuthsGetResponseDeviceJWE("DENIED", "FRAUDULENT",
                "DEN2", UUID.fromString("5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5"), "c07c4907-dc67-11e7-bb14-0469f8dc10a5",
                new String[]{"2648", "2046", "0583", "2963", "2046"},
                new AuthPolicy(null, null, "amount", null, 2,
                        Arrays.asList(
                                new AuthPolicy.Location(null, 200, 36.120825, -115.157216),
                                new AuthPolicy.Location("HQ North", 550, 36.121020, -115.156460)
                        )
                ),
                new AuthMethod[]{
                        new AuthMethod("wearables", false, false, true, true, null, null, null),
                        new AuthMethod("geofencing", null, true, true, true, null, null, null),
                        new AuthMethod("locations", true, true, true, true, true, true, false),
                        new AuthMethod("pin_code", true, true, true, true, true, true, false),
                        new AuthMethod("circle_code", false, false, true, true, null, null, null),
                        new AuthMethod("face", false, false, true, true, null, null, null),
                        new AuthMethod("fingerprint", false, false, true, true, null, null, null)
                }
        );
        assertEquals(expected, actual);
    }

    @Test
    public void verifyJsonParseWithEmptyPolicyBuildsEmptyPolicy() throws Exception {
        String json = "{" +
                "    \"type\": \"FAILED\"," +
                "    \"reason\": \"CONFIGURATION\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"," +
                "    \"auth_policy\": {}" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        assertEquals(new AuthPolicy(null, null, null, null, null), actual.getAuthPolicy());
    }

    @Test
    public void verifyJsonParseWithNoPolicyReturnsNullForPolicy() throws Exception {
        String json = "{" +
                "    \"type\": \"FAILED\"," +
                "    \"reason\": \"CONFIGURATION\"," +
                "    \"device_id\": \"c07c4907-dc67-11e7-bb14-0469f8dc10a5\"," +
                "    \"service_pins\": [\"2648\", \"2046\", \"0583\", \"2963\", \"2046\"]," +
                "    \"auth_request\": \"5d1acf5c-dc5d-11e7-9ea1-0469f8dc10a5\"," +
                "    \"auth_policy\": null" +
                "}";
        ServiceV3AuthsGetResponseDeviceJWE actual = new ObjectMapper().readValue(json, ServiceV3AuthsGetResponseDeviceJWE.class);
        assertNull(actual.getAuthPolicy());
    }

    @Test
    public void verifyJsonParseWithoutDenialReasonAndAuthMethodInsight() throws Exception {
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
                new String[]{"2648", "2046", "0583", "2963", "2046"},
                null,
                null
        );
        assertEquals(expected, actual);
    }
}