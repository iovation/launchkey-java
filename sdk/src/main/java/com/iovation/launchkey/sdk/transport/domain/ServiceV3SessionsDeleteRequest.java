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

/**
 * Transport object with the information required to end a user getServiceService session.
 */
public class ServiceV3SessionsDeleteRequest {
    private final String endUserIdentifier;

    /**
     * @param endUserIdentifier LaunchKey username, User Push ID, or White
     * Label Directory Identifier for the end user starting a getServiceService session.
     */
    public ServiceV3SessionsDeleteRequest(
            String endUserIdentifier
    ) {
        this.endUserIdentifier = endUserIdentifier;
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

        return getEndUserIdentifier() != null ? getEndUserIdentifier().equals(that.getEndUserIdentifier()) : that.getEndUserIdentifier() == null;
    }

    @Override
    public int hashCode() {
        return getEndUserIdentifier() != null ? getEndUserIdentifier().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceV3SessionsDeleteRequest{" +
                "endUserIdentifier='" + endUserIdentifier + '\'' +
                '}';
    }
}
