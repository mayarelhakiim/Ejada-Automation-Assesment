package com.ejada.automation.mobile.pages;

import com.ejada.automation.utils.ConfigReader;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MobileLoginPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput    = AppiumBy.accessibilityId("test-Username");
    private final By passwordInput    = AppiumBy.accessibilityId("test-Password");
    private final By loginButton      = AppiumBy.accessibilityId("test-LOGIN");
    private final By productsHeader   = AppiumBy.accessibilityId("test-PRODUCTS");
    private final By errorMessageText = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView");

    public MobileLoginPage(AndroidDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public boolean isLoginScreenDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).isDisplayed();
    }

    public void login(String username, String password) {
        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        userField.clear();
        userField.sendKeys(username);

        WebElement passField = driver.findElement(passwordInput);
        passField.clear();
        passField.sendKeys(password);

        driver.findElement(loginButton).click();
    }

    public boolean isProductsScreenDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productsHeader)).isDisplayed();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageText)).getText().trim();
    }
}