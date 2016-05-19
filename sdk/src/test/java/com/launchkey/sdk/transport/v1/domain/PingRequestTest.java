package com.launchkey.sdk.transport.v1.domain;

import com.launchkey.sdk.transport.v1.domain.PingRequest;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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
        assertEquals(null, new PingRequest().getFingerprint());
    }

    @Test
    public void testFingerprintConstructorReturnsExpectedStringInGetter() throws Exception {
        assertEquals("Fingerprint", new PingRequest("Fingerprint").getFingerprint());
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        PingRequest left = new PingRequest("Fingerprint");
        PingRequest right = new PingRequest("Fingerprint");
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        PingRequest left = new PingRequest("Fingerprint");
        PingRequest right = new PingRequest("Other Fingerprint");
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        PingRequest left = new PingRequest("Fingerprint");
        PingRequest right = new PingRequest("Fingerprint");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        PingRequest left = new PingRequest("Fingerprint");
        PingRequest right = new PingRequest("Other Fingerprint");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        PingRequest pingRequest = new PingRequest("Fingerprint");
        assertThat(pingRequest.toString(), containsString(PingRequest.class.getSimpleName()));
    }
}
