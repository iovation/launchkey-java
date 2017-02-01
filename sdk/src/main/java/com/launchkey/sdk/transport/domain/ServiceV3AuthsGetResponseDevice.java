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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsGetResponseDevice {
    private final boolean response;
    private final UUID authorizationRequestId;
    private final String deviceId;
    private final String[] app_pins;

    @JsonCreator
    public ServiceV3AuthsGetResponseDevice(
            @JsonProperty(value = "response") boolean response,
            @JsonProperty(value = "auth_request") UUID authorizationRequestId,
            @JsonProperty(value = "device_id") String deviceId,
            @JsonProperty(value = "app_pins") String[] app_pins) {
        this.response = response;
        this.authorizationRequestId = authorizationRequestId;
        this.deviceId = deviceId;
        this.app_pins = app_pins;
    }

    public boolean isResponse() {
        return response;
    }

    public UUID getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String[] getApp_pins() {
        return app_pins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsGetResponseDevice)) return false;

        ServiceV3AuthsGetResponseDevice that = (ServiceV3AuthsGetResponseDevice) o;

        if (isResponse() != that.isResponse()) return false;
        if (getAuthorizationRequestId() != null ? !getAuthorizationRequestId().equals(that.getAuthorizationRequestId()) : that.getAuthorizationRequestId() != null)
            return false;
        if (getDeviceId() != null ? !getDeviceId().equals(that.getDeviceId()) : that.getDeviceId() != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getApp_pins(), that.getApp_pins());
    }

    @Override
    public int hashCode() {
        int result = (isResponse() ? 1 : 0);
        result = 31 * result + (getAuthorizationRequestId() != null ? getAuthorizationRequestId().hashCode() : 0);
        result = 31 * result + (getDeviceId() != null ? getDeviceId().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getApp_pins());
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsGetResponseDevice{" +
                "response=" + response +
                ", authorizationRequestId=" + authorizationRequestId +
                ", deviceId='" + deviceId + '\'' +
                ", app_pins=" + Arrays.toString(app_pins) +
                '}';
    }
}
