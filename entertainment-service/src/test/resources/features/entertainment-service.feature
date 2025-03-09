@APITesting
Feature: Entertainment Service

  Scenario: Get entertainment options with valid request
    Given the entertainment service is running
    When I request entertainment options with search text "Test"
    Then I should receive a sorted list of entertainment options with status code 200

  Scenario: Get entertainment options with invalid request
    Given the entertainment service is running
    When I request entertainment options with search text "Test@!"
    Then I should receive an error response with status code 400

  Scenario: Get entertainment options with no search text
    Given the entertainment service is running
    When I request entertainment options with search text ""
    Then I should receive an error response with status code 400