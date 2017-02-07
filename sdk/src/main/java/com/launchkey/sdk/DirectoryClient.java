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

package com.launchkey.sdk;

import com.launchkey.sdk.service.DirectoryService;
import com.launchkey.sdk.service.ServiceService;

/**
 * CLient for Directory Credentials
 */
public interface DirectoryClient {
    /**
     * Get a Directory service based on the client credentials
     * @return Directory service
     */
    DirectoryService getDirectoryService();

    /**
     * Get a Service service for the provided Service ID
     * @param serviceId Service ID
     * @return Service service
     */
    ServiceService getServiceService(String serviceId);
}
