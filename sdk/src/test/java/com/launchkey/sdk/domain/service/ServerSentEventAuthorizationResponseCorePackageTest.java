package com.launchkey.sdk.domain.service;

import com.launchkey.sdk.domain.sse.AuthorizationResponseServerSentEventPackage;
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
public class ServerSentEventAuthorizationResponseCorePackageTest {

    private AuthorizationResponse authorizationResponse;
    private AuthorizationResponseServerSentEventPackage authorizationResponseCallbackResponse;

    @Before
    public void setUp() throws Exception {
        authorizationResponse = new AuthorizationResponse(null, true, null, null, null, null, null);
        authorizationResponseCallbackResponse = new AuthorizationResponseServerSentEventPackage(authorizationResponse);
    }

    @After
    public void tearDown() throws Exception {
        authorizationResponse = null;
        authorizationResponseCallbackResponse = null;
    }

    @Test
    public void testGetAuthResponse() throws Exception {
        assertSame(authorizationResponse, authorizationResponseCallbackResponse.getAuthorizationResponse());
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        AuthorizationResponseServerSentEventPackage left = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        AuthorizationResponseServerSentEventPackage right = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthorizationResponseServerSentEventPackage left = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse("left", true, null, null, null, null, null));
        AuthorizationResponseServerSentEventPackage right = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse("right", true, null, null, null, null, null));
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthorizationResponseServerSentEventPackage left = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        AuthorizationResponseServerSentEventPackage right = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthorizationResponseServerSentEventPackage left = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse("left", true, null, null, null, null, null));
        AuthorizationResponseServerSentEventPackage right = new AuthorizationResponseServerSentEventPackage(new AuthorizationResponse("right", true, null, null, null, null, null));
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authorizationResponseCallbackResponse.toString(), containsString(AuthorizationResponseServerSentEventPackage.class.getSimpleName()));
    }
}
