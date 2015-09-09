package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
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
public class DeOrbitCallbackResponseTest {

    private DeOrbitCallbackResponse deOrbitCallbackResponse;

    @Before
    public void setUp() throws Exception {
        deOrbitCallbackResponse = new DeOrbitCallbackResponse("1970-01-01 00:00:00", "User Hash");
    }

    @After
    public void tearDown() throws Exception {
        deOrbitCallbackResponse = null;
    }

    @Test
    public void testGetDeOrbitTime() throws Exception {
        assertEquals(new Date(0L), deOrbitCallbackResponse.getDeOrbitTime());
    }

    @Test
    public void testGetUserHash() throws Exception {
        assertEquals("User Hash", deOrbitCallbackResponse.getUserHash());
    }

    @Test( expected = IllegalArgumentException.class)
    public void testInvalidDateFormatThrowIllegalArgumentException() throws Exception {
        new DeOrbitCallbackResponse("1970-01-01+00:00:00", "User Hash");
    }


    @Test
    public void testJSONParseable() throws Exception {
        String json = "{\"launchkey_time\":\"1970-01-01 00:00:00\",\"user_hash\":\"User Hash\"}";
        ObjectMapper mapper = new ObjectMapper();
        DeOrbitCallbackResponse actual = mapper.readValue(json, DeOrbitCallbackResponse.class);
        assertEquals(deOrbitCallbackResponse, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"launchkey_time\":\"1970-01-01 00:00:00\",\"user_hash\":\"User Hash\"," +
                "\"unknown\": \"Unknown Value\"}";
        ObjectMapper mapper = new ObjectMapper();
        DeOrbitCallbackResponse actual = mapper.readValue(json, DeOrbitCallbackResponse.class);
        assertEquals(deOrbitCallbackResponse, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        DeOrbitCallbackResponse left = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        DeOrbitCallbackResponse right = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        DeOrbitCallbackResponse left = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        DeOrbitCallbackResponse right = new DeOrbitCallbackResponse("2001-01-01 01:01:02", "User Hash");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        DeOrbitCallbackResponse left = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        DeOrbitCallbackResponse right = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        DeOrbitCallbackResponse left = new DeOrbitCallbackResponse("2001-01-01 01:01:01", "User Hash");
        DeOrbitCallbackResponse right = new DeOrbitCallbackResponse("2001-01-01 01:01:02", "User Hash");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(deOrbitCallbackResponse.toString(), containsString(DeOrbitCallbackResponse.class.getSimpleName()));
    }
}