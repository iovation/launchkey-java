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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;

/**
 * Transport object with the information required to begin a user service
 * session.
 */
@JsonPropertyOrder({"username", "auth_request"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceV3SessionsPostRequest {
    private final String endUserIdentifier;
    private final UUID authorizationRequestId;

    /**
     * @param endUserIdentifier LaunchKey username, User Push ID, or White
     * Label Directory Identifier for the end user starting a service session.
     * @param authorizationRequestId Identifier of the authorization request
     * that was utilized to start the user service session. This should be null
     * if no authorization request was utilized.
     */
    public ServiceV3SessionsPostRequest(
            String endUserIdentifier,
            UUID authorizationRequestId
    ) {
        this.endUserIdentifier = endUserIdentifier;
        this.authorizationRequestId = authorizationRequestId;
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

    /**
     * Get the ID of the causal authorization request
     * @return Authorization request ID
     */
    @JsonProperty("auth_request")
    public UUID getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3SessionsPostRequest)) return false;

        ServiceV3SessionsPostRequest that = (ServiceV3SessionsPostRequest) o;

        if (getEndUserIdentifier() != null ? !getEndUserIdentifier().equals(that.getEndUserIdentifier()) : that.getEndUserIdentifier() != null)
            return false;
        return getAuthorizationRequestId() != null ? getAuthorizationRequestId().equals(that.getAuthorizationRequestId()) : that.getAuthorizationRequestId() == null;
    }

    @Override
    public int hashCode() {
        int result = getEndUserIdentifier() != null ? getEndUserIdentifier().hashCode() : 0;
        result = 31 * result + (getAuthorizationRequestId() != null ? getAuthorizationRequestId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3SessionsPostRequest{" +
                "endUserIdentifier='" + endUserIdentifier + '\'' +
                ", authorizationRequestId=" + authorizationRequestId +
                '}';
    }
}
