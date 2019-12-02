package com.iovation.launchkey.sdk.integration.steps;

import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import com.iovation.launchkey.sdk.integration.cucumber.converters.GeoCircleFenceConverter;
import com.iovation.launchkey.sdk.integration.cucumber.converters.TerritoryFenceConverter;
import com.iovation.launchkey.sdk.integration.managers.MutablePolicy;
import com.iovation.launchkey.sdk.integration.managers.PolicyContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@Singleton
public class PolicySteps {
    private final PolicyContext policyContext;
    private final GenericSteps genericSteps;

    @Inject
    public PolicySteps(PolicyContext policyContext, GenericSteps genericSteps) {
        this.policyContext = policyContext;
        this.genericSteps = genericSteps;
    }

    @When("^I create a new MethodAmountPolicy$")
    public void iCreateNewMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy = new MutablePolicy(new MethodAmountPolicy(false, false, null, 0));
    }

    @When("^I create a new Factors Policy$")
    public void iCreateNewFactorsPolicy() throws Throwable {
        policyContext.currentPolicy = new MutablePolicy(new FactorsPolicy(false, false, null, true, true, true));
    }

    @When("^I add the following GeoCircleFence items$")
    public void iAddGeoCircleFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<GeoCircleFence> fencesFromFeatureFile = (GeoCircleFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        policyContext.currentPolicy.addFences(fences);
    }

    @When("^I add the following TerritoryFence items$")
    public void iAddTerritoryFenceToCurrentPolicy(DataTable dataTable) throws Throwable {
        List<TerritoryFence> fencesFromFeatureFile = (TerritoryFenceConverter.fromDataTable(dataTable));
        List<Fence> fences = new ArrayList<>(fencesFromFeatureFile);
        policyContext.currentPolicy.addFences(fences);
    }

    @Then("^that fence has a country of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithCountryName(String countryName) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getCountry(), is(equalTo(countryName)));
    }

    @Then("^that fence has an administrative_area of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithAdminArea(String adminArea) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getAdministrativeArea(), is(equalTo(adminArea)));
    }

    @Then("^that fence has a postal_code of \"([^\"]*)\"$")
    public void directoryServicePolicyManagerHasTerritoryFenceWithPostalCode(String postalCode) {
        TerritoryFence thatFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(thatFence.getPostalCode(), is(equalTo(postalCode)));
    }

    @When("I set deny_rooted_jailbroken to {string}")
    public void iSetDeny_rooted_jailbrokenTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setDenyRootedJailBroken(Boolean.valueOf(arg0));
    }

    @When("I set deny_emulator_simulator to {string}")
    public void iSetDeny_emulator_simulatorTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setDenyEmulatorSimulator(Boolean.valueOf(arg0));
    }

    @When("I set the outside Policy to a new MethodAmountPolicy")
    public void iSetTheOutsidePolicyToANewMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new MethodAmountPolicy(false, false, null, 0));
    }

    @When("I set the factors to {string}")
    public void iSetTheFactorsTo(String arg0) throws Throwable {
        String[] factorsAsStrings = arg0.split("\\s*,\\s*");
        List<String> factors = new ArrayList<>();
        for (String stringFactor : factorsAsStrings) {
            factors.add(stringFactor.toUpperCase());
        }
        policyContext.currentPolicy.setFactors(factors);
    }

    @When("I set the amount to {string}")
    public void iSetTheAmountTo(String arg0) throws Throwable {
        policyContext.currentPolicy.setAmount(Integer.parseInt(arg0));
    }

    @When("^I set the outside Policy amount to \"(\\d+)\"$")
    public void setOutsidePolicyAmountTo(int amount) throws Throwable {
        policyContext.currentPolicy.setOutsidePolicyAmount(amount);
    }

    @When("^I set the inside Policy to a new Factors Policy$")
    public void setGeoFencePolicyInsidePolicyToFactorsPolicy() throws Throwable {
        policyContext.currentPolicy.addInsidePolicy(new FactorsPolicy(false, false, null, false, false, false));
    }

    @When("^I set the inside Policy to a new MethodAmountPolicy$")
    public void setGeofencePolicyInsidePolicyToMethodAmountPolicy() throws Throwable {
        policyContext.currentPolicy.addInsidePolicy(new MethodAmountPolicy(false, false, null, 0));
    }

    @When("^I set the outside Policy to a new Factors Policy$")
    public void setGeofencePolicyOutsidePolicyToFactorsPolicy() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new FactorsPolicy(false, false, null, false, false, false));
    }

    @When("^I set the outside Policy to a new MethodAmount Policy$$")
    public void setGeofencePolicyOutsidePolicyToMethodAmountPolicy$() throws Throwable {
        policyContext.currentPolicy.addOutsidePolicy(new MethodAmountPolicy(false, false, null, 0));
    }

    @When("^I set the outside Policy factors to \"([^\"]*)\"$")
    public void setOutsidePolicyFactors(String policyFactor) throws Throwable {
        policyContext.currentPolicy.addFactorToOutsidePolicy(policyFactor.toUpperCase());
    }

    @When("^I set the inside Policy amount to \"(\\d+)\"$")
    public void setInsidePolicyAmountTo(int amount) throws Throwable {
        policyContext.currentPolicy.setInsidePolicyAmount(amount);
    }

    @When("^I set the inside Policy factors to \"([^\"]*)\"$")
    public void setInsidePolicyFactors(String policyFactor) throws Throwable {
        policyContext.currentPolicy.addFactorToInsidePolicy(policyFactor.toUpperCase());
    }

    @Then("^that fence has a latitude of \"(-?\\d+\\.?\\d*)\"$")
    public void policyHasGeoFenceWithLatitude(double latitude) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        assertThat(thatFence.getLatitude(), is(equalTo(latitude)));
    }

    @Then("^that fence has a longitude of \"(-?\\d+\\.?\\d*)\"$")
    public void policyManagerHasGeoFenceWithLongitude(String longValueAsString) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        Double longitude = Double.parseDouble(longValueAsString);
        assertThat(thatFence.getLongitude(), is(equalTo(longitude)));
    }

    @Then("^that fence has a radius of \"(\\d+)\"$")
    public void directoryServicePolicyManagerHasGeoFenceWithRadius(double radius) {
        GeoCircleFence thatFence = (GeoCircleFence) policyContext.fenceCache;
        assertThat(thatFence.getRadius(), is(equalTo(radius)));
    }

    @Then("^the inside Policy should be a MethodAmountPolicy")
    public void theInsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy insidePolicy = currentPolicy.getInside();
        assertTrue(insidePolicy instanceof MethodAmountPolicy);
        policyContext.currentPolicy = new MutablePolicy(insidePolicy);
    }

    @Then("^the inside Policy should be a FactorsPolicy$")
    public void theInsidePolicyIsFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy insidePolicy = currentPolicy.getInside();
        assertTrue(insidePolicy instanceof FactorsPolicy);
        policyContext.currentPolicy = new MutablePolicy(insidePolicy);
    }

    @Then("^the outside Policy should be a FactorsPolicy$")
    public void theOutsidePolicyIsFactorsPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy outsidePolicy = currentPolicy.getOutside();
        assertTrue(outsidePolicy instanceof FactorsPolicy);
        policyContext.currentPolicy = new MutablePolicy(outsidePolicy);
    }

    @Then("^the outside Policy should be a MethodAmountPolicy$")
    public void theOutsidePolicyIsMethodAmountPolicy() throws Throwable {
        ConditionalGeoFencePolicy currentPolicy = (ConditionalGeoFencePolicy) policyContext.currentPolicy.toImmutablePolicy();
        Policy outsidePolicy = currentPolicy.getOutside();
        assertTrue(outsidePolicy instanceof MethodAmountPolicy);
        policyContext.currentPolicy = new MutablePolicy(outsidePolicy);
    }

    @Then("^(the inside Policy |the outside Policy )?amount should be set to \"(\\d+)\"$")
    public void currentPolicyAmountSetTo(@SuppressWarnings("unused") String ignore, int amount) {
        Policy policy = policyContext.currentPolicy.toImmutablePolicy();
        assertThat(policy, is(instanceOf(MethodAmountPolicy.class)));
        MethodAmountPolicy methodAmountPolicy = (MethodAmountPolicy) policy;
        assertEquals(amount, methodAmountPolicy.getAmount());
    }

    @Then("^the \"([^\"]+)\" fence has a latitude of \"(-?\\d+\\.?\\d*)\"$")
    public void theFenceHasALatitudeOf(String fenceName, double latitude) throws Throwable {
        assertThat(policyContext.fenceCache, is(instanceOf(GeoCircleFence.class)));
        GeoCircleFence geoCircleFence = (GeoCircleFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(geoCircleFence.getName())));
        assertThat(latitude, is(equalTo(geoCircleFence.getLatitude())));
    }

    @Then("^the \"([^\"]+)\" fence has a longitude of \"(-?\\d+\\.?\\d*)\"$")
    public void theFenceHasALongitudeOf(String fenceName, double longitude) throws Throwable {
        assertThat(policyContext.fenceCache, is(instanceOf(GeoCircleFence.class)));
        GeoCircleFence geoCircleFence = (GeoCircleFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(geoCircleFence.getName())));
        assertThat(longitude, is(equalTo(geoCircleFence.getLongitude())));
    }

    @Then("^the \"([^\"]+)\" fence has a radius of \"(\\d+\\.?\\d*)\"$")
    public void theFenceHasARadiusOf(String fenceName, double radius) throws Throwable {
        assertThat(policyContext.fenceCache, is(instanceOf(GeoCircleFence.class)));
        GeoCircleFence geoCircleFence = (GeoCircleFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(geoCircleFence.getName())));
        assertThat(radius, is(equalTo(geoCircleFence.getRadius())));
    }

    @Then("the {string} fence has a country of {string}")
    public void theFenceHasACountryOf(String fenceName, String country) {
        assertThat(policyContext.fenceCache, is(instanceOf(TerritoryFence.class)));
        TerritoryFence territoryFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(territoryFence.getName())));
        assertThat(country, is(equalTo(territoryFence.getCountry())));
   }

    @Then("the {string} fence has an administrative_area of {string}")
    public void theFenceHasAnAdministrativeAreaOf(String fenceName, String adminArea) {
        assertThat(policyContext.fenceCache, is(instanceOf(TerritoryFence.class)));
        TerritoryFence territoryFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(territoryFence.getName())));
        assertThat(adminArea, is(equalTo(territoryFence.getAdministrativeArea())));
    }

    @Then("the {string} fence has a postal_code of {string}")
    public void theFenceHasAPostalCodeOf(String fenceName, String postalCode) {
        assertThat(policyContext.fenceCache, is(instanceOf(TerritoryFence.class)));
        TerritoryFence territoryFence = (TerritoryFence) policyContext.fenceCache;
        assertThat(fenceName, is(equalTo(territoryFence.getName())));
        assertThat(postalCode, is(equalTo(territoryFence.getPostalCode())));
    }

    @Then("^(the outside Policy |the inside Policy )?factors should be set to \"([^\"]*)\"$")
    public void currentPolicyFactorsAre(@SuppressWarnings("unused") String ignore, String factorString) throws Throwable {
        Policy policy = policyContext.currentPolicy.toImmutablePolicy();
        assertThat(policy, is(instanceOf(FactorsPolicy.class)));
        FactorsPolicy factorsPolicy = (FactorsPolicy) policy;
        String[] factorsAsStrings = factorString.split("\\s*,\\s*");
        List<String> expectedFactors = new ArrayList<>();
        for (String stringFactor : factorsAsStrings) {
            expectedFactors.add(stringFactor.toUpperCase());
        }
        boolean inherence = expectedFactors.contains("INHERENCE");
        boolean possession = expectedFactors.contains("POSSESSION");
        boolean knowledge = expectedFactors.contains("KNOWLEDGE");
        assertEquals(inherence, factorsPolicy.isInherenceRequired());
        assertEquals(possession, factorsPolicy.isPossessionRequired());
        assertEquals(knowledge, factorsPolicy.isKnowledgeRequired());
    }

    @Then("^deny_rooted_jailbroken should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyRootedJailbrokenIsSetTo(String switchString) {
        Policy currentPolicyContext = policyContext.currentPolicy.toImmutablePolicy();
        assertEquals(Boolean.valueOf(switchString).booleanValue(), currentPolicyContext.getDenyRootedJailbroken());
    }

    @Then("^deny_emulator_simulator should be set to \"([^\"]*)\"$")
    public void currentPolicyContextDenyEmulatorSimulatorIsSetTo(String switchString) {
        Policy currentPolicyContext = policyContext.currentPolicy.toImmutablePolicy();
        assertEquals(Boolean.valueOf(switchString).booleanValue(), currentPolicyContext.getDenyEmulatorSimulator());
    }

    @Then("^the amount should be set to \"(\\d+)\"$")
    public void theAmountShouldBeSetTo(int amount) throws Throwable {
        MethodAmountPolicy policy = (MethodAmountPolicy) policyContext.currentPolicy.toImmutablePolicy();
        assertEquals(amount, policy.getAmount());
    }

    @When("I attempt to create a new Conditional Geofence Policy with the inside policy set to the new policy")
    public void iAttemptToCreateANewConditionalGeofencePolicyWithTheInsidePolicySetToTheNewPolicy() throws InvalidPolicyAttributes {
        Policy newPolicy = policyContext.currentPolicy.toImmutablePolicy();
        try {
            new ConditionalGeoFencePolicy(true, true, null, newPolicy, null);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("I attempt to create a new Conditional Geofence Policy with the outside policy set to the new policy")
    public void iAttemptToCreateANewConditionalGeofencePolicyWithTheOutsidePolicySetToTheNewPolicy() throws InvalidPolicyAttributes {
        Policy newPolicy = policyContext.currentPolicy.toImmutablePolicy();
        try {
            new ConditionalGeoFencePolicy(true, true, null, null, newPolicy);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("I attempt to set the inside policy to any Conditional Geofence Policy")
    public void iAttemptToSetTheInsidePolicyToAnyConditionalGeofencePolicy() throws InvalidPolicyAttributes {
        Policy currentPolicy = policyContext.currentPolicy.toImmutablePolicy();
        try {
            new ConditionalGeoFencePolicy(true, true, null,
                    new ConditionalGeoFencePolicy(
                            false, false,
                            Arrays.asList(new TerritoryFence("US", "US", null, null)),
                            new MethodAmountPolicy(false, false, null, 0),
                            new MethodAmountPolicy(false, false, null, 0)
                    ),
                    new MethodAmountPolicy(false, false, null, 0));
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Given("I have any Conditional Geofence Policy")
    public void iHaveAnyConditionalGeofencePolicy() throws Exception {
        policyContext.setCurrentPolicy(new ConditionalGeoFencePolicy(false, false,
                Arrays.asList(new TerritoryFence("US", "US", null, null)),
                new MethodAmountPolicy(false, false, null, 0),
                new MethodAmountPolicy(false, false, null, 0)));
    }

    @When("I set the factors to {factors}")
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
        assertEquals(inherence, policy.isInherenceRequired());
        assertEquals(knowledge, policy.isKnowledgeRequired());
        assertEquals(possession, policy.isPossessionRequired());
    }

    @When("^I set deny_rooted_jailbroken on the (Factors |Method Amount )?Policy to (True|False)$")
    public void iSetDenyRootedJailbrokenOnThePolicyToValue(@SuppressWarnings("unused") String ignore, Boolean value) throws Throwable {
        policyContext.currentPolicy.setDenyRootedJailBroken(value);
    }

    @When("^I set deny_emulator_simulator on the (Factors |Method Amount )?Policy to (True|False)$")
    public void iSetDenyEmulatorSimulatorOnThePolicyToValue(@SuppressWarnings("unused") String ignore, Boolean value) throws Throwable {
        policyContext.currentPolicy.setDenyEmulatorSimulator(value);
    }

}
