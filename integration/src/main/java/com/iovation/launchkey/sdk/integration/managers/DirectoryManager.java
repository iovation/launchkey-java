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
import com.iovation.launchkey.sdk.client.OrganizationClient;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.domain.KeyType;
import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.error.Forbidden;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.DirectoryEntity;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import io.cucumber.java.After;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Singleton
public class DirectoryManager {

    private final Set<DirectoryEntity> directories = new HashSet<>();
    private final OrganizationFactory factory;
    private List<DirectoryEntity> currentDirectoryEntityList = new ArrayList<>();
    private List<UUID> currentSdkKeys = new ArrayList<>();
    private DirectoryEntity currentDirectoryEntity;
    private DirectoryEntity previousDirectoryEntity;
    private final OrganizationClient client;
    private Set<PublicKeyEntity> currentPublicKeys = new HashSet<>();

    @Inject
    public DirectoryManager(OrganizationFactory factory) {
        cleanupState();
        this.factory = factory;
        client = factory.makeOrganizationClient();
    }

    @After(order = 10000)
    public void cleanupState() {
        currentDirectoryEntity = null;
        previousDirectoryEntity = null;
        currentDirectoryEntityList.clear();
        currentSdkKeys.clear();
        currentPublicKeys.clear();
        directories.clear();
    }

    public DirectoryClient getDirectoryClient(UUID directoryId) {
        return this.factory.makeDirectoryClient(directoryId.toString());
    }

    public DirectoryClient getDirectoryClient() {
        return this.factory.makeDirectoryClient(getCurrentDirectoryEntity().getId().toString());
    }

    public UUID createDirectory(String name) throws Throwable {
        UUID id = client.createDirectory(name);
        previousDirectoryEntity = currentDirectoryEntity;
        currentDirectoryEntity = new DirectoryEntity(id, name, null, null, null, null, null, null, true, null);
        directories.add(currentDirectoryEntity);
        return id;
    }

    public UUID createDirectory() throws Throwable {
        String name = Utils.createRandomDirectoryName();
        return createDirectory(name);
    }

    public DirectoryEntity getCurrentDirectoryEntity() {
        return currentDirectoryEntity;
    }

    public DirectoryEntity getPreviousDirectoryEntity() {
        return previousDirectoryEntity;
    }

    public void retrieveCurrentDirectory() throws Throwable {
        DirectoryEntity directory = retrieveDirectory(currentDirectoryEntity.getId());
        previousDirectoryEntity = currentDirectoryEntity;
        currentDirectoryEntity = directory;
    }

    public void updateCurrentDirectory(Boolean active, String androidKey, String p12, String p12Fingerprint,
                                Boolean denialContextInquiryEnabled, URI webhookUrl) throws Throwable {
        updateDirectory(currentDirectoryEntity.getId(), active, androidKey, p12, p12Fingerprint,
                denialContextInquiryEnabled, webhookUrl);
    }

    public void generateAndAddDirectorySdkKeyToCurrentDirectory() throws Throwable {
        generateAndAddDirectorySdkKeyToDirectory(currentDirectoryEntity.getId());
    }

    public DirectoryEntity retrieveDirectory(UUID directoryId) throws Throwable {
        Directory directory = client.getDirectory(directoryId);
        return DirectoryEntity.fromDirectory(directory);
    }

    public void retrieveCurrentDirectoryAsList() throws Throwable {
        retrieveDirectoryList(Collections.singletonList(currentDirectoryEntity.getId()));
    }

    public List<DirectoryEntity> getCurrentDirectoryEntityList() {
        return currentDirectoryEntityList;
    }

    public void retrieveDirectoryList(List<UUID> directoryIds) throws Throwable {
        setDirectoriesAsCurrentDirectoryEntityList(client.getDirectories(directoryIds));
    }

    public void retrieveAllDirectories() throws Throwable {
        setDirectoriesAsCurrentDirectoryEntityList(client.getAllDirectories());
    }

    private void setDirectoriesAsCurrentDirectoryEntityList(List<Directory> directories) {
        currentDirectoryEntityList.clear();
        for (Directory directory : directories) {
            currentDirectoryEntityList.add(DirectoryEntity.fromDirectory(directory));
        }
    }

    public void generateAndAddDirectorySdkKeyToDirectory(UUID directoryId) throws Throwable {
        UUID sdkKey = client.generateAndAddDirectorySdkKey(directoryId);
        currentDirectoryEntity.getSdkKeys().add(sdkKey);
    }

    public void updateDirectory(UUID directoryId, Boolean active, String androidKey, String p12,
                         String p12Fingerprint, Boolean denialContextInquiryEnabled, URI webhookUrl) throws Throwable {
        client.updateDirectory(directoryId, active, androidKey, p12, denialContextInquiryEnabled, webhookUrl);
        currentDirectoryEntity = new DirectoryEntity(currentDirectoryEntity.getId(), currentDirectoryEntity.getName(),
                active, currentDirectoryEntity.getServiceIds(), currentDirectoryEntity.getSdkKeys(), androidKey,
                p12Fingerprint, p12, denialContextInquiryEnabled, webhookUrl);
    }

    public void updateDirectory(UUID directoryId, boolean active) throws Throwable {
        client.updateDirectory(directoryId, active, null, null, null);
    }

    public void retrieveSDKKeyList(UUID directoryId) throws Throwable {
        List<UUID> sdkKeys = client.getAllDirectorySdkKeys(directoryId);
        currentSdkKeys.clear();
        currentSdkKeys.addAll(sdkKeys);
    }

    public void retrieveCurrentDirectorySdkKeys() throws Throwable {
        retrieveSDKKeyList(currentDirectoryEntity.getId());
    }

    public List<UUID> getCurrentSdkKeys() {
        return currentSdkKeys;
    }

    public void removeSdkKeyFromCurrentDirectory(UUID sdkKey) throws Throwable {
        removeSdkKeyFromDirectory(currentDirectoryEntity.getId(), sdkKey);
    }

    public void removeSdkKeyFromDirectory(UUID directoryId, UUID sdkKey) throws Throwable {
        client.removeDirectorySdkKey(directoryId, sdkKey);
    }

    public void addPublicKeyToCurrentDirectory(RSAPublicKey publicKey) throws Throwable {
        addPublicKeyToDirectory(currentDirectoryEntity.getId(), publicKey);
    }

    public void addPublicKeyToCurrentDirectory(RSAPublicKey publicKey, KeyType keyType) throws Throwable {
        addPublicKeyToDirectory(currentDirectoryEntity.getId(), publicKey, keyType);
    }

    public void addPublicKeyToDirectory(UUID directoryId, RSAPublicKey publicKey) throws Throwable {
        addPublicKeyToDirectory(directoryId, publicKey, null, null);
    }

    public void addPublicKeyToDirectory(UUID directoryId, RSAPublicKey publicKey, KeyType keyType) throws Throwable {
        addPublicKeyToDirectory(directoryId, publicKey, null, null, keyType);
    }

    public void addPublicKeyToDirectory(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws Throwable {
        String keyId = client.addDirectoryPublicKey(directoryId, publicKey, active, expires);
        currentDirectoryEntity.getPublicKeys().add(new PublicKeyEntity(keyId, publicKey, active, null, expires));
    }

    public void addPublicKeyToDirectory(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires,
                                        KeyType keyType)
            throws Throwable {
        String keyId = client.addDirectoryPublicKey(directoryId, publicKey, active, expires, keyType);
        currentDirectoryEntity.getPublicKeys().add(
                new PublicKeyEntity(keyId, publicKey, active, null, expires, keyType));
    }

    public void addPublicKeyToCurrentDirectory(RSAPublicKey publicKey, boolean active, Date expires) throws Throwable {
        addPublicKeyToDirectory(currentDirectoryEntity.getId(), publicKey, active, expires);
    }

    public void retrieveCurrentDirectoryPublicKeysList() throws Throwable {
        List<PublicKeyEntity> pubicKeys = new ArrayList<>();
        for (PublicKey key : client.getDirectoryPublicKeys(currentDirectoryEntity.getId())) {
            pubicKeys.add(PublicKeyEntity.fromPublicKey(key));
        }
        currentPublicKeys.clear();
        currentPublicKeys.addAll(pubicKeys);
    }

    public Set<PublicKeyEntity> getCurrentPublicKeys() {
        return currentPublicKeys;
    }

    public void updatePublicKey(String keyId, Boolean active, Date expires) throws Throwable {
        updatePublicKey(currentDirectoryEntity.getId(), keyId, active, expires);
    }

    public void updatePublicKey(UUID directoryId, String keyId, Boolean active, Date expires) throws Throwable {
        client.updateDirectoryPublicKey(directoryId, keyId, active, expires);
        if (directoryId == currentDirectoryEntity.getId()) {
            PublicKeyEntity currentPublicKeyEntity = null;
            for (PublicKeyEntity publicKeyEntity : currentDirectoryEntity.getPublicKeys()) {
                if (publicKeyEntity.getKeyId().equals(keyId)) {
                    currentPublicKeyEntity = publicKeyEntity;
                }
            }
            Date created;
            RSAPublicKey publicKey;
            if (currentPublicKeyEntity == null) {
                publicKey = null;
                created = null;
            } else {
                publicKey = currentPublicKeyEntity.getPublicKey();
                created = currentPublicKeyEntity.getCreated();
            }
            currentPublicKeyEntity = new PublicKeyEntity(keyId, publicKey, active, created, expires);
            currentPublicKeys.add(currentPublicKeyEntity);
        }
    }

    public void removePublicKey(UUID directoryId, String keyId) throws Throwable {
        client.removeDirectoryPublicKey(directoryId, keyId);
    }
}
