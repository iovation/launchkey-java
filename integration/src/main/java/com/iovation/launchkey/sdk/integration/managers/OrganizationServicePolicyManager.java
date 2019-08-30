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
import com.iovation.launchkey.sdk.domain.policy.PolicyAdapter;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import cucumber.api.java.After;

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

    public void retrievePolicyForService(UUID serviceId) throws Throwable {
        PolicyAdapter adapter = client.getServicePolicy(serviceId);
        ServicePolicy policy = (ServicePolicy) adapter;
        currentServicePolicyEntity = ServicePolicyEntity.fromServicePolicy(policy);
    }

    public void retrievePolicyForCurrentService() throws Throwable {
        retrievePolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }

    public void removePolicyForService(UUID serviceId) throws Throwable {
        client.removeServicePolicy(serviceId);
    }

    public void removePolicyForCurrentService() throws Throwable {
        removePolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }

    public void setPolicyForService(UUID serviceId) throws Throwable {
        client.setServicePolicy(serviceId, currentServicePolicyEntity.toServicePolicy());
    }

    public void setPolicyForCurrentService() throws Throwable {
        setPolicyForService(organizationServiceManager.getCurrentServiceEntity().getId());
    }
}
