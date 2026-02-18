Feature: Recurring Expense Creation
  As a user
  I want to create recurring expenses
  So that I don't have to manually enter the same expense multiple times

  Background:
    Given I am on the AddExpenseForm
    And a category exists with name "Utilities"

  Scenario: Toggle recurring checkbox
    When I check the "IsRecurring" checkbox
    Then the frequency dropdown should appear
    And the end date field should appear

  Scenario: Create monthly recurring expense
    When I enter expense amount "100"
    And I select category "Utilities"
    And I check the "IsRecurring" checkbox
    And I select frequency "MONTHLY"
    And I set end date to "2026-12-31"
    And I submit the form
    Then the recurring expense should be created
    And the submission should go to /api/recurring endpoint

  Scenario: Create yearly recurring expense
    When I enter expense amount "1200"
    And I select category "Utilities"
    And I check the "IsRecurring" checkbox
    And I select frequency "YEARLY"
    And I submit the form
    Then the recurring expense should be created with frequency YEARLY

  Scenario: Cannot submit recurring expense without frequency
    When I check the "IsRecurring" checkbox
    And I enter expense amount "100"
    And I select category "Utilities"
    And I don't select a frequency
    And I click submit
    Then the form should show a validation error
    And the recurring expense should not be created

  Scenario: One-time expense submits to correct endpoint
    When I enter expense amount "50"
    And I select category "Utilities"
    And I don't check the recurring checkbox
    And I submit the form
    Then the submission should go to /api/expense endpoint
    And the expense should be created immediately
