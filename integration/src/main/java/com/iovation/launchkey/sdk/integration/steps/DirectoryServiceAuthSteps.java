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
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.integration.cucumber.converters.MethodsListConverter;
import com.iovation.launchkey.sdk.integration.entities.AuthorizationResponseEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceAuthsManager;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class DirectoryServiceAuthSteps {
    private final GenericSteps genericSteps;
    private final DirectoryServiceAuthsManager directoryServiceAuthsManager;
    private final DirectoryDeviceManager directoryDeviceManager;
    private final ServiceAuthorizationPolicySteps policySteps;

    @Inject
    public DirectoryServiceAuthSteps(GenericSteps genericSteps,
                                     DirectoryServiceAuthsManager directoryServiceAuthsManager,
                                     DirectoryDeviceManager directoryDeviceManager,
                                     ServiceAuthorizationPolicySteps policySteps) {
        this.genericSteps = genericSteps;
        this.directoryServiceAuthsManager = directoryServiceAuthsManager;
        this.directoryDeviceManager = directoryDeviceManager;
        this.policySteps = policySteps;
    }


    @Given("^I made an Authorization request$")
    @When("^I make an Authorization request$")
    public void makeAuthForCurrentUserIdentifier() throws Throwable {
        directoryServiceAuthsManager.createAuthorizationRequest(directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to make an Authorization request$")
    public void iMakeAnAuthorizationRequest() throws Throwable {
        iAttemptToMakeAnAuthorizationRequest(directoryDeviceManager.getCurrentUserIdentifier());
    }

    @When("^I attempt to make an Authorization request with the context value \"([^\"]*)\"$")
    public void iAttemptToMakeAnAuthorizationRequestWithContext(String context) throws Throwable {
        try {
            directoryServiceAuthsManager
                    .createAuthorizationRequest(directoryDeviceManager.getCurrentUserIdentifier(), context);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to make an Authorization request for the User identified by \"([^\"]*)\"$")
    public void iAttemptToMakeAnAuthorizationRequest(String userIdentifier) throws Throwable {
        try {
            directoryServiceAuthsManager
                    .createAuthorizationRequest(userIdentifier);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I get the response for the Authorization request$")
    public void retrieveCurrentAuthRequest() throws Throwable {
        directoryServiceAuthsManager.getAuthorizationResponse();
    }

    @When("^I get the response for Authorization request \"([^\"]*)\"$")
    public void iGetTheResponseForAuthorizationRequest(String authRequest) throws Throwable {
        directoryServiceAuthsManager.getAuthorizationResponse();
    }

    @Then("^the Authorization response is not returned$")
    public void theAuthorizationResponseIsNotReturned() throws Throwable {
        assertThat(directoryServiceAuthsManager.getCurrentAuthResponse(), is(nullValue()));
    }

    @Then("^the Authorization response should be approved$")
    public void theAuthorizationResponseShouldBeApproved() {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        assertNotNull(response);
        assertTrue(response.isAuthorized());
    }

    @Then("^the Authorization response should be denied$")
    public void theAuthorizationResponseShouldBeDenied() {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        assertNotNull(response);
        assertFalse(response.isAuthorized());
    }

    @Then("^the Authorization response should require (\\d+) factors$")
    public void theAuthorizationResponseShouldRequireNFactors(int n) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        assertNotNull(response);
        //assertEquals(response.getPolicy?().getCount?());
    }

    @Then("the Authorization response should contain a geofence with a radius of {float}, a latitude of {float}, and a longitude of {float}")
    public void theAuthorizationResponseShouldContainAGeofenceWithARadiusOfXALatitudeOfYAndALongitudeOfZ(double radius, double latitude, double longitude) {
        // Get geofence from auth response
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
//        Geofence geofence = response.getGeofence?();
//        assertTrue(geofence.radius == radius);
//        assertTrue(geofence.latitude == latitude);
//        assertTrue(geofence.longitude == longitude);

    }

    @Then("^the Authorization response should require (knowledge|inherence|possession)$")
    public void theAuthorizationResponseShouldRequireFactor(String requiredString, String factor) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        //assertTrue(response.getFactorType?().equals(factor));
    }

    @Then("^the Authorization response should not require (knowledge|inherence|possession)$")
    public void theAuthorizationResponseShouldNotRequireFactor(String factor) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        //assertTrue(!response.getFactorType?().equals(factor));
    }

    @Then("the Authorization response should contain the following methods")
    public void theAuthorizationResponseShouldContainTheFollowingMethods(DataTable dataTable) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        //assertThat(response.getMethods?(), is(equalTo(MethodsListConverter.fromDataTable(dataTable))));
    }

    @When("^I attempt to make an Policy based Authorization request for the User identified by \"([^\"]*)\"$")
    public void iAttemptToMakeAnPolicyBasedAuthorizationRequestForTheUserIdentifiedBy(String userIdentifier)
            throws Throwable {
        try {
            directoryServiceAuthsManager
                    .createAuthorizationRequest(userIdentifier, null, policySteps.getCurrentAuthPolicy());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}
