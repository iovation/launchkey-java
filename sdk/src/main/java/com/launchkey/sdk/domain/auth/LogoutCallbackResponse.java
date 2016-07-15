/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.domain.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * A Server Sent Event callback used to inform an application that the user has remotely logged out.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoutCallbackResponse implements CallbackResponse {
    /**
     * Unique user identifier that will match the user hash in {@link AuthResponse#getUserHash()}
     */
    private final String userHash;

    /**
     * The date and time the remote logout was requested. This value is to be used to ensure the user has not started
     * a new session since the logout was request and inadvertently ending their application session.
     */
    private final Date logoutRequested;

    /**
     *
     * @param logoutRequested The date and time the remote logout was requested. This value is to be used to ensure
     *                        the user has not started a new session since the logout was request and inadvertently
     *                        ending their application session.
     * @param userHash Unique user identifier that will match the user hash in {@link AuthResponse#getUserHash()}
     */
    @JsonCreator
    public LogoutCallbackResponse(
            @JsonProperty("api_time") Date logoutRequested,
            @JsonProperty("user_hash") String userHash
    ) {
        this.logoutRequested = logoutRequested;
        this.userHash = userHash;
    }

    /**
     * Get the date and time the remote logout was requested. This value is to be used to ensure the user has not started
     * a new session since the logout was request and inadvertently ending their application session.
     * @return The date and time the remote logout was requested
     */
    public Date getLogoutRequested() {
        return this.logoutRequested;
    }

    /**
     * Get the unique user identifier that will match the user hash in {@link AuthResponse#getUserHash()}
     * @return Unique user identifier
     */
    public String getUserHash() {
        return userHash;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogoutCallbackResponse)) return false;

        LogoutCallbackResponse that = (LogoutCallbackResponse) o;

        if (logoutRequested != null ? !logoutRequested.equals(that.logoutRequested) : that.logoutRequested != null) return false;
        return !(userHash != null ? !userHash.equals(that.userHash) : that.userHash != null);

    }

    @Override public int hashCode() {
        int result = logoutRequested != null ? logoutRequested.hashCode() : 0;
        result = 31 * result + (userHash != null ? userHash.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "LogoutCallbackResponse{" +
                "deOrbitTime=" + logoutRequested +
                ", userHash='" + userHash + '\'' +
                '}';
    }
}
