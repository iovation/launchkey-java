package com.iovation.launchkey.sdk.crypto.jwt; /**
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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class JWTDataTest {
    @Test
    public void getIssuer() throws Exception {
        assertEquals("issuer", new JWTData("issuer", null, null, null).getIssuer());
    }

    @Test
    public void getSubject() throws Exception {
        assertEquals("subject", new JWTData(null, "subject", null, null).getSubject());
    }

    @Test
    public void getAudience() throws Exception {
        assertEquals("audience", new JWTData(null, null, "audience", null).getAudience());
    }

    @Test
    public void getKeyId() throws Exception {
        assertEquals("keyId", new JWTData(null, null, null, "keyId").getKeyId());
    }

    @Test
    public void equalsForSameObjectIsTrue() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        //noinspection EqualsWithItself
        assertTrue(actual.equals(actual));
    }

    @Test
    public void equalsForDifferentObjectWithSameValuesIsTrue() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "c", "d");
        assertTrue(actual.equals(expected));
    }

    @Test
    public void equalsForDifferentIssuerValueIsFalse() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("z", "b", "c", "b");
        assertFalse(actual.equals(expected));
    }

    @Test
    public void equalsForDifferentSubjectValueIsFalse() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "z", "c", "b");
        assertFalse(actual.equals(expected));
    }

    @Test
    public void equalsForDifferentAudienceValueIsFalse() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "z", "b");
        assertFalse(actual.equals(expected));
    }

    @Test
    public void equalsForDifferentKeyIdIsFalse() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "c", "z");
        assertFalse(actual.equals(expected));
    }

    @Test
    public void hashCodeForSameObjectIsEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        assertEquals(actual.hashCode(), actual.hashCode());
    }

    @Test
    public void hashCodeForDifferentObjectWithSameValuesIsEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "c", "d");
        assertEquals(expected.hashCode(), actual.hashCode());
    }

    @Test
    public void hashCodeForDifferentIssuerValueIsNotEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("z", "b", "c", "b");
        assertNotEquals(actual, expected);
    }

    @Test
    public void hashCodeForDifferentSubjectValueIsNotEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "z", "c", "b");
        assertNotEquals(actual, expected);
    }

    @Test
    public void hashCodeForDifferentAudienceValueIsNotEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "z", "b");
        assertNotEquals(actual, expected);
    }

    @Test
    public void hashCodeForDifferentKeyIdIsNotEqual() throws Exception {
        JWTData actual = new JWTData("a", "b", "c", "d");
        JWTData expected = new JWTData("a", "b", "c", "z");
        assertNotEquals(actual, expected);
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(new JWTData(null, null, null, null).toString(), containsString("JWTData"));
    }

}