/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.service.BasicDirectoryService;
import com.launchkey.sdk.service.BasicServiceService;
import com.launchkey.sdk.service.DirectoryService;
import com.launchkey.sdk.service.ServiceService;
import com.launchkey.sdk.transport.Transport;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A basic implementation of an {@link OrganizationClient}
 */
public class BasicOrganizationClient implements OrganizationClient {
    private final Map<String, DirectoryService> directoryServices;
    private final Map<String, ServiceService> serviceServices;
    private final Transport transport;

    public BasicOrganizationClient(Transport transport, UUID organizationId) {
        this.transport = transport;
        this.directoryServices = new ConcurrentHashMap<String, DirectoryService>();
        this.serviceServices = new ConcurrentHashMap<String, ServiceService>();
    }

    @Override
    public DirectoryService getDirectoryService(String directoryId) {
        DirectoryService service;
        if (directoryId == null) {
            throw new IllegalArgumentException("Directory ID cannot be null!");
        } else if (!directoryServices.containsKey(directoryId)) {
            final UUID directoryUUID;
            try {
                directoryUUID = UUID.fromString(directoryId);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Directory ID", e);
            }
            service = new BasicDirectoryService(directoryUUID, transport);
            directoryServices.put(directoryId, service);

        } else {
            service = directoryServices.get(directoryId);
        }
        return service;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public ServiceService getServiceService(String serviceId) {
        ServiceService service;
        if (serviceId == null) {
            throw new IllegalArgumentException("Service ID cannot be null!");
        } else if (!serviceServices.containsKey(serviceId)) {
            final UUID serviceUUID;
            try {
                serviceUUID = UUID.fromString(serviceId);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Service ID", e);
            }
            service = new BasicServiceService(serviceUUID, transport);
            serviceServices.put(serviceId, service);

        } else {
            service = serviceServices.get(serviceId);
        }
        return service;
    }
}
