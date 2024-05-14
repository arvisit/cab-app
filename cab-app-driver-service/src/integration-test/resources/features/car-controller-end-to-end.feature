Feature: Car Controller End to End

  @e2e
  Scenario: Retrieving existing cars by default
    Given User wants to get details about existing cars
    When he performs request for cars with no request parameters
    Then response should have 200 status, json content type, contain info about 4 cars

  @e2e
  Scenario: Retrieving an existing car by id
    Given User wants to get details about an existing car with id '6810330b-769e-4278-b050-f66371e2ee52'
    When he performs search car by id via request
    Then response should have 200 status, json content type, contain car with requested id
