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
import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import com.iovation.launchkey.sdk.integration.entities.ServiceEntity;
import cucumber.api.java.After;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Singleton
public class OrganizationServiceManager {
    private ServiceEntity currentServiceEntity;
    private ServiceEntity previousServiceEntity;
    private List<ServiceEntity> services = new ArrayList<>();
    private OrganizationClient client;
    private List<ServiceEntity> currentServiceEntities = new ArrayList<>();
    private Set<PublicKeyEntity> currentServicePublicKeys = new HashSet<>();

    @Inject
    public OrganizationServiceManager(OrganizationFactory factory) {
        client = factory.makeOrganizationClient();
        resetState();
    }

    @After
    public void resetState() {
        currentServiceEntity = null;
        previousServiceEntity = null;
        currentServiceEntities.clear();
        currentServicePublicKeys.clear();
    }

    @After
    public void cleanupServices() throws Throwable {
        List<ServiceEntity> cleanup = new ArrayList<>();
        cleanup.addAll(services);
        for (ServiceEntity service : cleanup) {
            try {
                client.updateService(service.getId(), service.getName(), "Ready for deletion", null, null, false);
                services.remove(service);
            } catch (Exception e) {
                System.err.println(
                        "Unable to set Organization Service " + service.getId() + " as inactive due to error " + e);
            }
        }
    }

    public ServiceEntity getCurrentServiceEntity() {
        if (currentServiceEntity == null) {
            throw new RuntimeException("Current Service must be defined.");
        }
        return currentServiceEntity;
    }

    public ServiceEntity getPreviousServiceEntity() {
        return previousServiceEntity;
    }

    public List<ServiceEntity> getCurrentServiceEntities() {
        return currentServiceEntities;
    }

    public void createService(String name, String description, URI icon, URI callbackURL, Boolean active)
            throws Throwable {
        UUID id = client.createService(name, description, icon, callbackURL, active);
        previousServiceEntity = currentServiceEntity;
        currentServiceEntity = new ServiceEntity(id, name, description, icon, callbackURL, active);
    }

    public void createService(String name) throws Throwable {
        createService(name, null, null, null, true);
    }

    public void createService() throws Throwable {
        createService(Utils.createRandomServiceName());
    }

    public void createService(String description, URI icon, URI callbackUrl, Boolean active) throws Throwable {
        createService(Utils.createRandomServiceName(), description, icon, callbackUrl, active);
    }

    public void retrieveCurrentService() throws Throwable {
        retrieveService(currentServiceEntity.getId());
    }

    public void retrieveService(UUID serviceId) throws Throwable {
        Service service = client.getService(serviceId);
        previousServiceEntity = currentServiceEntity;
        currentServiceEntity = ServiceEntity.fromService(service);
    }

    public void updateService(UUID serviceId, String name, String description, URI icon, URI callbackURL, Boolean active)
            throws Throwable {
        client.updateService(serviceId, name, description, icon, callbackURL, active);
    }

    public void updateService(UUID serviceId, String description, URI icon, URI callbackUrl, Boolean active) throws Throwable {
        updateService(serviceId, getCurrentServiceEntity().getName(), description, icon, callbackUrl, active);
    }

    public void retrieveServices(List<UUID> serviceIds) throws Throwable {
        List<ServiceEntity> services = new ArrayList<>();
        for (Service service : client.getServices(serviceIds)) {
            services.add(ServiceEntity.fromService(service));
        }
        currentServiceEntities.clear();
        currentServiceEntities.addAll(services);
    }

    public void retrieveAllServices() throws Throwable {
        List<ServiceEntity> services = new ArrayList<>();
        for (Service service : client.getAllServices()) {
            services.add(ServiceEntity.fromService(service));
        }
        currentServiceEntities.clear();
        currentServiceEntities.addAll(services);
    }

    public void addPublicKeyToCurrentService(UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires) throws Throwable {
        String keyId = client.addServicePublicKey(serviceId, publicKey, active, expires);
        getCurrentServiceEntity().getPublicKeys().add(new PublicKeyEntity(keyId, publicKey, active, null, expires));
    }

    public void addPublicKeyToCurrentService(RSAPublicKey publicKey, Boolean active, Date expires) throws Throwable {
        addPublicKeyToCurrentService(getCurrentServiceEntity().getId(), publicKey, active, expires);
    }

    public void updatePublicKey(UUID serviceId, String keyId, Boolean active, Date expires) throws Throwable{
        client.updateServicePublicKey(serviceId, keyId, active, expires);
    }

    public void updatePublicKey(String keyId, Boolean active, Date expires) throws Throwable{
        updatePublicKey(getCurrentServiceEntity().getId(), keyId, active, expires);
    }

    public void retrieveCurrentServicePublicKeysList() throws Throwable {
        retrieveServicePublicKeysList(getCurrentServiceEntity().getId());
    }

    public void retrieveServicePublicKeysList(UUID serviceId) throws Throwable {
        List<PublicKeyEntity> publicKeys = new ArrayList<>();
        for (PublicKey publicKey : client.getServicePublicKeys(serviceId)) {
            publicKeys.add(PublicKeyEntity.fromPublicKey(publicKey));
        }
        currentServicePublicKeys.clear();
        currentServicePublicKeys.addAll(publicKeys);
    }

    public Set<PublicKeyEntity> getCurrentServicePublicKeys() {
        return currentServicePublicKeys;
    }

    public void removePublicKey(UUID serviceId, String keyId) throws Throwable {
        client.removeServicePublicKey(serviceId, keyId);
    }
}
