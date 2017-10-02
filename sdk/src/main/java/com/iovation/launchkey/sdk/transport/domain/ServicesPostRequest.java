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

@JsonPropertyOrder({"name", "icon", "description", "callback_url", "active"})
public class ServicesPostRequest {
    private final String name;
    private final URI icon;
    private final String description;
    private final URI callbackUrl;
    private final Boolean active;

    public ServicesPostRequest(String name, URI icon, String description, URI callbackUrl, Boolean active) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.callbackUrl = callbackUrl;
        this.active = active;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("icon")
    public URI getIcon() {
        return icon;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("callback_url")
    public URI getCallbackURL() {
        return callbackUrl;
    }

    @JsonProperty("active")
    public Boolean isActive() {
        return active;
    }
}
