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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceV3AuthsPostRequest {
    private final String username;
    private final AuthPolicy policy;
    private final String context;
    private final String title;
    private final Integer ttl;
    private final String pushTitle;
    private final String pushBody;

    public ServiceV3AuthsPostRequest(String username, AuthPolicy policy, String context, String title, Integer ttl,
                                     String pushTitle, String pushBody) {
        this.username = username;
        this.policy = policy;
        this.context = context;
        this.title = title;
        this.ttl = ttl;
        this.pushTitle = pushTitle;
        this.pushBody = pushBody;
    }

    @Deprecated
    public ServiceV3AuthsPostRequest(String username, AuthPolicy policy, String context) {
        this(username, policy, context, null, null, null, null);
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

    @JsonProperty("ttl")
    public Integer getTTL() {
        return ttl;
    }

    @JsonProperty("push_title")
    public String getPushTitle() {
        return pushTitle;
    }

    @JsonProperty("push_body")
    public String getPushBody() {
        return pushBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostRequest)) return false;
        ServiceV3AuthsPostRequest that = (ServiceV3AuthsPostRequest) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPolicy(), that.getPolicy()) &&
                Objects.equals(getContext(), that.getContext()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getTTL(), that.getTTL()) &&
                Objects.equals(getPushTitle(), that.getPushTitle()) &&
                Objects.equals(pushBody, that.getPushBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPolicy(), getContext(), getTitle(), ttl, getPushTitle(), pushBody);
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostRequest{" +
                "username='" + username + '\'' +
                ", policy=" + policy +
                ", context='" + context + '\'' +
                ", title='" + title + '\'' +
                ", ttl=" + ttl +
                ", pushTitle='" + pushTitle + '\'' +
                ", pushBody='" + pushBody + '\'' +
                '}';
    }
}