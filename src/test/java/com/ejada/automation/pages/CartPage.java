package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle      = By.cssSelector("[data-test='title']");
    private final By cartItemNames  = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public boolean isCartPageDisplayed() {
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
        return title.equalsIgnoreCase("Your Cart") && driver.getCurrentUrl().contains("cart.html");
    }

    public List<String> getCartProductNames() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItemNames));
        return driver.findElements(cartItemNames)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public void clickCheckoutButton() {
        driver.findElement(checkoutButton).click();
    }
}