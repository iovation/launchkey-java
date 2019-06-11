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

package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceSessionManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

@Singleton
public class ServiceSessionSteps {
    private final GenericSteps genericSteps;
    private final DirectoryServiceSessionManager directoryServiceSessionManager;
    private final DirectoryServiceManager directoryServiceManager;
    private final DirectoryDeviceManager directoryDeviceManager;

    @Inject
    public ServiceSessionSteps(GenericSteps genericSteps,
                               DirectoryServiceSessionManager directoryServiceSessionManager,
                               DirectoryServiceManager directoryServiceManager,
                               DirectoryDeviceManager directoryDeviceManager) {
        this.genericSteps = genericSteps;
        this.directoryServiceSessionManager = directoryServiceSessionManager;
        this.directoryServiceManager = directoryServiceManager;
        this.directoryDeviceManager = directoryDeviceManager;
    }

    @Given("^I sent a Session Start request$")
    @When("^I send a Session Start request with no Auth Request ID$")
    public void iSendASessionStartRequestWithNoAuthRequestID() throws Throwable {
        directoryServiceSessionManager.startSession(directoryServiceManager.getCurrentServiceEntity().getId(),
                directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I send a Session Start request with Auth Request ID \"([^\"]*)\"$")
    public void iSendASessionStartRequestWithAuthRequestID(String authRequestId) throws Throwable {
        directoryServiceSessionManager.startSession(directoryServiceManager.getCurrentServiceEntity().getId(),
                directoryDeviceManager.getCurrentUserIdentifier(), authRequestId);
    }

    @When("^I attempt to send a Session Start request for user \"([^\"]*)\"$")
    public void iAttemptToSendASessionStartRequestForUser(String username) throws Throwable {
        try {
            directoryServiceSessionManager
                    .startSession(directoryServiceManager.getCurrentServiceEntity().getId(), username);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I send a Session End request$")
    public void iSendASessionEndRequest() throws Throwable {
        directoryServiceSessionManager.startSession(directoryServiceManager.getCurrentServiceEntity().getId(),
                directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to send a Session End request for user \"([^\"]*)\"$")
    public void iAttemptToSendASessionEndRequestForUser(String username) throws Throwable {
        try {
            directoryServiceSessionManager
                    .endSession(directoryServiceManager.getCurrentServiceEntity().getId(), username);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}
