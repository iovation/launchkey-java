package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.service.auth.AuthResponse;
import com.launchkey.sdk.service.auth.AuthResponseCallbackResponse;
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
public class AuthResponseCallbackResponseTest {

    private AuthResponse authResponse;
    private AuthResponseCallbackResponse authResponseCallbackResponse;

    @Before
    public void setUp() throws Exception {
        authResponse = new AuthResponse(null, true, null, null, null, null);
        authResponseCallbackResponse = new AuthResponseCallbackResponse(authResponse);
    }

    @After
    public void tearDown() throws Exception {
        authResponse = null;
        authResponseCallbackResponse = null;
    }

    @Test
    public void testGetAuthResponse() throws Exception {
        assertSame(authResponse, authResponseCallbackResponse.getAuthResponse());
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        AuthResponseCallbackResponse left = new AuthResponseCallbackResponse(new AuthResponse(null, true, null, null, null, null));
        AuthResponseCallbackResponse right = new AuthResponseCallbackResponse(new AuthResponse(null, true, null, null, null, null));
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthResponseCallbackResponse left = new AuthResponseCallbackResponse(new AuthResponse("left", true, null, null, null, null));
        AuthResponseCallbackResponse right = new AuthResponseCallbackResponse(new AuthResponse("right", true, null, null, null, null));
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthResponseCallbackResponse left = new AuthResponseCallbackResponse(new AuthResponse(null, true, null, null, null, null));
        AuthResponseCallbackResponse right = new AuthResponseCallbackResponse(new AuthResponse(null, true, null, null, null, null));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthResponseCallbackResponse left = new AuthResponseCallbackResponse(new AuthResponse("left", true, null, null, null, null));
        AuthResponseCallbackResponse right = new AuthResponseCallbackResponse(new AuthResponse("right", true, null, null, null, null));
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authResponseCallbackResponse.toString(), containsString(AuthResponseCallbackResponse.class.getSimpleName()));
    }
}
