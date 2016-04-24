package com.launchkey.sdk.transport.v1.domain;

import com.launchkey.sdk.transport.v1.domain.Policy.Factor;
import com.launchkey.sdk.transport.v1.domain.Policy.Policy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
public class AuthsRequestTest {
    private AuthsRequest authsRequest;
    private Policy policy;

    @Before
    public void setUp() throws Exception {
        policy = new Policy(new ArrayList<Policy.MinimumRequirement>(), new ArrayList<Factor>());
        //noinspection SpellCheckingInspection
        authsRequest = new AuthsRequest(
                "dennis",
                9999999999L,
                "qGp+BP88k4Sh0CbD8L7ERsu4gKNOnCAt0IB3vB0EoWy44jJiZo5nGS6sTR2VSxJ00l7zRvOIRKYi8pgWqurLEx+3W0s7xOFvjJeonuomixDc7Y5CMx5fCNkM6i1KCM/hDNpIp93uSCYCYvzuo6Gw6cJx0peG6UUyV+6+vn/36bLq8fKn6WfKrywzTbBTh52ckvk2kiZiixIiWSyIDrkFDYrW3bwXJsgUrbzkC4vNLDaVGdN8JZYmerJ5fNFiEOUXuvt6mhHFgWfCRljgr3AZTN+smOfNyYCBWU4WPjENyZAUXPIURW5FqqSfyJBnf1Fcf/xA/QRwiEISVJrWi3scfQ==",
                "rk1mJeZ4GeqYZmpCZtZCW/D1qR5I69WxOeiLW5gELgSC5sFrsSzhxFdn+hkQvHWKWZr6gBAvmdDA63HFLdC9OHD9WxWjgClSUygxO/04RUFLS1mKaEfyf9DW8gLl7/dp5wuvrIqZ7DJxfLWOQOZKY0L6gopZ6dYF8szfVs+50z/3xcl24KQZ1yz13YKB6S2ud3nJsifnp0/pqVqB+M56Tj5sCqFtel1kJmdY5ayVYBvC5SkzKdqAePcYKxDJm9KSM1mjuXKZ4wD+C04kq7qZx2XPGQHC5xf6pmLlZgYwCuk+ynyWDDjxXAiVk5H1HZYKOmNbWJEQPk/IzfTazESFsg==",
                0,
                1,
                "expected context",
                policy
        );
    }

    @After
    public void tearDown() throws Exception {
        policy = null;
        authsRequest = null;
    }

    @Test
    public void testSessionValueOfZeroDoesNotThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 0, 0, null, null);
    }

    @Test
    public void testSessionValueOfOneDoesNotThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 1, 0, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSessionValueOfNotZeroOrOneDoesThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 2, 0, null, null);
    }

    @Test
    public void testUserPushIDValueOfZeroDoesNotThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 0, 0, null, null);
    }

    @Test
    public void testUserPushIdValueOfOneDoesNotThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 0, 1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserPushIdValueOfNotZeroOrOneDoesThrowException() throws Exception {
        new AuthsRequest(null, 0, null, null, 0, 2, null, null);
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals("dennis", authsRequest.getUsername());
    }

    @Test
    public void testGetAppKey() throws Exception {
        assertEquals(9999999999L, authsRequest.getAppKey());
    }

    @Test
    public void testGetRocketKeyEqualsGetAppKey() throws Exception {
        assertEquals(authsRequest.getAppKey(), authsRequest.getRocketKey());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testGetSecretKey() throws Exception {
        assertEquals(
                "qGp+BP88k4Sh0CbD8L7ERsu4gKNOnCAt0IB3vB0EoWy44jJiZo5nGS6sTR2VSxJ00l7zRvOIRKYi8pgWqurLEx+3W0s7xOFvjJeonuomixDc7Y5CMx5fCNkM6i1KCM/hDNpIp93uSCYCYvzuo6Gw6cJx0peG6UUyV+6+vn/36bLq8fKn6WfKrywzTbBTh52ckvk2kiZiixIiWSyIDrkFDYrW3bwXJsgUrbzkC4vNLDaVGdN8JZYmerJ5fNFiEOUXuvt6mhHFgWfCRljgr3AZTN+smOfNyYCBWU4WPjENyZAUXPIURW5FqqSfyJBnf1Fcf/xA/QRwiEISVJrWi3scfQ==",
                authsRequest.getSecretKey()
        );
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetSignature() throws Exception {
        assertEquals(
                "rk1mJeZ4GeqYZmpCZtZCW/D1qR5I69WxOeiLW5gELgSC5sFrsSzhxFdn+hkQvHWKWZr6gBAvmdDA63HFLdC9OHD9WxWjgClSUygxO/04RUFLS1mKaEfyf9DW8gLl7/dp5wuvrIqZ7DJxfLWOQOZKY0L6gopZ6dYF8szfVs+50z/3xcl24KQZ1yz13YKB6S2ud3nJsifnp0/pqVqB+M56Tj5sCqFtel1kJmdY5ayVYBvC5SkzKdqAePcYKxDJm9KSM1mjuXKZ4wD+C04kq7qZx2XPGQHC5xf6pmLlZgYwCuk+ynyWDDjxXAiVk5H1HZYKOmNbWJEQPk/IzfTazESFsg==",
                authsRequest.getSignature()
        );
    }

    @Test
    public void testGetSession() throws Exception {
        assertEquals(0, authsRequest.getSession());
    }

    @Test
    public void testGetUserPushId() throws Exception {
        assertEquals(1, authsRequest.getUserPushID());
    }

    @Test
    public void testGetContext() throws Exception {
        assertEquals("expected context", authsRequest.getContext());
    }

    @Test
    public void testGetPolicy() throws Exception {
        assertEquals(policy, authsRequest.getPolicy());
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        AuthsRequest left = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        AuthsRequest right = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        AuthsRequest left = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        AuthsRequest right = new AuthsRequest(
                "other user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        AuthsRequest left = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        AuthsRequest right = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        AuthsRequest left = new AuthsRequest(
                "user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );
        AuthsRequest right = new AuthsRequest(
                "other user",
                12345L,
                "secret key",
                "private key",
                0,
                1,
                "context",
                policy
        );

        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(authsRequest.toString(), containsString(AuthsRequest.class.getSimpleName()));
    }
}