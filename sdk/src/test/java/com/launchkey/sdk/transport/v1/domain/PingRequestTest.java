package com.launchkey.sdk.transport.v1.domain;

import com.launchkey.sdk.transport.v1.domain.PingRequest;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

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
public class PingRequestTest {

    @Test
    public void testEmptyConstructorGetterReturnsNull() throws Exception {
        assertEquals(null, new PingRequest().getDateStamp());
    }

    @Test
    public void testDateStampConstructorReturnsExpectedStringInGetter() throws Exception {
        assertEquals("1970-01-01 00:00:00", new PingRequest(new Date(0L)).getDateStamp());
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        PingRequest left = new PingRequest(new Date(0L));
        PingRequest right = new PingRequest(new Date(0L));
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        PingRequest left = new PingRequest(new Date(0L));
        PingRequest right = new PingRequest(new Date(1000L));
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        PingRequest left = new PingRequest(new Date(0L));
        PingRequest right = new PingRequest(new Date(0L));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        PingRequest left = new PingRequest(new Date(0L));
        PingRequest right = new PingRequest(new Date(1000L));

        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        PingRequest pingRequest = new PingRequest(new Date(0L));
        assertThat(pingRequest.toString(), containsString(PingRequest.class.getSimpleName()));
    }
}