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
import com.iovation.launchkey.sdk.client.OrganizationClient;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.domain.policy.Policy;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import io.cucumber.java.After;

import java.util.UUID;

@Singleton
public class OrganizationServicePolicyManager {
    private final OrganizationServiceManager organizationServiceManager;
    private final OrganizationClient client;
    private ServicePolicyEntity currentServicePolicyEntity;

    @Inject
    public OrganizationServicePolicyManager(OrganizationFactory organizationFactory,
                                            OrganizationServiceManager organizationServiceManager) {
        this.organizationServiceManager = organizationServiceManager;
        this.client = organizationFactory.makeOrganizationClient();
        cleanUp();
    }

    @After
    public void cleanUp() {
        this.currentServicePolicyEntity = new ServicePolicyEntity();
    }

    public ServicePolicyEntity getCurrentServicePolicyEntity() {
        return currentServicePolicyEntity;
    }

    @Deprecated
    public void retrievePolicyForService(UUID serviceId) throws Throwable {
        ServicePolicy policy = client.getServicePolicy(serviceId);
        currentServicePolicyEntity = ServicePolicyEntity.fromServicePolicy(policy);
    }

    public void retrievePolicyForCurrentService() throws Throwable {
        retrievePolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }

    public void removePolicyForService(UUID serviceId) throws Throwable {
        client.removeServicePolicy(serviceId);
        currentServicePolicyEntity = new ServicePolicyEntity();
    }

    public void removePolicyForCurrentService() throws Throwable {
        removePolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }

    public void setPolicyForService(UUID serviceId) throws Throwable {
        client.setServicePolicy(serviceId, currentServicePolicyEntity.toServicePolicy());
    }

    public void setAdvancedPolicyForCurrentService() throws Throwable {
        setPolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }

    public void setAdvancedPolicyForCurrentService(Policy policy) throws Throwable {
        client.setAdvancedServicePolicy(organizationServiceManager.getCurrentServiceEntity().getId(), policy);
    }

    public Policy getCurrentlySetOrganizationServiceAdvancedPolicy() throws Throwable {
        return client.getAdvancedServicePolicy(organizationServiceManager.getCurrentServiceEntity().getId());
    }
}
