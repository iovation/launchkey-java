package com.iovation.launchkey.sdk.integration;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

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
public class OrganizationServicePublicKeySteps {
    private final GenericSteps genericSteps;
    private final OrganizationServiceManager organizationServiceManager;
    private final KeysManager keysManager;

    @Inject
    public OrganizationServicePublicKeySteps(OrganizationServiceManager organizationServiceManager,
                                             KeysManager keysManager, GenericSteps genericSteps) {
        this.genericSteps = genericSteps;
        this.keysManager = keysManager;
        this.organizationServiceManager = organizationServiceManager;

    }

    @And("^I add(?:ed)? (a|another) Public Key to the Organization Service$")
    public void iAddedAnSDKKeyToTheOrganizationService(String keyIdentifier) throws Throwable {
        RSAPublicKey key =
                keyIdentifier.equals("another") ? keysManager.getBetaPublicKey() : keysManager.getAlphaPublicKey();
        organizationServiceManager.addPublicKeyToCurrentService(key, null, null);
    }

    @And("^I retrieve the current Organization Service's Public Keys$")
    public void iRetrieveTheCurrentOrganizationServiceSPublicKeys() throws Throwable {
        organizationServiceManager.retrieveCurrentServicePublicKeysList();
    }

    @Then("^the Public Key is in the list of Public Keys for the Organization Service")
    public void thePublicKeyIsInTheListOfPublicKeysForTheOrganizationService() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheOrganizationService(keysManager.getAlphaPublicKeyMD5Fingerprint());
    }

    @Then("^the other Public Key is in the list of Public Keys for the Organization Service")
    public void theOtherPublicKeyIsInTheListOfPublicKeysForTheOrganizationService() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheOrganizationService(keysManager.getBetaPublicKeyMD5Fingerprint());
    }

    private void aPublicKeyIsInTheListOfPublicKeysForTheOrganizationService(String keyId) throws Throwable {
        boolean found = false;
        Set<PublicKeyEntity> keys = organizationServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            found = key.getKeyId().equals(keyId);
            if (found) break;
        }
        assertThat("Key with ID " + keyId + " was expected but not found in keys:\n" + keys, found, is(true));
    }

    @When("^I attempt to add a Public Key to the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToAddAnPublicKeyToTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager
                    .addPublicKeyToCurrentService(serviceId, keysManager.getAlphaPublicKey(), null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I attempt to add the same Public Key to the Organization Service")
    public void iAttemptToAddTheSamePublicKeyToTheOrganizationService() throws Throwable {
        try {
            organizationServiceManager.addPublicKeyToCurrentService(keysManager.getAlphaPublicKey(), null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the Organization Service Public Keys list is empty$")
    public void theOrganizationServicePublicKeysListIsEmpty() throws Throwable {
        assertThat(organizationServiceManager.getCurrentServicePublicKeys(), is(empty()));
    }

    @And("^I updated? the Organization Service Public Key to inactive$")
    public void iUpdatedTheOrganizationServicePublicKeyToInactive() throws Throwable {
        PublicKeyEntity currentPublicKey = getCurrentServicePublicKeyEntity();
        organizationServiceManager.updatePublicKey(currentPublicKey.getKeyId(), false,
                currentPublicKey.getExpires());
    }

    @And("^I updated? the Organization Service Public Key expiration date to \"([^\"]*)\"$")
    public void iUpdatedTheOrganizationServicePublicKeyExpirationDateTo(String dateString) throws Throwable {
        Date expires = Utils.parseDateString(dateString);
        PublicKeyEntity currentPublicKey = getCurrentServicePublicKeyEntity();
        organizationServiceManager.updatePublicKey(currentPublicKey.getKeyId(), currentPublicKey.getActive(), expires);
    }

    @And("^the Organization Service Public Key is inactive$")
    public void theOrganizationServicePublicKeyIsInactive() throws Throwable {
        assertThat(getCurrentPublicKeyListEntity().getActive(), is(false));
    }

    private PublicKeyEntity getCurrentServicePublicKeyEntity() {
        return organizationServiceManager.getCurrentServiceEntity().getPublicKeys()
                .get(organizationServiceManager.getCurrentServiceEntity().getPublicKeys().size() - 1);
    }

    private PublicKeyEntity getCurrentPublicKeyListEntity() {
        String keyId = getCurrentServicePublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = organizationServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : keys) {
            if (key.getKeyId().equals(keyId)) return key;
        }
        throw new RuntimeException("No key " + keyId + " found in list: " + keys);
    }

    @And("^the Organization Service Public Key Expiration Date is \"([^\"]*)\"$")
    public void theDirectoryExpirationDateIs(String expiresText) throws Throwable {
        assertThat(getCurrentPublicKeyListEntity().getExpires(), is(equalTo(Utils.parseDateString(expiresText))));
    }

    @Given("^I added a Public Key to the Organization Service which is (active|inactive) and expires on \"([^\"]*)\"$")
    public void iAddedAPublicKeyToTheDirectoryWhichIsInactiveAndExpiresOn(String activeText, String expiresText)
            throws Throwable {
        boolean active = activeText.equals("active");
        organizationServiceManager.addPublicKeyToCurrentService(keysManager.getAlphaPublicKey(), active,
                Utils.parseDateString(expiresText));
    }

    @When("^I attempt to update a Public Key for the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateAPublicKeyToTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        PublicKeyEntity key = getCurrentServicePublicKeyEntity();
        try {
            organizationServiceManager.updatePublicKey(serviceId, key.getKeyId(), null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to update a Public Key identified by \"([^\"]*)\" for the Organization Service$")
    public void iAttemptToUpdateAPublicKeyIdentifiedByForTheOrganizationService(String keyId) throws Throwable {
        try {
            organizationServiceManager
                    .updatePublicKey(organizationServiceManager.getCurrentServiceEntity().getId(), keyId, null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the current Organization Service Public Key$")
    public void iRemoveTheCurrentDirectoryServicePublicKey() throws Throwable {
        organizationServiceManager.removePublicKey(organizationServiceManager.getCurrentServiceEntity().getId(),
                getCurrentServicePublicKeyEntity().getKeyId());
    }

    @When("^I attempt to remove the current Organization Service Public Key$")
    public void iAttemptToRemoveTheCurrentDirectoryServicePublicKey() throws Throwable {
        UUID serviceId = organizationServiceManager.getCurrentServiceEntity().getId();
        String keyId = getCurrentServicePublicKeyEntity().getKeyId();
        try {
            organizationServiceManager.removePublicKey(serviceId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the last current Organization Service's Public Key is not in the list$")
    public void theLastCurrentDirectoryServiceSPublicKeyIsNotInTheList() throws Throwable {
        String keyId = getCurrentServicePublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = organizationServiceManager.getCurrentServicePublicKeys();
        for (PublicKeyEntity key : organizationServiceManager.getCurrentServicePublicKeys()) {
            if (key.getKeyId().equals(keyId))
                throw new Exception("Current Public Key " + keyId + " was in the list but was not expected: " + keys);
        }
    }

    @When("^I attempt to remove a Public Key from the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveAPublicKeyFromTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager.removePublicKey(serviceId, getCurrentServicePublicKeyEntity().getKeyId());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to remove a Public Key identified by \"([^\"]*)\" from the Organization Service$")
    public void iAttemptToRemoveAPublicKeyIdentifiedByFromTheDirectoryService(String keyId) throws Throwable {
        try {
            organizationServiceManager
                    .removePublicKey(organizationServiceManager.getCurrentServiceEntity().getId(), keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to retrieve the Public Keys for the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePublicKeysForTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager.retrieveServicePublicKeysList(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}