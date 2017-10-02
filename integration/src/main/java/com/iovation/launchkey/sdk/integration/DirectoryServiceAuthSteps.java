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

package com.iovation.launchkey.sdk.integration;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.integration.entities.AuthorizationResponseEntity;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class DirectoryServiceAuthSteps {
    private final GenericSteps genericSteps;
    private final DirectoryServiceAuthsManager directoryServiceAuthsManager;
    private final DirectoryServiceManager directoryServiceManager;
    private final DirectoryDeviceManager directoryDeviceManager;
    private final ServiceAuthorizationPolicySteps policySteps;
    private AuthorizationResponseEntity currentAuthorizationResponse;
    private String currentAuthRequestId;

    @Inject
    public DirectoryServiceAuthSteps(GenericSteps genericSteps,
                                     DirectoryServiceAuthsManager directoryServiceAuthsManager,
                                     DirectoryServiceManager directoryServiceManager,
                                     DirectoryDeviceManager directoryDeviceManager,
                                     ServiceAuthorizationPolicySteps policySteps) {
        this.genericSteps = genericSteps;
        this.directoryServiceAuthsManager = directoryServiceAuthsManager;
        this.directoryServiceManager = directoryServiceManager;
        this.directoryDeviceManager = directoryDeviceManager;
        this.policySteps = policySteps;
        currentAuthorizationResponse = null;
        currentAuthRequestId = null;
    }

    public void cleanUp() {
        currentAuthRequestId = null;
        currentAuthorizationResponse = null;
    }

    @When("^I attempt to make an Authorization request$")
    public void iMakeAnAuthorizationRequest() throws Throwable {
        iAttemptToMakeAnAuthorizationRequest(directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to make an Authorization request with the context value \"([^\"]*)\"$")
    public void iAttemptToMakeAnAuthorizationRequestWithContext(String context) throws Throwable {
        try {
            currentAuthRequestId = directoryServiceAuthsManager
                    .createAuthsRequest(directoryServiceManager.getCurrentServiceEntity().getId(),
                            directoryDeviceManager.getCurrentUserIdentifier(), context);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to make an Authorization request for the User identified by \"([^\"]*)\"$")
    public void iAttemptToMakeAnAuthorizationRequest(String userIdentifier) throws Throwable {
        try {
            currentAuthRequestId = directoryServiceAuthsManager
                    .createAuthsRequest(directoryServiceManager.getCurrentServiceEntity().getId(), userIdentifier);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I get the response for Authorization request \"([^\"]*)\"$")
    public void iGetTheResponseForAuthorizationRequest(UUID authRequest) throws Throwable {
        AuthorizationResponse response = directoryServiceAuthsManager
                .getAuthResponse(directoryServiceManager.getCurrentServiceEntity().getId(), authRequest);
        currentAuthorizationResponse =
                response == null ? null : AuthorizationResponseEntity.fromAuthorizationResponse(response);
    }

    @Then("^the Authorization response is not returned$")
    public void theAuthorizationResponseIsNotReturned() throws Throwable {
        assertThat(currentAuthorizationResponse, is(nullValue()));
    }

    @When("^I attempt to make an Policy based Authorization request for the User identified by \"([^\"]*)\"$")
    public void iAttemptToMakeAnPolicyBasedAuthorizationRequestForTheUserIdentifiedBy(String userIdentifier)
            throws Throwable {
        try {
            currentAuthRequestId = directoryServiceAuthsManager
                    .createAuthsRequest(directoryServiceManager.getCurrentServiceEntity().getId(), userIdentifier,
                            policySteps.getCurrentAuthPolicy());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}
