package com.ejada.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/MobileLogin.feature",
        glue = {"com.ejada.automation.mobile.steps", "com.ejada.automation.mobile.hooks"},
        tags = "@Mobile",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/mobile-cucumber-report.html",
                "json:target/cucumber-reports/mobile-cucumber.json"
        },
        monochrome = true
)
public class MobileTestRunner extends AbstractTestNGCucumberTests {
}