package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.entities.DeviceEntity;
import com.iovation.launchkey.sdk.integration.entities.LinkingResponseEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import com.iovation.launchkey.sdk.integration.mobile.driver.SampleAppMobileDriver;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

public class DirectoryDeviceSteps {
    private final DirectoryManager directoryManager;
    private final DirectoryDeviceManager directoryDeviceManager;
    private final GenericSteps genericSteps;
    private final SampleAppMobileDriver driver;

    @Inject
    public DirectoryDeviceSteps(DirectoryManager directoryManager,
                                DirectoryDeviceManager directoryDeviceManager,
                                GenericSteps genericSteps,
                                SampleAppMobileDriver driver) {
        this.directoryManager = directoryManager;
        this.directoryDeviceManager = directoryDeviceManager;
        this.genericSteps = genericSteps;
        this.driver = driver;
    }

    @When("^I ma[k|d]e a Device linking request$")
    public void iMakeADeviceLinkingRequest() throws Throwable {
        directoryDeviceManager.createLinkingRequest(Utils.createRandomDirectoryUserName(), null);
    }

    @When("^I ma[k|d]e a Device linking request with a TTL of (\\d+) seconds$")
    public void iMakeADeviceLinkingRequest(int seconds) throws Throwable {
        directoryDeviceManager.createLinkingRequest(Utils.createRandomDirectoryUserName(), seconds);
    }

    @And("^I retrieve the Devices list for the current User$")
    public void iRetrieveTheDevicesListForTheCurrentUser() throws Throwable {
        directoryDeviceManager.retrieveUserDevices();
    }

    @And("^I retrieve the Devices list for the user \"([^\"]*)\"$")
    public void iRetrieveTheDevicesListForTheCurrentUser(String userIdentifier) throws Throwable {
        directoryDeviceManager.retrieveUserDevices(userIdentifier);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Then("^the Device linking response contains a valid QR Code URL$")
    public void theDeviceLinkingResponseContainsAValidQRCodeURL() {
        LinkingResponseEntity response = directoryDeviceManager.getCurrentLinkingResponse();
        String url = response.getQrCodeURL();
        try {
            URI.create(url);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new RuntimeException("Could not parse QR Code URL as URL", e);
        }
        assertThat("QR Code URL does not contain linking code", url, containsString(response.getLinkingCode()));
    }

    @And("^the Device linking response contains a valid Linking Code$")
    public void theDeviceLinkingResponseContainsAValidLinkingCode() throws Throwable {
        String linkingCode = directoryDeviceManager.getCurrentLinkingResponse().getLinkingCode();
        assertThat(linkingCode, not(isEmptyOrNullString()));
    }

    @And("^the Device linking response contains a valid Device ID$")
    public void theDeviceLinkingResponseContainsAValidDeviceID() {
        UUID deviceId = directoryDeviceManager.getCurrentLinkingResponse().getDeviceId();
        assertThat(deviceId, not(nullValue(UUID.class)));
    }

    @And("^the Device List has (\\d+) Devices?$")
    public void theDeviceListHasNumberOfDevices(int expectedDeviceCount) throws Throwable {
        assertThat(directoryDeviceManager.getCurrentDevicesList().size(), is(equalTo(expectedDeviceCount)));
    }

    @Then("^all of the devices should be inactive$")
    public void allOfTheDevicesShouldBeInactive() throws Throwable {
        directoryDeviceManager.retrieveUserDevices();
        for (DeviceEntity device : directoryDeviceManager.getCurrentDevicesList()) {
            assertFalse(device.isActive());
        }
    }

    @Then("^all of the devices should be active$")
    public void allOfTheDevicesShouldBeActive() throws Throwable {
        directoryDeviceManager.retrieveUserDevices();
        for (DeviceEntity device : directoryDeviceManager.getCurrentDevicesList()) {
            assertTrue(device.isActive());
        }
    }


    @When("^I attempt to unlink the device with the ID \"([^\"]*)\"$")
    public void iAttemptToUnlinkTheDeviceWithTheID(String deviceIdString) throws Throwable {
        UUID deviceId = UUID.fromString(deviceIdString);
        try {
            directoryDeviceManager.unlinkDevice(directoryDeviceManager.getCurrentUserIdentifier(), deviceId);
        } catch (Exception e) {
            this.genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to unlink the device from the User Identifier \"([^\"]*)\"$")
    public void iAttemptToUnlinkTheDeviceFromTheUserIdentifier(String userIdentifier) throws Throwable {
        try {
            directoryDeviceManager.unlinkDevice(userIdentifier, UUID.randomUUID());
        } catch (Exception e) {
            this.genericSteps.setCurrentException(e);
        }
    }


    @And("^I have a linked Device$")
    public void linkDevice() throws Throwable {
        iMakeADeviceLinkingRequest();
        iLinkMyDevice();
    }

    @When("^I link my device$")
    public void iLinkMyDevice() {
        iLinkedMyPhysicalDeviceWithTheNameDeviceName(null);
    }


    @When("I link my physical device with the name {word}")
    public void iLinkedMyPhysicalDeviceWithTheNameDeviceName(String deviceName) {
        String sdkKey = directoryManager.getCurrentDirectoryEntity().getSdkKeys().get(0).toString();
        String linkingCode = directoryDeviceManager.getCurrentLinkingResponse().getLinkingCode();
        driver.linkDevice(sdkKey, linkingCode, deviceName);
    }

    @When("^I approve the auth request$")
    public void iApproveTheAuthRequest() {
        driver.approveRequest();
    }

    @When("^I deny the auth request$")
    public void iDenyTheAuthRequest() {
        driver.denyRequest();
    }

    @When("^I receive the auth request and acknowledge the failure message$")
    public void iReceiveTheAuthRequestAndAcknowledgeTheFailureMessage() {
        driver.receiveAndAcknowledgeAuthFailure();
    }

    @And("^there should be (\\d+) Devices? in the Devices list$")
    public void thereShouldBeDeviceInTheDevicesList(int devices) throws Throwable {
        assertThat(directoryDeviceManager.getCurrentDevicesList().size(), equalTo(devices));
    }

    @When("^I unlink the current Device$")
    public void iUnlinkTheCurrentDevice() throws Throwable {
        List<DeviceEntity> devices = directoryDeviceManager.getCurrentDevicesList();
        DeviceEntity currentDevice = devices.get(devices.size() - 1);
        directoryDeviceManager.unlinkDevice(directoryDeviceManager.getCurrentUserIdentifier(),
                UUID.fromString(currentDevice.getId()));
    }
}
