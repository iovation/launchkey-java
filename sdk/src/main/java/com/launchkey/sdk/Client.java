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

import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;

/**
 * @deprecated Replaced with {@link AppClient} and {@link OrgClient}
 */
@Deprecated
public interface Client {
    /**
     * Get an auth service
     * @return auth service
     */
    AuthService auth();

    /**
     * Get a white label service
     * @return white label service
     */
    WhiteLabelService whiteLabel();
}
