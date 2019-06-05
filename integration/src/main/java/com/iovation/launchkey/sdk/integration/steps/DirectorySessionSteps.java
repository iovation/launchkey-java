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
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectorySessionManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class DirectorySessionSteps {
    private final DirectoryDeviceManager directoryDeviceManager;
    private final DirectorySessionManager directorySessionManager;
    private final GenericSteps genericSteps;

    @Inject
    public DirectorySessionSteps(DirectoryDeviceManager directoryDeviceManager,
                                 DirectorySessionManager directorySessionManager, GenericSteps genericSteps) {
        this.directoryDeviceManager = directoryDeviceManager;
        this.directorySessionManager = directorySessionManager;
        this.genericSteps = genericSteps;
    }

    @Then("^the Service User Session List has (\\d+) Sessions$")
    public void theServiceUserSessionListHasSessions(int numberOfSessions) throws Throwable {
        assertThat(directorySessionManager.getCurrentSessionList(), hasSize(numberOfSessions));
    }

    @When("^I retrieve the Session list for the current User$")
    public void iRetrieveTheSessionListForTheCurrentUser() throws Throwable {
        directorySessionManager.retrieveSessionList(directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to retrieve the Session list for the User \"([^\"]*)\"$")
    public void iRetrieveTheSessionListForTheUser(String userIdentifier) throws Throwable {
        try {
            directorySessionManager.retrieveSessionList(userIdentifier);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I delete the Sessions for the current User$")
    public void iDeleteTheSessionsForTheCurrentUser() throws Throwable {
        directorySessionManager.endAllSessionsForUser(directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to delete the Sessions for the User \"([^\"]*)\"$")
    public void iAttemptToDeleteTheSessionsForTheUser(String userIdentifier) throws Throwable {
        try {
            directorySessionManager.endAllSessionsForUser(userIdentifier);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}
