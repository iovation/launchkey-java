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

package com.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Transport object with the information required to end a user service session.
 */
public class ServiceV3SessionsDeleteRequest {
    private final UUID serviceId;
    private final String endUserIdentifier;

    /**
     * @param serviceId ID of the service for which the user session has ended.
     * @param endUserIdentifier LaunchKey username, User Push ID, or White
     * Label Directory Identifier for the end user starting a service session.
     */
    public ServiceV3SessionsDeleteRequest(
            UUID serviceId, String endUserIdentifier
    ) {
        this.serviceId = serviceId;
        this.endUserIdentifier = endUserIdentifier;
    }

    /**
     * Get the ID for the service.
     * @return ID id the service.
     */
    @JsonIgnore
    public UUID getServiceId() {
        return serviceId;
    }

    /**
     * Get the identifier for the end user
     * @return LaunchKey username, User Push ID, or White Label Directory
     * Identifier for the end user
     */
    @JsonProperty("username")
    public String getEndUserIdentifier() {
        return endUserIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3SessionsDeleteRequest)) return false;

        ServiceV3SessionsDeleteRequest that = (ServiceV3SessionsDeleteRequest) o;

        if (getServiceId() != null ? !getServiceId().equals(that.getServiceId()) : that.getServiceId() != null)
            return false;
        return getEndUserIdentifier() != null ? getEndUserIdentifier().equals(that.getEndUserIdentifier()) : that.getEndUserIdentifier() == null;
    }

    @Override
    public int hashCode() {
        int result = getServiceId() != null ? getServiceId().hashCode() : 0;
        result = 31 * result + (getEndUserIdentifier() != null ? getEndUserIdentifier().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceSessionsDeleteRequest{" +
                "serviceId=" + serviceId +
                ", endUserIdentifier='" + endUserIdentifier + '\'' +
                '}';
    }
}
