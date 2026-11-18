package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderConfirmationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By completeHeader = By.cssSelector("[data-test='complete-header']");
    private final By completeText   = By.cssSelector("[data-test='complete-text']");

    public OrderConfirmationPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public String getThankYouHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText().trim();
    }

    public String getDispatchMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeText)).getText().trim();
    }
}