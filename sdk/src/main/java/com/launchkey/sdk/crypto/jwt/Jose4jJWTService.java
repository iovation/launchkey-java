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

import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.service.ping.PingService;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;

public class Jose4jJWTService implements JWTService {
    private final String platformIdentifier;
    private final String entityIdentifier;
    private final RSAPrivateKey privateKey;
    private final PingService pingService;
    private final int requestExpireSeconds;

    /**
     * @param platformIdentifier   JWT identifier for the Platform API. Used as the audience for encoding and the
     *                             issuer for decoding.
     * @param entityIdentifier     JWT identifier for the entity interacting with the Platform APi. Used as the
     *                             issuer for encoding and the audience for decoding.
     * @param privateKey           RSA Private Key of the RSA public/private key pair that will be used to generate digital
     *                             signatures when encoding.
     * @param pingService          Ping service to obtain the Platform time to prevent time differential errors when
     *                             encoding and decoding as well as obtaining the RSA Public Key of the RSA public/private
     *                             key pair of the Platfgorm API which will be used to verify digital signatures when decoding.
     * @param requestExpireSeconds The number of seconds from the issue ("iss") time to use for the expiration ("exp")
     *                             time when encoding.
     */
    public Jose4jJWTService(
            String platformIdentifier, String entityIdentifier, RSAPrivateKey privateKey, PingService pingService,
            int requestExpireSeconds
    ) {
        this.platformIdentifier = platformIdentifier;
        this.entityIdentifier = entityIdentifier;
        this.privateKey = privateKey;
        this.pingService = pingService;
        this.requestExpireSeconds = requestExpireSeconds;
    }

    @Override
    public String encode(
            String jti, String method, String path, String contentHashAlgorithm, String contentHash
    ) throws JWTError {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setJwtId(jti);
        jwtClaims.setIssuer(entityIdentifier);
        jwtClaims.setAudience(platformIdentifier);

        NumericDate now = getNow();
        jwtClaims.setIssuedAt(now);
        jwtClaims.setNotBefore(now);

        NumericDate exp = getNow();
        exp.addSeconds((long) requestExpireSeconds);
        jwtClaims.setExpirationTime(exp);

        jwtClaims.setClaim("Path", path);
        jwtClaims.setClaim("Method", method);

        if (contentHashAlgorithm != null) {
            jwtClaims.setClaim("Content-Hash-Alg", contentHashAlgorithm);
            jwtClaims.setClaim("Content-Hash", contentHash);
        }

        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(privateKey);
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

    @Override public JWTClaims decode(String jwt) throws JWTError {
        JWTClaims claims;
        try {
            AlgorithmConstraints algorithmConstraints = new AlgorithmConstraints(
                    AlgorithmConstraints.ConstraintType.WHITELIST,
                    AlgorithmIdentifiers.RSA_USING_SHA256,
                    AlgorithmIdentifiers.RSA_USING_SHA384,
                    AlgorithmIdentifiers.RSA_USING_SHA512
            );

            NumericDate evaluationTime = getNow();
            Key publicKey = pingService.getPublicKey();
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(publicKey)
                    .setEvaluationTime(evaluationTime)
                    .setAllowedClockSkewInSeconds(5)
                    .setExpectedAudience(entityIdentifier)
                    .setExpectedIssuer(platformIdentifier)
                    .setRequireJwtId()
                    .setRequireIssuedAt()
                    .setRequireNotBefore()
                    .setRequireExpirationTime()
                    .setJwsAlgorithmConstraints(algorithmConstraints)
                    .build();

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
        } catch (BaseException e) {
            throw new JWTError("An error occurred retrieving the platform public key to verify the JWT signature", e);
        }
        return claims;
    }

    private NumericDate getNow() throws JWTError {
        NumericDate numericDate;
        try {
            numericDate = NumericDate.fromMilliseconds(pingService.getPlatformTime().getTime());
        } catch (BaseException e) {
            throw new JWTError("An error occurred retrieving the Platform time", e);
        }
        return numericDate;
    }
}
