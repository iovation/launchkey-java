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

import java.util.UUID;

public class DirectoryV3DevicesDeleteRequest {
    private final String identifier;
    private final UUID deviceId;


    public DirectoryV3DevicesDeleteRequest(String identifier, UUID deviceId) {
        this.identifier = identifier;
        this.deviceId = deviceId;
    }

    @JsonProperty(value = "identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty(value = "device_id")
    public UUID getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesDeleteRequest)) return false;

        DirectoryV3DevicesDeleteRequest that = (DirectoryV3DevicesDeleteRequest) o;

        if (getIdentifier() != null ? !getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() != null)
            return false;
        return getDeviceId() != null ? getDeviceId().equals(that.getDeviceId()) : that.getDeviceId() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdentifier() != null ? getIdentifier().hashCode() : 0;
        result = 31 * result + (getDeviceId() != null ? getDeviceId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesDeleteRequest{" +
                "identifier='" + identifier + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }
}
