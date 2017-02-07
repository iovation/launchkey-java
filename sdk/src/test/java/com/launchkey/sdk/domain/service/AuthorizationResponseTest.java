package com.launchkey.sdk.domain.service;

import com.launchkey.sdk.domain.service.AuthorizationResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

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
public class AuthorizationResponseTest {

    private AuthorizationResponse authorizationResponse;

    @Before
    public void setUp() throws Exception {
        authorizationResponse = new AuthorizationResponse(
                "Auth Request ID",
                true,
                "User Hash",
                "Organization User ID",
                "User Push ID",
                "Device ID",
                Arrays.asList("pin1", "pin2")
            );
    }

    @After
    public void tearDown() throws Exception {
        authorizationResponse = null;
    }

    @Test
    public void testGetAuthRequestId() throws Exception {
        assertEquals("Auth Request ID", authorizationResponse.getAuthorizationRequestId());
    }

    @Test
    public void testIsAuthorized() throws Exception {
        assertTrue(authorizationResponse.isAuthorized());
    }

    @Test
    public void testGetUserHash() throws Exception {
        assertEquals("User Hash", authorizationResponse.getServiceUserHash());
    }

    @Test
    public void testGetOrganizationUserId() throws Exception {
        assertEquals("Organization User ID", authorizationResponse.getOrganizationUserHash());
    }

    @Test
    public void testGetUserPushId() throws Exception {
        assertEquals("User Push ID", authorizationResponse.getUserPushId());
    }

    @Test
    public void testGetDeviceId() throws Exception {
        assertEquals("Device ID", authorizationResponse.getDeviceId());
    }

    @Test
    public void testGetServicePins() throws Exception {
        assertEquals(Arrays.asList("pin1", "pin2"), authorizationResponse.getServicePins());
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        AuthorizationResponse left = new AuthorizationResponse(null, true, null, null, null, null, null);
        AuthorizationResponse right = new AuthorizationResponse(null, true, null, null, null, null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthorizationResponse left = new AuthorizationResponse("left", true, null, null, null, null, null);
        AuthorizationResponse right = new AuthorizationResponse("right", true, null, null, null, null, null);
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthorizationResponse left = new AuthorizationResponse(null, true, null, null, null, null, null);
        AuthorizationResponse right = new AuthorizationResponse(null, true, null, null, null, null, null);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthorizationResponse left = new AuthorizationResponse("left", true, null, null, null, null, null);
        AuthorizationResponse right = new AuthorizationResponse("right", true, null, null, null, null, null);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authorizationResponse.toString(), containsString(AuthorizationResponse.class.getSimpleName()));
    }
}
