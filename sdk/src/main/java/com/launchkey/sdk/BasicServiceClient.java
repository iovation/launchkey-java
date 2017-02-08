/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.service.BasicServiceService;
import com.launchkey.sdk.service.ServiceService;
import com.launchkey.sdk.transport.Transport;

import java.util.UUID;

/**
 * Basic client for interacting with the LaunchKey API utilizing
 * Service credentials.
 */
public class BasicServiceClient implements ServiceClient {

    private final ServiceService serviceService;

    public BasicServiceClient(Transport transport, UUID serviceID) {
        serviceService = new BasicServiceService(serviceID, transport);
    }

    @Override
    public ServiceService getServiceService() {
        return serviceService;
    }
}
