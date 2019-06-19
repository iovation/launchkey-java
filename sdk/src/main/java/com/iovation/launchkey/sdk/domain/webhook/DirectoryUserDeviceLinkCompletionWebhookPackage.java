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

package com.iovation.launchkey.sdk.domain.webhook;

import com.iovation.launchkey.sdk.domain.directory.DeviceLinkCompletionResponse;

/**
 * A Server Sent Event callback used to inform an application that the user has completed linking a Device.
 */
public class DirectoryUserDeviceLinkCompletionWebhookPackage implements WebhookPackage {

    private final DeviceLinkCompletionResponse deviceLinkCompletionResponse;

    /**
     * @param deviceLinkCompletionResponse Device link completion response
     */
    public DirectoryUserDeviceLinkCompletionWebhookPackage(DeviceLinkCompletionResponse deviceLinkCompletionResponse)
    {
        this.deviceLinkCompletionResponse = deviceLinkCompletionResponse;
    }

    /**
     * Get the device link completion response
     * @return The device link completion response
     */
    public DeviceLinkCompletionResponse getDeviceLinkCompletionResponse() {
        return deviceLinkCompletionResponse;
    }
}
