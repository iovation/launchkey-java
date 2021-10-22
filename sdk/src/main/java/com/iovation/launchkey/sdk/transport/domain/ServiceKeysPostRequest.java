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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.iovation.launchkey.sdk.domain.KeyType;

import java.util.Date;
import java.util.UUID;

@JsonPropertyOrder({"service_id", "public_key", "date_expires", "active", "key_type"})
public class ServiceKeysPostRequest {
    private final UUID serviceId;
    private final String publicKey;
    private final Date expires;
    private final Boolean active;
    private final KeyType key_type;

    public ServiceKeysPostRequest(UUID serviceId, String publicKey, Date expires, Boolean active, KeyType key_type) {
        this.serviceId = serviceId;
        this.publicKey = publicKey;
        this.expires = expires;
        this.active = active;
        this.key_type = key_type;
    }

    public ServiceKeysPostRequest(UUID serviceId, String publicKey, Date expires, Boolean active) {
        this.serviceId = serviceId;
        this.publicKey = publicKey;
        this.expires = expires;
        this.active = active;
        this.key_type = KeyType.BOTH;
    }

    @JsonProperty("service_id")
    public UUID getServiceId() {
        return serviceId;
    }

    @JsonProperty("public_key")
    public String getPublicKey() {
        return publicKey;
    }

    @JsonProperty("date_expires")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    public Date getExpires() {
        return expires;
    }

    @JsonProperty("active")
    public Boolean isActive() {
        return active;
    }

    @JsonProperty("key_type")
    public Integer getKeyType() {
        return key_type.value();
    }
}
