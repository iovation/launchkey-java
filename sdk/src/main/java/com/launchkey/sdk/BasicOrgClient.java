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

import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelServiceFactory;
import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelService;

/**
 * A basic implementation of an {@link OrgClient}
 */
public class BasicOrgClient implements OrgClient {
    private final WhiteLabelServiceFactory whiteLabelServiceFactory;

    /**
     * @param whiteLabelServiceFactory White Label Service factor to build White Label Services for the Provided SDK Key
     */
    public BasicOrgClient(WhiteLabelServiceFactory whiteLabelServiceFactory) {
        this.whiteLabelServiceFactory = whiteLabelServiceFactory;
    }

    @Override
    public WhiteLabelService whiteLabel(String sdkKey) {
        return whiteLabelServiceFactory.getService(sdkKey);
    }
}
