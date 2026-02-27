Feature: Analytics & Visualization
  As a user
  I want to see charts of my expenses
  So that I can understand my spending patterns

  Background:
    Given the application is running
    And the following categories exist:
      | name      | type      | color   |
      | Food      | LEISURE   | #FF5733 |
      | Rent      | MANDATORY | #33FF57 |
      | Transport | MANDATORY | #3357FF |

  Scenario: Category breakdown for a month
    Given the following expenses exist for "2026-02":
      | category | amount | description |
      | Food     | 100.0  | Groceries   |
      | Food     | 50.0   | Dinner      |
      | Rent     | 1000.0 | Monthly rent|
    When I view the analytics for "2026-02"
    Then the category breakdown should include:
      | category | total  | color   |
      | Food     | 150.0  | #FF5733 |
      | Rent     | 1000.0 | #33FF57 |
    And "Transport" should not be in the breakdown

  Scenario: Monthly spending trend
    Given the following expenses exist:
      | date       | category | amount |
      | 2026-01-15 | Food     | 200.0  |
      | 2026-02-10 | Rent     | 1000.0 |
      | 2026-02-20 | Food     | 100.0  |
    When I view the monthly trend for "2026"
    Then the trend should show for "1" the amount 200.0
    And the trend should show for "2" the amount 1100.0
