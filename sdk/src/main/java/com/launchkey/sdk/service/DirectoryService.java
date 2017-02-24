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

package com.launchkey.sdk.service;

import com.launchkey.sdk.client.DirectoryClient;
import com.launchkey.sdk.domain.directory.Device;
import com.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.launchkey.sdk.error.*;

import java.util.List;

/**
 * Service for interacting with directory entities.
 */
@Deprecated
public interface DirectoryService extends DirectoryClient {
    // Intentionally left blank
}
