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

import java.util.Arrays;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsGetResponseDevice {
    private final boolean response;
    private final UUID authorizationRequestId;
    private final String deviceId;
    private final String[] servicePins;

    @JsonCreator
    public ServiceV3AuthsGetResponseDevice(
            @JsonProperty(value = "response") boolean response,
            @JsonProperty(value = "auth_request") UUID authorizationRequestId,
            @JsonProperty(value = "device_id") String deviceId,
            @JsonProperty(value = "service_pins") String[] servicePins) {
        this.response = response;
        this.authorizationRequestId = authorizationRequestId;
        this.deviceId = deviceId;
        this.servicePins = servicePins;
    }

    public boolean getResponse() {
        return response;
    }

    public UUID getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String[] getServicePins() {
        return servicePins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsGetResponseDevice)) return false;

        ServiceV3AuthsGetResponseDevice that = (ServiceV3AuthsGetResponseDevice) o;

        if (getResponse() != that.getResponse()) return false;
        if (getAuthorizationRequestId() != null ? !getAuthorizationRequestId().equals(that.getAuthorizationRequestId()) : that.getAuthorizationRequestId() != null)
            return false;
        if (getDeviceId() != null ? !getDeviceId().equals(that.getDeviceId()) : that.getDeviceId() != null)
            return false;
        return Arrays.equals(getServicePins(), that.getServicePins());
    }

    @Override
    public int hashCode() {
        int result = (getResponse() ? 1 : 0);
        result = 31 * result + (getAuthorizationRequestId() != null ? getAuthorizationRequestId().hashCode() : 0);
        result = 31 * result + (getDeviceId() != null ? getDeviceId().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getServicePins());
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsGetResponseDevice{" +
                "response=" + response +
                ", authorizationRequestId=" + authorizationRequestId +
                ", deviceId='" + deviceId + '\'' +
                ", servicePins=" + Arrays.toString(servicePins) +
                '}';
    }
}
