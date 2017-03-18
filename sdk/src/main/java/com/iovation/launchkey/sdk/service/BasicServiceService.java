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

package com.iovation.launchkey.sdk.service;

import com.iovation.launchkey.sdk.client.BasicServiceClient;
import com.iovation.launchkey.sdk.transport.Transport;

import java.util.UUID;

@Deprecated
public class BasicServiceService extends BasicServiceClient implements ServiceService {

    public BasicServiceService(UUID serviceId, Transport transport) {
        super(serviceId, transport);
    }
}
