Feature: Category Management
  As a user
  I want to manage expense categories
  So that I can organize my expenses by type and color

  Background:
    Given the application is running
    And I am logged in

  Scenario: List categories with color indicators
    Given the following categories exist:
      | name      | type       | color   |
      | Food      | MANDATORY  | #FF0000 |
      | Movies    | LEISURE    | #00FF00 |
    When I open Category Management
    Then I should see a category named "Food" with color "#FF0000"
    And I should see a category named "Movies" with color "#00FF00"
    And the type for "Food" should be "MANDATORY"
    And the type for "Movies" should be "LEISURE"

  Scenario: Create a new category
    When I open Category Management
    And I create a new category:
      | name      | type        | color   |
      | Savings   | INVESTMENTS | #0000FF |
    Then the category "Savings" should be visible in the list
    And its type should be "INVESTMENTS"

  Scenario: Edit an existing category
    Given a category "Rent" exists with type "MANDATORY" and color "#666666"
    When I edit the category "Rent" to:
      | name      | type       | color   |
      | Housing   | MANDATORY  | #333333 |
    Then the category "Housing" should be visible
    And the category "Rent" should not be visible
    And its color should be "#333333"

  Scenario: Delete a category with no expenses
    Given a category "Unused" exists with no expenses
    When I delete the category "Unused"
    Then the category "Unused" should be removed from the list

  Scenario: Cannot delete a category with existing expenses
    Given a category "Active" exists
    And it has 3 expenses recorded
    When I try to delete the category "Active"
    Then I should see an error message "Cannot delete category with existing expenses"
    And the category "Active" should still be in the list
