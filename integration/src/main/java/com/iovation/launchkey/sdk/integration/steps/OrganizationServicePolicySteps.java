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
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.cucumber.converters.LocationListConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.TimeFenceListConverter;
import com.iovation.launchkey.sdk.integration.entities.ServicePolicyEntity;
import com.iovation.launchkey.sdk.integration.managers.OrganizationServicePolicyManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@Singleton
public class OrganizationServicePolicySteps {
    private OrganizationServicePolicyManager organizationServicePolicyManager;
    private GenericSteps genericSteps;

    @Inject
    public OrganizationServicePolicySteps(
            OrganizationServicePolicyManager organizationServicePolicyManager,
            GenericSteps genericSteps) {
        this.organizationServicePolicyManager = organizationServicePolicyManager;
        this.genericSteps = genericSteps;
    }

    @When("^I retrieve the Policy for the Current Organization Service$")
    public void iRetrieveTheOrganizationServicePolicy() throws Throwable {
        organizationServicePolicyManager.retrievePolicyForCurrentService();
    }

    @When("^I attempt to remove the Policy for the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveThePolicyForTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServicePolicyManager.removePolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the Policy for the Organization Service$")
    public void iRemoveThePolicyForTheOrganizationService() throws Throwable {
        organizationServicePolicyManager.removePolicyForCurrentService();
    }

    @When("^I attempt to retrieve the Policy for the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePolicyForTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServicePolicyManager.retrievePolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to set the Policy for the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToSetThePolicyForTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServicePolicyManager.setPolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I set the Policy for the Organization Service$")
    public void iSetThePolicyForTheOrganizationService() throws Throwable {
        organizationServicePolicyManager.setPolicyForCurrentService();
    }

    @When("^the Organization Service Policy is set to require (\\d+) factors?$")
    public void theOrganizationServicePolicyRequiresFactors(int requiredFactors) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity().setRequiredFactors(requiredFactors);
    }

    @When("^I set the Policy for the Current Organization Service$")
    public void iSetThePolicyForTheCurrentOrganizationService() throws Throwable {
        organizationServicePolicyManager.setPolicyForCurrentService();
    }

    @When("^the Organization Service Policy is (not set|set) to require inherence$")
    public void theOrganizationServicePolicyIsSetToRequireInherence(String switchString) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequireInherenceFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Organization Service Policy is (not set|set) to require knowledge$")
    public void theOrganizationServicePolicyIsSetToRequireKnowledge(String switchString) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequireKnowledgeFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Organization Service Policy is (not set|set) to require possession$")
    public void theOrganizationServicePolicyIsSetToRequirePossession(String switchString) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequirePossessionFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Organization Service Policy is (not set|set) to require jail break protection$")
    public void theOrganizationServicePolicyIsSetToRequireJailBreakProtection(String switchString) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity()
                .setJailBreakProtectionEnabled(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @Then("^the Organization Service Policy has no requirement for number of factors$")
    public void theOrganizationServicePolicyRequiresFactory() throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getRequiredFactors(), is(nullValue()));
    }

    @Then("^the Organization Service Policy requires (\\d+) factors$")
    public void theOrganizationServicePolicyRequiresFactory(int numberOfFactors) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getRequiredFactors(),
                is(equalTo(numberOfFactors)));
    }

    @Then("^the Organization Service Policy (does not|does|has no) require(?:ment for)? inherence$")
    public void theOrganizationServicePolicyDoesNotRequireInherence(String switchString) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getRequireInherenceFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Organization Service Policy (does not|does|has no) require(?:ment for)? knowledge$")
    public void theOrganizationServicePolicyDoesNotRequireKnowledge(String switchString) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getRequireKnowledgeFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Organization Service Policy (does not|does|has no) require(?:ment for)? possession$")
    public void theOrganizationServicePolicyDoesNotRequirePossession(String switchString) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getRequirePossessionFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Organization Service Policy has (\\d+) locations$")
    public void theOrganizationServicePolicyHasLocations(int numberOfLocations) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getLocations(),
                hasSize(numberOfLocations));
    }

    @Then("^the Organization Service Policy has (\\d+) time fences$")
    public void theOrganizationServicePolicyHasTimeFences(int numberOfFences) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences(),
                hasSize(numberOfFences));
    }

    @Then("^the Organization Service Policy (does not|does|has no) require(?:ment for)? jail break protection$")
    public void theOrganizationServicePolicyDoesNotRequireJailBreakProtection(String switchString) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getJailBreakProtectionEnabled(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Given("^the Organization Service Policy is set to have the following Time Fences:$")
    public void theOrganizationServicePolicyIsSetToHaveTheFollowingTimeFences(DataTable dataTable) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences().addAll(TimeFenceListConverter.fromDataTable(dataTable));
    }

    @Given("^the Organization Service Policy has the following Time Fences:$")
    public void theOrganizationServicePolicyHasTheFollowingTimeFences(DataTable dataTable) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences(), is(equalTo(TimeFenceListConverter.fromDataTable(dataTable))));
    }

    @Given("^the Organization Service Policy is set to have the following Geofence locations:$")
    public void theOrganizationServicePolicyIsSetToHaveTheFollowingGeofenceLocations(DataTable dataTable) throws Throwable {
        organizationServicePolicyManager.getCurrentServicePolicyEntity().getLocations().addAll(LocationListConverter.fromDataTable(dataTable));
    }

    @Given("^the Organization Service Policy has the following Geofence locations:$")
    public void theOrganizationServicePolicyHasTheFollowingGeofenceLocations(DataTable dataTable) throws Throwable {
        assertThat(organizationServicePolicyManager.getCurrentServicePolicyEntity().getLocations(), is(equalTo(LocationListConverter.fromDataTable(dataTable))));
    }
}
