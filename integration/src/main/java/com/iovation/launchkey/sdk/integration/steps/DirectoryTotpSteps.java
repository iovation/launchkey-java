package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.domain.DirectoryUserTotp;
import com.iovation.launchkey.sdk.integration.managers.DirectoryTotpManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DirectoryTotpSteps {
    private final DirectoryTotpManager directoryTotpManager;

    @Inject
    public DirectoryTotpSteps(DirectoryTotpManager directoryTotpManager) {
        this.directoryTotpManager = directoryTotpManager;
    }

    @Given("I have created a User TOTP")
    @When("I make a User TOTP create request")
    public void iMakeAUserTOTPCreateRequest() throws Throwable {
        directoryTotpManager.generateUserTotp();
    }

    @Then("the User TOTP create response contains {string} for the algorithm")
    public void theUserTOTPCreateResponseContainsForTheAlgorithm(String expected) throws Throwable {
        String actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getAlgorithm();
        assertEquals(expected, actual);
    }

    @Then("the User TOTP create response contains {int} for the digits")
    public void theUserTOTPCreateResponseContainsForTheDigits(int expected) throws Throwable {
        int actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getDigits();
        assertEquals(expected, actual);
    }

    @Then("the User TOTP create response contains {int} for the period")
    public void theUserTOTPCreateResponseContainsForThePeriod(int expected) throws Throwable {
        int actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getPeriod();
        assertEquals(expected, actual);
    }

    @Then("the User TOTP create response contains a valid secret")
    public void theUserTOTPCreateResponseContainsAValidSecret() throws Throwable {
        DirectoryUserTotp response = directoryTotpManager.getCurrentGenerateUserTotpResponse();
        String actual = response.getSecret();
        assertNotNull(actual);
        assertEquals(32, actual.length());
    }

    @When("I make a User TOTP delete request")
    public void iMakeAUserTOTPDeleteRequest() throws Throwable {
        directoryTotpManager.removeTotpCodeForUser();
    }
}
