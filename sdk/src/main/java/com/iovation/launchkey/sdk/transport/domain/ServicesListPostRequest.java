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

import java.util.List;
import java.util.UUID;

public class ServicesListPostRequest {
    private final List<UUID> serviceIds;

    public ServicesListPostRequest(List<UUID> serviceIds) {
        this.serviceIds = serviceIds;
    }

    @JsonProperty("service_ids")
    public List<UUID> getServiceIds() {
        return serviceIds;
    }
}
