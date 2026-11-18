package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OverviewPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle     = By.cssSelector("[data-test='title']");
    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By finishButton  = By.id("finish");

    public OverviewPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public boolean isOverviewPageDisplayed() {
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
        return title.equalsIgnoreCase("Checkout: Overview");
    }

    public double getDisplayedItemTotalBeforeTax() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel)).getText();
        String numericPart = text.replace("Item total: $", "").trim();
        return Double.parseDouble(numericPart);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void clickFinishButton() {
        driver.findElement(finishButton).click();
    }
}