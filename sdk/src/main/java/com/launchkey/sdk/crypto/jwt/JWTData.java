/**
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

package com.launchkey.sdk.crypto.jwt;

public class JWTData {
    private final String issuer;
    private final String subject;
    private final String audience;
    private final String keyId;

    public JWTData(String issuer, String subject, String audience, String keyId) {
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
        this.keyId = keyId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public String getAudience() {
        return audience;
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JWTData)) return false;

        JWTData jwtData = (JWTData) o;

        if (getIssuer() != null ? !getIssuer().equals(jwtData.getIssuer()) : jwtData.getIssuer() != null) return false;
        if (getSubject() != null ? !getSubject().equals(jwtData.getSubject()) : jwtData.getSubject() != null)
            return false;
        if (getAudience() != null ? !getAudience().equals(jwtData.getAudience()) : jwtData.getAudience() != null)
            return false;
        return getKeyId() != null ? getKeyId().equals(jwtData.getKeyId()) : jwtData.getKeyId() == null;
    }

    @Override
    public int hashCode() {
        int result = getIssuer() != null ? getIssuer().hashCode() : 0;
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getAudience() != null ? getAudience().hashCode() : 0);
        result = 31 * result + (getKeyId() != null ? getKeyId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JWTData{" +
                "issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                ", keyId='" + keyId + '\'' +
                '}';
    }
}
