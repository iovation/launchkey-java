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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsPostResponse {
    private final UUID authRequest;
    private final String pushPackage;
    private List<String> deviceIds;

    @JsonCreator
    public ServiceV3AuthsPostResponse(
            @JsonProperty(value = "auth_request") UUID authRequest,
            @JsonProperty(value = "push_package") String pushPackage,
            @JsonProperty(value = "device_ids") List<String> deviceIds
    ) {
        this.authRequest = authRequest;
        this.pushPackage = pushPackage;
        this.deviceIds = deviceIds;
    }

    public UUID getAuthRequest() {
        return authRequest;
    }

    public String getPushPackage() {
        return pushPackage;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceV3AuthsPostResponse that = (ServiceV3AuthsPostResponse) o;
        return Objects.equals(authRequest, that.authRequest) &&
                Objects.equals(pushPackage, that.pushPackage) &&
                Objects.equals(deviceIds, that.deviceIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authRequest, pushPackage, deviceIds);
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostResponse{" +
                "authRequest=" + authRequest +
                ", pushPackage='" + pushPackage + '\'' +
                ", deviceIds=" + deviceIds +
                '}';
    }
}
