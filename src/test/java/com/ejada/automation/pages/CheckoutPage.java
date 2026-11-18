package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle       = By.cssSelector("[data-test='title']");
    private final By firstNameInput  = By.id("first-name");
    private final By lastNameInput   = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton  = By.id("continue");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public boolean isCheckoutInfoPageDisplayed() {
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
        return title.equalsIgnoreCase("Checkout: Your Information")
                && driver.getCurrentUrl().contains("checkout-step-one.html");
    }

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(postalCodeInput).sendKeys(postalCode);
    }

    public void clickContinueButton() {
        driver.findElement(continueButton).click();
    }
}