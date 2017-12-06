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

package com.iovation.launchkey.sdk.domain;

import java.util.Date;

@SuppressWarnings("WeakerAccess")
public class PublicKey {
    private final String id;
    private final boolean active;
    private final Date created;
    private final Date expires;

    /**
     * Create a an entity representing an RSA public key belonging to another entity
     * @param id The MD5 id of the RSA public key.
     * @param active Is the public key currently active
     * @param created The date the public key was created
     * @param expires The date the public key will expire
     */
    public PublicKey(String id, boolean active, Date created, Date expires) {
        this.id = id;
        this.active = active;
        this.created = created;
        this.expires = expires;
    }

    /**
     * Get the MD5 id of the RSA public key.
     * @return The MD5 id of the RSA public key. This is used to identify the key.
     */
    public String getId() {
        return id;
    }

    /**
     * Is the public key currently active
     * @return True if the  public key is currently active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Get the date the public key was created
     * @return The date the public key was created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Get the date the public key will expire
     * @return The date the public key will expire
     */
    public Date getExpires() {
        return expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicKey)) return false;

        PublicKey publicKey = (PublicKey) o;

        return getId() != null ? getId().equals(publicKey.getId()) :
                publicKey.getId() == null;
    }

    @Override
    public int hashCode() {
        return 22 * getClass().hashCode() + (getId() != null ? getId().hashCode() : 0);
    }

    @Override
    public String toString() {
        return "PublicKey{" +
                "id='" + id + '\'' +
                ", active=" + active +
                ", created=" + created +
                ", expires=" + expires +
                '}';
    }
}
