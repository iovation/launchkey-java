package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.domain.DirectoryUserTotp;
import com.iovation.launchkey.sdk.integration.managers.DirectoryTotpManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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

    @Then("the User TOTP create response contains a valid algorithm")
    public void theUserTOTPCreateResponseContainsForTheAlgorithm() throws Throwable {
        String actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getAlgorithm();
        List<String> validAlgorithms = Arrays.asList("SHA1", "SHA256", "SHA512");
        assertTrue(validAlgorithms.contains(actual));
    }

    @Then("the User TOTP create response contains a valid amount of digits")
    public void theUserTOTPCreateResponseContainsAValidAmountOfDigits() throws Throwable {
        int actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getDigits();
        assertTrue(actual >= 6);
    }

    @Then("the User TOTP create response contains a valid period")
    public void theUserTOTPCreateResponseContainsForThePeriod() throws Throwable {
        int actual = directoryTotpManager.getCurrentGenerateUserTotpResponse().getPeriod();
        assertTrue(actual >= 30);
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
