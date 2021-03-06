package com.iovation.launchkey.sdk.domain.service;

import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
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
    private AuthorizationResponseWebhookPackage authorizationResponseCallbackResponse;

    @Before
    public void setUp() throws Exception {
        authorizationResponse = new AuthorizationResponse(null, true, null, null, null, null, null);
        authorizationResponseCallbackResponse = new AuthorizationResponseWebhookPackage(authorizationResponse);
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
        AuthorizationResponseWebhookPackage left = new AuthorizationResponseWebhookPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        AuthorizationResponseWebhookPackage right = new AuthorizationResponseWebhookPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        AuthorizationResponseWebhookPackage left = new AuthorizationResponseWebhookPackage(new AuthorizationResponse("left", true, null, null, null, null, null));
        AuthorizationResponseWebhookPackage right = new AuthorizationResponseWebhookPackage(new AuthorizationResponse("right", true, null, null, null, null, null));
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        AuthorizationResponseWebhookPackage left = new AuthorizationResponseWebhookPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        AuthorizationResponseWebhookPackage right = new AuthorizationResponseWebhookPackage(new AuthorizationResponse(null, true, null, null, null, null, null));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        AuthorizationResponseWebhookPackage left = new AuthorizationResponseWebhookPackage(new AuthorizationResponse("left", true, null, null, null, null, null));
        AuthorizationResponseWebhookPackage right = new AuthorizationResponseWebhookPackage(new AuthorizationResponse("right", true, null, null, null, null, null));
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authorizationResponseCallbackResponse.toString(), containsString(AuthorizationResponseWebhookPackage.class.getSimpleName()));
    }
}
