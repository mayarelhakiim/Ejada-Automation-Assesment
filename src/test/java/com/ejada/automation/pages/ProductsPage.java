package com.ejada.automation.pages;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle        = By.cssSelector("[data-test='title']");
    private final By inventoryItems   = By.cssSelector("[data-test='inventory-item']");
    private final By itemName         = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice        = By.cssSelector("[data-test='inventory-item-price']");
    private final By addToCartButton  = By.tagName("button");
    private final By shoppingCartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public boolean isProductsPageDisplayed() {
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
        return title.equalsIgnoreCase("Products") && driver.getCurrentUrl().contains("inventory.html");
    }

    public List<String> addTopTwoMostExpensiveProductsAndGetNames(double[] expectedSumHolder) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        List<WebElement> items = driver.findElements(inventoryItems);

        // Sort product cards from highest price to lowest price
        List<WebElement> sortedItems = new ArrayList<>(items);
        sortedItems.sort(Comparator.comparingDouble(this::extractPrice).reversed());

        List<String> selectedNames = new ArrayList<>();
        double sum = 0.0;

        for (int i = 0; i < 2 && i < sortedItems.size(); i++) {
            WebElement product = sortedItems.get(i);
            String name = product.findElement(itemName).getText().trim();
            double price = extractPrice(product);

            product.findElement(addToCartButton).click();
            selectedNames.add(name);
            sum += price;
        }

        expectedSumHolder[0] = sum;
        return selectedNames;
    }

    private double extractPrice(WebElement productElement) {
        String priceText = productElement.findElement(itemPrice).getText().replace("$", "").trim();
        return Double.parseDouble(priceText);
    }

    public void clickCartButton() {
        driver.findElement(shoppingCartLink).click();
    }
}