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

package com.iovation.launchkey.sdk.crypto.jwt;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

/**
 * Class representing a JWT Claim including proprietary claims used by the Platform API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        {"tid", "iss", "sub", "aud", "iat", "nbf", "exp", "Content-Hash-Alg", "Content-Hash", "Method", "Path"})
public class JWTClaims {

    private final String tokenId;

    private final String issuer;

    private final String subject;

    private final String audience;

    private final Integer issuedAt;

    private final Integer notBefore;

    private final Integer expiresAt;

    private final String contentHashAlgorithm;

    private final String contentHash;

    private final Integer statusCode;

    private final String cacheControlHeader;

    private final String locationHeader;

    private final String method;

    private final String path;

    /**
     * @param tokenId "tid" claim. Globally unique identifier for this token. This is similar no a nonce
     * and is meant to be utilized to protect against replay attacks. It will be the echo
     * of the request token in the response to ensure this is the proper response.
     * See: <a href="https://tools.ietf.org/html/rfc7519#section-4.1.7">RFC</a>
     * @param issuer "iss" claim. This identifies the originator of the claim.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.1">RFC</a>
     * @param subject "aud" claim. This identifies the subject of the claim.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.2">RFC</a>
     * @param audience "aud" claim. This identifies the intended audience of the claim.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.3">RFC</a>
     * @param issuedAt "iat" claim. This is the number of seconds past the EPOC at which time the claim
     * was created.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.6">RFC</a>
     * @param notBefore "nbf" claim. This is the number of seconds past the EPOC at which time the claim
     * will be valid.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.5">RFC</a>
     * @param expiresAt "nbf" claim. This is the number of seconds past the EPOC at which time the claim
     * will expire.
     * See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.5">RFC</a>
     * @param contentHash Proprietary claim which contains the hash of the HTTP message body utilizing the
     * hashing algorithm. It will be null if there is no message body.
     * specified in contentHashAlgorithm parameter.
     * @param contentHashAlgorithm Hashing algorithm utilized to create the contentHash parameter. It will be null
     * if there is no message body.
     * @param statusCode Expected response status code. This will never be null.
     * @param cacheControlHeader Expected value for the "Cache-Control" header. This may be null if not was provided
     * in the response.
     * @param locationHeader Expected value for the "Location" header. This may be null if not was provided
     * in the response.
     * @param method The expected request method value. This may be null if this is not a request.
     * @param path The expected request path value. This may be null if this is not a request.
     */
    @JsonCreator
    public JWTClaims(
            String tokenId,
            String issuer,
            String subject,
            String audience,
            Integer issuedAt,
            Integer notBefore,
            Integer expiresAt,
            String contentHashAlgorithm,
            String contentHash,
            Integer statusCode,
            String cacheControlHeader,
            String locationHeader,
            String method,
            String path
    ) {
        this.tokenId = tokenId;
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
        this.issuedAt = issuedAt;
        this.notBefore = notBefore;
        this.expiresAt = expiresAt;
        this.contentHashAlgorithm = contentHashAlgorithm;
        this.contentHash = contentHash;
        this.statusCode = statusCode;
        this.cacheControlHeader = cacheControlHeader;
        this.locationHeader = locationHeader;
        this.method = method;
        this.path = path;
    }

    /**
     * Get the token ID
     *
     * @return Token ID
     */
    @JsonProperty("tid")
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Get the claim's issuer
     *
     * @return The issuer
     */
    @JsonProperty("iss")
    public String getIssuer() {
        return issuer;
    }

    /**
     * Get the claim's subject
     *
     * @return The subject
     */
    @JsonProperty("sub")
    public String getSubject() {
        return subject;
    }

    /**
     * Get the claim's audience
     *
     * @return The audience
     */
    @JsonProperty("aud")
    public String getAudience() {
        return audience;
    }

    /**
     * Get the timestamp for issued at
     *
     * @return The timestamp for issued at
     */
    @JsonProperty("iat")
    public Integer getIssuedAt() {
        return issuedAt;
    }

    /**
     * Get the timestamp for not before
     *
     * @return The timestamp for not before
     */
    @JsonProperty("nbf")
    public Integer getNotBefore() {
        return notBefore;
    }

    /**
     * Get the timestamp for expire
     *
     * @return The timestamp for expire
     */
    @JsonProperty("exp")
    public Integer getExpiresAt() {
        return expiresAt;
    }

    /**
     * Get the content hash
     *
     * @return The content hash
     */
    @JsonProperty("Content-Hash")
    public String getContentHash() {
        return contentHash;
    }

    /**
     * Get the content hash algorithm
     *
     * @return The content hash algorithm
     */
    @JsonProperty("Content-Hash-Alg")
    public String getContentHashAlgorithm() {
        return contentHashAlgorithm;
    }

    /**
     * Get the response status code
     * @return The response status code
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Get the expected Cache-Control header value
     * @return The expected Cache-Control header value
     */
    public String getCacheControlHeader() {
        return cacheControlHeader;
    }

    /**
     * Get the expected Location header value
     * @return The expected Location header value
     */
    public String getLocationHeader() {
        return locationHeader;
    }

    /**
     * Get the expected request method value
     * @return The expected request method value
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get the expected request path value
     * @return The expected request path value
     */
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JWTClaims jwtClaims = (JWTClaims) o;
        return Objects.equals(tokenId, jwtClaims.tokenId) &&
                Objects.equals(issuer, jwtClaims.issuer) &&
                Objects.equals(subject, jwtClaims.subject) &&
                Objects.equals(audience, jwtClaims.audience) &&
                Objects.equals(issuedAt, jwtClaims.issuedAt) &&
                Objects.equals(notBefore, jwtClaims.notBefore) &&
                Objects.equals(expiresAt, jwtClaims.expiresAt) &&
                Objects.equals(contentHashAlgorithm, jwtClaims.contentHashAlgorithm) &&
                Objects.equals(contentHash, jwtClaims.contentHash) &&
                Objects.equals(statusCode, jwtClaims.statusCode) &&
                Objects.equals(cacheControlHeader, jwtClaims.cacheControlHeader) &&
                Objects.equals(locationHeader, jwtClaims.locationHeader) &&
                Objects.equals(method, jwtClaims.method) &&
                Objects.equals(path, jwtClaims.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId, issuer, subject, audience, issuedAt, notBefore, expiresAt, contentHashAlgorithm,
                contentHash, statusCode, cacheControlHeader, locationHeader, method, path);
    }
}
