package com.ejada.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.ejada.automation.steps", "com.ejada.automation.hooks"},
        tags = "not @Mobile",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/web-cucumber-report.html",
                "json:target/cucumber-reports/web-cucumber.json"
        },
        monochrome = true
)
public class WebTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}