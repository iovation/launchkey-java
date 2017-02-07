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

package com.launchkey.sdk.domain.directory;

public class Device {
    private final String id;
    private final String name;
    private final DeviceStatus status;
    private final String type;

    /**
     * A device that may perform user interaction with the Platform API
     * @param id Unique identifier for the device
     * @param name Unique name of the device
     * @param status Status of the device (link pending, linked, unlink pending)
     * @param type Type of device (iOS, Android, etc.)
     */
    public Device(String id, String name, DeviceStatus status, String type) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
    }

    /**
     * Get the unique identifier for the device
     * @return Unique identifier for the device
     */
    public String getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (getId() != null ? !getId().equals(device.getId()) : device.getId() != null) return false;
        if (getName() != null ? !getName().equals(device.getName()) : device.getName() != null) return false;
        if (getStatus() != device.getStatus()) return false;
        return getType() != null ? getType().equals(device.getType()) : device.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                '}';
    }
}
