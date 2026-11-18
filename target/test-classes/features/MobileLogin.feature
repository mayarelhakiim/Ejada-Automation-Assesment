@Mobile
Feature: SauceLabs Mobile App Login Validation

  @MobilePositiveLogin
  Scenario: Verify successful login with valid credentials on the Android mobile app
    Given the SauceLabs mobile application is launched on the login screen
    When the mobile user logs in with username "standard_user" and password "secret_sauce"
    Then the mobile user should be logged in successfully and see the Products screen

  @MobileNegativeLogin
  Scenario: Verify error message when logging in with invalid credentials on the Android mobile app
    Given the SauceLabs mobile application is launched on the login screen
    When the mobile user logs in with username "locked_out_user" and password "secret_sauce"
    Then the mobile login error message "Sorry, this user has been locked out." should be displayed