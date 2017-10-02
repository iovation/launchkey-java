Feature: Service Client Authorization Request
  In order to begin an authorization request
  As a Directory Service
  I can create an Authorization Request for a User

  # As we cannot link devices to properly attempt authorization requests, we will simply verify that the
  # requests are formatted properly and can be accepted by the API

  Background:
    Given I created a Directory
    And I created a Directory Service

  Scenario: Making a request with a valid User an no linked Devices raises EntityNotFound
    Given I made a Device linking request
    When I attempt to make an Authorization request
    Then a com.iovation.launchkey.sdk.error.EntityNotFound exception is thrown

  Scenario: Making a request with an invalid User Throws EntityNotFound
    When I attempt to make an Authorization request for the User identified by "Not a valid user"
    Then a com.iovation.launchkey.sdk.error.EntityNotFound exception is thrown

  Scenario: Making a request including context with an invalid User Throws EntityNotFound
    When I attempt to make an Authorization request with the context value "Hello iovation!"
    Then a com.iovation.launchkey.sdk.error.EntityNotFound exception is thrown
