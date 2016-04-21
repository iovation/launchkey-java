/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
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

/**
 * Response given for pair request
 */
public class PairResponse {
    private final LinkResponse linkResponse;

    /**
     * @param code      Code to to be used by the mobile app to pair the user and device by manually entering the code.
     * @param qrCodeUrl URL for a QR code image to be used by the mobile app to pair the user and device by reading
     *                  the QR code on the mobile device.
     */
    @JsonCreator
    public PairResponse(String code, String qrCodeUrl) {
        linkResponse = new LinkResponse(code, qrCodeUrl);
    }

    public PairResponse(LinkResponse linkResponse) {
        this.linkResponse = linkResponse;
    }

    /**
     * Get the pairing code
     *
     * @return Code to to be used by the mobile app to pair the user and device by manually entering the code.
     */
    public String getCode() {
        return linkResponse.getCode();
    }

    /**
     * Get the URL for the pairing QR code
     *
     * @return URL for a QR code image to be used by the mobile app to pair the user and device by reading
     * the QR code on the mobile device.
     */
    public String getQrCodeUrl() {
        return linkResponse.getQrCodeUrl();
    }

    @Override public String toString() {
        return "PairResponse{" +
                "code='" + linkResponse.getCode() + '\'' +
                ", qrCodeUrl='" + linkResponse.getQrCodeUrl() + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairResponse)) return false;

        PairResponse that = (PairResponse) o;

        return linkResponse.equals(that.linkResponse);

    }

    @Override public int hashCode() {
        return linkResponse.hashCode();
    }
}
