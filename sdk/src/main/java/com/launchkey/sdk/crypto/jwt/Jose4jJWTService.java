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

package com.launchkey.sdk.crypto.jwt;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.*;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;

import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jose4jJWTService implements JWTService {
    private final String apiIdentifier;
    private final Map<String, RSAPrivateKey> privateKeys;
    private final String currentPrivateKeyId;
    private final int requestExpireSeconds;

    /**
     * @param apiIdentifier JWT identifier for the Platform API. Used as the apiIdentifier for encoding and the
     * issuer for decoding.
     * @param privateKeys Mapped list of RSA Private Key by key ID of the RSA public/private key pairs that will be
     * used to generate digital encoding and decoding as well as obtaining the RSA Public Key of the RSA public/private
     * key pair of the Platform API which will be used to verify digital signatures when decoding.
     * @param currentPrivateKeyId Public key fingerprint of the RSA private key that wi8ll be used to encode requests.
     * @param requestExpireSeconds The number of seconds from the issue ("iss") time to use for the expiration ("exp")
     * time when encoding.
     */
    public Jose4jJWTService(
            String apiIdentifier, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKeyId, int requestExpireSeconds
    ) {
        this.apiIdentifier = apiIdentifier;
        this.privateKeys = privateKeys;
        this.currentPrivateKeyId = currentPrivateKeyId;
        this.requestExpireSeconds = requestExpireSeconds;
    }

    @Override
    public String encode(
            String jti, String issuer, String subject, Date currentTime, String method, String path, String contentHashAlgorithm, String contentHash
    ) throws JWTError {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setJwtId(jti);
        jwtClaims.setIssuer(issuer);
        jwtClaims.setSubject(subject);
        jwtClaims.setAudience(apiIdentifier);

        NumericDate now = NumericDate.fromMilliseconds(currentTime.getTime());
        jwtClaims.setIssuedAt(now);
        jwtClaims.setNotBefore(now);

        NumericDate exp = NumericDate.fromMilliseconds(currentTime.getTime());
        exp.addSeconds((long) requestExpireSeconds);
        jwtClaims.setExpirationTime(exp);

        Map<String, String> request = new HashMap<String, String>();
        //noinspection SpellCheckingInspection
        request.put("meth", method);
        request.put("path", path);
        if (contentHashAlgorithm != null) {
            request.put("func", contentHashAlgorithm);
            request.put("hash", contentHash);
        }
        jwtClaims.setClaim("request", request);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setKeyIdHeaderValue(currentPrivateKeyId);
        jws.setKey(privateKeys.get(currentPrivateKeyId));
        jws.setPayload(jwtClaims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new JWTError("An error occurred encoding the JWT", e);
        }
        return jwt;
    }

    @Override
    public JWTClaims decode(PublicKey publicKey, String expectedAudience, String expectedTokenId, Date currentTime, String jwt) throws JWTError {
        JWTClaims claims;
        try {
            AlgorithmConstraints algorithmConstraints = new AlgorithmConstraints(
                    AlgorithmConstraints.ConstraintType.WHITELIST,
                    AlgorithmIdentifiers.RSA_USING_SHA256,
                    AlgorithmIdentifiers.RSA_USING_SHA384,
                    AlgorithmIdentifiers.RSA_USING_SHA512
            );

            NumericDate evaluationTime = NumericDate.fromMilliseconds(currentTime.getTime());
            JwtConsumerBuilder builder = new JwtConsumerBuilder()
                    .setVerificationKey(publicKey)
                    .setEvaluationTime(evaluationTime)
                    .setAllowedClockSkewInSeconds(5)
                    .setExpectedAudience(expectedAudience)
                    .setExpectedIssuer(apiIdentifier)
                    .setRequireIssuedAt()
                    .setRequireNotBefore()
                    .setRequireExpirationTime()
                    .setJwsAlgorithmConstraints(algorithmConstraints);
            if (expectedTokenId != null) {
                builder.setRequireJwtId()
                        .registerValidator(new JwtIdValidator(expectedTokenId))
                        .build();
            }
            JwtConsumer jwtConsumer = builder.build();

            JwtClaims libraryClaims = jwtConsumer.processToClaims(jwt);
            claims = new JWTClaims(
                    libraryClaims.getJwtId(),
                    libraryClaims.getIssuer(),
                    libraryClaims.getAudience().get(0),
                    (int) libraryClaims.getIssuedAt().getValue(),
                    (int) libraryClaims.getNotBefore().getValue(),
                    (int) libraryClaims.getExpirationTime().getValue(),
                    libraryClaims.getStringClaimValue("Content-Hash-Alg"),
                    libraryClaims.getStringClaimValue("Content-Hash"),
                    libraryClaims.getStringClaimValue("Method"),
                    libraryClaims.getStringClaimValue("Path")
            );
        } catch (InvalidJwtException e) {
            throw new JWTError("An error occurred parsing the JWT", e);
        } catch (MalformedClaimException e) {
            throw new JWTError("An error occurred parsing a claim", e);
        }
        return claims;
    }

    @Override
    public JWTData getJWTData(String jwt) throws JWTError {
        String keyId = null;
        JWTData jwtData;
        try {
            JwtConsumer consumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setSkipSignatureVerification()
                    .build();
            JwtContext jwtContext = consumer.process(jwt);
            for (JsonWebStructure joseObject : jwtContext.getJoseObjects()) {
                keyId = joseObject.getKeyIdHeaderValue();
                if (keyId != null) {
                    break;
                }
            }
            if (keyId == null) {
                throw new JWTError("No kid found!", null);
            }
            JwtClaims claims = consumer.processToClaims(jwt);
            jwtData = new JWTData(
                    claims.getIssuer(),
                    claims.getSubject(),
                    claims.getAudience().get(0),
                    keyId
            );
        } catch (InvalidJwtException e) {
            throw new JWTError("An error occurred parsing the JWT", e);
        } catch (MalformedClaimException e) {
            throw new JWTError("An error occurred parsing the JWT", e);
        }
        return jwtData;
    }

    /**
     * Validator to assert JWT ID (tid) is the expected value
     */
    public class JwtIdValidator implements Validator {

        private final String expected;

        public JwtIdValidator(String expected) {
            this.expected = expected;
        }

        @Override
        public String validate(JwtContext jwtContext) throws MalformedClaimException {
            String jti = jwtContext.getJwtClaims().getJwtId();
            return jti != null && jti.equals(expected) ? null : "Mismatched JWT ID";
        }
    }
}
