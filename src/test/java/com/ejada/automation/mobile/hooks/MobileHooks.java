package com.ejada.automation.mobile.hooks;

import com.ejada.automation.mobile.base.MobileDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class MobileHooks {

    @Before("@Mobile")
    public void setUpMobile() {
        MobileDriverManager.initDriver();
    }

    @After("@Mobile")
    public void tearDownMobile(Scenario scenario) {
        if (scenario.isFailed() && MobileDriverManager.getDriver() != null) {
            byte[] screenshot = ((TakesScreenshot) MobileDriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Mobile Failure Screenshot");
        }
        MobileDriverManager.quitDriver();
    }
}