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

import java.util.Date;

public class ServerSentEventUserServiceSessionEnd implements ServerSentEvent {
    private final String userHash;
    private final Date apiTime;

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public ServerSentEventUserServiceSessionEnd(
            @JsonProperty("service_user_hash") String userHash,
            @JsonProperty("api_time") Date apiTime
    ) {
        this.userHash = userHash;
        this.apiTime = apiTime;
    }

    public String getUserHash() {
        return userHash;
    }

    public Date getApiTime() {
        return apiTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSentEventUserServiceSessionEnd)) return false;

        ServerSentEventUserServiceSessionEnd that = (ServerSentEventUserServiceSessionEnd) o;

        if (userHash != null ? !userHash.equals(that.userHash) : that.userHash != null) return false;
        return apiTime != null ? apiTime.equals(that.apiTime) : that.apiTime == null;
    }

    @Override
    public int hashCode() {
        int result = userHash != null ? userHash.hashCode() : 0;
        result = 31 * result + (apiTime != null ? apiTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserServiceSessionEndServerSentEvent{" +
                "userHash='" + userHash + '\'' +
                ", apiTime=" + apiTime +
                '}';
    }
}
