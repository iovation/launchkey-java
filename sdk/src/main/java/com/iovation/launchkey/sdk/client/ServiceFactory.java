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

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.UUIDHelper;
import com.iovation.launchkey.sdk.transport.Transport;

import java.util.UUID;

public class ServiceFactory {

    private final Transport transport;
    private final UUID serviceId;

    public ServiceFactory(Transport transport, UUID serviceID) {
        UUIDHelper.validateVersion(serviceID, 1);
        this.transport = transport;
        this.serviceId = serviceID;
    }

    public ServiceClient makeServiceClient() {
        return new BasicServiceClient(serviceId, transport);
    }
}
