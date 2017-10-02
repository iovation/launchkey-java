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

import java.util.UUID;

public class ServiceKeysListPostRequest {
    private final UUID serviceId;

    /**
     * Request to obtain the list of Public Keys for a Service
     * @param serviceId ID of the Service whose Public Keys will be retrieved.
     */
    public ServiceKeysListPostRequest(UUID serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Get the Service ID for the request
     * @return Service ID
     */
    @JsonProperty("service_id")
    public UUID getServiceId() {
        return serviceId;
    }
}
