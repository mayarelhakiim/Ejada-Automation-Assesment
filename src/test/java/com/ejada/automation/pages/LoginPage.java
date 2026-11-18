package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators using stable IDs and data-test attributes
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public void openLoginPage() {
        driver.get(ConfigReader.getProperty("baseUrl"));
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

    public String getErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText().trim();
    }
}