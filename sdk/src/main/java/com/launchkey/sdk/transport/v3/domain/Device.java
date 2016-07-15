/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    private final String name;
    private final String deviceType;
    private final Integer status;
    private final Date created;
    private final Date updated;

    @JsonCreator
    public Device(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "status", required = true) Integer status,
            @JsonProperty(value = "device_type", required = true) String deviceType,
            @JsonProperty(value = "created", required = true) Date created,
            @JsonProperty(value = "updated", required = true) Date updated
    ) {
        this.name = name;
        this.status = status;
        this.deviceType = deviceType;
        this.created = created;
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (getName() != null ? !getName().equals(device.getName()) : device.getName() != null) return false;
        if (getDeviceType() != null ? !getDeviceType().equals(device.getDeviceType()) : device.getDeviceType() != null) {
            return false;
        }
        if (getStatus() != null ? !getStatus().equals(device.getStatus()) : device.getStatus() != null) return false;
        if (getCreated() != null ? !getCreated().equals(device.getCreated()) : device.getCreated() != null)
            return false;
        return getUpdated() != null ? getUpdated().equals(device.getUpdated()) : device.getUpdated() == null;

    }

    @Override public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getDeviceType() != null ? getDeviceType().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getCreated() != null ? getCreated().hashCode() : 0);
        result = 31 * result + (getUpdated() != null ? getUpdated().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
