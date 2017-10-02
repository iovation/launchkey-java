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
import com.iovation.launchkey.sdk.client.DirectoryClient;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import cucumber.api.java.After;

import java.util.UUID;

@Singleton
public class DirectoryServicePolicyManager {
    private final DirectoryManager directoryManager;
    private final DirectoryServiceManager directoryServiceManager;
    private ServicePolicyEntity currentServicePolicyEntity;

    @Inject
    public DirectoryServicePolicyManager(DirectoryManager directoryManager,
                                         DirectoryServiceManager directoryServiceManager) {
        this.directoryManager = directoryManager;
        this.directoryServiceManager = directoryServiceManager;
        cleanUp();
    }

    @After
    public void cleanUp() {
        this.currentServicePolicyEntity = new ServicePolicyEntity();
    }

    ServicePolicyEntity getCurrentServicePolicyEntity() {
        return currentServicePolicyEntity;
    }

    void retrievePolicyForService(UUID serviceId) throws Throwable {
        ServicePolicy policy = getDirectoryClient().getServicePolicy(serviceId);
        currentServicePolicyEntity = ServicePolicyEntity.fromServicePolicy(policy);
    }

    void retrievePolicyForCurrentService() throws Throwable {
        retrievePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    void removePolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().removeServicePolicy(serviceId);
    }

    void removePolicyForCurrentService() throws Throwable {
        removePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    void setPolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().setServicePolicy(serviceId, currentServicePolicyEntity.toServicePolicy());
    }

    void setPolicyForCurrentService() throws Throwable {
        setPolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    private DirectoryClient getDirectoryClient() {
        return directoryManager.getDirectoryClient();
    }
}
