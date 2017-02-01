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

import java.util.List;

public class DirectoryV3DevicesListPostResponse {
    private final List<DirectoryV3DevicesListPostResponseDevice> devices;

    public DirectoryV3DevicesListPostResponse(List<DirectoryV3DevicesListPostResponseDevice> devices) {
        this.devices = devices;
    }

    public List<DirectoryV3DevicesListPostResponseDevice> getDevices() {
        return devices.subList(0, devices.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesListPostResponse)) return false;

        DirectoryV3DevicesListPostResponse that = (DirectoryV3DevicesListPostResponse) o;

        return getDevices() != null ? getDevices().equals(that.getDevices()) : that.getDevices() == null;
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesListPostResponse{" +
                "devices=" + devices +
                '}';
    }

    @Override
    public int hashCode() {
        return getDevices() != null ? getDevices().hashCode() : 0;
    }
}
