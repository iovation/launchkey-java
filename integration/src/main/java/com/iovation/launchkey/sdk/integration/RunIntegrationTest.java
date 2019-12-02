package com.iovation.launchkey.sdk.integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-htmlreport","json:target/cucumber-report.json"},
        features = {"classpath:features"},
        glue = {"classpath:com.iovation.launchkey.sdk.integration"}
)
public class RunIntegrationTest {
}
