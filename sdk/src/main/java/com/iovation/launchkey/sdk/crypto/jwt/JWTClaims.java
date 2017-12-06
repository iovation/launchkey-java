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

    private final int statusCode;

    private final String cacheControlHeader;

    private final String locationHeader;

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
            int statusCode,
            String cacheControlHeader,
            String locationHeader
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
    public int getStatusCode() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JWTClaims)) return false;

        JWTClaims jwtClaims = (JWTClaims) o;

        if (statusCode != jwtClaims.statusCode) return false;
        if (tokenId != null ? !tokenId.equals(jwtClaims.tokenId) : jwtClaims.tokenId != null) return false;
        if (issuer != null ? !issuer.equals(jwtClaims.issuer) : jwtClaims.issuer != null) return false;
        if (subject != null ? !subject.equals(jwtClaims.subject) : jwtClaims.subject != null) return false;
        if (audience != null ? !audience.equals(jwtClaims.audience) : jwtClaims.audience != null) return false;
        if (issuedAt != null ? !issuedAt.equals(jwtClaims.issuedAt) : jwtClaims.issuedAt != null) return false;
        if (notBefore != null ? !notBefore.equals(jwtClaims.notBefore) : jwtClaims.notBefore != null) return false;
        if (expiresAt != null ? !expiresAt.equals(jwtClaims.expiresAt) : jwtClaims.expiresAt != null) return false;
        if (contentHashAlgorithm != null ? !contentHashAlgorithm.equals(jwtClaims.contentHashAlgorithm) :
                jwtClaims.contentHashAlgorithm != null) return false;
        if (contentHash != null ? !contentHash.equals(jwtClaims.contentHash) : jwtClaims.contentHash != null)
            return false;
        if (cacheControlHeader != null ? !cacheControlHeader.equals(jwtClaims.cacheControlHeader) :
                jwtClaims.cacheControlHeader != null) return false;
        return locationHeader != null ? locationHeader.equals(jwtClaims.locationHeader) :
                jwtClaims.locationHeader == null;
    }

    @Override
    public int hashCode() {
        int result = tokenId != null ? tokenId.hashCode() : 0;
        result = 31 * result + (issuer != null ? issuer.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (audience != null ? audience.hashCode() : 0);
        result = 31 * result + (issuedAt != null ? issuedAt.hashCode() : 0);
        result = 31 * result + (notBefore != null ? notBefore.hashCode() : 0);
        result = 31 * result + (expiresAt != null ? expiresAt.hashCode() : 0);
        result = 31 * result + (contentHashAlgorithm != null ? contentHashAlgorithm.hashCode() : 0);
        result = 31 * result + (contentHash != null ? contentHash.hashCode() : 0);
        result = 31 * result + statusCode;
        result = 31 * result + (cacheControlHeader != null ? cacheControlHeader.hashCode() : 0);
        result = 31 * result + (locationHeader != null ? locationHeader.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JWTClaims{" +
                "tokenId='" + tokenId + '\'' +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                ", issuedAt=" + issuedAt +
                ", notBefore=" + notBefore +
                ", expiresAt=" + expiresAt +
                ", contentHashAlgorithm='" + contentHashAlgorithm + '\'' +
                ", contentHash='" + contentHash + '\'' +
                ", statusCode=" + statusCode +
                ", cacheControlHeader='" + cacheControlHeader + '\'' +
                ", locationHeader='" + locationHeader + '\'' +
                '}';
    }
}
