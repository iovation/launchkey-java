/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk;

import com.iovation.launchkey.sdk.service.DirectoryService;
import com.iovation.launchkey.sdk.service.ServiceService;

/**
 * Client for Organization based credentials.
 */
@Deprecated
public interface OrganizationClient {

    /**
     * Get a Directory Service for the provided Directory ID
     * @param directoryId Directory ID
     * @return Directory getServiceService
     */
    DirectoryService getDirectoryService(String directoryId);

    /**
     * Get a Service service for the provided Service ID
     * @param serviceId Service ID
     * @return Service getServiceService
     */
    ServiceService getServiceService(String serviceId);
}

