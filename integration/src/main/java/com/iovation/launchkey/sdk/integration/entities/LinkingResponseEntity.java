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

package com.iovation.launchkey.sdk.integration.entities;

import java.net.URI;

public class LinkingResponseEntity {
    private final String linkingCode;
    private final String qrCodeURL;

    public LinkingResponseEntity(String linkingCode, String qrCodeURL) {
        this.linkingCode = linkingCode;
        this.qrCodeURL = qrCodeURL;
    }

    public String getLinkingCode() {
        return linkingCode;
    }

    public String getQrCodeURL() {
        return qrCodeURL;
    }
}
