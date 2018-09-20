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

import java.util.Objects;

public class ServiceV3AuthsPostRequest {
    private final String username;
    private final AuthPolicy policy;
    private final String context;
    private final String title;

    public ServiceV3AuthsPostRequest(String username, AuthPolicy policy, String context, String title) {
        this.username = username;
        this.policy = policy;
        this.context = context;
        this.title = title;
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

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostRequest)) return false;
        ServiceV3AuthsPostRequest that = (ServiceV3AuthsPostRequest) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPolicy(), that.getPolicy()) &&
                Objects.equals(getContext(), that.getContext()) &&
                Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPolicy(), getContext(), getTitle());
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostRequest{" +
                "username='" + username + '\'' +
                ", policy=" + policy +
                ", context='" + context + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}