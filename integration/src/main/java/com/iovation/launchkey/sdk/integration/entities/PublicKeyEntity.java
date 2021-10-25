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

package com.iovation.launchkey.sdk.integration.entities;

import com.iovation.launchkey.sdk.domain.KeyType;
import com.iovation.launchkey.sdk.domain.PublicKey;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class PublicKeyEntity {
    private String keyId;
    private RSAPublicKey publicKey;
    Boolean active;
    Date created;
    Date expires;
    KeyType keyType;

    public PublicKeyEntity(String keyId, RSAPublicKey publicKey, Boolean active, Date created, Date expires,
                           KeyType keyType) {
        this.keyId = keyId;
        this.publicKey = publicKey;
        this.active = active;
        this.created = created;
        this.expires = expires;
        this.keyType = keyType;
    }

    public PublicKeyEntity(String keyId, RSAPublicKey publicKey, Boolean active, Date created, Date expires) {
        this.keyId = keyId;
        this.publicKey = publicKey;
        this.active = active;
        this.created = created;
        this.expires = expires;
        this.keyType = KeyType.BOTH;
    }

    public String getKeyId() {
        return keyId;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public Boolean getActive() {
        return active;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicKeyEntity)) return false;

        PublicKeyEntity that = (PublicKeyEntity) o;

        if (keyId != null ? !keyId.equals(that.keyId) : that.keyId != null) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (keyType != null ? !keyType.equals(that.keyType) : that.keyType != null) return false;
        return expires != null ? expires.equals(that.expires) : that.expires == null;
    }

    @Override
    public int hashCode() {
        return keyId != null ? keyId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PublicKeyEntity{" +
                "keyId='" + keyId + '\'' +
                ", publicKey=" + publicKey +
                ", active=" + active +
                ", created=" + created +
                ", expires=" + expires +
                ", keyType=" + keyType +
                '}';
    }

    public static PublicKeyEntity fromPublicKey(PublicKey publicKey) {
        return new PublicKeyEntity(publicKey.getId(), null, publicKey.isActive(), publicKey.getCreated(),
                publicKey.getExpires(), publicKey.getKeyType());
    }
}
