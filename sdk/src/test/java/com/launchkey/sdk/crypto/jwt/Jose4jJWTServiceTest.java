package com.launchkey.sdk.crypto.jwt;

import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.service.ping.PingService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
public class Jose4jJWTServiceTest {


    private static final int EXPIRE_SECONDS = 15;
    private static final String PLATFORM_IDENTIFIER = "Platform Identifier";
    private static final String ENTITY_IDENTIFIER = "Entity Identifier";

    private final Provider provider = new BouncyCastleProvider();

    private PingService pingService;
    private Jose4jJWTService jwtService;
    private Date platformDate;
    private KeyPair keyPair;
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

    @Before
    public void setUp() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();

        pingService = mock(PingService.class);
        when(pingService.getPublicKey()).thenReturn((RSAPublicKey) keyPair.getPublic());
        platformDate = new Date();
        when(pingService.getPlatformTime()).thenReturn(platformDate);

        jwtService = new Jose4jJWTService(
                PLATFORM_IDENTIFIER,
                null,
                null,
                EXPIRE_SECONDS
        );
    }

    @Test
    public void encodeEncodesSomethingThatProperlyDecodes() throws Exception {

        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("jti", "Expected JTI");
        expected.put("iss", ENTITY_IDENTIFIER);
        expected.put("aud", PLATFORM_IDENTIFIER);
        expected.put("iat", Long.valueOf(platformDate.getTime() / 1000));
        expected.put("nbf", Long.valueOf(platformDate.getTime() / 1000));
        expected.put("exp", Long.valueOf(platformDate.getTime() / 1000) + EXPIRE_SECONDS);
        expected.put("Content-Hash-Alg", "SHA256");
        expected.put("Content-Hash", "Hash Value");
        expected.put("Path", "/path/to/resource");
        expected.put("Method", "OPTIONS");

        String encoded = jwtService.encode(
                (String) expected.get("jti"),
                null,
                null,
                null,
                (String) expected.get("Method"),
                (String) expected.get("Path"),
                (String) expected.get("Content-Hash-Alg"),
                (String) expected.get("Content-Hash")
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

    @Test
    public void decodeCanDecodeProperly() throws Exception {

        final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_PEM);
        when(pingService.getPublicKey()).thenReturn(publicKey);

        JWTClaims expected = new JWTClaims(
                "3ab9236f-375a-4b70-b064-e5c4570d4a7c",
                "application:1000000000",
                "organization:9770924838",
                1467915940,
                1467915940,
                1467916240,
                "SHA512",
                "2e67b030f0217787cb7557ea5fd7de2384e5bc76e229717afb4fd4fa8a9c4f7517517fcaf3dc2485e9acc4f98d4aad9210b4e8219191a061f9a03c7dc4cffbdb",
                null,
                null
        );

        Date then = new Date(1467915940L * 1000L);
        when(pingService.getPlatformTime()).thenReturn(then);
        JWTService jwtService = new Jose4jJWTService(
                "application:1000000000",
                null,
                null,
                EXPIRE_SECONDS
        );
        JWTClaims actual = jwtService.decode(null, null, null, null, TOKEN);
        assertEquals(expected, actual);
    }

    @Test(expected = JWTError.class)
    public void encodeJoseErrorThrowsJwtError() throws Exception {
        // Build and set the Ping service to return a key that is too small
        // see: org.jose4j.jwx.KeyValidationSupport#MIN_RSA_KEY_LENGTH
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", provider);
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        jwtService = new Jose4jJWTService(
                PLATFORM_IDENTIFIER,
                null,
                null,
                EXPIRE_SECONDS
        );


        jwtService.encode(null, null, null, null, null, null, null, null);
    }

    @Test(expected = JWTError.class)
    public void decodeInvalidJwtThrowsJwtError() throws Exception {
        JWTClaims actual = jwtService.decode(null, null, null, null, "aksjfhaslkhf");

    }

    @Test(expected = JWTError.class)
    public void decodeMalformedClaimExceptionThrowsJwtError() throws Exception {
        final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_PEM);
        when(pingService.getPublicKey()).thenReturn(publicKey);
        JWTService jwtService = new Jose4jJWTService(
                "Invalid provider identity",
                null,
                null,
                EXPIRE_SECONDS
        );
        jwtService.decode(null, null, null, null, TOKEN);
    }

    @Test(expected = JWTError.class)
    public void decodePingServiceExceptionThrowsJwtError() throws Exception {
        when(pingService.getPublicKey()).thenThrow(new CommunicationErrorException(null, null, null));
        jwtService.decode(null, null, null, null, TOKEN);
    }

    @Test(expected = JWTError.class)
    public void decodePingServiceGetPlatformTImeExceptionThrowsJwtError() throws Exception {
        when(pingService.getPlatformTime()).thenThrow(new CommunicationErrorException(null, null, null));
        jwtService.decode(null, null, null, null, TOKEN);
    }
}
