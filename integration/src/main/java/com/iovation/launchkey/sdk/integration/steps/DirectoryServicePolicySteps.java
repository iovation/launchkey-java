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
import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.integration.Utils;
import com.iovation.launchkey.sdk.integration.cucumber.converters.GeoCircleFenceConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.LocationListConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.TerritoryFenceConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.TimeFenceListConverter;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServicePolicyManager;
import com.iovation.launchkey.sdk.integration.managers.MutablePolicy;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@Singleton
public class DirectoryServicePolicySteps {
    private DirectoryServicePolicyManager directoryServicePolicyManager;
    private GenericSteps genericSteps;

    @Inject
    public DirectoryServicePolicySteps(
            DirectoryServicePolicyManager directoryServicePolicyManager,
            GenericSteps genericSteps) {
        this.directoryServicePolicyManager = directoryServicePolicyManager;
        this.genericSteps = genericSteps;
    }

    @When("^I retrieve the Policy for the Current Directory Service$")
    public void iRetrieveTheDirectoryServicePolicy() throws Throwable {
        directoryServicePolicyManager.retrievePolicyForCurrentService();
    }

    @When("^I attempt to remove the Policy for the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRemoveThePolicyForTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServicePolicyManager.removePolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I remove the Policy for the Directory Service$")
    public void iRemoveThePolicyForTheDirectoryService() throws Throwable {
        directoryServicePolicyManager.removeServicePolicyForCurrentService();
    }

    @When("^I attempt to retrieve the Policy for the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveThePolicyForTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServicePolicyManager.retrievePolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I attempt to set the Policy for the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToSetThePolicyForTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServicePolicyManager.setServicePolicyForService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I set the Policy for the Directory Service$")
    public void iSetThePolicyForTheDirectoryService() throws Throwable {
        directoryServicePolicyManager.setServicePolicyForCurrentService();
    }

    @When("^the Directory Service Policy is set to require (\\d+) factors?$")
    public void theDirectoryServicePolicyRequiresFactors(int requiredFactors) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity().setRequiredFactors(requiredFactors);
    }

    @When("^I set the Policy for the Current Directory Service$")
    public void iSetThePolicyForTheCurrentDirectoryService() throws Throwable {
        directoryServicePolicyManager.setServicePolicyForCurrentService();
    }

    @When("^the Directory Service Policy is (not set|set) to require inherence$")
    public void theDirectoryServicePolicyIsSetToRequireInherence(String switchString) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequireInherenceFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Directory Service Policy is (not set|set) to require knowledge$")
    public void theDirectoryServicePolicyIsSetToRequireKnowledge(String switchString) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequireKnowledgeFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Directory Service Policy is (not set|set) to require possession$")
    public void theDirectoryServicePolicyIsSetToRequirePossession(String switchString) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity()
                .setRequirePossessionFactor(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @When("^the Directory Service Policy is (not set|set) to require jail break protection$")
    public void theDirectoryServicePolicyIsSetToRequireJailBreakProtection(String switchString) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity()
                .setJailBreakProtectionEnabled(Utils.getBooleanFromBooleanTextSwitch(switchString));
    }

    @Then("^the Directory Service Policy has no requirement for number of factors$")
    public void theDirectoryServicePolicyRequiresFactory() throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getRequiredFactors(), is(nullValue()));
    }

    @Then("^the Directory Service Policy requires (\\d+) factors$")
    public void theDirectoryServicePolicyRequiresFactory(int numberOfFactors) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getRequiredFactors(),
                is(equalTo(numberOfFactors)));
    }

    @Then("^the Directory Service Policy (does not|does|has no) require(?:ment for)? inherence$")
    public void theDirectoryServicePolicyDoesNotRequireInherence(String switchString) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getRequireInherenceFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Directory Service Policy (does not|does|has no) require(?:ment for)? knowledge$")
    public void theDirectoryServicePolicyDoesNotRequireKnowledge(String switchString) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getRequireKnowledgeFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Directory Service Policy (does not|does|has no) require(?:ment for)? possession$")
    public void theDirectoryServicePolicyDoesNotRequirePossession(String switchString) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getRequirePossessionFactor(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Then("^the Directory Service Policy has (\\d+) locations$")
    public void theDirectoryServicePolicyHasLocations(int numberOfLocations) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getLocations(),
                hasSize(numberOfLocations));
    }

    @Then("^the Directory Service Policy has (\\d+) time fences$")
    public void theDirectoryServicePolicyHasTimeFences(int numberOfFences) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences(),
                hasSize(numberOfFences));
    }

    @Then("^the Directory Service Policy (does not|does|has no) require(?:ment for)? jail break protection$")
    public void theDirectoryServicePolicyDoesNotRequireJailBreakProtection(String switchString) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getJailBreakProtectionEnabled(),
                is(Utils.getBooleanFromBooleanTextSwitch(switchString)));
    }

    @Given("^the Directory Service Policy is set to have the following Time Fences:$")
    public void theDirectoryServicePolicyIsSetToHaveTheFollowingTimeFences(DataTable dataTable) throws Throwable {

        directoryServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences().addAll(TimeFenceListConverter.fromDataTable(dataTable));
    }

    @Given("^the Directory Service Policy has the following Time Fences:$")
    public void theDirectoryServicePolicyHasTheFollowingTimeFences(DataTable dataTable) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getTimeFences(), is(equalTo(TimeFenceListConverter.fromDataTable(dataTable))));
    }

    @Given("^the Directory Service Policy is set to have the following Geofence locations:$")
    public void theDirectoryServicePolicyIsSetToHaveTheFollowingGeofenceLocations(DataTable dataTable) throws Throwable {
        directoryServicePolicyManager.getCurrentServicePolicyEntity().getLocations().addAll(LocationListConverter.fromDataTable(dataTable));
    }

    @Given("^the Directory Service Policy has the following Geofence locations:$")
    public void theDirectoryServicePolicyHasTheFollowingGeofenceLocations(DataTable dataTable) throws Throwable {
        assertThat(directoryServicePolicyManager.getCurrentServicePolicyEntity().getLocations(), is(equalTo(LocationListConverter.fromDataTable(dataTable))));
    }

    @When("^I create a new MethodAmountPolicy$")
    public void iCreateNewMethodAmountPolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(new MethodAmountPolicy());
    }

    @When("^I create a new Factors Policy$")
    public void iCreateNewFactorsPolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(new FactorsPolicy());
    }

    @And("^I add the following GeoCircleFence items$")
    public void iAddGeoCircleFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<GeoCircleFence> fencesFromFeatureFile = (GeoCircleFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        directoryServicePolicyManager.currentPolicyContext.addFences(fences);
    }

    @And("^I add the following TerritoryFence items$")
    public void iAddTerritoryFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<TerritoryFence> fencesFromFeatureFile = (TerritoryFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        directoryServicePolicyManager.currentPolicyContext.addFences(fences);
    }

    @And("I set the Policy for the Current Directory Service to the new policy")
    public void iSetPolicyForCurrentDirectory() throws Throwable {
        directoryServicePolicyManager.setPolicyForCurrentServiceToCurrentPolicyContext();
    }

    @Then("^the Directory Service Policy has \"([^\"]*)\" fence(?:s)?$")
    public void directoryServicePolicyHasAmountFences(String stringAmount) throws Throwable {
        int amount = Integer.getInteger(stringAmount);
        Policy policy = directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        assertThat(policy.getFences().size(), is(equalTo(amount)));
    }

    @And("^the Directory Service Policy contains the GeoCircleFence \"([^\"]*)\"$")
    public void directoryServicePolicyHasGeoCircleFenceNamed(String fenceName) throws Throwable {
        Policy policy = directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        boolean fenceNameMatches = false;
        for (Fence fence : policy.getFences()) {
            if (fence.getFenceName().equals(fenceName)) {
                fenceNameMatches = true;
                directoryServicePolicyManager.fenceCache.cachedFence = fence;
            }
        }
        assertThat(fenceNameMatches, is(equalTo(true)));
    }

    @And("^that fence has a latitude of \"([^\"]*)\"$")
    public void directoryServicePolicyHasGeoFenceWithLatitude(String latValueAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) directoryServicePolicyManager.fenceCache.cachedFence;
        Double latitude = Double.parseDouble(latValueAsString);
        assertThat(thatFence.getLatitude(), is(equalTo(latitude)));
    }

    @And("^that fence has a longitude of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasGeoFenceWithLongitude(String longValueAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) directoryServicePolicyManager.fenceCache.cachedFence;
        Double longitude = Double.parseDouble(longValueAsString);
        assertThat(thatFence.getLongitude(), is(equalTo(longitude)));
    }

    @And("^that fence has a radius of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasGeoFenceWithRadius(String radiusAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) directoryServicePolicyManager.fenceCache.cachedFence;
        Double radius = Double.parseDouble(radiusAsString);
        assertThat(thatFence.getRadius(), is(equalTo(radius)));
    }

    @And("^the Directory Service Policy contains the TerritoryFence \"([^\"]*)\"$")
    public void directoryServicePolicyHasTerritoryFenceNamed(String fenceName) throws Throwable {
        Policy policy = directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        boolean fenceNameMatches = false;
        for (Fence fence : policy.getFences()) {
            if (fence.getFenceName().equals(fenceName)) {
                fenceNameMatches = true;
                directoryServicePolicyManager.fenceCache.cachedFence = fence;
            }
        }
        assertThat(fenceNameMatches, is(equalTo(true)));
    }

    @And("^that fence has a country of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithCountryName(String countryName) {
        TerritoryFence thatFence = (TerritoryFence) directoryServicePolicyManager.fenceCache.cachedFence;
        assertThat(thatFence.getCountry(), is(equalTo(countryName)));
    }

    @And("^that fence has an administrative_area of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithAdminArea(String adminArea) {
        TerritoryFence thatFence = (TerritoryFence) directoryServicePolicyManager.fenceCache.cachedFence;
        assertThat(thatFence.getAdministrativeArea(), is(equalTo(adminArea)));
    }

    @And("^that fence has a postal_code of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithPostalCode(String postalCode) {
        TerritoryFence thatFence = (TerritoryFence) directoryServicePolicyManager.fenceCache.cachedFence;
        assertThat(thatFence.getPostalCode(), is(equalTo(postalCode)));
    }

    @Given("^the Directory Service is set to any Conditional Geofence Policy$")
    public void directoryServiceIsSetToAnyConditionalGeofencePolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(new ConditionalGeoFencePolicy());
        directoryServicePolicyManager.setPolicyForCurrentServiceToCurrentPolicyContext();
    }

    @When("^I set the inside Policy to a new Factors Policy$")
    public void setGeoFencePolicyInsidePolicyToFactorsPolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext.addInsidePolicy(new FactorsPolicy());
    }

    @When("^I set the inside Policy to a new MethodAmountPolicy$")
    public void setGeofencePolicyInsidePolicyToMethodAmountPolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext.addInsidePolicy(new MethodAmountPolicy());
    }

    @When("^I set the outside Policy to a new Factors Policy$")
    public void setGeofencePolicyOutsidePolicyToFactorsPolicy() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext.addOutsidePolicy(new FactorsPolicy());
    }

    @When("^I set the outside Policy to a new MethodAmount Policy$$")
    public void setGeofencePolicyOutsidePolicyToMethodAmountPolicy$() throws Throwable {
        directoryServicePolicyManager.currentPolicyContext.addOutsidePolicy(new MethodAmountPolicy());
    }

    @And("^I set the outside Policy factors to \"([^\"]*)\"$")
    public void setOutsidePolicyFactors(String policyFactor) throws Throwable {
        Factor factor = Factor.valueOf(policyFactor.toUpperCase());
        directoryServicePolicyManager.currentPolicyContext.addFactorToOutsidePolicy(factor);
    }

    @And("^I set the inside Policy amount to \"([^\"]*)\"$")
    public void setInsidePolicyAmountTo(String stringAmount) throws Throwable {
        int amount = Integer.getInteger(stringAmount);
        directoryServicePolicyManager.currentPolicyContext.setInsidePolicyAmount(amount);
    }

    @And("^I set the inside Policy factors to \"([^\"]*)\"$")
    public void setInsidePolicyFactors(String policyFactor) throws Throwable {
        Factor factor = Factor.valueOf(policyFactor.toUpperCase());
        directoryServicePolicyManager.currentPolicyContext.addFactorToInsidePolicy(factor);
    }

    @Then("^the inside Policy should be a MethodAmountPolicy")
    public void theInsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        Policy insidePolicy = currentPolicy.getInPolicy();
        assertTrue(insidePolicy instanceof MethodAmountPolicy);
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(insidePolicy);
    }

    @Then("^the inside Policy should be a FactorsPolicy$")
    public void theInsidePolicyIsFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        Policy insidePolicy = currentPolicy.getInPolicy();
        assertTrue(insidePolicy instanceof FactorsPolicy);
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(insidePolicy);
    }

    @Then("^the outside Policy should be a FactorsPolicy$")
    public void theOutsidePolicyisFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        Policy outsidePolicy = currentPolicy.getOutPolicy();
        assertTrue(outsidePolicy instanceof FactorsPolicy);
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(outsidePolicy);
    }

    @Then("^the outside Policy should be a MethodAmountPolicy$")
    public void theOutsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) directoryServicePolicyManager.getCurrentlySetDirectoryServicePolicy();
        Policy outsidePolicy = currentPolicy.getOutPolicy();
        assertTrue(outsidePolicy instanceof MethodAmountPolicy);
        directoryServicePolicyManager.currentPolicyContext = new MutablePolicy(outsidePolicy);
    }

    @And("^I set the outside Policy amount to \"([^\"]*)\"$")
    public void setOutsidePolicyAmountTo(String stringAmount) throws Throwable {
        int amount = Integer.getInteger(stringAmount);
        directoryServicePolicyManager.currentPolicyContext.setOutsidePolicyAmount(amount);
    }

    @And("^amount should be set to \"([^\"]*)\"$")
    public void currentPolicyContextAmountSetTo(String stringAmount) {
        int amount = Integer.getInteger(stringAmount);
        MethodAmountPolicy methodAmountPolicy = (MethodAmountPolicy) directoryServicePolicyManager.currentPolicyContext.toImmutablePolicy();
        assertEquals(methodAmountPolicy.getAmount(),amount);
    }

    @And("^factors should be set to \"([^\"]*)\"$")
    public void theInsidePolicyFactorsAre(String factorString) throws Throwable {
        Factor factor = Factor.valueOf(factorString.toUpperCase());
        List<Factor> expectedFactors = new ArrayList<>();
        expectedFactors.add(factor);
        FactorsPolicy factorsPolicy = (FactorsPolicy) directoryServicePolicyManager.currentPolicyContext.toImmutablePolicy();
        assertEquals(new HashSet<>(expectedFactors), new HashSet<>(factorsPolicy.getFactors()));
    }

    @And("^deny_rooted_jailbroken should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyRootedJailbrokenIsSetTo(String switchString) {
        Policy currentPolicyContext = directoryServicePolicyManager.currentPolicyContext.toImmutablePolicy();
        assertEquals((boolean)currentPolicyContext.getDenyRootedJailbroken(), Boolean.getBoolean(switchString));
    }

    @And("^deny_emulator_simulator should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyEmulatorSimulatorIsSetTo(String switchString) {
        Policy currentPolicyContext = directoryServicePolicyManager.currentPolicyContext.toImmutablePolicy();
        assertEquals((boolean)currentPolicyContext.getDenyEmulatorSimulator(), Boolean.getBoolean(switchString));
    }
}
