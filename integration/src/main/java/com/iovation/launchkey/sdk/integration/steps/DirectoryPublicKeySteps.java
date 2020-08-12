package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.PublicKeyEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
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
public class DirectoryPublicKeySteps {
    private final GenericSteps genericSteps;
    private final DirectoryManager directoryManager;
    private final KeysManager keysManager;

    @Inject
    public DirectoryPublicKeySteps(DirectoryManager directoryManager, KeysManager keysManager,
                                   GenericSteps genericSteps) {
        this.genericSteps = genericSteps;
        this.keysManager = keysManager;
        this.directoryManager = directoryManager;

    }

    @And("^I add(?:ed)? (a|another) Public Key to the Directory$")
    public void iAddedAnSDKKeyToTheDirectory(String keyIdentifier) throws Throwable {
        RSAPublicKey key =
                keyIdentifier.equals("another") ? keysManager.getBetaPublicKey() : keysManager.getAlphaPublicKey();
        directoryManager.addPublicKeyToCurrentDirectory(key);
    }

    @And("^I retrieve the current Directory's Public Keys$")
    public void iRetrieveTheCurrentDirectorySPublicKeys() throws Throwable {
        directoryManager.retrieveCurrentDirectoryPublicKeysList();
    }

    @Then("^the Public Key is in the list of Public Keys for the Directory$")
    public void thePublicKeyIsInTheListOfPublicKeysForTheDirectory() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheDirectory(keysManager.getAlphaPublicKeyMD5Fingerprint());
    }

    @Then("^the other Public Key is in the list of Public Keys for the Directory$")
    public void theOtherPublicKeyIsInTheListOfPublicKeysForTheDirectory() throws Throwable {
        aPublicKeyIsInTheListOfPublicKeysForTheDirectory(keysManager.getBetaPublicKeyMD5Fingerprint());
    }

    private void aPublicKeyIsInTheListOfPublicKeysForTheDirectory(String keyId) throws Throwable {
        boolean found = false;
        Set<PublicKeyEntity> keys = directoryManager.getCurrentPublicKeys();
        for (PublicKeyEntity key : keys) {
            found = key.getKeyId().equals(keyId);
            if (found) break;
        }
        assertThat("Key with ID " + keyId + " was expected but not found in keys:\n" + keys, found, is(true));
    }

    @When("^I attempt to add a Public Key to the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToAddAnPublicKeyToTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.addPublicKeyToDirectory(directoryId, keysManager.getAlphaPublicKey());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to update a Public Key for the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateAPublicKeyForTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        PublicKeyEntity key = getCurrentPublicKeyEntity();
        try {
            directoryManager.updatePublicKey(directoryId, key.getKeyId(), key.getActive(), key.getExpires());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I attempt to add the same Public Key to the Directory$")
    public void iAttemptToAddTheSamePublicKeyToTheDirectory() throws Throwable {
        try {
            directoryManager.addPublicKeyToCurrentDirectory(keysManager.getAlphaPublicKey());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the Directory Public Keys list is empty$")
    public void theDirectoryPublicKeysListIsEmpty() throws Throwable {
        assertThat(directoryManager.getCurrentPublicKeys(), is(empty()));
    }

    @And("^I updated? the Directory Public Key to inactive$")
    public void iUpdatedTheDirectoryPublicKeyToInactive() throws Throwable {
        PublicKeyEntity currentPublicKey = getCurrentPublicKeyEntity();
        directoryManager.updatePublicKey(currentPublicKey.getKeyId(), false,
                currentPublicKey.getExpires());
    }

    @And("^I updated? the Directory Public Key expiration date to \"([^\"]*)\"$")
    public void iUpdatedTheDirectoryPublicKeyExpirationDateTo(String dateString) throws Throwable {
        Date expires = Utils.parseDateString(dateString);
        PublicKeyEntity currentPublicKey = getCurrentPublicKeyEntity();
        directoryManager.updatePublicKey(currentPublicKey.getKeyId(), currentPublicKey.getActive(), expires);
    }

    @And("^the Directory Public Key is inactive$")
    public void theDirectoryPublicKeyIsInactive() throws Throwable {
        assertThat("Unexpected value for active", getCurrentPublicKeyListEntity().getActive(), is(false));
    }

    private PublicKeyEntity getCurrentPublicKeyEntity() {
        return directoryManager.getCurrentDirectoryEntity().getPublicKeys()
                .get(directoryManager.getCurrentDirectoryEntity().getPublicKeys().size() - 1);
    }

    private PublicKeyEntity getCurrentPublicKeyListEntity() {
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = directoryManager.getCurrentPublicKeys();
        for (PublicKeyEntity key : keys) {
            if (key.getKeyId().equals(keyId)) return key;
        }
        throw new RuntimeException("No key " + keyId + " found in list: " + keys);
    }

    @And("^the Directory Public Key Expiration Date is \"([^\"]*)\"$")
    public void theDirectoryExpirationDateIs(String expiresText) throws Throwable {
        assertThat(getCurrentPublicKeyListEntity().getExpires(), is(equalTo(Utils.parseDateString(expiresText))));
    }

    @When("^I attempt to retrieve the Public Keys for the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePublicKeysForTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.retrieveSDKKeyList(directoryId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Given("^I added a Public Key to the Directory which is (active|inactive) and expires on \"([^\"]*)\"$")
    public void iAddedAPublicKeyToTheDirectoryWhichIsInactiveAndExpiresOn(String activeText, String expiresText)
            throws Throwable {
        boolean active = activeText.equals("active");
        directoryManager.addPublicKeyToCurrentDirectory(keysManager.getAlphaPublicKey(), active,
                Utils.parseDateString(expiresText));
    }

    @When("^I attempt to update a Public Key identified by \"([^\"]*)\" for the Directory$")
    public void iAttemptToUpdateAPublicKeyIdentifiedByForTheDirectory(String keyId) throws Throwable {
        UUID directoryId = directoryManager.getCurrentDirectoryEntity().getId();
        try {
            directoryManager.updatePublicKey(directoryId, keyId, null, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the current Directory Public Key$")
    public void iRemoveTheCurrentDirectoryPublicKey() throws Throwable {
        directoryManager.removePublicKey(directoryManager.getCurrentDirectoryEntity().getId(), getCurrentPublicKeyEntity().getKeyId());
    }

    @When("^I attempt to remove the current Directory Public Key$")
    public void iAttemptToRemoveTheCurrentDirectoryPublicKey() throws Throwable {
        UUID directoryId = directoryManager.getCurrentDirectoryEntity().getId();
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        try {
            directoryManager.removePublicKey(directoryId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the last current Directory's Public Key is not in the list$")
    public void theLastCurrentDirectorySPublicKeyIsNotInTheList() throws Throwable {
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        Set<PublicKeyEntity> keys = directoryManager.getCurrentPublicKeys();
        for (PublicKeyEntity key : directoryManager.getCurrentPublicKeys()) {
            if (key.getKeyId().equals(keyId)) throw new Exception("Current Public Key " + keyId + " was in the list but was not expected: " + keys);
        }
    }

    @When("^I attempt to remove a Public Key from the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveAPublicKeyFromTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        String keyId = getCurrentPublicKeyEntity().getKeyId();
        try {
            directoryManager.removePublicKey(directoryId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to remove a Public Key identified by \"([^\"]*)\" from the Directory$")
    public void iAttemptToRemoveAPublicKeyIdentifiedByFromTheDirectory(String keyId) throws Throwable {
        UUID directoryId = directoryManager.getCurrentDirectoryEntity().getId();
        try {
            directoryManager.removePublicKey(directoryId, keyId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}