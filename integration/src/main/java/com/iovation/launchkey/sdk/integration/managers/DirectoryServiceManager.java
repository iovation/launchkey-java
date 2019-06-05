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
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.error.Forbidden;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import com.iovation.launchkey.sdk.integration.entities.ServiceEntity;
import cucumber.api.java.After;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Singleton
public class DirectoryServiceManager {
    private final OrganizationFactory factory;
    private final Map<UUID, DirectoryClient> directoryClients = new HashMap<>();
    private ServiceEntity currentServiceEntity;
    private ServiceEntity previousServiceEntity;
    private List<ServiceEntity> currentServiceEntities = new ArrayList<>();
    private Set<PublicKeyEntity> currentServicePublicKeys = new HashSet<>();

    @Inject
    public DirectoryServiceManager(OrganizationFactory factory) {
        this.factory = factory;
        cleanupState();
    }

    @After
    public void cleanupState() {
        currentServiceEntity = null;
        previousServiceEntity = null;
        currentServiceEntities.clear();
        currentServicePublicKeys.clear();
    }

    @After(order = 20000)
    public void cleanupServices() throws Throwable {
        Set<UUID> directoryIds = directoryClients.keySet();
        for (UUID directoryId : directoryIds) {
            try {
                DirectoryClient client = directoryClients.get(directoryId);
                for (Service service : client.getAllServices()) {
                    client.updateService(service.getId(), service.getName(), "Ready for deletion", null, null, false);
                }
                directoryClients.remove(directoryId);
            } catch (Forbidden e) {
                System.err.println(
                        "Unable to set all Directory Services to inactive for Directory " + directoryId + " due to error " + e);
                // There is no recovering from this, do not attempt to deactivate the Services for this Directory again
                directoryClients.remove(directoryId);
            } catch (Exception e) {
                System.err.println(
                        "Unable to set all Directory Services to inactive for Directory " + directoryId +
                                " due to error " + e);
            }
        }
    }

    public ServiceEntity getCurrentServiceEntity() {
        if (currentServiceEntity == null) throw new RuntimeException("Service must be created");
        return currentServiceEntity;
    }

    public List<ServiceEntity> getCurrentServiceEntities() {
        return currentServiceEntities;
    }

    public Set<PublicKeyEntity> getCurrentServicePublicKeys() {
        return currentServicePublicKeys;
    }

    private UUID createService(UUID directoryId, String name, String description, URI icon, URI callbackURL,
                               Boolean active) throws Throwable {
        UUID serviceId = getClient(directoryId).createService(name, description, icon, callbackURL, active);
        previousServiceEntity = currentServiceEntity;
        currentServiceEntity = new ServiceEntity(serviceId, name, description, icon, callbackURL, active);
        return serviceId;
    }

    public UUID createService(UUID directoryId, String name) throws Throwable {
        return createService(directoryId, name, null, null, null, true);
    }

    public UUID createService(UUID directoryId) throws Throwable {
        return createService(directoryId, Utils.createRandomServiceName());
    }

    public UUID createService(UUID directoryId, String description, URI icon, URI callbackUrl, Boolean active)
            throws Throwable {
        return createService(directoryId, Utils.createRandomServiceName(), description, icon, callbackUrl, active);
    }

    public ServiceClient getServiceClient()
            throws Throwable {
        return factory.makeServiceClient(getCurrentServiceEntity().getId().toString());
    }

    private DirectoryClient getClient(UUID directoryId) {
        if (!directoryClients.containsKey(directoryId)) {
            directoryClients.put(directoryId, factory.makeDirectoryClient(directoryId.toString()));
        }
        return directoryClients.get(directoryId);
    }

    public void retrieveService(UUID directoryId, UUID serviceId) throws Throwable {
        Service service = getClient(directoryId).getService(serviceId);
        previousServiceEntity = currentServiceEntity;
        currentServiceEntity = ServiceEntity.fromService(service);
    }

    public void retrieveCurrentService(UUID directoryId) throws Throwable {
        retrieveService(directoryId, currentServiceEntity.getId());
    }

    public ServiceEntity getPreviousServiceEntity() {
        return previousServiceEntity;
    }

    public void addPublicKeyToService(UUID directoryId, UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws Throwable {
        getClient(directoryId).addServicePublicKey(serviceId, publicKey, active, expires);
    }


    public void addPublicKeyToCurrentService(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws Throwable {
        String keyId = getClient(directoryId)
                .addServicePublicKey(getCurrentServiceEntity().getId(), publicKey, active, expires);
        currentServiceEntity.getPublicKeys().add(new PublicKeyEntity(keyId, publicKey, active, null, expires));
    }


    public void updatePublicKey(UUID directoryId, UUID serviceId, String keyId, Boolean active, Date expires)
            throws Throwable {
        getClient(directoryId).updateServicePublicKey(serviceId, keyId, active, expires);
    }

    public void retrievePublicKeysList(UUID directoryId, UUID serviceId) throws Throwable {
        List<PublicKeyEntity> publicKeys = new ArrayList<>();
        for (PublicKey publicKey : getClient(directoryId).getServicePublicKeys(serviceId)) {
            publicKeys.add(PublicKeyEntity.fromPublicKey(publicKey));
        }
        currentServicePublicKeys.clear();
        currentServicePublicKeys.addAll(publicKeys);
    }

    public void removePublicKey(UUID directoryId, UUID serviceId, String keyId) throws Throwable {
        getClient(directoryId).removeServicePublicKey(serviceId, keyId);
    }

    public void updateService(UUID directoryId, UUID serviceId, String description, URI icon, URI callbackURL,
                       Boolean active) throws Throwable {
        String name = Utils.createRandomServiceName();
        getClient(directoryId).updateService(serviceId, name, description, icon, callbackURL, active);
        previousServiceEntity = currentServiceEntity;
        currentServiceEntity = new ServiceEntity(serviceId, name, description, icon, callbackURL, active);
    }

    public void retrieveServices(UUID directoryId, List<UUID> serviceIds) throws Throwable {
        List<ServiceEntity> services = new ArrayList<>();
        for (Service service : getClient(directoryId).getServices(serviceIds)) {
            services.add(ServiceEntity.fromService(service));
        }
        currentServiceEntities.clear();
        currentServiceEntities.addAll(services);
    }

    public void retrieveAllServices(UUID directoryId) throws Throwable {
        List<ServiceEntity> services = new ArrayList<>();
        for (Service service : getClient(directoryId).getAllServices()) {
            services.add(ServiceEntity.fromService(service));
        }
        currentServiceEntities.clear();
        currentServiceEntities.addAll(services);
    }
}
