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
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;

import java.util.UUID;

@Singleton
public class DirectoryServiceAuthsManager {

    private final OrganizationFactory factory;

    @Inject
    public DirectoryServiceAuthsManager(OrganizationFactory factory) {
        this.factory = factory;
    }

    String createAuthsRequest(UUID serviceId, String userIdentifier) throws Throwable {
        return getServiceClient(serviceId).authorize(userIdentifier);
    }

    String createAuthsRequest(UUID serviceId, String userIdentifier, String context) throws Throwable {
        return getServiceClient(serviceId).authorize(userIdentifier, context);
    }

    String createAuthsRequest(UUID serviceId, String userIdentifier, AuthPolicy policy) throws Throwable {
        return getServiceClient(serviceId).authorize(userIdentifier, null, policy);
    }

    AuthorizationResponse getAuthResponse(UUID serviceId, UUID authRequest) throws Throwable {
        return getServiceClient(serviceId).getAuthorizationResponse(authRequest.toString());
    }

    private ServiceClient getServiceClient(UUID serviceId) {
        return factory.makeServiceClient(serviceId.toString());
    }
}
