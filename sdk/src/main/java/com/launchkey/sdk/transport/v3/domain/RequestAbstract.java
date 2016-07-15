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

package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.interfaces.RSAPrivateKey;

public class RequestAbstract implements Request {
    private final RSAPrivateKey privateKey;
    private final String issuer;

    /**
     * @param privateKey PrivateKey to sign requests
     * @param issuer Issuer to identify the issuer of the request
     */
    public RequestAbstract(RSAPrivateKey privateKey, String issuer) {
        this.privateKey = privateKey;
        this.issuer = issuer;
    }

    @JsonIgnore
    @Override public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    @JsonIgnore
    @Override public String getIssuer() {
        return issuer;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestAbstract)) return false;

        RequestAbstract that = (RequestAbstract) o;

        if (getPrivateKey() != null ? !getPrivateKey().equals(that.getPrivateKey()) : that.getPrivateKey() != null) {
            return false;
        }
        return getIssuer() != null ? getIssuer().equals(that.getIssuer()) : that.getIssuer() == null;

    }

    @Override public int hashCode() {
        int result = getPrivateKey() != null ? getPrivateKey().hashCode() : 0;
        result = 31 * result + (getIssuer() != null ? getIssuer().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "RequestAbstract{" +
                "privateKey=" + privateKey +
                ", issuer='" + issuer + '\'' +
                '}';
    }
}
