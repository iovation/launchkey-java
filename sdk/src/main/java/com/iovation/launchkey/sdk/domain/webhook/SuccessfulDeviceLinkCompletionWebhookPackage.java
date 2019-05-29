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

import com.iovation.launchkey.sdk.domain.directory.DeviceLinkCompletion;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;

import java.util.UUID;

/**
 * A Server Sent Event callback used to inform an application that the user has remotely ended their session.
 */
public class SuccessfulDeviceLinkCompletionWebhookPackage implements WebhookPackage {

    private final DeviceLinkCompletion deviceLinkCompletion;

    /**
     * @param deviceLinkCompletion Device link completion data
     */
    public SuccessfulDeviceLinkCompletionWebhookPackage(DeviceLinkCompletion deviceLinkCompletion)
    {
        this.deviceLinkCompletion = deviceLinkCompletion;
    }

    /**
     * Get the device link completion data
     * @return The device link completion data
     */
    public DeviceLinkCompletion getDeviceLinkCompletion() {
        return deviceLinkCompletion;
    }
}
