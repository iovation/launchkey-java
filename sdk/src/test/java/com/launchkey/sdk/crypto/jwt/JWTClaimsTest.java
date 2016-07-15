package com.launchkey.sdk.crypto.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JWTClaimsTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private JWTClaims jwtClaims;

    @Before
    public void setUp() throws Exception {
        jwtClaims = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
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
    public void getMethod() throws Exception {
        assertEquals("Method", jwtClaims.getMethod());
    }

    @Test
    public void getPath() throws Exception {
        assertEquals("Path", jwtClaims.getPath());
    }

    @Test
    public void jsonEncode() throws Exception {
        String expected = "{\"tid\":\"Token ID\",\"iss\":\"Issuer\",\"aud\":\"Audience\",\"iat\":11111,\"nbf\":22222,\"exp\":33333,\"Content-Hash-Alg\":\"Body Hash Alg\",\"Content-Hash\":\"Body Hash\",\"Method\":\"Method\",\"Path\":\"Path\"}";
        String actual = OBJECT_MAPPER.writeValueAsString(jwtClaims);
        assertEquals(expected, actual);
    }

    @Test
    public void jsonEncodeSkipsNullItems() throws Exception {
        String expected = "{\"tid\":\"Token ID\",\"iss\":\"Issuer\",\"aud\":\"Audience\",\"Method\":\"Method\",\"Path\":\"Path\"}";
        String actual = OBJECT_MAPPER.writeValueAsString(new JWTClaims("Token ID", "Issuer", "Audience", null, null, null, null, null, "Method", "Path"));
        assertEquals(expected, actual);
    }

    @Test
    public void jsonDecode() throws Exception {
        String json = "{\"tid\":\"Token ID\",\"iss\":\"Issuer\",\"aud\":\"Audience\",\"iat\":11111,\"nbf\":22222,\"exp\":33333,\"Content-Hash\":\"Body Hash\",\"Content-Hash-Alg\":\"Body Hash Alg\",\"Method\":\"Method\",\"Path\":\"Path\"}";
        JWTClaims actual = OBJECT_MAPPER.readValue(json, JWTClaims.class);
        assertEquals(jwtClaims, actual);
    }

    @Test
    public void jsonDecodeWithNoMethod() throws Exception {
        String json = "{\"tid\":\"Token ID\",\"iss\":\"Issuer\",\"aud\":\"Audience\",\"iat\":11111,\"nbf\":22222,\"exp\":33333,\"Content-Hash\":\"Body Hash\",\"Content-Hash-Alg\":\"Body Hash Alg\",\"Path\":\"Path\"}";
        JWTClaims actual = OBJECT_MAPPER.readValue(json, JWTClaims.class);
        assertNull(actual.getMethod());
    }

    @Test
    public void jsonDecodeWithNoPath() throws Exception {
        String json = "{\"tid\":\"Token ID\",\"iss\":\"Issuer\",\"aud\":\"Audience\",\"iat\":11111,\"nbf\":22222,\"exp\":33333,\"Content-Hash\":\"Body Hash\",\"Content-Hash-Alg\":\"Body Hash Alg\",\"Method\":\"Method\"}";
        JWTClaims actual = OBJECT_MAPPER.readValue(json, JWTClaims.class);
        assertNull(actual.getPath());
    }

    @Test
    public void jsonDecodeWithUnknownValueDoesNotError() throws Exception {
        String json = "{\"unk\":\"value\"}";
        JWTClaims actual = OBJECT_MAPPER.readValue(json, JWTClaims.class);
        assertNotNull(actual);
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
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertTrue(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenTokenIdIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Other Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenIssuersDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Other Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenAudienceIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Other Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenIssuedAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11112,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenNotBeforeIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22223,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenExpiresAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33334,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenContentHashAlgorithmIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Other Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenContentHashIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Other Body Hash",
                "Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenMethodIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Other Method",
                "Path"
        );
        assertFalse(jwtClaims.equals(other));
    }

    @Test
    public void equalsIsFalseWhenPathIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Other Path"
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
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenTokenIdIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Other Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenIssuersDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Other Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenAudienceIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Other Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenIssuedAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11112,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenNotBeforedIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22223,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenExpiresAtIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33334,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenBodyHashAlgorithmIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Other Body Hash Alg",
                "Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenBodyHashIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Other Body Hash",
                "Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenMethodIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Other Method",
                "Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }

    @Test
    public void hashcodeIsNotEqualWhenPathIsDifferent() throws Exception {
        JWTClaims other = new JWTClaims(
                "Token ID",
                "Issuer",
                "Audience",
                11111,
                22222,
                33333,
                "Body Hash Alg",
                "Body Hash",
                "Method",
                "Other Path"
        );
        assertNotEquals(jwtClaims.hashCode(), other.hashCode());
    }
}
