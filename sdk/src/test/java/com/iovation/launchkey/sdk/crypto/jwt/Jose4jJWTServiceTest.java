package com.iovation.launchkey.sdk.crypto.jwt;

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
public class Jose4jJWTServiceTest {


    private static final int EXPIRE_SECONDS = 15;
    private static final String PLATFORM_IDENTIFIER = "Platform Identifier";
    private static final String ENTITY_IDENTIFIER = "Entity Identifier";

    private final Provider provider = new BouncyCastleProvider();

    private Jose4jJWTService jwtService;
    private Date platformDate;
    private static KeyPair keyPair = null;
    private static String currentPrivateKeyId = null;

    @SuppressWarnings("SpellCheckingInspection")
    private static final String PUBLIC_KEY_PEM = "-----BEGIN PUBLIC KEY-----\n" +
            "\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8zQos4iDSjmUVrFUAg5G\n" +
            "uhU6GehNKb8MCXFadRWiyLGjtbGZAk8fusQU0Uj9E3o0mne0SYESACkhyK+3M1Er\n" +
            "bHlwYJHN0PZHtpaPWqsRmNzui8PvPmhm9QduF4KBFsWu1sBw0ibBYsLrua67F/wK\n" +
            "PaagZRnUgrbRUhQuYt+53kQNH9nLkwG2aMVPxhxcLJYPzQCat6VjhHOX0bgiNt1i\n" +
            "HRHU2phxBcquOW2HpGSWcpzlYgFEhPPQFAxoDUBYZI3lfRj49gBhGQi32qQ1YiWp\n" +
            "aFxOB8GA0Ny5SfI67u6w9Nz9Z9cBhcZBfJKdq5uRWjZWslHjBN3emTAKBpAUPNET\n" +
            "nwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TOKEN = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9" +
            "." +
            "eyJDb250ZW50LUhhc2giOiIyZTY3YjAzMGYwMjE3Nzg3Y2I3NTU3ZWE1ZmQ3ZGUyMzg0ZTViYzc2ZTIyOTcxN2FmYjRmZDRmYThhOWM" +
            "0Zjc1MTc1MTdmY2FmM2RjMjQ4NWU5YWNjNGY5OGQ0YWFkOTIxMGI0ZTgyMTkxOTFhMDYxZjlhMDNjN2RjNGNmZmJkYiIsImF1ZCI6Im" +
            "9yZ2FuaXphdGlvbjo5NzcwOTI0ODM4IiwiaXNzIjoiYXBwbGljYXRpb246MTAwMDAwMDAwMCIsImFsZyI6IlJTNTEyIiwiY3R5IjoiY" +
            "XBwbGljYXRpb24vanNvbiIsImp0aSI6IjNhYjkyMzZmLTM3NWEtNGI3MC1iMDY0LWU1YzQ1NzBkNGE3YyIsIkNvbnRlbnQtSGFzaC1B" +
            "bGciOiJTSEE1MTIiLCJleHAiOjE0Njc5MTYyNDAsImlhdCI6MTQ2NzkxNTk0MCwibmJmIjoxNDY3OTE1OTQwfQ" +
            "." +
            "Y8Et5C8lD2qTj7cWwGeIwxPJqMlHfPKOziFzi5xACIc_RII-9sV4Q-6xT8w83SmVQKkLWmvrNgQFTMQR0YeTpGsnp7ey1N1JLmOdDgc" +
            "9DJ29M7fkKvtbyBA36ca6e8-bcVLcJWlPzNZsJddkc-kcwRFV0yn8EsQKTrM_sbofMZS39NPZJYOcf7gGbo402hw2tj1HgP2wIH0uZW" +
            "F1YnOW0nk6GA4UrMQgurkmsyISIaY6n8smTGk9OH3nXIiaHLDqoc7uDsFJ38OHXcoYKovevsEb9IX0jR2FcgJlgsCGBL9xuzNCyALQd" +
            "ajMkeH5b5BMmIUjJ8LPlbBJsfj0wUj20A";
    private HashMap<String, RSAPrivateKey> privateKeys;

    @Before
    public void setUp() throws Exception {
        if (keyPair == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            currentPrivateKeyId = JCECrypto.getRsaPublicKeyFingerprint(provider, (RSAPrivateKey) keyPair.getPrivate());
        }
        platformDate = new Date();
        privateKeys = new HashMap<>();
        privateKeys.put(currentPrivateKeyId, (RSAPrivateKey) keyPair.getPrivate());
        jwtService = new Jose4jJWTService(
                PLATFORM_IDENTIFIER,
                privateKeys,
                currentPrivateKeyId,
                EXPIRE_SECONDS
        );
    }


    @Test
    public void encodeEncodesSomethingThatProperlyDecodes() throws Exception {

        Map<String, Object> expected = new HashMap<>();
        expected.put("jti", "Expected JTI");
        expected.put("iss", ENTITY_IDENTIFIER);
        expected.put("sub", null);
        expected.put("aud", PLATFORM_IDENTIFIER);
        expected.put("iat", Long.valueOf(platformDate.getTime() / 1000));
        expected.put("nbf", Long.valueOf(platformDate.getTime() / 1000));
        expected.put("exp", Long.valueOf(platformDate.getTime() / 1000) + EXPIRE_SECONDS);
        Map<String, String> request = new HashMap<>();
        request.put("func", "S256");
        request.put("hash", "Hash Value");
        request.put("path", "/path/to/resource");
        request.put("meth", "OPTIONS");
        expected.put("request", request);

        String encoded = jwtService.encode(
                "Expected JTI",
                ENTITY_IDENTIFIER,
                null,
                platformDate,
                "OPTIONS",
                "/path/to/resource",
                "S256",
                "Hash Value"
        );
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(keyPair.getPublic())
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.WHITELIST,
                        AlgorithmIdentifiers.RSA_USING_SHA256
                ))
                .setSkipAllValidators()
                .build();

        Map<String, Object> actual = jwtConsumer.processToClaims(encoded).getClaimsMap();
        assertEquals(expected, actual);
    }

    @Test(expected = JWTError.class)
    public void encodeJoseErrorThrowsJwtErrorWhenCannotSign() throws Exception {
        // Build and set the Ping service to return a key that is too small
        // see: org.jose4j.jwx.KeyValidationSupport#MIN_RSA_KEY_LENGTH
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", provider);
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        jwtService = new Jose4jJWTService(
                PLATFORM_IDENTIFIER,
                new HashMap<String, RSAPrivateKey>(),
                "Current Private Key",
                EXPIRE_SECONDS
        );


        jwtService.encode("JTI", "issuer", "subject", new Date(), "Method", "Path", "HashAlg", "Hash");
    }

    @Test(expected = JWTError.class)
    public void decodeInvalidJwtThrowsJwtError() throws Exception {
        final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_PEM);
        jwtService.decode(publicKey, "audience", "token ID", new Date(), "any-string");
    }

    @Test(expected = JWTError.class)
    public void decodeMalformedClaimExceptionThrowsJwtError() throws Exception {
        final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_PEM);
        JWTService jwtService = new Jose4jJWTService(
                "Invalid provider identity",
                new HashMap<String, RSAPrivateKey>(),
                "Current Key ID",
                EXPIRE_SECONDS
        );
        jwtService.decode(publicKey, "audience", "token ID", new Date(), TOKEN);
    }

    @Test
    public void testJwtDoesNotCollideForSameRequestData() throws Exception {
        String issuer = "rnd:" + UUID.randomUUID().toString();
        String subject = "rnd:" + UUID.randomUUID().toString();
        String method = "GET";
        String path = "/public/v3/ping";
        String a =
                jwtService.encode(UUID.randomUUID().toString(), issuer, subject, new Date(), method, path, null, null);
        String b =
                jwtService.encode(UUID.randomUUID().toString(), issuer, subject, new Date(), method, path, null, null);
        assertNotEquals(a, b);
    }

    @Test
    public void testJwtDoesNotCollideForSameRequestDataHash() throws Exception {
        String issuer = "rnd:" + UUID.randomUUID().toString();
        String subject = "rnd:" + UUID.randomUUID().toString();
        String method = "GET";
        String path = "/public/v3/ping";
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        String a =
                jwtService.encode(UUID.randomUUID().toString(), issuer, subject, new Date(), method, path, null, null);
        String b =
                jwtService.encode(UUID.randomUUID().toString(), issuer, subject, new Date(), method, path, null, null);
        assertNotEquals(Hex.encodeHexString(sha256.digest(a.getBytes())),
                Hex.encodeHexString(sha256.digest(b.getBytes())));
    }

    @Test
    public void testDecodeProcessesTID() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        jwtClaims.setJwtId(expected);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, expected, new Date(), jwt).getTokenId();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesIssuer() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getIssuer();
        assertEquals(PLATFORM_IDENTIFIER, actual);
    }

    @Test
    public void testDecodeProcessesSubject() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        jwtClaims.setSubject(expected);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getSubject();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesAudience() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        jwtClaims.setAudience(expected);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), expected, jwtClaims.getJwtId(), new Date(), jwt).getAudience();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesIssuedAt() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final long actual =
                (long) jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getIssuedAt();
        assertEquals(jwtClaims.getIssuedAt().getValue(), actual);
    }

    @Test
    public void testDecodeProcessesNotBefore() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final long actual =
                (long) jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getNotBefore();
        assertEquals(jwtClaims.getNotBefore().getValue(), actual);
    }

    @Test
    public void testDecodeProcessesExpiresAt() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final long actual =
                (long) jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getExpiresAt();
        assertEquals(jwtClaims.getExpirationTime().getValue(), actual);
    }

    @Test
    public void testDecodeProcessesResponseContentHash() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        Map<String, Object> response = new HashMap<>();
        response.put("hash", expected);
        jwtClaims.setClaim("response", response);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getContentHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesResponseContentHashAlgorithm() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        Map<String, Object> response = new HashMap<>();
        response.put("func", expected);
        jwtClaims.setClaim("response", response);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getContentHashAlgorithm();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesResponseStatusCode() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        Integer expected = 200;
        Map<String, Object> response = new HashMap<>();
        response.put("status", expected);
        jwtClaims.setClaim("response", response);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final Integer actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesResponseCacheControlHeader() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        Map<String, Object> response = new HashMap<>();
        response.put("cache", expected);
        jwtClaims.setClaim("response", response);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getCacheControlHeader();
        assertEquals(expected, actual);
    }

    @Test
    public void testDecodeProcessesResponseLocationHeader() throws Exception {
        JwtClaims jwtClaims = getBaseClaims();
        String expected = "Expected";
        Map<String, Object> response = new HashMap<>();
        response.put("location", expected);
        jwtClaims.setClaim("response", response);
        String jwt = getJwsCompactSerializationFromJtwClaims(jwtClaims);
        final String actual =
                jwtService.decode(keyPair.getPublic(), ENTITY_IDENTIFIER, jwtClaims.getJwtId(), new Date(), jwt)
                        .getLocationHeader();
        assertEquals(expected, actual);
    }

    private JwtClaims getBaseClaims() {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setGeneratedJwtId();
        jwtClaims.setExpirationTimeMinutesInTheFuture(60000);
        jwtClaims.setAudience(ENTITY_IDENTIFIER);
        jwtClaims.setIssuer(PLATFORM_IDENTIFIER);
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setNotBeforeMinutesInThePast(0);
        return jwtClaims;
    }

    private String getJwsCompactSerializationFromJtwClaims(JwtClaims claims) throws JWTError {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setKeyIdHeaderValue(currentPrivateKeyId);
        jws.setKey(privateKeys.get(currentPrivateKeyId));
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new JWTError("An error occurred encoding the JWT", e);
        }
        return jwt;
    }
}
