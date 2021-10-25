package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.domain.KeyType;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.DirectoryEntity;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import com.iovation.launchkey.sdk.integration.entities.ServiceEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceManager;
import com.iovation.launchkey.sdk.integration.managers.KeysManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.guice.ScenarioScoped;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

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
@ScenarioScoped
public class DirectoryServicePublicKeySteps {
    private final GenericSteps genericSteps;
    private final DirectoryServiceManager directoryServiceManager;
    private final KeysManager keysManager;
    private final DirectoryManager directoryManager;

    @Inject
    public DirectoryServicePublicKeySteps(DirectoryServiceManager directoryServiceManager,
                                          DirectoryManager directoryManager, KeysManager keysManager,
                                          GenericSteps genericSteps) {
        this.genericSteps = genericSteps;
        this.directoryManager = directoryManager;
        this.keysManager = keysManager;
        this.directoryServiceManager = directoryServiceManager;

    }

    @And("^I add(?:ed)? (a|another) Public Key to the Directory Service$")
    public void iAddedAnSDKKeyToTheDirectoryService(String keyIdentifier) throws Throwable {
        RSAPublicKey key =
                keyIdentifier.equals("another") ? keysManager.getBetaPublicKey() : keysManager.getAlphaPublicKey();
        directoryServiceManager.addPublicKeyToCurrentService(getCurrentDirectoryId(), key, null, null);
    }

    @When("^I add a Public Key with a? (.+) type to the Directory Service$")
    public void iAddAPublicKeyWithKeyTypeToTheDirectoryService(String rawKeyType) throws Throwable {
        KeyType keyType = Utils.stringToKeyType(rawKeyType);
        RSAPublicKey key = keysManager.getAlphaPublicKey();
        directoryServiceManager.addPublicKeyToCurrentService(getCurrentDirectoryId(), key, null, null, keyType);
    }

    @And("^I retrieve the current Directory Service's Public Keys$")
    public void iRetrieveTheCurrentDirectoryServicesPublicKeys() throws Throwable {
        directoryServiceManager.retrievePublicKeysList(getCurrentDirectoryId(), getCurrentServiceEntity().getId());
    }

    @Then("^the Public Key is in the list of Public Keys for the Directory Service$")
    public void thePublicKeyIsInTheListOfPublicKeysForTheDirectoryService() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheDirectoryService(keysManager.getAlphaPublicKeyMD5Fingerprint());
    }

    @Then("^the other Public Key is in the list of Public Keys for the Directory Service")
    public void theOtherPublicKeyIsInTheListOfPublicKeysForTheDirectoryService() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheDirectoryService(keysManager.getBetaPublicKeyMD5Fingerprint());
    }

    @Then("^the Public Key is in the list of Public Keys for the Directory Service and has a? \"(.+)\" key type$")
    public void thePublicKeyIsInTheListOfPublicKeysForTheDirectoryServiceAndHasKeyType(String rawKeyType) throws Throwable {
        String keyId = keysManager.getAlphaPublicKeyMD5Fingerprint();
        aPublicKeyIsInTheListOfPublicKeysForTheDirectoryService(keyId);

        KeyType keyType = null;
        Set<PublicKeyEntity> keys = directoryServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            if (key.getKeyId().equals(keyId)) {
                keyType = key.getKeyType();
                break;
            }
        }

        if (keyType == null) throw new Exception(
                "Key with ID " + keyId + " was expected but not found in keys:\n" + keys);

        assertThat("Expected a key type of \"" + rawKeyType + "\" but received \"" + Utils.keyTypeToString(keyType) + "\"",
                Utils.stringToKeyType(rawKeyType), is(keyType));
    }

    private void aPublicKeyIsInTheListOfPublicKeysForTheDirectoryService(String keyId) throws Throwable {
        boolean found = false;
        Set<PublicKeyEntity> keys = directoryServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            found = key.getKeyId().equals(keyId);
            if (found) break;
        }
        assertThat("Key with ID " + keyId + " was expected but not found in keys:\n" + keys, found, is(true));
    }

    @When("^I attempt to add a Public Key to the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToAddAnPublicKeyToTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServiceManager
                    .addPublicKeyToService(getCurrentDirectoryId(), serviceId, keysManager.getAlphaPublicKey(), null,
                            null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I attempt to add the same Public Key to the Directory Service$")
    public void iAttemptToAddTheSamePublicKeyToTheDirectory() throws Throwable {
        try {
            directoryServiceManager.addPublicKeyToService(getCurrentDirectoryId(),
                    getCurrentServiceEntity().getId(), keysManager.getAlphaPublicKey(), null,
                    null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I attempt to add a Public Key with a? \"(.+)\" type to the Directory Service$")
    public void iAttemptToAddAPublicKeyWithCustomKeyTypeToTheDirectory(String keyType) throws Throwable {
        try {
            directoryServiceManager.addPublicKeyToCurrentService(getCurrentDirectoryId(),
                    keysManager.getAlphaPublicKey(), null, null, Utils.stringToKeyType(keyType));
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the Directory Service Public Keys list is empty$")
    public void theDirectoryPublicKeysListIsEmpty() throws Throwable {
        assertThat(directoryServiceManager.getCurrentServicePublicKeys(), is(empty()));
    }

    @When("^I attempt to update a Public Key for the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateAPublicKeyForTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        UUID directoryId = getCurrentDirectoryId();
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        try {
            directoryServiceManager.updatePublicKey(directoryId, serviceId, keyId, null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I updated? the Directory Service Public Key to inactive$")
    public void iUpdatedTheDirectoryPublicKeyToInactive() throws Throwable {
        PublicKeyEntity currentPublicKey = getCurrentPublicKeyEntity();
        directoryServiceManager
                .updatePublicKey(getCurrentDirectoryId(), getCurrentServiceEntity().getId(),
                        currentPublicKey.getKeyId(), false, currentPublicKey.getExpires());
    }

    @And("^I updated? the Directory Service Public Key expiration date to \"([^\"]*)\"$")
    public void iUpdatedTheDirectoryServicePublicKeyExpirationDateTo(String dateString) throws Throwable {
        Date expires = Utils.parseDateString(dateString);
        PublicKeyEntity currentPublicKey = getCurrentPublicKeyEntity();
        directoryServiceManager.updatePublicKey(getCurrentDirectoryId(), getCurrentServiceEntity().getId(),
                currentPublicKey.getKeyId(), currentPublicKey.getActive(), expires);
    }

    @And("^the Directory Service Public Key is inactive$")
    public void theDirectoryServicePublicKeyIsInactive() throws Throwable {
        assertThat(getCurrentPublicKeyListEntity().getActive(), is(false));
    }

    @And("^the Directory Service Public Key Expiration Date is \"([^\"]*)\"$")
    public void theDirectoryServiceExpirationDateIs(String expiresText) throws Throwable {
        assertThat(getCurrentPublicKeyListEntity().getExpires(), is(equalTo(Utils.parseDateString(expiresText))));
    }

    @When("^I attempt to retrieve the Public Keys for the Directory Service with the Service ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePublicKeysForTheDirectoryServiceWithTheServiceID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServiceManager.retrievePublicKeysList(getCurrentDirectoryId(), serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to retrieve the Public Keys for the Service with the Directory ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePublicKeysForTheDirectoryServiceWithTheDirectoryID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryServiceManager.retrievePublicKeysList(directoryId, getCurrentServiceEntity().getId());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Given("^I added a Public Key to the Directory Service which is (active|inactive) and expires on \"([^\"]*)\"$")
    public void iAddedAPublicKeyToTheDirectoryServiceWhichIsInactiveAndExpiresOn(String activeText, String expiresText)
            throws Throwable {
        boolean active = activeText.equals("active");
        directoryServiceManager.addPublicKeyToCurrentService(
                getCurrentDirectoryId(),
                keysManager.getAlphaPublicKey(),
                active,
                Utils.parseDateString(expiresText));
    }

    private UUID getCurrentDirectoryId() {
        DirectoryEntity directory = directoryManager.getCurrentDirectoryEntity();
        assertThat("Creating a Directory is required.", directory, not(nullValue()));
        return directory.getId();
    }

    private ServiceEntity getCurrentServiceEntity() {
        ServiceEntity service = directoryServiceManager.getCurrentServiceEntity();
        assertThat("Creating a Service is required.", service, not(nullValue()));
        return service;
    }

    @SuppressWarnings("Duplicates")
    private PublicKeyEntity getCurrentPublicKeyEntity() {
        return getCurrentServiceEntity().getPublicKeys()
                .get(getCurrentServiceEntity().getPublicKeys().size() - 1);
    }

    private PublicKeyEntity getCurrentPublicKeyListEntity() {
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = directoryServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            if (key.getKeyId().equals(keyId)) return key;
        }
        throw new RuntimeException("No key " + keyId + " found in list: " + keys);
    }

    @When("^I attempt to update a Public Key identified by \"([^\"]*)\" for the Directory Service$")
    public void iAttemptToUpdateAPublicKeyIdentifiedByForTheDirectoryService(String keyId) throws Throwable {
        try {
            directoryServiceManager
                    .updatePublicKey(getCurrentDirectoryId(), getCurrentServiceEntity().getId(), keyId, null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the current Directory Service Public Key$")
    public void iRemoveTheCurrentDirectoryServicePublicKey() throws Throwable {
        directoryServiceManager.removePublicKey(getCurrentDirectoryId(), getCurrentServiceEntity().getId(),
                getCurrentPublicKeyEntity().getKeyId());
    }

    @When("^I attempt to remove the current Directory Service Public Key$")
    public void iAttemptToRemoveTheCurrentDirectoryServicePublicKey() throws Throwable {
        UUID directoryId = getCurrentDirectoryId();
        UUID serviceId = getCurrentServiceEntity().getId();
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        try {
            directoryServiceManager.removePublicKey(directoryId, serviceId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the last current Directory Service's Public Key is not in the list$")
    public void theLastCurrentDirectoryServiceSPublicKeyIsNotInTheList() throws Throwable {
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = directoryServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : directoryServiceManager.getCurrentServicePublicKeys()) {
            if (key.getKeyId().equals(keyId))
                throw new Exception("Current Public Key " + keyId + " was in the list but was not expected: " + keys);
        }
    }

    @When("^I attempt to remove a Public Key from the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveAPublicKeyFromTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        UUID directoryId = getCurrentDirectoryId();
        try {
            directoryServiceManager.removePublicKey(directoryId, serviceId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to remove a Public Key identified by \"([^\"]*)\" from the Directory Service$")
    public void iAttemptToRemoveAPublicKeyIdentifiedByFromTheDirectoryService(String keyId) throws Throwable {
        UUID serviceId = getCurrentServiceEntity().getId();
        UUID directoryId = getCurrentDirectoryId();
        try {
            directoryServiceManager.removePublicKey(directoryId, serviceId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the Directory Service Public Key is in the list of Public Keys for the Directory Service$")
    public void theDirectoryServicePublicKeyIsInTheListOfPublicKeysForTheDirectoryService() throws Exception {
        boolean found = false;
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = directoryServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            if (key.getKeyId().equals(keyId)) {
                found = true;
                break;
            }
        }
        assertThat("Key with ID " + keyId + " eas expected be found in keys list but was not: " + keys, found,
                is(equalTo(true)));
    }
}