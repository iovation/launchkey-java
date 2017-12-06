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

package com.iovation.launchkey.sdk.integration;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceClient;

import java.util.UUID;

public class DirectoryServiceSessionManager {
    private final OrganizationFactory factory;

    @Inject
    public DirectoryServiceSessionManager(OrganizationFactory factory) {
        this.factory = factory;
    }

    void startSession(UUID serviceId, String userIdentifier) throws Throwable {
        getServiceClient(serviceId).sessionStart(userIdentifier);
    }

    void startSession(UUID serviceId, String userIdentifier, String authRequestId) throws Throwable {
        getServiceClient(serviceId).sessionStart(userIdentifier, authRequestId);
    }

    void endSession(UUID serviceId, String userIdentifier) throws Throwable {
        getServiceClient(serviceId).sessionEnd(userIdentifier);
    }

    private ServiceClient getServiceClient(UUID serviceId) {
        return factory.makeServiceClient(serviceId.toString());
    }
}
