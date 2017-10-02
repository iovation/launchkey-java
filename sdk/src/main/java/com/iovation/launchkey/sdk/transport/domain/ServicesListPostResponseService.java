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

import java.net.URI;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicesListPostResponseService {

    private final UUID id;
    private final String name;
    private final String description;
    private final URI icon;
    private final URI callbackURL;
    private final boolean active;

    @JsonCreator
    public ServicesListPostResponseService(@JsonProperty("id") UUID id, @JsonProperty("name") String name,
                                           @JsonProperty("description") String description, @JsonProperty("icon") URI icon,
                                           @JsonProperty("callback_url") URI callbackURL, @JsonProperty("active") boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.callbackURL = callbackURL;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public URI getIcon() {
        return icon;
    }

    public URI getCallbackURL() {
        return callbackURL;
    }

    public boolean isActive() {
        return active;
    }
}
