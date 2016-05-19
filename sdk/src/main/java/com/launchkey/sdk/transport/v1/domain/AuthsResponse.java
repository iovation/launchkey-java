/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response data from an "auths" call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthsResponse {
    /**
     * The unique ID of the authentication request.
     */
    private final String authRequest;

    /**
     * @param authRequest The unique ID of the authentication request.
     */
    @JsonCreator
    public AuthsResponse(@JsonProperty("auth_request") String authRequest) {
        this.authRequest = authRequest;
    }

    /**
     * @return The unique ID of the authentication request.
     */
    public String getAuthRequestId() {
        return authRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthsResponse)) return false;

        AuthsResponse that = (AuthsResponse) o;

        return !(authRequest != null ? !authRequest.equals(that.authRequest) : that.authRequest != null);

    }

    @Override
    public int hashCode() {
        return authRequest != null ? authRequest.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AuthsResponse{" +
                "authRequestId='" + authRequest + '\'' +
                '}';
    }
}
