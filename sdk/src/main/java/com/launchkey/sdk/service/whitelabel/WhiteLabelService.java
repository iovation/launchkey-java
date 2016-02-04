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

import com.launchkey.sdk.service.error.LaunchKeyException;

public interface WhiteLabelService {
    /**
     * Pair a device with a White Label user.  If the user does not exist in the White Label Group, the user will
     * be created.
     *
     * @param identifier Permanent and unique identifier of this user within your application.
     * This identifier will be used authenticate the user as well as pair devices additional devices to
     * the user's account within your white label group.
     *
     * @return {@link PairResponse} object containing information for pairing a device with a user.
     *
     * @throws LaunchKeyException when an error occurs pairing the user
     */
    PairResponse pairUser(String identifier) throws LaunchKeyException;
}
