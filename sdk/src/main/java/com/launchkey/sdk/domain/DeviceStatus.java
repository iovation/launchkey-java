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

/**
 * Enum to represent device statuses
 */
public enum DeviceStatus {
    LINK_PENDING (0, false),
    LINKED (1, true),
    UNLINK_PENDING (2, true);

    private final int statusCode;
    private final boolean active;

    /**
     * @param statusCode Device status code
     * @param active Is the device active based on the status code
     */
    DeviceStatus(int statusCode, boolean active) {
        this.statusCode = statusCode;
        this.active = active;
    }

    /**
     * Get the device status code
     * @return The device status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Is the device active based on the status code
     * @return Is the device active based on the status code
     */
    public boolean isActive() {
        return active;
    }

    public static DeviceStatus fromCode(int code) {
        DeviceStatus status = null;
        for (DeviceStatus deviceStatus : DeviceStatus.values()) {
            if (deviceStatus.getStatusCode() == code) {
                return deviceStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status code: ".concat(String.valueOf(code)));
    }
}
