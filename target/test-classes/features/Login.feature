Feature: SauceDemo Login Validation

  @Scenario1 @InvalidLogin
  Scenario Outline: Verify error messages when logging in with invalid credentials from external data file
    Given the user navigates to the SauceDemo login page
    When the user attempts to login using dataset index <datasetIndex> from the test data file
    Then the appropriate error message for dataset index <datasetIndex> should be displayed

    Examples:
      | datasetIndex |
      | 0            |
      | 1            |
      | 2            |
      | 3            |