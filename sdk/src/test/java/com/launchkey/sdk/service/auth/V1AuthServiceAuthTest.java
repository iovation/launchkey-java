package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class V1AuthServiceAuthTest {
    ObjectMapper mapper;
    V1AuthService.Auth auth;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        auth = new V1AuthService.Auth("Auth Request ID", true, "Device ID");
    }

    @After
    public void tearDown() throws Exception {
        mapper = null;
        auth = null;
    }

    @Test
    public void testGetAuthRequestId() throws Exception {
        assertEquals("Auth Request ID", auth.getAuthRequestId());
    }

    @Test
    public void testIsAuthorized() throws Exception {
        assertTrue(auth.isAuthorized());
    }

    @Test
    public void testGetDeviceId() throws Exception {
        assertEquals("Device ID", auth.getDeviceId());
    }

    @Test
    public void testJSONParsable() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"response\": true," +
                "\"auth_request\":\"dopkpq2oqhf9ej8uuud21geh6bwi9394\"," +
                "\"device_id\":\"dsf67\"}";
        ObjectMapper mapper = new ObjectMapper();
        V1AuthService.Auth actual = mapper.readValue(json, V1AuthService.Auth.class);
        assertNotNull(actual);
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"response\": true," +
                "\"app_pins\" : \"8106,6367,2124,6585,2357\"," +
                "\"auth_request\" : \"dopkpq2oqhf9ej8uuud21geh6bwi9394\"," +
                "\"device_id\" : \"dsf67\"}";
        ObjectMapper mapper = new ObjectMapper();
        AuthsResponse actual = mapper.readValue(json, AuthsResponse.class);
        assertNotNull(actual);
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParseSetsAuthRequestId() throws Exception {
        String json = "{\"response\": true," +
                "\"auth_request\":\"dopkpq2oqhf9ej8uuud21geh6bwi9394\"," +
                "\"device_id\":\"dsf67\"}";
        ObjectMapper mapper = new ObjectMapper();
        V1AuthService.Auth actual = mapper.readValue(json, V1AuthService.Auth.class);
        assertEquals("dopkpq2oqhf9ej8uuud21geh6bwi9394", actual.getAuthRequestId());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParseSetsDeviceId() throws Exception {
        String json = "{\"response\": true," +
                "\"auth_request\":\"dopkpq2oqhf9ej8uuud21geh6bwi9394\"," +
                "\"device_id\":\"dsf67\"}";
        ObjectMapper mapper = new ObjectMapper();
        V1AuthService.Auth actual = mapper.readValue(json, V1AuthService.Auth.class);
        assertEquals("dsf67", actual.getDeviceId());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParseSetsResponse() throws Exception {
        String json = "{\"response\": true," +
                "\"auth_request\":\"dopkpq2oqhf9ej8uuud21geh6bwi9394\"," +
                "\"device_id\":\"dsf67\"}";
        ObjectMapper mapper = new ObjectMapper();
        V1AuthService.Auth actual = mapper.readValue(json, V1AuthService.Auth.class);
        assertTrue(actual.isAuthorized());
    }
}