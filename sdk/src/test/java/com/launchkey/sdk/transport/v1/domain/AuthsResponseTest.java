package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AuthsResponseTest {
    private AuthsResponse authsResponse;

    @Before
    @SuppressWarnings("SpellCheckingInspection")
    public void setUp() throws Exception {
        this.authsResponse = new AuthsResponse("4yjuyyg59cqf2s890uhhhx3vmtgv115a");
    }

    @After
    public void tearDown() throws Exception {
        this.authsResponse = null;
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetAuthRequestId() throws Exception {
        assertEquals("4yjuyyg59cqf2s890uhhhx3vmtgv115a", this.authsResponse.getAuthRequestId());
    }

    @Test
    public void testJSONParsable() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"auth_request\": \"4yjuyyg59cqf2s890uhhhx3vmtgv115a\"}";
        ObjectMapper mapper = new ObjectMapper();
        AuthsResponse actual = mapper.readValue(json, AuthsResponse.class);
        assertEquals(this.authsResponse, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String json = "{\"auth_request\": \"4yjuyyg59cqf2s890uhhhx3vmtgv115a\", \"unknown\": \"Unknown Value\"}";
        ObjectMapper mapper = new ObjectMapper();
        AuthsResponse actual = mapper.readValue(json, AuthsResponse.class);
        assertEquals(this.authsResponse, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        AuthsResponse left = new AuthsResponse("Req ID");
        AuthsResponse right = new AuthsResponse("Req ID");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthsResponse left = new AuthsResponse("Left Req ID");
        AuthsResponse right = new AuthsResponse("Right Req ID");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthsResponse left = new AuthsResponse("Req ID");
        AuthsResponse right = new AuthsResponse("Req ID");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthsResponse left = new AuthsResponse("Left Req ID");
        AuthsResponse right = new AuthsResponse("Right Req ID");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(this.authsResponse.toString(), containsString(AuthsResponse.class.getSimpleName()));
    }
}