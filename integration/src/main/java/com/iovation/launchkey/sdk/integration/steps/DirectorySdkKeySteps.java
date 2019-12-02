package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
public class DirectorySdkKeySteps {
    private final GenericSteps genericSteps;
    private final DirectoryManager directoryManager;

    @Inject
    public DirectorySdkKeySteps(DirectoryManager directoryManager, GenericSteps genericSteps) {
        this.genericSteps = genericSteps;
        this.directoryManager = directoryManager;
    }

    @And("^I generat(?:ed)? and add(?:ed)? (\\d+) SDK Keys? to the Directory$")
    public void iAddedAnSDKKeyToTheDirectory(int numberOfKeys) throws Throwable {
        for (int i=0; i < numberOfKeys; i++) {
            directoryManager.generateAndAddDirectorySdkKeyToCurrentDirectory();
        }
    }

    @Given("^I have added an SDK Key to the Directory$")
    @When("^I generate and add an SDK Key to the Directory$")
    public void iGenerateAndAddAnSDKKeyToTheDirectory() throws Throwable {
        directoryManager.generateAndAddDirectorySdkKeyToCurrentDirectory();
    }

    @Then("^the SDK Key is in the list for the Directory$")
    public void theSDKKeyIsInTheListForTheDirectory() throws Throwable {
        List<UUID> sdkKeys = directoryManager.getPreviousDirectoryEntity().getSdkKeys();
        assertThat(directoryManager.getCurrentDirectoryEntity().getSdkKeys(), hasItem(sdkKeys.get(sdkKeys.size() - 1)));
    }

    @When("^I attempt to generate and add an SDK Key to the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToGenerateAndAddAnSDKKeyToTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.generateAndAddDirectorySdkKeyToDirectory(directoryId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @And("^I retrieve the current Directory's SDK Keys$")
    public void iRetrieveTheCurrentDirectorySDKKeys() throws Throwable {
        directoryManager.retrieveCurrentDirectorySdkKeys();
    }

    @Then("^all of the SDK Keys for the Directory are in the SDK Keys list$")
    public void allOfTheSDKKeysForTheDirectoryAreInTheSDKKeysList() throws Throwable {
        assertThat(directoryManager.getCurrentSdkKeys(),
                is(equalTo(directoryManager.getCurrentDirectoryEntity().getSdkKeys())));
    }

    @When("^I attempt to retrieve the current Directory SDK Keys for the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveTheCurrentDirectorySDKKeysForTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.retrieveSDKKeyList(directoryId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the last generated SDK Key from the Directory$")
    public void iRemoveTheLastGeneratedSDKKeyFromTheDirectory() throws Throwable {
        UUID lastSdkKey = getLastAddedSdkKey();
        directoryManager.removeSdkKeyFromCurrentDirectory(lastSdkKey);
    }

    @Then("^the last generated SDK Key is not in the list for the Directory$")
    public void theLastGeneratedSDKKeyIsNotInTheListForTheDirectory() throws Throwable {
        assertThat(directoryManager.getCurrentSdkKeys(), not(hasItem(getLastAddedSdkKey())));
    }

    @When("^I attempt to remove the last generated SDK Key from the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveTheLastGeneratedSDKKeyFromTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        UUID lastSdkKey = UUID.randomUUID();
        try {
            directoryManager.removeSdkKeyFromDirectory(directoryId, lastSdkKey);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to remove the last generated SDK Key from the Directory$")
    public void iAttemptToRemoveTheLastGeneratedSDKKeyFromTheDirectory() throws Throwable {
        UUID lastSdkKey = getLastAddedSdkKey();
        try {
            directoryManager.removeSdkKeyFromCurrentDirectory(lastSdkKey);
        } catch (java.lang.Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    private UUID getLastAddedSdkKey() {
        return directoryManager.getCurrentDirectoryEntity().getSdkKeys()
                .get(directoryManager.getCurrentDirectoryEntity().getSdkKeys().size() - 1);
    }

    @When("^I attempt to remove the last generated SDK Key \"([^\"]*)\" from the Directory$")
    public void iAttemptToRemoveTheLastGeneratedSDKKeyFromTheDirectory(String uuid) throws Throwable {
        UUID sdkKey = UUID.fromString(uuid);
        try {
            directoryManager.removeSdkKeyFromCurrentDirectory(sdkKey);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}