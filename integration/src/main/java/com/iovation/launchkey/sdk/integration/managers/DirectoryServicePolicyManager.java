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
    public PolicyCache policyCache;
    private Boolean usingNewPolicyFormat;

    @Inject
    public DirectoryServicePolicyManager(DirectoryManager directoryManager,
                                         DirectoryServiceManager directoryServiceManager,
                                         PolicyCache policyCache) {
        this.directoryManager = directoryManager;
        this.directoryServiceManager = directoryServiceManager;
        this.policyCache = policyCache;
        cleanUp();
    }

    @After
    public void cleanUp() {
        this.currentServicePolicyEntity = new ServicePolicyEntity();
    }
    private DirectoryClient getDirectoryClient() {
        return directoryManager.getDirectoryClient();
    }

    // Any Policy
    public void retrievePolicyForService(UUID serviceId) throws Throwable {
        PolicyAdapter adapter = getDirectoryClient().getServicePolicy(serviceId);
        if (adapter instanceof ServicePolicy) {
            ServicePolicy policy = (ServicePolicy) adapter;
            currentServicePolicyEntity = ServicePolicyEntity.fromServicePolicy(policy);
        }
        else if (adapter instanceof Policy) {
            policyCache.setCachedPolicy((Policy) adapter);
        }
        else {
            throw new Throwable("Retrieved Policy of unknown type");
        }
    }

    public void removePolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().removeServicePolicy(serviceId);
    }

    public void retrievePolicyForCurrentService() throws Throwable {
        retrievePolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }

    // Legacy Policy

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

    private void setPolicyForService(UUID serviceId) throws Throwable {
        getDirectoryClient().setServicePolicy(serviceId, policyCache.cachedPolicy);
    }

    public void setPolicyForCurrentService() throws Throwable {
        setPolicyForService(directoryServiceManager.getCurrentServiceEntity().getId());
    }


}
