package com.launchkey.sdk.transport.v1.domain;

import com.launchkey.sdk.transport.v1.domain.PollRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

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
public class PollRequestTest {
    private PollRequest pollRequest;

    @Before
    public void setUp() throws Exception {
        pollRequest = new PollRequest(
                "4yjuyyg59cqf2s890uhhhx3vmtgv115a",
                9999999999L,
                "qGp+BP88k4Sh0CbD8L7ERsu4gKNOnCAt0IB3vB0EoWy44jJiZo5nGS6sTR2VSxJ00l7zRvOIRKYi8pgWqurLEx+3W0s7xOFvjJeonuomixDc7Y5CMx5fCNkM6i1KCM/hDNpIp93uSCYCYvzuo6Gw6cJx0peG6UUyV+6+vn/36bLq8fKn6WfKrywzTbBTh52ckvk2kiZiixIiWSyIDrkFDYrW3bwXJsgUrbzkC4vNLDaVGdN8JZYmerJ5fNFiEOUXuvt6mhHFgWfCRljgr3AZTN+smOfNyYCBWU4WPjENyZAUXPIURW5FqqSfyJBnf1Fcf/xA/QRwiEISVJrWi3scfQ==",
                "rk1mJeZ4GeqYZmpCZtZCW/D1qR5I69WxOeiLW5gELgSC5sFrsSzhxFdn+hkQvHWKWZr6gBAvmdDA63HFLdC9OHD9WxWjgClSUygxO/04RUFLS1mKaEfyf9DW8gLl7/dp5wuvrIqZ7DJxfLWOQOZKY0L6gopZ6dYF8szfVs+50z/3xcl24KQZ1yz13YKB6S2ud3nJsifnp0/pqVqB+M56Tj5sCqFtel1kJmdY5ayVYBvC5SkzKdqAePcYKxDJm9KSM1mjuXKZ4wD+C04kq7qZx2XPGQHC5xf6pmLlZgYwCuk+ynyWDDjxXAiVk5H1HZYKOmNbWJEQPk/IzfTazESFsg=="
        );

    }

    @After
    public void tearDown() throws Exception {
        pollRequest = null;

    }

    @Test
    public void testGetAuthRequest() throws Exception {
        assertEquals("4yjuyyg59cqf2s890uhhhx3vmtgv115a", pollRequest.getAuthRequest());
    }

    @Test
    public void testGetAppKey() throws Exception {
        assertEquals(9999999999L, pollRequest.getAppKey());
    }


    @Test
    public void testGetRocketKeyEqualsGetAppKey() throws Exception {
        assertEquals(pollRequest.getAppKey(), pollRequest.getRocketKey());
    }

    @Test
    public void testGetSecretKey() throws Exception {
        assertEquals(
                "qGp+BP88k4Sh0CbD8L7ERsu4gKNOnCAt0IB3vB0EoWy44jJiZo5nGS6sTR2VSxJ00l7zRvOIRKYi8pgWqurLEx+3W0s7xOFvjJeonuomixDc7Y5CMx5fCNkM6i1KCM/hDNpIp93uSCYCYvzuo6Gw6cJx0peG6UUyV+6+vn/36bLq8fKn6WfKrywzTbBTh52ckvk2kiZiixIiWSyIDrkFDYrW3bwXJsgUrbzkC4vNLDaVGdN8JZYmerJ5fNFiEOUXuvt6mhHFgWfCRljgr3AZTN+smOfNyYCBWU4WPjENyZAUXPIURW5FqqSfyJBnf1Fcf/xA/QRwiEISVJrWi3scfQ==",
                pollRequest.getSecretKey()
        );
    }

    @Test
    public void testGetSignature() throws Exception {
        assertEquals(
                "rk1mJeZ4GeqYZmpCZtZCW/D1qR5I69WxOeiLW5gELgSC5sFrsSzhxFdn+hkQvHWKWZr6gBAvmdDA63HFLdC9OHD9WxWjgClSUygxO/04RUFLS1mKaEfyf9DW8gLl7/dp5wuvrIqZ7DJxfLWOQOZKY0L6gopZ6dYF8szfVs+50z/3xcl24KQZ1yz13YKB6S2ud3nJsifnp0/pqVqB+M56Tj5sCqFtel1kJmdY5ayVYBvC5SkzKdqAePcYKxDJm9KSM1mjuXKZ4wD+C04kq7qZx2XPGQHC5xf6pmLlZgYwCuk+ynyWDDjxXAiVk5H1HZYKOmNbWJEQPk/IzfTazESFsg==",
                pollRequest.getSignature()
        );
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        PollRequest left = new PollRequest("auth request", 12345, "secret key", "signature");
        PollRequest right = new PollRequest("auth request", 12345, "secret key", "signature");
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        PollRequest left = new PollRequest("other auth request", 12345, "secret key", "signature");
        PollRequest right = new PollRequest("auth request", 12345, "secret key", "signature");
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        PollRequest left = new PollRequest("auth request", 12345, "secret key", "signature");
        PollRequest right = new PollRequest("auth request", 12345, "secret key", "signature");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        PollRequest left = new PollRequest("other auth request", 12345, "secret key", "signature");
        PollRequest right = new PollRequest("auth request", 12345, "secret key", "signature");
        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        PollRequest pollRequest = new PollRequest("auth request", 12345, "secret key", "signature");
        assertThat(pollRequest.toString(), containsString(PollRequest.class.getSimpleName()));
    }
}