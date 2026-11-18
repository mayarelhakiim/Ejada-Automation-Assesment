Feature: SauceDemo End-to-End Purchase Flow

  @Scenario2 @PurchaseFlow
  Scenario: Complete an order for the two most expensive products and verify checkout details
    Given the user navigates to the SauceDemo login page
    When the user logs in with valid credentials
    Then the user should be logged in successfully and navigated to the Products page
    When the user adds the two most expensive products to the cart
    And the user clicks on the Cart button
    Then the user should be navigated to the Cart page and see the selected products
    When the user clicks on the Checkout button
    Then the user should be navigated to the Checkout page
    When the user fills the checkout form with first name "Mayar", last name "Elhakim", and postal code "11511"
    And the user clicks on the Continue button
    Then the user should be navigated to the Overview page
    And the items total amount before taxes should match the sum of the selected products
    And the current URL should match "https://www.saucedemo.com/checkout-step-two.html"
    When the user clicks on the Finish button
    Then both the Thank You message and the order has been dispatched message should be displayed