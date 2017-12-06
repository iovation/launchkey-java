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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeysListPostResponsePublicKey {
    private final String id;
    private final String publicKey;
    private final Date created;
    private final Date expires;
    private final boolean active;

    @JsonCreator
    public KeysListPostResponsePublicKey(@JsonProperty("id") String id,
                                         @JsonProperty("public_key") String publicKey,
                                         @JsonProperty("date_created") Date created,
                                         @JsonProperty("date_expires") Date expires,
                                         @JsonProperty("active") boolean active) {
        this.id = id;
        this.publicKey = publicKey;
        this.created = created;
        this.expires = expires;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public boolean isActive() {
        return active;
    }
}
