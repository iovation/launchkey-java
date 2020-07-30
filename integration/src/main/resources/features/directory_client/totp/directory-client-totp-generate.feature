Feature: Directory Client can generate TOTP for user
  In order to manage User TOTP
  As a Directory Client
  I can request a shared secret be generated and return the data about that secret

  Background:
    Given I created a Directory

  Scenario: Creating TOTP returns the expected data
    When I make a User TOTP create request
    Then the User TOTP create response contains "SHA1" for the algorithm
    And the User TOTP create response contains 6 for the digits
    And the User TOTP create response contains 30 for the period
    And the User TOTP create response contains a valid secret

