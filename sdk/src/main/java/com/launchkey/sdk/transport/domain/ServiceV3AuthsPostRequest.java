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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceV3AuthsPostRequest {
    private final String username;
    private final ServiceV3AuthsPostRequestPolicy policy;
    private final String context;

    public ServiceV3AuthsPostRequest(String username, ServiceV3AuthsPostRequestPolicy policy, String context) {
        this.username = username;
        this.policy = policy;
        this.context = context;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("policy")
    public String getPolicy() {
        try {
            return new ObjectMapper().writeValueAsString(policy);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @JsonProperty("context")
    public String getContext() {
        return context;
    }
}