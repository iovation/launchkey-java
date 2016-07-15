/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelService;

/**
 * Client for performing Organization based interaction.
 */
public interface OrgClient {

    /**
     * Get a white label service
     * @param sdkKey SDK Key for the White Label Group
     * @return white label service
     */
    WhiteLabelService whiteLabel(String sdkKey);
}
