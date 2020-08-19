package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.managers.DirectoryTotpManager;
import com.iovation.launchkey.sdk.integration.managers.ServiceTotpManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceTotpSteps {

    private final ServiceTotpManager serviceTotpManager;
    private final DirectoryTotpManager directoryTotpManager;
    private final GenericSteps genericSteps;

    @Inject
    public ServiceTotpSteps(ServiceTotpManager serviceTotpManager, DirectoryTotpManager directoryTotpManager, GenericSteps genericSteps) {
        this.serviceTotpManager = serviceTotpManager;
        this.directoryTotpManager = directoryTotpManager;
        this.genericSteps = genericSteps;
    }

    @When("I verify a TOTP code with a valid code")
    public void iVerifyATOTPCodeWithAValidCode() throws Throwable {
        String userId = directoryTotpManager.getCurrentUserId();
        String totpCode = directoryTotpManager.getCodeForCurrentUserTotpResponse();
        serviceTotpManager.verifyUserTotpCode(userId, totpCode);
    }

    @When("I verify a TOTP code with an invalid code")
    public void iVerifyATOaTPCodeWithAnInvalidCode() throws Throwable {
        String userId = directoryTotpManager.getCurrentUserId();
        String totpCode = "notvalid";
        serviceTotpManager.verifyUserTotpCode(userId, totpCode);
    }


    @When("I attempt to verify a TOTP code with an invalid User")
    public void iVerifyATOTPCodeWithAnInvalidUser() throws Throwable {
        String totpCode = directoryTotpManager.getCodeForCurrentUserTotpResponse();
        try{
            serviceTotpManager.verifyUserTotpCode("not a valid user id", totpCode);
        }catch(Exception e){
            genericSteps.setCurrentException(e);
        }
    }

    @Then("the TOTP verification response is True")
    public void theTOTPVerificationResponseIsTrue() {
        assertTrue(serviceTotpManager.getCurrentVerifyUserTotpResponse());
    }

    @Then("the TOTP verification response is False")
    public void theTOTPVerificationResponseIsFalse() {
        assertFalse(serviceTotpManager.getCurrentVerifyUserTotpResponse());
    }

}
