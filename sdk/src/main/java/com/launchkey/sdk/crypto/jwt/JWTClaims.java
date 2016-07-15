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

import com.fasterxml.jackson.annotation.*;

/**
 * Class representing a JWT Claim including proprietary claims used by the Platform API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"tid", "iss", "aud", "iat", "nbf", "exp", "Content-Hash-Alg", "Content-Hash", "Method", "Path"})
public class JWTClaims {

    private final String tokenId;

    private final String issuer;

    private final String audience;

    private final Integer issuedAt;

    private final Integer notBefore;

    private final Integer expiresAt;

    private final String contentHashAlgorithm;

    private final String contentHash;

    private final String method;

    private final String path;

    /**
     * @param tokenId              "tid" claim. Globally unique identifier for this token. This is similar no a nonce
     *                             and is meant to be utilized to protect against replay attacks. It will be the echo
     *                             of the request token in the response to ensure this is the proper response.
     *                             See: <a href="https://tools.ietf.org/html/rfc7519#section-4.1.7">RFC</a>
     * @param issuer               "iss" claim. This identifies the originator of the claim.
     *                             See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.1">RFC</a>
     * @param audience             "aud" claim. This identifies the intended audience of the claim.
     *                             See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.3">RFC</a>
     * @param issuedAt             "iat" claim. This is the number of seconds past the EPOC at which time the claim
     *                             was created.
     *                             See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.6">RFC</a>
     * @param notBefore            "nbf" claim. This is the number of seconds past the EPOC at which time the claim
     *                             will be valid.
     *                             See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.5">RFC</a>
     * @param expiresAt            "nbf" claim. This is the number of seconds past the EPOC at which time the claim
     *                             will expire.
     *                             See:  <a href="https://tools.ietf.org/html/rfc7519#section-4.1.5">RFC</a>
     * @param contentHash          Proprietary claim which contains the hash of the HTTP message body utilizing the
     *                             hashing algorithm. It will be null if there is no message body.
     *                             specified in contentHashAlgorithm parameter.
     * @param contentHashAlgorithm Hashing algorithm utilized to create the contentHash parameter. It will be null
     *                             if there is no message body.
     * @param method               Proprietary claim which contains the expected HTTP method of the request. This
     *                             will be absent from responses.
     * @param path                 Proprietary claim which contains the expected PATH of the request. This will
     *                             be absent from responses.
     */
    @JsonCreator
    public JWTClaims(
            @JsonProperty("tid") String tokenId,
            @JsonProperty("iss") String issuer,
            @JsonProperty("aud") String audience,
            @JsonProperty("iat") Integer issuedAt,
            @JsonProperty("nbf") Integer notBefore,
            @JsonProperty("exp") Integer expiresAt,
            @JsonProperty("Content-Hash-Alg") String contentHashAlgorithm,
            @JsonProperty("Content-Hash") String contentHash,
            @JsonProperty("Method") String method,
            @JsonProperty("Path") String path
    ) {
        this.tokenId = tokenId;
        this.issuer = issuer;
        this.audience = audience;
        this.issuedAt = issuedAt;
        this.notBefore = notBefore;
        this.expiresAt = expiresAt;
        this.contentHashAlgorithm = contentHashAlgorithm;
        this.contentHash = contentHash;
        this.method = method;
        this.path = path;
    }

    /**
     * Get the token ID
     * @return Token ID
     */
    @JsonProperty("tid")
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Get the claim's issuer
     * @return The issuer
     */
    @JsonProperty("iss")
    public String getIssuer() {
        return issuer;
    }

    /**
     * Get the claim's audience
     * @return The audience
     */
    @JsonProperty("aud")
    public String getAudience() {
        return audience;
    }

    /**
     * Get the timestamp for issued at
     * @return The timestamp for issued at
     */
    @JsonProperty("iat")
    public Integer getIssuedAt() {
        return issuedAt;
    }

    /**
     * Get the timestamp for not before
     * @return The timestamp for not before
     */
    @JsonProperty("nbf")
    public Integer getNotBefore() {
        return notBefore;
    }

    /**
     * Get the timestamp for expire
     * @return The timestamp for expire
     */
    @JsonProperty("exp")
    public Integer getExpiresAt() {
        return expiresAt;
    }

    /**
     * Get the content hash
     * @return The content hash
     */
    @JsonProperty("Content-Hash")
    public String getContentHash() {
        return contentHash;
    }

    /**
     * Get the content hash algorithm
     * @return The content hash algorithm
     */
    @JsonProperty("Content-Hash-Alg")
    public String getContentHashAlgorithm() {
        return contentHashAlgorithm;
    }

    /**
     * Get the request method
     * @return The request method
     */
    @JsonProperty("Method")
    public String getMethod() {
        return method;
    }

    /**
     * Get the request path
     * @return The request path
     */
    @JsonProperty("Path")
    public String getPath() {
        return path;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JWTClaims)) return false;

        JWTClaims jwtClaims = (JWTClaims) o;

        if (getIssuedAt() != null ? !getIssuedAt().equals(jwtClaims.getIssuedAt()) : jwtClaims.getIssuedAt() != null) {
            return false;
        }
        if (getNotBefore() != null ? !getNotBefore().equals(jwtClaims.getNotBefore()) : jwtClaims.getNotBefore() != null) {
            return false;
        }
        if (getExpiresAt() != null ? !getExpiresAt().equals(jwtClaims.getExpiresAt()) : jwtClaims.getExpiresAt() != null) {
            return false;
        }
        if (getTokenId() != null ? !getTokenId().equals(jwtClaims.getTokenId()) : jwtClaims.getTokenId() != null) {
            return false;
        }
        if (getIssuer() != null ? !getIssuer().equals(jwtClaims.getIssuer()) : jwtClaims.getIssuer() != null) {
            return false;
        }
        if (getAudience() != null ? !getAudience().equals(jwtClaims.getAudience()) : jwtClaims.getAudience() != null) {
            return false;
        }
        if (getContentHash() != null ? !getContentHash().equals(jwtClaims.getContentHash()) : jwtClaims.getContentHash() != null) {
            return false;
        }
        if (getContentHashAlgorithm() != null ? !getContentHashAlgorithm().equals(jwtClaims.getContentHashAlgorithm()) : jwtClaims
                .getContentHashAlgorithm() != null) {
            return false;
        }
        if (getMethod() != null ? !getMethod().equals(jwtClaims.getMethod()) : jwtClaims.getMethod() != null) {
            return false;
        }
        return getPath() != null ? getPath().equals(jwtClaims.getPath()) : jwtClaims.getPath() == null;

    }

    @Override public int hashCode() {
        int result = getTokenId() != null ? getTokenId().hashCode() : 0;
        result = 31 * result + (getIssuer() != null ? getIssuer().hashCode() : 0);
        result = 31 * result + (getAudience() != null ? getAudience().hashCode() : 0);
        result = 31 * result + (getIssuedAt() != null ? getIssuedAt().hashCode() : 0);
        result = 31 * result + (getNotBefore() != null ? getNotBefore().hashCode() : 0);
        result = 31 * result + (getExpiresAt() != null ? getExpiresAt().hashCode() : 0);
        result = 31 * result + (getContentHashAlgorithm() != null ? getContentHashAlgorithm().hashCode() : 0);
        result = 31 * result + (getContentHash() != null ? getContentHash().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "JWTClaims{" +
                "tokenId='" + tokenId + '\'' +
                ", issuer='" + issuer + '\'' +
                ", audience='" + audience + '\'' +
                ", issuedAt=" + issuedAt +
                ", notBefore=" + notBefore +
                ", expiresAt=" + expiresAt +
                ", contentHash='" + contentHash + '\'' +
                ", contentHashAlgorithm='" + contentHashAlgorithm + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
