Feature: Directory Client can create TOTP for user
  In order to manage User TOTP
  As a Directory Client
  I can request a shared secret be generated and return the data about that secret

  Background:
    Given I created a Directory

  Scenario: Deleting TOTP succeeds
    When I make a User TOTP delete request
    Then there are no errors

