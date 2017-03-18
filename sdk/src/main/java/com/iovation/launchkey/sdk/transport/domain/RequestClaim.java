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

package com.iovation.launchkey.sdk.transport.domain;

import java.security.PrivateKey;
import java.util.UUID;

public class RequestClaim {
    public final PrivateKey issuerPrivateKey;
    public final UUID issuer;
    public final UUID subject;

    public RequestClaim(PrivateKey issuerPrivateKey, UUID issuer, UUID subject) {
        this.issuerPrivateKey = issuerPrivateKey;
        this.issuer = issuer;
        this.subject = subject;
    }

    public RequestClaim(PrivateKey issuerPrivateKey, UUID issuer) {
        this(issuerPrivateKey, issuer, issuer);
    }

    public PrivateKey getIssuerPrivateKey() {
        return issuerPrivateKey;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public UUID getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestClaim)) return false;

        RequestClaim that = (RequestClaim) o;

        if (getIssuerPrivateKey() != null ? !getIssuerPrivateKey().equals(that.getIssuerPrivateKey()) : that.getIssuerPrivateKey() != null)
            return false;
        if (getIssuer() != null ? !getIssuer().equals(that.getIssuer()) : that.getIssuer() != null) return false;
        return getSubject() != null ? getSubject().equals(that.getSubject()) : that.getSubject() == null;
    }

    @Override
    public int hashCode() {
        int result = getIssuerPrivateKey() != null ? getIssuerPrivateKey().hashCode() : 0;
        result = 31 * result + (getIssuer() != null ? getIssuer().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RequestClaim{" +
                "issuerPrivateKey=" + issuerPrivateKey +
                ", issuer=" + issuer +
                ", subject=" + subject +
                '}';
    }
}
