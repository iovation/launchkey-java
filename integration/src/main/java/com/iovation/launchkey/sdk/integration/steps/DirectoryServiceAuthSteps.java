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
import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import com.iovation.launchkey.sdk.domain.policy.TerritoryFence;
import com.iovation.launchkey.sdk.domain.service.*;
import com.iovation.launchkey.sdk.integration.cucumber.converters.MethodsListConverter;
import com.iovation.launchkey.sdk.integration.entities.DeviceEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceAuthsManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class DirectoryServiceAuthSteps {
    private final GenericSteps genericSteps;
    private final DirectoryServiceAuthsManager directoryServiceAuthsManager;
    private final DirectoryDeviceManager directoryDeviceManager;

    @Inject
    public DirectoryServiceAuthSteps(GenericSteps genericSteps,
                                     DirectoryServiceAuthsManager directoryServiceAuthsManager,
                                     DirectoryDeviceManager directoryDeviceManager) {
        this.genericSteps = genericSteps;
        this.directoryServiceAuthsManager = directoryServiceAuthsManager;
        this.directoryDeviceManager = directoryDeviceManager;
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
        directoryServiceAuthsManager.getAuthorizationResponse(authRequest);
    }

    @When("^I get the response for the Advanced Authorization request \"([^\"]*)\"$")
    public void iGetTheResponseForAdvancedAuthorizationRequest(String authRequest) throws Throwable {
        directoryServiceAuthsManager.getAdvancedAuthorizationResponse(authRequest);
    }

    @When("I get the response for the Advanced Authorization request")
    public void iGetTheResponseForTheAdvancedAuthorizationRequest() throws Throwable {
        directoryServiceAuthsManager.getAdvancedAuthorizationResponse();
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
        assertEquals(response.getPolicy().getRequiredFactors(), Integer.valueOf(n));
    }

    @Then("the Authorization response should contain a geofence with a radius of {double}, a latitude of {double}, and a longitude of {double}")
    public void theAuthorizationResponseShouldContainAGeofenceWithARadiusOfXALatitudeOfYAndALongitudeOfZ(double radius, double latitude, double longitude) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        AuthPolicy.Location expected = new AuthPolicy.Location(radius, latitude, longitude);
        assertThat(response.getPolicy().getLocations(), hasItem(expected));
    }

    @Then("the Authorization response should contain a geofence with a radius of {double}, a latitude of {double}, a longitude of {double}, and a name of {string}")
    public void theAuthorizationResponseShouldContainAGeofenceWithARadiusOfXALatitudeOfYALongitudeOfZAndANameOfZ(double radius, double latitude, double longitude, String name) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        AuthPolicy.Location expected = new AuthPolicy.Location(name, radius, latitude, longitude);
        assertThat(response.getPolicy().getLocations(), hasItem(expected));
    }

    @Then("^the Authorization response should require (knowledge|inherence|possession)$")
    public void theAuthorizationResponseShouldRequireFactor(String factor) {
        AuthPolicy policy = directoryServiceAuthsManager.getCurrentAuthResponse().getPolicy();
        switch (factor) {
            case "knowledge":
                assertTrue(policy.isKnowledgeFactorRequired());
                break;
            case "inherence":
                assertTrue(policy.isInherenceFactorRequired());
                break;
            case "possession":
                assertTrue(policy.isPossessionFactorRequired());
                break;
        }
    }

    @Then("^the Authorization response should not require (knowledge|inherence|possession)$")
    public void theAuthorizationResponseShouldNotRequireFactor(String factor) {
        AuthPolicy policy = directoryServiceAuthsManager.getCurrentAuthResponse().getPolicy();
        switch (factor) {
            case "knowledge":
                assertFalse(policy.isKnowledgeFactorRequired());
                break;
            case "inherence":
                assertFalse(policy.isInherenceFactorRequired());
                break;
            case "possession":
                assertFalse(policy.isPossessionFactorRequired());
                break;
        }
    }

    @Then("the Authorization response should contain the following methods:")
    public void theAuthorizationResponseShouldContainTheFollowingMethods(DataTable dataTable) {
        AuthorizationResponse response = directoryServiceAuthsManager.getCurrentAuthResponse();
        assertThat(response.getAuthMethods(), is(equalTo(MethodsListConverter.fromDataTable(dataTable))));
    }

    @When("^I attempt to make an Policy based Authorization request for the User identified by \"([^\"]*)\"$")
    public void iAttemptToMakeAnPolicyBasedAuthorizationRequestForTheUserIdentifiedBy(String userIdentifier)
            throws Throwable {
        try {
            directoryServiceAuthsManager
                    .createAuthorizationRequest(userIdentifier, null, directoryServiceAuthsManager.getCurrentAuthPolicy());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("the Advanced Authorization response should have the requirement {string}")
    public void theAdvancedAuthorizationResponseShouldHaveTheRequirement(String name) {
        AuthorizationResponsePolicy policy = directoryServiceAuthsManager.getCurrentAdvancedAuthorizationResponse().getPolicy();
        assertEquals(Requirement.valueOf(name), policy.getRequirement());
    }

    @Then("^the Advanced Authorization response should require (Knowledge|Inherence|Possession)$")
    public void theAdvancedAuthorizationResponseShouldRequireFactor(String factor) {
        AuthorizationResponsePolicy policy = directoryServiceAuthsManager.getCurrentAdvancedAuthorizationResponse().getPolicy();
        switch (factor) {
            case "Knowledge":
                assertTrue(policy.wasKnowledgeRequired());
                break;
            case "Inherence":
                assertTrue(policy.wasInherenceRequired());
                break;
            case "Possession":
                assertTrue(policy.wasPossessionRequired());
                break;
        }
    }

    @Then("the Advanced Authorization response should have amount set to {int}")
    public void theAdvancedAuthorizationResponseShouldHaveAmountSetTo(int amount) {
        AuthorizationResponsePolicy policy = directoryServiceAuthsManager.getCurrentAdvancedAuthorizationResponse().getPolicy();
        assertEquals(amount, policy.getAmount());
    }

    @Then("^the Advanced Authorization response should contain a GeoCircleFence with a radius of (\\d+\\.?\\d*), a latitude of (-?\\d+\\.?\\d*), a longitude of (-?\\d+\\.?\\d*), and a name of \"([^\"]+)\"$")
    public void theAdvancedAuthorizationResponseShouldContainAGeoCircleFenceWithARadiusOfALatitudeOfALongitudeOfAndANameOf(double radius, double latitude, double longitude, String name) {
        GeoCircleFence expected = new GeoCircleFence(name, latitude, longitude, radius);
        AuthorizationResponsePolicy policy = directoryServiceAuthsManager.getCurrentAdvancedAuthorizationResponse().getPolicy();
        assertThat(policy.getFences(), hasItem(expected));
    }

    @Then("the Advanced Authorization response should contain a TerritoryFence with a country of {string}, a administrative area of {string}, a postal code of {string}, and a name of {string}")
    public void theAdvancedAuthorizationResponseShouldContainATerritoryFenceWithACountryOfAAdministrativeAreaOfAPostalCodeOfAndANameOf(String country, String admin, String postal_code, String name) {
        TerritoryFence expected = new TerritoryFence(name, country, admin, postal_code);
        AuthorizationResponsePolicy policy = directoryServiceAuthsManager.getCurrentAdvancedAuthorizationResponse().getPolicy();
        assertThat(policy.getFences(), hasItem(expected));
    }

    @Then("the Authorization Request response Device IDs matches the current Devices list")
    public void theAuthorizationRequestResponseDevicesListIsComprisedOfTheIDOfCurrentDevice() throws Throwable {
        AuthorizationRequest response = directoryServiceAuthsManager.getAuthorizationRequest();
        List<String> authRequestDeviceIds = response.getDeviceIds();
        assertThat("Authorization request response device IDs should not be null", authRequestDeviceIds, notNullValue());
        directoryDeviceManager.retrieveUserDevices();
        List<String> currentDeviceIds = new ArrayList<>();
        for (DeviceEntity device :  directoryDeviceManager.getCurrentDevicesList()) {
            currentDeviceIds.add(device.getId());
        }
        assertThat(authRequestDeviceIds, is(equalTo(currentDeviceIds)));
    }
}
