package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.integration.entities.DirectoryEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import com.iovation.launchkey.sdk.integration.managers.KeysManager;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matcher;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

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
public class DirectorySteps {
    private final GenericSteps genericSteps;
    private final DirectoryManager directoryManager;
    private final KeysManager keysManager;

    @Inject
    public DirectorySteps(DirectoryManager directoryManager, KeysManager keysManager, GenericSteps genericSteps) {
        this.genericSteps = genericSteps;
        this.keysManager = keysManager;
        this.directoryManager = directoryManager;
    }

    @When("^I create a Directory with a unique name$")
    public void iCreateADirectoryWithAUniqueName() throws Throwable {
        directoryManager.createDirectory();
    }

    @Given("^I created a Directory$")
    public void iCreatedADirectory() throws Throwable {
        directoryManager.createDirectory();
    }

    @When("^I retrieve the (?:created|updated|current) Directory$")
    public void iRetrieveTheCreatedDirectory() throws Throwable {
        directoryManager.retrieveCurrentDirectory();
    }

    @When("^I attempt to create a Directory with the same name$")
    public void iAttemptToCreateADirectoryWithTheSameName() throws Throwable {
        try {
            if (directoryManager.getCurrentDirectoryEntity() == null)
                throw new Exception("Directory must be created before executing this step.");
            directoryManager.createDirectory(directoryManager.getCurrentDirectoryEntity().getName());
        } catch (Exception e) {
            this.genericSteps.setCurrentException(e);
        }
    }

    @Then("^the Directory name is the same as was sent$")
    public void theDirectoryNameIsTheSameAsWasSent() {
        theDirectoryNameIs(directoryManager.getPreviousDirectoryEntity().getName());
    }

    @Then("^the Directory name is \"([^\"]*)\"$")
    public void theDirectoryNameIs(String name) {
        Matcher<Object> matcher;
        if (name.equalsIgnoreCase("null")) {
            matcher = nullValue();
        } else {
            matcher = equalTo(name);
        }
        assertThat(directoryManager.getCurrentDirectoryEntity().getName(), is(matcher));
    }

    @Then("^the Android Key is \"([^\"]*)\"$")
    public void theAndroidKeyIs(String key) {
        Matcher<Object> matcher;
        if (key.equalsIgnoreCase("null")) {
            matcher = nullValue();
        } else {
            matcher = equalTo(key);
        }
        assertThat(directoryManager.getCurrentDirectoryEntity().getAndroidKey(), is(matcher));
    }

    @When("^I updated? the Directory as active$")
    public void iUpdateTheDirectoryAsActive() throws Throwable {
        directoryManager
                .updateCurrentDirectory(true, directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                        directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                        directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @When("^I updated? the Directory as inactive$")
    public void iUpdateTheDirectoryAsInactive() throws Throwable {
        directoryManager
                .updateCurrentDirectory(false, directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                        directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                        directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @Then("^the Directory is active$")
    public void theDirectoryIsActive() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getActive(), is(true));
    }

    @Then("^the Directory is not active$")
    public void theDirectoryIsNotActive() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getActive(), is(false));
    }

    @Then("^the Directory has no Service IDs$")
    public void theDirectoryHasNoServiceIDs() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getServiceIds(), is(empty()));
    }

    @Then("^the Directory has no SDK Keys$")
    public void theDirectoryHasNoSDKKeys() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getSdkKeys(), is(empty()));
    }

    @When("^I updated? the Directory Android Key with \"([^\"]*)\"$")
    public void iUpdateTheDirectoryAndroidKeyWith(String androidKey) throws Throwable {
        directoryManager.updateCurrentDirectory(directoryManager.getCurrentDirectoryEntity().getActive(), androidKey,
                directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @When("^I updated? the Directory Android Key with null$")
    public void iUpdateTheDirectoryAndroidKeyWithNull() throws Throwable {
        iUpdateTheDirectoryAndroidKeyWith(null);
    }

    @Then("^the Directory Android Key is \"([^\"]*)\"$")
    public void thenTheDirectoryAndroidKeyIs(String androidKey) {
        assertThat(directoryManager.getCurrentDirectoryEntity().getAndroidKey(), is(equalTo(androidKey)));
    }

    @Then("^the Directory has no Android Key$")
    public void theDirectoryHasNoAndroidKey() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getAndroidKey(), is(nullValue()));
    }

    @When("^I updated? the Directory iOS P12 with null$")
    public void iUpdateTheDirectoryIOSPWithNull() throws Throwable {
        directoryManager
                .updateCurrentDirectory(true, directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                        directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                        directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                        directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @When("^I updated? the Directory iOS P12 with a valid certificate$")
    public void iUpdateTheDirectoryIOSPWithAValidCertificate() throws Throwable {
        directoryManager.updateCurrentDirectory(directoryManager.getCurrentDirectoryEntity().getActive(),
                directoryManager.getCurrentDirectoryEntity().getAndroidKey(), keysManager.getBase64EncodedAlphaP12(),
                keysManager.getAlphaCertificateFingerprint(),
                directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @Then("^Directory the iOS Certificate Fingerprint matches the provided certificate$")
    public void directoryTheIOSCertificateFingerprintMatchesTheProvidedCertificate() {
        // Write code here that turns the phrase above into concrete actions
        assertThat(directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                is(equalTo(directoryManager.getPreviousDirectoryEntity().getIosCertificateFingerprint())));
    }

    @Then("^the Directory has no IOS Certificate Fingerprint$")
    public void theDirectoryHasNoIOSCertificateFingerprint() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(), is(nullValue()));
    }

    @When("^I update the Directory denial context inquiry enabled flag to false")
    public void iUpdateTheDirectoryDenialContextInquiryEnabledFlag() throws Throwable {
        directoryManager.updateCurrentDirectory(directoryManager.getCurrentDirectoryEntity().getActive(),
                directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                false,
                directoryManager.getCurrentDirectoryEntity().getWebhookUrl());
    }

    @Then("^Directory denial context inquiry enabled flag is false")
    public void directoryTheDirectoryDenialContextInquiryEnabledFlagIsValue() {
        assertThat(Boolean.FALSE,
                is(equalTo(directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled())));
    }

    @When("^I updated? the Directory webhook url to \"([^\"]*)\"$")
    public void iUpdatedTheDirectoryWebhookUrlTo(String webhookUrl) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        directoryManager.updateCurrentDirectory(directoryManager.getCurrentDirectoryEntity().getActive(),
                directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
                URI.create(webhookUrl));
    }

    @When("^I updated? the Directory webhook url to null$")
    public void iUpdatedTheDirectoryWebhookUrlToNull() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        directoryManager.updateCurrentDirectory(directoryManager.getCurrentDirectoryEntity().getActive(),
                directoryManager.getCurrentDirectoryEntity().getAndroidKey(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificate(),
                directoryManager.getCurrentDirectoryEntity().getIosCertificateFingerprint(),
                directoryManager.getCurrentDirectoryEntity().isDenialContextInquiryEnabled(),
               null);
    }

    @And("^the Directory webhook url is \"([^\"]*)\"$")
    public void theDirectoryWebhookUrlIs(String webhookUrl) {
        assertThat(URI.create(webhookUrl),
                is(equalTo(directoryManager.getCurrentDirectoryEntity().getWebhookUrl())));
    }

    @And("^the Directory webhook url is empty$")
    public void theDirectoryWebhookUrlIsEmpty() {
        assertThat(null, is(equalTo(directoryManager.getCurrentDirectoryEntity().getWebhookUrl())));
    }

    @And("^the Directory has the added SDK Keys?$")
    public void theDirectoryHasTheAddedSDKKeys() {
        Collections.sort(directoryManager.getCurrentDirectoryEntity().getSdkKeys());
        Collections.sort(directoryManager.getPreviousDirectoryEntity().getSdkKeys());

        assertThat(directoryManager.getCurrentDirectoryEntity().getSdkKeys(),
                is(equalTo(directoryManager.getPreviousDirectoryEntity().getSdkKeys())));
    }

    @Then("^the ID matches the value returned when the Directory was created$")
    public void theIDMatchesTheValueReturnedWhenTheDirectoryWasCreated() {
        assertThat(directoryManager.getCurrentDirectoryEntity().getId(),
                is(equalTo(directoryManager.getPreviousDirectoryEntity().getId())));
    }

    @And("^the Directory has the added Service IDs?$")
    public void theDirectoryHasTheAddedServiceIDs() {
        Collections.sort(directoryManager.getPreviousDirectoryEntity().getServiceIds());
        Collections.sort(directoryManager.getCurrentDirectoryEntity().getServiceIds());
        assertThat(directoryManager.getCurrentDirectoryEntity().getServiceIds(),
                is(equalTo(directoryManager.getPreviousDirectoryEntity().getServiceIds())));
    }

    @When("^I attempt retrieve the Directory identified by \"([^\"]*)\"$")
    public void iAttemptRetrieveTheDirectoryIdentifiedBy(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.retrieveDirectory(directoryId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve a list of Directories with the created Directory's ID$")
    public void iRetrieveAListOfDirectoriesWithTheCreatedDirectorySID() throws Throwable {
        directoryManager.retrieveCurrentDirectoryAsList();
    }

    @Then("^the current Directory list is a list with only the current Directory$")
    public void theCurrentDirectoryIsAListWithOnlyTheCurrentDirectory() {
        assertThat(directoryManager.getCurrentDirectoryEntityList(),
                is(equalTo(Collections.singletonList(directoryManager.getCurrentDirectoryEntity()))));
    }

    @Then("^the current Directory is in the Directory list$")
    public void theCurrentDirectoryIsInTheDirectoryList() {
        assertThat(directoryManager.getCurrentDirectoryEntityList(),
                hasItem(directoryManager.getCurrentDirectoryEntity()));
    }

    @When("^I attempt retrieve a list of Directories with the Directory ID \"([^\"]*)\"$")
    public void iAttemptRetrieveAListOfDirectoriesWithTheDirectoryID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.retrieveDirectoryList(Collections.singletonList(directoryId));
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve a list of all Directories$")
    public void iRetrieveAListOfAllDirectories() throws Throwable {
        directoryManager.retrieveAllDirectories();
    }


    @When("^I attempt to update the active status of the Directory with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateTheActiveStatusOfTheDirectoryWithTheID(String uuid) throws Throwable {
        UUID directoryId = UUID.fromString(uuid);
        try {
            directoryManager.updateDirectory(directoryId, false);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^DenialContextInquiryEnabled (is|should be) set to \"([^\"]+)\"$")
    public void denialContextInquiryEnabledIsSetTo(@SuppressWarnings("unused") String ignore, boolean value) {
        assertThat(value, is(equalTo(value)));
    }

    @When("^I update the DenialContextInquiryEnabled to \"([^\"]+)\"$")
    public void iUpdateTheDenialContextInquiryEnabledTo(boolean value) throws Throwable {
        DirectoryEntity directory = directoryManager.getCurrentDirectoryEntity();
        directoryManager.updateDirectory(directory.getId(), directory.getActive(), null, null, null, value, null);
    }
}