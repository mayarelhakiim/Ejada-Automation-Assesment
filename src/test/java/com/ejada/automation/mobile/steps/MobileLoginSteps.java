package com.ejada.automation.mobile.steps;

import com.ejada.automation.mobile.base.MobileDriverManager;
import com.ejada.automation.mobile.pages.MobileLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class MobileLoginSteps {

    private MobileLoginPage mobileLoginPage;

    @Given("the SauceLabs mobile application is launched on the login screen")
    public void sauceLabsMobileApplicationIsLaunched() {
        mobileLoginPage = new MobileLoginPage(MobileDriverManager.getDriver());
        Assert.assertTrue(mobileLoginPage.isLoginScreenDisplayed(),
                "Mobile login screen was not displayed upon launch.");
    }

    @When("the mobile user logs in with username {string} and password {string}")
    public void mobileUserLogsInWithCredentials(String username, String password) {
        mobileLoginPage.login(username, password);
    }

    @Then("the mobile user should be logged in successfully and see the Products screen")
    public void mobileUserShouldBeLoggedInSuccessfully() {
        Assert.assertTrue(mobileLoginPage.isProductsScreenDisplayed(),
                "Products screen was not displayed after valid mobile login.");
    }

    @Then("the mobile login error message {string} should be displayed")
    public void mobileLoginErrorMessageShouldBeDisplayed(String expectedError) {
        String actualError = mobileLoginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError,
                "Mobile login error message mismatch.");
    }
}