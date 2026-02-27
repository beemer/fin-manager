Feature: Recurring Expense Generation
  As a user
  I want my recurring expenses to generate expense instances
  So that I can see them in my monthly budget automatically

  Background:
    Given a category exists with name "Subscriptions"

  Scenario: Generate monthly instances for a new recurring expense
    Given a monthly recurring expense exists:
      | description | Netflix |
      | amount      | 15.99   |
      | startDate   | 2026-01-01 |
    When I trigger manual generation up to "2026-03-01"
    Then 3 expense instances should be created
    And the expenses for "2026-01" should include "Netflix"
    And the expenses for "2026-02" should include "Netflix"
    And the expenses for "2026-03" should include "Netflix"

  Scenario: Do not generate beyond end date
    Given a monthly recurring expense exists:
      | description | Gym |
      | amount      | 30.00 |
      | startDate   | 2026-01-01 |
      | endDate     | 2026-02-15 |
    When I trigger manual generation up to "2026-04-01"
    Then 2 expense instances should be created
    And the expenses for "2026-03" should not include "Gym"

  Scenario: Avoid duplicate generation (Idempotency)
    Given a monthly recurring expense exists:
      | description | Internet |
      | amount      | 50.00 |
      | startDate   | 2026-01-01 |
    When I trigger manual generation up to "2026-01-15"
    Then 1 expense instances should be created
    When I trigger manual generation up to "2026-01-31"
    Then 0 additional expense instances should be created

  Scenario: Generate yearly instances
    Given a yearly recurring expense exists:
      | description | Amazon Prime |
      | amount      | 139.00 |
      | startDate   | 2025-01-01 |
    When I trigger manual generation up to "2026-12-31"
    Then 2 expense instances should be created
    And the expenses for "2025-01" should include "Amazon Prime"
    And the expenses for "2026-01" should include "Amazon Prime"
