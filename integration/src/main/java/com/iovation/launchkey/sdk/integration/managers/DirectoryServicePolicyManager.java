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

package com.iovation.launchkey.sdk.integration.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.client.DirectoryClient;
import com.iovation.launchkey.sdk.domain.policy.Policy;
import com.iovation.launchkey.sdk.domain.policy.PolicyAdapter;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import cucumber.api.java.After;

import java.util.UUID;

@Singleton
public class DirectoryServicePolicyManager {
    private final DirectoryManager directoryManager;
    private final DirectoryServiceManager directoryServiceManager;
    private ServicePolicyEntity currentServicePolicyEntity;

    // TODO: these in wrong place
    // Singleton instance variables that are accessed in DirectoryServicePolicySteps
    public FenceCache fenceCache;
    public MutablePolicy currentPolicyContext;

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
    private DirectoryClient getDirectoryClient() {
        return directoryManager.getDirectoryClient();
    }

    public void retrievePolicyForService(UUID serviceId) throws Throwable {
        PolicyAdapter adapter = getDirectoryClient().getServicePolicy(serviceId);
        if (adapter instanceof ServicePolicy) {
            ServicePolicy policy = (ServicePolicy) adapter;
            currentServicePolicyEntity = ServicePolicyEntity.fromServicePolicy(policy);
        }
        else {
            throw new Throwable("Expecting legacy policy type received new policy type");
        }
    }

    public void removePolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().removeServicePolicy(serviceId);
    }

    public void retrievePolicyForCurrentService() throws Throwable {
        retrievePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    public ServicePolicyEntity getCurrentServicePolicyEntity() {
        return currentServicePolicyEntity;
    }

    public void removeServicePolicyForCurrentService() throws Throwable {
        removePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    public void setServicePolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().setServicePolicy(serviceId, currentServicePolicyEntity.toServicePolicy());
    }

    public void setServicePolicyForCurrentService() throws Throwable {
        setServicePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    // New Policy Objects

    public void setPolicyForCurrentServiceToCurrentPolicyContext() throws Throwable {
        getDirectoryClient().setServicePolicy(directoryServiceManager.getCurrentServiceEntity().getId(), currentPolicyContext.toImmutablePolicy());
    }

    public Policy getCurrentlySetDirectoryServicePolicy() throws Throwable {
        PolicyAdapter adapter = getDirectoryClient().getServicePolicy(directoryServiceManager.getCurrentServiceEntity().getId());
        if (adapter instanceof ServicePolicy) {
            throw new Throwable("Something is wrong, returned legacy policy format expecting new format");
        }
        return (Policy) adapter;
    }
}
