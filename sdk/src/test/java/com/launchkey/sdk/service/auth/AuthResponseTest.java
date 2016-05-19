package com.launchkey.sdk.service.auth;

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
public class AuthResponseTest {

    private AuthResponse authResponse;

    @Before
    public void setUp() throws Exception {
        authResponse = new AuthResponse(
                "Auth Request ID",
                true,
                "User Hash",
                "Organization User ID",
                "User Push ID",
                "Device ID"
        );
    }

    @After
    public void tearDown() throws Exception {
        authResponse = null;
    }

    @Test
    public void testGetAuthRequestId() throws Exception {
        assertEquals("Auth Request ID", authResponse.getAuthRequestId());
    }

    @Test
    public void testIsAuthorized() throws Exception {
        assertTrue(authResponse.isAuthorized());
    }

    @Test
    public void testGetUserHash() throws Exception {
        assertEquals("User Hash", authResponse.getUserHash());
    }

    @Test
    public void testGetOrganizationUserId() throws Exception {
        assertEquals("Organization User ID", authResponse.getOrganizationUserId());
    }

    @Test
    public void testGetUserPushId() throws Exception {
        assertEquals("User Push ID", authResponse.getUserPushId());
    }

    @Test
    public void testGetDeviceId() throws Exception {
        assertEquals("Device ID", authResponse.getDeviceId());
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        AuthResponse left = new AuthResponse(null, true, null, null, null, null);
        AuthResponse right = new AuthResponse(null, true, null, null, null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthResponse left = new AuthResponse("left", true, null, null, null, null);
        AuthResponse right = new AuthResponse("right", true, null, null, null, null);
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthResponse left = new AuthResponse(null, true, null, null, null, null);
        AuthResponse right = new AuthResponse(null, true, null, null, null, null);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthResponse left = new AuthResponse("left", true, null, null, null, null);
        AuthResponse right = new AuthResponse("right", true, null, null, null, null);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authResponse.toString(), containsString(AuthResponse.class.getSimpleName()));
    }
}
