package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.PollResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PollResponseTest {

    private PollResponse pollResponse;

    @Before
    @SuppressWarnings("SpellCheckingInspection")
    public void setUp() throws Exception {
        pollResponse = new PollResponse(
                "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                        "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                        "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                        "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                        "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                        "FhCylnZmNjrLkUjGOaVApg==",
                "5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV",
                "30xIPFOdoq2TZKpVUZohnL6aEPCorReUYPb6vrKax3B",
                "UA-d2389830-7ffb-419b-a553-c23a09a50b45"
        );
    }

    @After
    public void tearDown() throws Exception {
        pollResponse = null;
    }

    @Test
    public void testGetAuth() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String expected = "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                "FhCylnZmNjrLkUjGOaVApg==";
        assertEquals(expected, pollResponse.getAuth());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetOrganizationUser() throws Exception {
        assertEquals("30xIPFOdoq2TZKpVUZohnL6aEPCorReUYPb6vrKax3B", pollResponse.getOrganizationUser());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetUserHash() throws Exception {
        assertEquals("5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV", pollResponse.getUserHash());
    }

    @Test
    public void testGetUserPushId() throws Exception {
        assertEquals("UA-d2389830-7ffb-419b-a553-c23a09a50b45", pollResponse.getUserPushId());
    }

    @Test
    public void testJSONParsableWithNoOptionalFields() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        PollResponse expected = new PollResponse(
                "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                        "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                        "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                        "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                        "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                        "FhCylnZmNjrLkUjGOaVApg==",
                "5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV",
                null,
                null
        );
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"user_hash\": \"5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV\", " +
                "\"auth\": \"hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\\n" +
                "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\\n" +
                "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\\n" +
                "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\\n" +
                "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\\n" +
                "FhCylnZmNjrLkUjGOaVApg==\"}";
        ObjectMapper mapper = new ObjectMapper();
        PollResponse actual = mapper.readValue(json, PollResponse.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJSONParsableWithOrganizationUserOptionalField() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        PollResponse expected = new PollResponse(
                "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                        "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                        "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                        "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                        "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                        "FhCylnZmNjrLkUjGOaVApg==",
                "5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV",
                "30xIPFOdoq2TZKpVUZohnL6aEPCorReUYPb6vrKax3B",
                null
        );
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"user_hash\": \"5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV\", " +
                "\"auth\": \"hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\\n" +
                "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\\n" +
                "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\\n" +
                "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\\n" +
                "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\\n" +
                "FhCylnZmNjrLkUjGOaVApg==\", " +
                "\"organization_user\": \"30xIPFOdoq2TZKpVUZohnL6aEPCorReUYPb6vrKax3B\"}";
        ObjectMapper mapper = new ObjectMapper();
        PollResponse actual = mapper.readValue(json, PollResponse.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJSONParsableWithUserPushIdOptionalField() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        PollResponse expected = new PollResponse(
                "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                        "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                        "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                        "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                        "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                        "FhCylnZmNjrLkUjGOaVApg==",
                "5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV",
                null,
                "UA-d2389830-7ffb-419b-a553-c23a09a50b45"
        );
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"user_hash\": \"5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV\", " +
                "\"auth\": \"hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\\n" +
                "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\\n" +
                "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\\n" +
                "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\\n" +
                "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\\n" +
                "FhCylnZmNjrLkUjGOaVApg==\", " +
                "\"user_push_id\": \"UA-d2389830-7ffb-419b-a553-c23a09a50b45\"}";
        ObjectMapper mapper = new ObjectMapper();
        PollResponse actual = mapper.readValue(json, PollResponse.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        PollResponse expected = new PollResponse(
                "hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\n" +
                        "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\n" +
                        "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\n" +
                        "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\n" +
                        "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\n" +
                        "FhCylnZmNjrLkUjGOaVApg==",
                "5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV",
                null,
                null
        );
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"user_hash\": \"5VeE21s5ZVI5vY8R5Lx9zOv1XnCSReQyFidc8r1C5iV\", " +
                "\"auth\": \"hg7gSUbpI9Q3tv5sA2E285hZ76cKEsTnaioxgOSno6kWDIxCov7hgEB5pHa4g88Y\\n" +
                "kylEA7Q6IT8GfTEW6ZK/S+hzkR0L3eJJe+rGiv7wzch0sCzypTk7yOdV2N/9S+jy\\n" +
                "c/bUIAph8ICqJD97WZHqLUhljvm+zK7/dXetkPSULPvpM5J4IzUoRggEBLX6LrWF\\n" +
                "Ktw3DLtpJlayoT9ZDecOLtiiDtEoUUGtC71u9jwekoxIu7Sy5v0VzF/3Zv7j+MSZ\\n" +
                "G9XUre4VM1CsdZ0IUl12TtbdDX7hDyqFRxKJAWDsJNFsJ0bVrP7tHP6/oeobT7AT\\n" +
                "FhCylnZmNjrLkUjGOaVApg==\", \"unknown\": \"Unknown Value\"}";
        ObjectMapper mapper = new ObjectMapper();
        PollResponse actual = mapper.readValue(json, PollResponse.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        PollResponse left = new PollResponse("auth", "user hash", "org user", "user push ID");
        PollResponse right = new PollResponse("auth", "user hash", "org user", "user push ID");
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        PollResponse left = new PollResponse("auth", "user hash", "org user", "user push ID");
        PollResponse right = new PollResponse("auth2", "user hash", "org user", "user push ID");
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        PollResponse left = new PollResponse("auth", "user hash", "org user", "user push ID");
        PollResponse right = new PollResponse("auth", "user hash", "org user", "user push ID");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        PollResponse left = new PollResponse("auth", "user hash", "org user", "user push ID");
        PollResponse right = new PollResponse("auth2", "user hash", "org user", "user push ID");
        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        PollResponse pollResponse = new PollResponse("auth", "user hash", "org user", "user push ID");
        assertThat(pollResponse.toString(), containsString(PollResponse.class.getSimpleName()));
    }
}
