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

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryV3DevicesPostResponse {
    public final String code;
    public final String qrcode;

    @JsonCreator
    public DirectoryV3DevicesPostResponse(
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "qrcode", required = true) String qrcode) {
        this.code = code;
        this.qrcode = qrcode;
    }

    public String getCode() {
        return code;
    }

    public String getQRCode() {
        return qrcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesPostResponse)) return false;

        DirectoryV3DevicesPostResponse that = (DirectoryV3DevicesPostResponse) o;

        if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) return false;
        return getQRCode() != null ? getQRCode().equals(that.getQRCode()) : that.getQRCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getQRCode() != null ? getQRCode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesPostResponse{" +
                "code='" + code + '\'' +
                ", qrcode='" + qrcode + '\'' +
                '}';
    }
}
