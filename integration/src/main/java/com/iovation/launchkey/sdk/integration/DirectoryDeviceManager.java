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
import com.iovation.launchkey.sdk.client.OrganizationClient;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.iovation.launchkey.sdk.error.EntityNotFound;
import com.iovation.launchkey.sdk.integration.entities.DeviceEntity;
import com.iovation.launchkey.sdk.integration.entities.LinkingResponseEntity;
import cucumber.api.java.After;

import java.util.*;

@Singleton
public class DirectoryDeviceManager {
    private final OrganizationClient organizationClient;
    private final List<DeviceEntity> currentDevicesList = new ArrayList<>();
    private final DirectoryManager directoryManager;
    private LinkingResponseEntity currentLinkingResponse = null;
    private String currentUserIdentifier;
    private Map<UUID, Set<String>> directoryUserIdentifiers = new HashMap<>();

    @Inject
    public DirectoryDeviceManager(OrganizationFactory factory, DirectoryManager directoryManager) throws Throwable {
        this.directoryManager = directoryManager;
        this.organizationClient = factory.makeOrganizationClient();
    }

    @After(order = 20000)
    public void tearDown() throws Throwable {
        for (Map.Entry<UUID, Set<String>> entry : directoryUserIdentifiers.entrySet()) {
            UUID directoryId = entry.getKey();
            organizationClient.updateDirectory(directoryId, true, null, null, null);
            DirectoryClient directoryClient = directoryManager.getDirectoryClient(directoryId);
            try {
                for (String userIdentifier : entry.getValue()) {
                    for (Device device : directoryClient.getLinkedDevices(userIdentifier)) {
                        try {
                            directoryClient.unlinkDevice(userIdentifier, device.getId());
                        } catch (EntityNotFound e) {
                            // Ignore because it's not there
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Unable to unlink all User Devices for Directory " + directoryId);
            }
            organizationClient.updateDirectory(directoryId, false, null, null, null);
        }
    }

    void createLinkingRequest(String userIdentifier, Integer ttl) throws Throwable {
        UUID directoryId = directoryManager.getCurrentDirectoryEntity().getId();
        DirectoryUserDeviceLinkData response;
        if (ttl == null) {
            response = directoryManager.getDirectoryClient(directoryId).linkDevice(userIdentifier);
        } else {
            response = directoryManager.getDirectoryClient(directoryId).linkDevice(userIdentifier, ttl);
        }
        currentUserIdentifier = userIdentifier;
        if (!directoryUserIdentifiers.containsKey(directoryId)) {
            directoryUserIdentifiers.put(directoryId, new HashSet<String>());
        }
        directoryUserIdentifiers.get(directoryId).add(userIdentifier);
        currentLinkingResponse = new LinkingResponseEntity(response.getCode(), response.getQrCodeUrl());
    }

    void retrieveUserDevices() throws Throwable {
        retrieveUserDevices(currentUserIdentifier);
    }

    void retrieveUserDevices(String userIdentifier) throws Throwable {
        List<DeviceEntity> devices = new ArrayList<>();
        for (Device device : directoryManager.getDirectoryClient().getLinkedDevices(userIdentifier)) {
            devices.add(new DeviceEntity(device.getId(), device.getName(), device.getStatus().getStatusCode(),
                    device.getType(), device.getCreated(), device.getUpdated()));
        }
        currentDevicesList.clear();
        currentDevicesList.addAll(devices);
    }

    List<DeviceEntity> getCurrentDevicesList() {
        return currentDevicesList;
    }

    LinkingResponseEntity getCurrentLinkingResponse() {
        return currentLinkingResponse;
    }

    String getCurrentUserIdentifier() {
        return currentUserIdentifier;
    }

    void unlinkDevice(String userIdentifier, UUID deviceId) throws Throwable {
        directoryManager.getDirectoryClient().unlinkDevice(userIdentifier, deviceId.toString());
    }
}
