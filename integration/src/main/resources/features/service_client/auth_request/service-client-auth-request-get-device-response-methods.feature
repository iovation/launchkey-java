@device_testing
Feature: Service Client Authorization Request: Get Device Response Methods
  In order to understand an auth response
  As a Directory Service
  I can retrieve an Authorization Requests that been responded to and determine
  the policy that was used

   Background:
    Given I created a Directory
    And I have added an SDK Key to the Directory
    And I created a Directory Service
    And I have a linked Device

   Scenario: Verify that an auth request with no policy contains the expected methods
    When I make an Authorization request
    And I approve the auth request
    And I get the response for the Authorization request
    Then the Authorization response should contain the following methods:
      | Method      | Set   | Active | Allowed | Supported  | User Required | Passed | Error |
      | wearables   | False | False  | True    | False      |               |        |       |
      | geofencing  |       | True   | True    | True       |               |        |       |
      | locations   | False | False  | True    | True       |               |        |       |
      | pin_code    | False | False  | True    | True       |               |        |       |
      | circle_code | False | False  | True    | True       |               |        |       |
      | face        | False | False  | True    | False      |               |        |       |
      | fingerprint | False | False  | True    | True       |               |        |       |