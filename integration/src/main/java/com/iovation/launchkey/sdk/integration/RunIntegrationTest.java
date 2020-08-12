package com.iovation.launchkey.sdk.integration;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-htmlreport","json:target/cucumber-report.json"},
        features = {"classpath:features"},
        glue = {"classpath:com.iovation.launchkey.sdk.integration"},
        objectFactory = CucumberGuiceObjectFactory.class
)
public class RunIntegrationTest {
}
