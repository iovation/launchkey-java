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

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsPostResponse {
    private final UUID authRequest;

    @JsonCreator
    public ServiceV3AuthsPostResponse(@JsonProperty(value = "auth_request") UUID authRequest) {
        this.authRequest = authRequest;
    }

    public UUID getAuthRequest() {
        return authRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostResponse)) return false;

        ServiceV3AuthsPostResponse that = (ServiceV3AuthsPostResponse) o;

        return getAuthRequest() != null ? getAuthRequest().equals(that.getAuthRequest()) : that.getAuthRequest() == null;
    }

    @Override
    public int hashCode() {
        return getAuthRequest() != null ? getAuthRequest().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostResponse{" +
                "authRequest=" + authRequest +
                '}';
    }
}
