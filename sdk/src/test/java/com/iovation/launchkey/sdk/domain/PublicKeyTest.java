package com.iovation.launchkey.sdk.domain; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class PublicKeyTest {
    private final String fingerprint;
    private final boolean active;
    private final Date created;
    private final Date expires;
    private PublicKey publicKey;

    public PublicKeyTest() {
        fingerprint = "Expected Fingerprint";
        active = true;
        created = new Date(0L);
        expires = new Date(1L);
    }

    @Before
    public void setUp() throws Exception {
        publicKey = new PublicKey(fingerprint, active, created, expires);
    }

    @Test
    public void getFingerprint() throws Exception {
        assertEquals(fingerprint, publicKey.getId());
    }

    @Test
    public void isActive() throws Exception {
        assertEquals(active, publicKey.isActive());
    }

    @Test
    public void getCreated() throws Exception {
        assertEquals(created, publicKey.getCreated());
    }

    @Test
    public void getExpires() throws Exception {
        assertEquals(expires, publicKey.getExpires());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        //noinspection EqualsWithItself
        assertTrue(publicKey.equals(publicKey));
    }

    @Test
    public void equalsIsTrueForSameFingerPrint() throws Exception {
        assertTrue(publicKey.equals(new PublicKey(fingerprint, !active, null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentFingerprint() throws Exception {
        assertFalse(publicKey.equals(new PublicKey("Different Fingerprint", active, created, expires)));
    }

    @Test
    public void equalsIsFalseForFingerprintStringObject() throws Exception {
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(publicKey.equals(fingerprint));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(publicKey.hashCode(), publicKey.hashCode());
    }

    @Test
    public void hashCodeIsEqualForSameFingerPrint() throws Exception {
        assertEquals(publicKey.hashCode(), new PublicKey(fingerprint, !active, null, null).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentFingerprint() throws Exception {
        assertNotEquals(publicKey.hashCode(), new PublicKey("Different Fingerprint", active, created, expires).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForFingerprintStringObject() throws Exception {
        assertNotEquals(publicKey.hashCode(), fingerprint.hashCode());
    }
}