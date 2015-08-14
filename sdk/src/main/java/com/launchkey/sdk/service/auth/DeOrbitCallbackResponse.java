/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.launchkey.sdk.transport.v1.domain.LaunchKeyDateFormat;

import java.text.ParseException;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeOrbitCallbackResponse implements CallbackResponse {
    /**
     * The date and time the de-orbit was performed. This value is to be used tp ensure the user has not started a new
     * session since the de-orbit before ending their application session.
     */
    private final Date deOrbitTime;

    /**
     * Unique user identifier that will match the user hash in {@link AuthResponse}
     */
    private final String userHash;

    @JsonCreator
    public DeOrbitCallbackResponse(
            @JsonProperty("launchkey_time") String deOrbitTime,
            @JsonProperty("user_hash") String userHash
    ) {
        try {
            this.deOrbitTime = new LaunchKeyDateFormat().parseObject(deOrbitTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid LaunchKey Date format for deOrbitTime");
        }
        this.userHash = userHash;
    }

    public Date getDeOrbitTime() {
        return deOrbitTime;
    }

    public String getUserHash() {
        return userHash;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeOrbitCallbackResponse)) return false;

        DeOrbitCallbackResponse that = (DeOrbitCallbackResponse) o;

        if (deOrbitTime != null ? !deOrbitTime.equals(that.deOrbitTime) : that.deOrbitTime != null) return false;
        return !(userHash != null ? !userHash.equals(that.userHash) : that.userHash != null);

    }

    @Override public int hashCode() {
        int result = deOrbitTime != null ? deOrbitTime.hashCode() : 0;
        result = 31 * result + (userHash != null ? userHash.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "DeOrbitCallbackResponse{" +
                "deOrbitTime=" + deOrbitTime +
                ", userHash='" + userHash + '\'' +
                '}';
    }
}
