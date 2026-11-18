package com.ejada.automation.steps;

import com.ejada.automation.base.DriverManager;
import com.ejada.automation.pages.LoginPage;
import com.ejada.automation.utils.JsonDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("the user navigates to the SauceDemo login page")
    public void userNavigatesToSauceDemoLoginPage() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.openLoginPage();
    }

    @When("the user attempts to login using dataset index {int} from the test data file")
    public void userAttemptsToLoginUsingDatasetIndex(int datasetIndex) {
        String username = JsonDataReader.getUsername(datasetIndex);
        String password = JsonDataReader.getPassword(datasetIndex);
        loginPage.login(username, password);
    }

    @Then("the appropriate error message for dataset index {int} should be displayed")
    public void appropriateErrorMessageShouldBeDisplayed(int datasetIndex) {
        String expectedError = JsonDataReader.getExpectedError(datasetIndex);
        String actualError = loginPage.getErrorMessageText();

        Assert.assertEquals(actualError, expectedError,
                "Error message mismatch for dataset index " + datasetIndex);
    }
}