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
import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryV3SessionsListPostResponseSession {
    private final UUID serviceId;
    private final String serviceName;
    private final URI serviceIcon;
    private final UUID authRequest;
    private final Date created;

    @JsonCreator
    public DirectoryV3SessionsListPostResponseSession(
            @JsonProperty("service_id") UUID serviceId, @JsonProperty("service_name") String serviceName,
            @JsonProperty("service_icon") URI serviceIcon, @JsonProperty("auth_request") UUID authRequest,
            @JsonProperty("date_created") Date created) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.authRequest = authRequest;
        this.created = created;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public URI getServiceIcon() {
        return serviceIcon;
    }

    public UUID getAuthRequest() {
        return authRequest;
    }

    public Date getCreated() {
        return created;
    }
}
