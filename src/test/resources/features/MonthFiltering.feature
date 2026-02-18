Feature: Month-Based Expense Filtering
  As a user
  I want to see expenses filtered by the selected month
  So that I can view my spending for a specific time period

  Background:
    Given the database has expenses from multiple months
    And I am on the Dashboard

  Scenario: View expenses for current month
    When I select the current month
    Then I should see only expenses from the current month
    And the total should be the sum of current month expenses

  Scenario: Filter expenses when changing months
    When I change to the previous month
    Then the expense list should update
    And I should see only expenses from the selected month

  Scenario: Empty month shows no expenses
    When I select a month with no expenses
    Then the expense list should be empty
    And the total expenses should be 0

  Scenario: API request includes month parameter
    When I select a specific month "2026-02"
    Then the API request should include query parameter month=2026-02
    And the response should contain expenses only from that month
