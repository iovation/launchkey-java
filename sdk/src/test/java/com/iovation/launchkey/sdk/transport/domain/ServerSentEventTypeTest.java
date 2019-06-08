package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSentEventTypeTest {

    @Test
    public void getType() {
        assertEquals("test", new ServerSentEventType("test").getType());
    }

    @Test
    public void parseWithTypeSetsType() throws Exception {
        String json = "{\"type\": \"DEVICE_LINK_COMPLETION\"," +
                "\"device_id\": \"04ca395b-1721-4876-bacc-4c914b0e4971\"," +
                "\"device_public_key\":\"-----BEGIN PUBLIC KEY-----\\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD38jQRiR5RffOHDcvU2Qp9X3qy\\n" +
                "wKTYV0tKP/OpaA61BvtKudOlE1TUQNSmBBtzKSsVeA2SQ8BAwUF7smQn+uZpT90C\\n" +
                "XMXnba7wNu7YfsmBIbjIjQ0M7xRm2z/jzVo0ltKog1RU2/5CY67Zw+wXcW7UuTCC\\n" +
                "sv+nCk+H6gDCPl4TYwIDAQAB\\n-----END PUBLIC KEY-----\"," +
                "\"device_public_key_id\": \"d2:8e:16:91:39:5b:9d:24:73:0e:36:0a:9a:ef:7e:de\"}";
        ServerSentEventType actual = new ObjectMapper().readValue(json, ServerSentEventType.class);
        assertEquals("DEVICE_LINK_COMPLETION", actual.getType());
    }

    @Test
    public void parseWithoutTypeDefaultsToAuthResponse() throws Exception {
        String json = "{\"auth\":\"Auth\", \"auth_jwe\":\"Auth JWE\", \"user_push_id\":\"UPID\"," +
                "\"service_user_hash\":\"SUH\",\"public_key_id\":\"PKID\",\"org_user_hash\":\"OUH\"}";

        ServerSentEventType actual = new ObjectMapper().readValue(json, ServerSentEventType.class);
        assertEquals("AUTHORIZATION_RESPONSE", actual.getType());
    }
}