/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.domain.directory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

/**
 * Response given for a White Label device link request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryUserDeviceLinkData {
    /**
     * Code to to be used by the mobile app to link the user and device by manually entering the code.
     */
    private final String code;

    /**
     * URL for a QR code image to be used by the mobile app to link the user and device by reading
     * the QR code on the mobile device.
     */
    private final String qrCodeUrl;
    private final UUID deviceId;

    /**
     * @param code      Code to to be used by the mobile app to link the user and device by manually entering the code.
     * @param qrCodeUrl URL for a QR code image to be used by the mobile app to link the user and device by reading
     *                  the QR code on the mobile device.
     * @deprecated Please use {@link #DirectoryUserDeviceLinkData(String, String, UUID)}
     */
    @Deprecated
    public DirectoryUserDeviceLinkData(String code, String qrCodeUrl) {
        this(code, qrCodeUrl, null);
    }

    /**
     * @param code      Code to to be used by the mobile app to link the user and device by manually entering the code.
     * @param qrCodeUrl URL for a QR code image to be used by the mobile app to link the user and device by reading
     *                  the QR code on the mobile device.
     * @param deviceId Identifier for the Device to be linked
     */
    @JsonCreator
    public DirectoryUserDeviceLinkData(@JsonProperty(value = "code", required = true) String code,
                                       @JsonProperty(value = "qrcode", required = true) String qrCodeUrl,
                                       @JsonProperty(value = "device_id", required = true) UUID deviceId) {
        this.code = code;
        this.qrCodeUrl = qrCodeUrl;
        this.deviceId = deviceId;
    }

    /**
     * Get the pairing code
     *
     * @return Code to to be used by the mobile app to link the user and device by manually entering the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the URL for the pairing QR code
     *
     * @return URL for a QR code image to be used by the mobile app to link the user and device by reading
     * the QR code on the mobile device.
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * Get the ID of the Device that will be linked
     * @return Device ID
     */
    public UUID getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryUserDeviceLinkData)) return false;
        DirectoryUserDeviceLinkData that = (DirectoryUserDeviceLinkData) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getQrCodeUrl(), that.getQrCodeUrl()) &&
                Objects.equals(getDeviceId(), that.getDeviceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getQrCodeUrl(), getDeviceId());
    }

    @Override
    public String toString() {
        return "DirectoryUserDeviceLinkData{" +
                "code='" + code + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }
}
