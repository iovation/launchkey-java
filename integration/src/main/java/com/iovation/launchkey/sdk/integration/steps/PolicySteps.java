package com.iovation.launchkey.sdk.integration.steps;

import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import com.iovation.launchkey.sdk.integration.cucumber.converters.GeoCircleFenceConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.TerritoryFenceConverter;
import com.iovation.launchkey.sdk.integration.managers.MutablePolicy;
import com.iovation.launchkey.sdk.integration.managers.PolicyContext;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Singleton
public class PolicySteps {
    private PolicyContext policyContext;

    @Inject
    public PolicySteps(PolicyContext policyContext) {
        this.policyContext = policyContext;
    }

    @When("^I create a new MethodAmountPolicy$")
    public void iCreateNewMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy = new MutablePolicy(new MethodAmountPolicy());
    }

    @When("^I create a new Factors Policy$")
    public void iCreateNewFactorsPolicy() throws Throwable {
        policyContext.currentPolicy = new MutablePolicy(new FactorsPolicy());
    }

    @And("^I add the following GeoCircleFence items$")
    public void iAddGeoCircleFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<GeoCircleFence> fencesFromFeatureFile = (GeoCircleFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        policyContext.currentPolicy.addFences(fences);
    }

    @And("^I add the following TerritoryFence items$")
    public void iAddTerritoryFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<TerritoryFence> fencesFromFeatureFile = (TerritoryFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        policyContext.currentPolicy.addFences(fences);
    }

    @And("^that fence has a country of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithCountryName(String countryName) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getCountry(), is(equalTo(countryName)));
    }

    @And("^that fence has an administrative_area of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithAdminArea(String adminArea) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getAdministrativeArea(), is(equalTo(adminArea)));
    }

    @And("^that fence has a postal_code of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithPostalCode(String postalCode) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getPostalCode(), is(equalTo(postalCode)));
    }

    @And("I set deny_rooted_jailbroken to {string}")
    public void iSetDeny_rooted_jailbrokenTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setDenyRootedJailBroken(Boolean.valueOf(arg0));
    }

    @And("I set deny_emulator_simulator to {string}")
    public void iSetDeny_emulator_simulatorTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setDenyEmulatorSimulator(Boolean.valueOf(arg0));
    }

    @When("I set the outside Policy to a new MethodAmountPolicy")
    public void iSetTheOutsidePolicyToANewMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new MethodAmountPolicy());
    }

    @And("I set the factors to {string}")
    public void iSetTheFactorsTo(String arg0) throws Throwable {
        String[] factorsAsStrings = arg0.split("\\s*,\\s*");
        List<String> factors = new ArrayList<>();
        for (String stringFactor : factorsAsStrings) {
            factors.add(stringFactor.toUpperCase());
        }
        policyContext.currentPolicy.setFactors(factors);
    }

    @And("I set the amount to {string}")
    public void iSetTheAmountTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setAmount(Integer.parseInt(arg0));
    }

    @And("^I set the outside Policy amount to \"([^\"]*)\"$")
    public void setOutsidePolicyAmountTo(String stringAmount) throws Throwable {
        int amount = Integer.getInteger(stringAmount);
        policyContext.currentPolicy.setOutsidePolicyAmount(amount);
    }

    @When("^I set the inside Policy to a new Factors Policy$")
    public void setGeoFencePolicyInsidePolicyToFactorsPolicy() throws Throwable {
        policyContext.currentPolicy.addInsidePolicy(new FactorsPolicy());
    }

    @When("^I set the inside Policy to a new MethodAmountPolicy$")
    public void setGeofencePolicyInsidePolicyToMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy.addInsidePolicy(new MethodAmountPolicy());
    }

    @When("^I set the outside Policy to a new Factors Policy$")
    public void setGeofencePolicyOutsidePolicyToFactorsPolicy() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new FactorsPolicy());
    }

    @When("^I set the outside Policy to a new MethodAmount Policy$$")
    public void setGeofencePolicyOutsidePolicyToMethodAmountPolicy$() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new MethodAmountPolicy());
    }

    @And("^I set the outside Policy factors to \"([^\"]*)\"$")
    public void setOutsidePolicyFactors(String policyFactor) throws Throwable {
        policyContext.currentPolicy.addFactorToOutsidePolicy(policyFactor.toUpperCase());
    }

    @And("^I set the inside Policy amount to \"([^\"]*)\"$")
    public void setInsidePolicyAmountTo(String stringAmount) throws Throwable {
        int amount = Integer.getInteger(stringAmount);
        policyContext.currentPolicy.setInsidePolicyAmount(amount);
    }

    @And("^I set the inside Policy factors to \"([^\"]*)\"$")
    public void setInsidePolicyFactors(String policyFactor) throws Throwable {
        policyContext.currentPolicy.addFactorToInsidePolicy(policyFactor.toUpperCase());
    }

    @And("^that fence has a latitude of \"([^\"]*)\"$")
    public void directoryServicePolicyHasGeoFenceWithLatitude(String latValueAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        Double latitude = Double.parseDouble(latValueAsString);
        assertThat(thatFence.getLatitude(), is(equalTo(latitude)));
    }

    @And("^that fence has a longitude of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasGeoFenceWithLongitude(String longValueAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        Double longitude = Double.parseDouble(longValueAsString);
        assertThat(thatFence.getLongitude(), is(equalTo(longitude)));
    }

    @And("^that fence has a radius of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasGeoFenceWithRadius(String radiusAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        Double radius = Double.parseDouble(radiusAsString);
        assertThat(thatFence.getRadius(), is(equalTo(radius)));
    }

    @Then("^the inside Policy should be a MethodAmountPolicy")
    public void theInsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy insidePolicy = currentPolicy.getInPolicy();
        assertTrue(insidePolicy instanceof MethodAmountPolicy);
        policyContext.currentPolicy = new MutablePolicy(insidePolicy);
    }

    @Then("^the inside Policy should be a FactorsPolicy$")
    public void theInsidePolicyIsFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy insidePolicy = currentPolicy.getInPolicy();
        assertTrue(insidePolicy instanceof FactorsPolicy);
        policyContext.currentPolicy = new MutablePolicy(insidePolicy);
    }

    @Then("^the outside Policy should be a FactorsPolicy$")
    public void theOutsidePolicyisFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy outsidePolicy = currentPolicy.getOutPolicy();
        assertTrue(outsidePolicy instanceof FactorsPolicy);
        policyContext.currentPolicy = new MutablePolicy(outsidePolicy);
    }

    @Then("^the outside Policy should be a MethodAmountPolicy$")
    public void theOutsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy outsidePolicy = currentPolicy.getOutPolicy();
        assertTrue(outsidePolicy instanceof MethodAmountPolicy);
        policyContext.currentPolicy = new MutablePolicy(outsidePolicy);
    }

    @And("^amount should be set to \"([^\"]*)\"$")
    public void currentPolicyContextAmountSetTo(String stringAmount) {
        int amount = Integer.getInteger(stringAmount);
        MethodAmountPolicy methodAmountPolicy = (MethodAmountPolicy) policyContext.currentPolicy.toImmutablePolicy();
        assertEquals(methodAmountPolicy.getAmount(),amount);
    }

    @And("^factors should be set to \"([^\"]*)\"$")
    public void theInsidePolicyFactorsAre(String factorString) throws Throwable {
        String[] factorsAsStrings = factorString.split("\\s*,\\s*");
        List<String> expectedFactors = new ArrayList<>();
        for (String stringFactor : factorsAsStrings) {
            expectedFactors.add(stringFactor.toUpperCase());
        }
        boolean inherence = false;
        boolean possession = false;
        boolean knowledge = false;
        for (String factor : expectedFactors) {
            if (factor.equals("KNOWLEDGE")) {
                knowledge = true;
            }
            if (factor.equals("INHERENCE")) {
                inherence = true;
            }
            if (factor.equals("POSSESSION")) {
                possession = true;
            }
        }
        FactorsPolicy factorsPolicy = (FactorsPolicy) policyContext.currentPolicy.toImmutablePolicy();
        assertEquals(inherence,factorsPolicy.isInherenceRequired());
        assertEquals(possession,factorsPolicy.isPossessionRequired());
        assertEquals(knowledge,factorsPolicy.isKnowledgeRequired());
    }

    @And("^deny_rooted_jailbroken should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyRootedJailbrokenIsSetTo(String switchString) {
        Policy currentPolicyContext = policyContext.currentPolicy.toImmutablePolicy();
        assertEquals((boolean)currentPolicyContext.getDenyRootedJailbroken(), Boolean.getBoolean(switchString));
    }

    @And("^deny_emulator_simulator should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyEmulatorSimulatorIsSetTo(String switchString) {
        Policy currentPolicyContext = policyContext.currentPolicy.toImmutablePolicy();
        assertEquals((boolean)currentPolicyContext.getDenyEmulatorSimulator(), Boolean.getBoolean(switchString));
    }

    @Then("the amount should be set to {string}")
    public void theAmountShouldBeSetTo(String arg0) throws Throwable {
        MethodAmountPolicy policy = (MethodAmountPolicy) policyContext.currentPolicy.toImmutablePolicy();
        int amount = Integer.parseInt(arg0);
        assertEquals(policy.getAmount(),amount);
    }

    @And("I attempt to create a new Conditional Geofence Policy with the inside policy set to the new policy")
    public void iAttemptToCreateANewConditionalGeofencePolicyWithTheInsidePolicySetToTheNewPolicy() throws InvalidPolicyAttributes {
        Policy newPolicy = policyContext.currentPolicy.toImmutablePolicy();
        ConditionalGeoFencePolicy condGeoPolicy = new ConditionalGeoFencePolicy(true,true,null,newPolicy,null);
    }

    @And("I attempt to create a new Conditional Geofence Policy with the outside policy set to the new policy")
    public void iAttemptToCreateANewConditionalGeofencePolicyWithTheOutsidePolicySetToTheNewPolicy() throws InvalidPolicyAttributes {
        Policy newPolicy = policyContext.currentPolicy.toImmutablePolicy();
        ConditionalGeoFencePolicy condGeoPolicy = new ConditionalGeoFencePolicy(true,true,null,null,newPolicy);
    }

    @When("I attempt to set the inside policy to any Conditional Geofence Policy")
    public void iAttemptToSetTheInsidePolicyToAnyConditionalGeofencePolicy() throws InvalidPolicyAttributes {
        Policy currentPolicy = policyContext.currentPolicy.toImmutablePolicy();
        ConditionalGeoFencePolicy condGeoPolicy = new ConditionalGeoFencePolicy(true,true,null,new ConditionalGeoFencePolicy(),null);
    }

    @Given("I have any Conditional Geofence Policy")
    public void iHaveAnyConditionalGeofencePolicy() {
        policyContext.setCurrentPolicy(new ConditionalGeoFencePolicy());
    }

    @And("I set the factors to {factors}")
    public void iSetTheFactorsToFactors(List<String> factors) throws Throwable {
        policyContext.currentPolicy.setFactors(factors);
    }

    @Then("factors should be set to {factors}")
    public void factorsShouldBeSetToFactors(List<String> factors) {
        FactorsPolicy policy = (FactorsPolicy) policyContext.currentPolicy.toImmutablePolicy();
        boolean inherence = false;
        boolean possession = false;
        boolean knowledge = false;
        for (String factor : factors) {
            if (factor.equals("KNOWLEDGE")) {
                knowledge = true;
            }
            if (factor.equals("INHERENCE")) {
                inherence = true;
            }
            if (factor.equals("POSSESSION")) {
                possession = true;
            }
        }
        assertEquals(policy.isInherenceRequired(),inherence);
        assertEquals(policy.isKnowledgeRequired(), knowledge);
        assertEquals(policy.isPossessionRequired(), possession);
    }

    @And("I set deny_rooted_jailbroken on the Policy to {value}")
    public void iSetDenyRootedJailbrokenOnThePolicyToValue(Boolean value) throws Throwable {
        policyContext.currentPolicy.setDenyRootedJailBroken(value);
    }

    @And("I set deny_emulator_simulator on the Policy to {value}")
    public void iSetDenyEmulatorSimulatorOnThePolicyToValue(Boolean value) throws Throwable {
        policyContext.currentPolicy.setDenyEmulatorSimulator(value);
    }

}
