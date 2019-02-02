package com.iovation.launchkey.sdk.integration;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.entities.DeviceEntity;
import com.iovation.launchkey.sdk.integration.entities.LinkingResponseEntity;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;

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
    private final DirectoryDeviceManager directoryDeviceManager;
    private final GenericSteps genericSteps;
    private String currentUserIdentifier = null;

    @Inject
    public DirectoryDeviceSteps(DirectoryDeviceManager directoryDeviceManager, GenericSteps genericSteps) {
        this.directoryDeviceManager = directoryDeviceManager;
        this.genericSteps = genericSteps;
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

    @And("^the Device List has (\\d+) Devices?$")
    public void theDeviceListHasNumberOfDevices(int expectedDeviceCount) throws Throwable {
        assertThat(directoryDeviceManager.getCurrentDevicesList().size(), is(equalTo(expectedDeviceCount)));
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

    @When("^I attempt to retrieve the Device List for a User identified by \"([^\"]*)\"$")
    public void iAttemptToRetrieveTheDeviceListForAUSerIdentifiedBy(String userIdentifier) throws Throwable {
        try {
            directoryDeviceManager.retrieveUserDevices(userIdentifier);
        } catch (Exception e) {
            this.genericSteps.setCurrentException(e);
        }
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
