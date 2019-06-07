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

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryV3DevicesPostResponse {
    private final String code;
    private final String qrcode;
    private final UUID deviceId;

    @JsonCreator
    public DirectoryV3DevicesPostResponse(
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "qrcode", required = true) String qrcode,
            @JsonProperty(value = "device_id", required = true) UUID deviceId) {
        this.code = code;
        this.qrcode = qrcode;
        this.deviceId = deviceId;
    }

    public String getCode() {
        return code;
    }

    public String getQRCode() {
        return qrcode;
    }


    public UUID getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesPostResponse)) return false;
        DirectoryV3DevicesPostResponse that = (DirectoryV3DevicesPostResponse) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getQRCode(), that.getQRCode()) &&
                Objects.equals(getDeviceId(), that.getDeviceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), qrcode, getDeviceId());
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesPostResponse{" +
                "code='" + code + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }
}
