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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.net.URI;
import java.util.UUID;

@JsonPropertyOrder({"service_id", "name", "description", "icon", "callback_url", "active"})
public class ServicesPatchRequest {
    private final UUID serviceId;
    private final String name;
    private final String description;
    private final URI icon;
    private final URI callbackURL;
    private final Boolean active;

    public ServicesPatchRequest(UUID serviceId, String name, String description, URI icon, URI callbackURL,
                                Boolean active) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.callbackURL = callbackURL;
        this.active = active;
    }

    @JsonProperty("service_id")
    public UUID getServiceId() {
        return serviceId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("icon")
    public URI getIcon() {
        return icon;
    }

    @JsonProperty("callback_url")
    public URI getCallbackURL() {
        return callbackURL;
    }

    @JsonProperty("active")
    public Boolean isActive() {
        return active;
    }
}
