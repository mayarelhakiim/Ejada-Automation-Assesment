package com.ejada.automation.hooks;

import com.ejada.automation.base.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before("not @Mobile")
    public void setUp() {
        DriverManager.initDriver();
    }

    @After("not @Mobile")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.getDriver() != null) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
        DriverManager.quitDriver();
    }
}