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

package com.launchkey.sdk.domain;

import java.util.Date;

public class Device {
    private final String name;

    private final DeviceStatus status;
    private final String type;
    private final Date created;
    private final Date updated;

    /**
     * A device that may perform user interaction with the Platform API
     * @param name Unique name of the device
     * @param status Status of the device (link pending, linked, unlink pending)
     * @param type Type of device (iOS, Android, etc.)
     * @param created When the device was created.
     * @param updated When the device was last updated.
     */
    public Device(String name, DeviceStatus status, String type, Date created, Date updated) {
        this.name = name;
        this.status = status;
        this.type = type;
        this.created = created;
        this.updated = updated;
    }

    /**
     * Get the unique name of the device
     * @return Unique name of the device
     */
    public String getName() {
        return name;
    }

    /**
     * Get the status of the device (link pending, linked, unlink pending)
     * @return Status of the device
     */
    public DeviceStatus getStatus() {
        return status;
    }

    /**
     * Get the type of device (iOS, Android, etc.)
     * @return Type of device
     */
    public String getType() {
        return type;
    }

    /**
     * Get the date in which the device was created.
     * @return When the device was created.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Get the date in which the device was last updated.
     * @return When the device was last updated.
     */
    public Date getUpdated() {
        return updated;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (getName() != null ? !getName().equals(device.getName()) : device.getName() != null) return false;
        if (getStatus() != device.getStatus()) return false;
        if (getType() != null ? !getType().equals(device.getType()) : device.getType() != null) return false;
        if (getCreated() != null ? !getCreated().equals(device.getCreated()) : device.getCreated() != null)
            return false;
        return getUpdated() != null ? getUpdated().equals(device.getUpdated()) : device.getUpdated() == null;

    }

    @Override public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getCreated() != null ? getCreated().hashCode() : 0);
        result = 31 * result + (getUpdated() != null ? getUpdated().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
