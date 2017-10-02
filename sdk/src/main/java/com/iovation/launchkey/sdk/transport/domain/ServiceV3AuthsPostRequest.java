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

public class ServiceV3AuthsPostRequest {
    private final String username;
    private final AuthPolicy policy;
    private final String context;

    public ServiceV3AuthsPostRequest(String username, AuthPolicy policy, String context) {
        this.username = username;
        this.policy = policy;
        this.context = context;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("policy")
    public AuthPolicy getPolicy() {
        return policy;
    }

    @JsonProperty("context")
    public String getContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostRequest)) return false;

        ServiceV3AuthsPostRequest that = (ServiceV3AuthsPostRequest) o;

        if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null)
            return false;
        if (getPolicy() != null ? !getPolicy().equals(that.getPolicy()) : that.getPolicy() != null) return false;
        return getContext() != null ? getContext().equals(that.getContext()) : that.getContext() == null;
    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getPolicy() != null ? getPolicy().hashCode() : 0);
        result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostRequest{" +
                "username='" + username + '\'' +
                ", policy=" + policy +
                ", context='" + context + '\'' +
                '}';
    }
}