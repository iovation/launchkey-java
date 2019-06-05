package com.iovation.launchkey.sdk.integration.cucumber;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-htmlreport","json:target/cucumber-report.json"},
        features = {
                "classpath:features/service_client/auth_request/service-client-auth-request-get-device-response-policy.feature",
                "classpath:features/service_client/auth_request/service-client-auth-request-get-device-response.feature",
                "classpath:features/service_client/auth_request/service-client-auth-request-get.feature"
        },
        glue = {"classpath:com.iovation.launchkey.sdk.integration"}
)
public class RunIntegrationTest {
}
