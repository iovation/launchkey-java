/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.whitelabel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response given for link request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkResponse {
    /**
     * Code to to be used by the mobile app to link the user and device by manually entering the code.
     */
    private final String code;

    /**
     * URL for a QR code image to be used by the mobile app to link the user and device by reading
     * the QR code on the mobile device.
     */
    private final String qrCodeUrl;

    /**
     * @param code      Code to to be used by the mobile app to link the user and device by manually entering the code.
     * @param qrCodeUrl URL for a QR code image to be used by the mobile app to link the user and device by reading
     *                  the QR code on the mobile device.
     */
    @JsonCreator
    public LinkResponse(@JsonProperty("code") String code, @JsonProperty("qrcode") String qrCodeUrl) {
        this.code = code;
        this.qrCodeUrl = qrCodeUrl;
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

    @Override public String toString() {
        return "LinkResponse{" +
                "code='" + code + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkResponse)) return false;

        LinkResponse that = (LinkResponse) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(qrCodeUrl != null ? !qrCodeUrl.equals(that.qrCodeUrl) : that.qrCodeUrl != null);

    }

    @Override public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (qrCodeUrl != null ? qrCodeUrl.hashCode() : 0);
        return result;
    }
}
