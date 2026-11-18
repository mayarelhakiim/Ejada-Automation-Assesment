package com.ejada.automation.steps;

import com.ejada.automation.base.DriverManager;
import com.ejada.automation.pages.CartPage;
import com.ejada.automation.pages.CheckoutPage;
import com.ejada.automation.pages.LoginPage;
import com.ejada.automation.pages.OrderConfirmationPage;
import com.ejada.automation.pages.OverviewPage;
import com.ejada.automation.pages.ProductsPage;
import com.ejada.automation.utils.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class PurchaseSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private OverviewPage overviewPage;
    private OrderConfirmationPage confirmationPage;

    private List<String> selectedProducts;
    private final double[] expectedPriceSum = new double[1];

    @When("the user logs in with valid credentials")
    public void userLogsInWithValidCredentials() {
        loginPage = new LoginPage(DriverManager.getDriver());
        String username = ConfigReader.getProperty("validUsername");
        String password = ConfigReader.getProperty("validPassword");
        loginPage.login(username, password);
    }

    @Then("the user should be logged in successfully and navigated to the Products page")
    public void userShouldBeNavigatedToProductsPage() {
        productsPage = new ProductsPage(DriverManager.getDriver());
        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                "User was not navigated to the Products page after valid login.");
    }

    @When("the user adds the two most expensive products to the cart")
    public void userAddsTwoMostExpensiveProductsToCart() {
        selectedProducts = productsPage.addTopTwoMostExpensiveProductsAndGetNames(expectedPriceSum);
        Assert.assertEquals(selectedProducts.size(), 2, "Expected 2 products to be selected.");
    }

    @And("the user clicks on the Cart button")
    public void userClicksOnCartButton() {
        productsPage.clickCartButton();
    }

    @Then("the user should be navigated to the Cart page and see the selected products")
    public void userShouldBeNavigatedToCartPageAndSeeSelectedProducts() {
        cartPage = new CartPage(DriverManager.getDriver());
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page was not displayed.");

        List<String> actualCartProducts = cartPage.getCartProductNames();
        Assert.assertEquals(actualCartProducts, selectedProducts,
                "Products in cart do not match the two most expensive products selected.");
    }

    @When("the user clicks on the Checkout button")
    public void userClicksOnCheckoutButton() {
        cartPage.clickCheckoutButton();
    }

    @Then("the user should be navigated to the Checkout page")
    public void userShouldBeNavigatedToCheckoutPage() {
        checkoutPage = new CheckoutPage(DriverManager.getDriver());
        Assert.assertTrue(checkoutPage.isCheckoutInfoPageDisplayed(),
                "Checkout information page was not displayed.");
    }

    @When("the user fills the checkout form with first name {string}, last name {string}, and postal code {string}")
    public void userFillsCheckoutForm(String firstName, String lastName, String postalCode) {
        checkoutPage.fillCustomerInformation(firstName, lastName, postalCode);
    }

    @And("the user clicks on the Continue button")
    public void userClicksOnContinueButton() {
        checkoutPage.clickContinueButton();
    }

    @Then("the user should be navigated to the Overview page")
    public void userShouldBeNavigatedToOverviewPage() {
        overviewPage = new OverviewPage(DriverManager.getDriver());
        Assert.assertTrue(overviewPage.isOverviewPageDisplayed(),
                "Checkout Overview page was not displayed.");
    }

    @And("the items total amount before taxes should match the sum of the selected products")
    public void itemsTotalBeforeTaxesShouldMatchSum() {
        double actualSubtotal = overviewPage.getDisplayedItemTotalBeforeTax();
        Assert.assertEquals(actualSubtotal, expectedPriceSum[0], 0.01,
                "Item total before taxes does not match the sum of the two products.");
    }

    @And("the current URL should match {string}")
    public void currentUrlShouldMatch(String expectedUrl) {
        Assert.assertEquals(overviewPage.getCurrentUrl(), expectedUrl,
                "Overview page URL mismatch.");
    }

    @When("the user clicks on the Finish button")
    public void userClicksOnFinishButton() {
        overviewPage.clickFinishButton();
    }

    @Then("both the Thank You message and the order has been dispatched message should be displayed")
    public void verifyOrderConfirmationMessages() {
        confirmationPage = new OrderConfirmationPage(DriverManager.getDriver());

        String header = confirmationPage.getThankYouHeader();
        String dispatchText = confirmationPage.getDispatchMessage();

        Assert.assertTrue(header.toLowerCase().contains("thank you"),
                "Expected 'Thank You' message, but found: " + header);
        Assert.assertTrue(dispatchText.toLowerCase().contains("order has been dispatched"),
                "Expected 'order has been dispatched' message, but found: " + dispatchText);
    }
}