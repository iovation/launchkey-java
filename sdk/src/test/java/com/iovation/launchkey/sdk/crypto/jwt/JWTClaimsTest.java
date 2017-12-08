package com.iovation.launchkey.sdk.crypto.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressWarnings("deprecation")
public class JWTClaimsTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private JWTClaims jwtClaims;

    @Before
    public void setUp() throws Exception {
        jwtClaims = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );

    }

    @After
    public void tearDown() throws Exception {
        jwtClaims = null;

    }

    @Test
    public void getTokenId() throws Exception {
        assertEquals("Token ID", jwtClaims.getTokenId());
    }

    @Test
    public void getIssuer() throws Exception {
        assertEquals("Issuer", jwtClaims.getIssuer());
    }

    @Test
    public void getAudience() throws Exception {
        assertEquals("Audience", jwtClaims.getAudience());
    }

    @Test
    public void getIssuedAt() throws Exception {
        assertEquals(11111, jwtClaims.getIssuedAt().intValue());
    }

    @Test
    public void getNotBefore() throws Exception {
        assertEquals(22222, jwtClaims.getNotBefore().intValue());
    }

    @Test
    public void getExpiresAt() throws Exception {
        assertEquals(33333, jwtClaims.getExpiresAt().intValue());
    }

    @Test
    public void getContentHash() throws Exception {
        assertEquals("Body Hash", jwtClaims.getContentHash());
    }

    @Test
    public void getContentHashAlgorithm() throws Exception {
        assertEquals("Body Hash Alg", jwtClaims.getContentHashAlgorithm());
    }

    @Test
    public void getStatusCode() throws Exception {
        assertEquals(Integer.valueOf(201), jwtClaims.getStatusCode());
    }

    @Test
    public void getLocationHeader() throws Exception {
        assertEquals("Location Header", jwtClaims.getLocationHeader());
    }

    @Test
    public void getCacheControlHeader() throws Exception {
        assertEquals("Cache Header", jwtClaims.getCacheControlHeader());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(jwtClaims.toString(), containsString(jwtClaims.getClass().getSimpleName()));

    }

    @Test
    public void equalsIsTrueWhenSameObject() throws Exception {
        assertTrue(jwtClaims.equals(jwtClaims));
    }

    @Test
    public void equalsIsTrueWhenEquivalentObject() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertTrue(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenTokenIdIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Other Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenIssuersDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Other Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenSubjectDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Other Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenAudienceIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Other Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenIssuedAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11112,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenNotBeforeIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22223,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenExpiresAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33334,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenContentHashAlgorithmIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Other Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenContentHashIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Other Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenStatusCodeIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                501,
                "Cache Header",
                "Location Header"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenCacheHeaderIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache X Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void equalsIsFalseWhenLocationHeaderIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location X Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsEqualWhenSameObject() throws Exception {
        assertTrue(jwtClaims.equals(jwtClaims));
    }

    @Test
    public void hashcodeIsEqualWhenEquivalentObject() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenTokenIdIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Other Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenIssuersDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Other Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenSubjectDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Other Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenAudienceIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Other Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenIssuedAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11112,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenNotBeforeIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22223,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenExpiresAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33334,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenBodyHashAlgorithmIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Other Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenBodyHashIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Other Body Hash",
                201,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenStatusCodeIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                501,
                "Cache Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenCacheIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache X Header",
                "Location Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenLocationIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Subject",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                201,
                "Cache Header",
                "Location X Header"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }
}
