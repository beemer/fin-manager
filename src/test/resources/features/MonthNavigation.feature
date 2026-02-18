Feature: Month Navigation in Dashboard
  As a user
  I want to navigate between months
  So that I can view expenses for different time periods

  Scenario: Navigate to previous month
    Given I am on the Dashboard
    When I click the Previous button
    Then the displayed month should be the previous month

  Scenario: Navigate to next month
    Given I am on the Dashboard
    When I click the Next button
    Then the displayed month should be the next month

  Scenario: Navigate to today's month
    Given I am on the Dashboard
    And the current month is not displayed
    When I click the Today button
    Then the displayed month should be the current month

  Scenario: Month format is YYYY-MM
    Given I am on the Dashboard
    When I view the selected month
    Then the month should be displayed in YYYY-MM format
