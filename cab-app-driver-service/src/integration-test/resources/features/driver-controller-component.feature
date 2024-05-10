Feature: Driver Controller Component

  @component
  Scenario: Saving a new driver
    Given User wants to save a new driver with name 'Jack Black', email 'jack.black@mail.com', card number '0000111122223333' and car details: color id 1, manufacturer id 1, registration number '0000AA-1'
    When he performs saving via request
    Then response should have 201 status, json content type, contain driver with expected parameters and id

  @component
  Scenario: Updating an existing driver
    Given User wants to update an existing driver with new values for name 'Jack Black', email 'jack.black@mail.com', card number '0000111122223333' and car details: color id 1, manufacturer id 1, registration number '0000AA-1'
    When he performs update of existing driver with id 'd1e04703-1af4-43ee-aae7-4ee06881ee59' via request
    Then response should have 200 status, json content type, contain driver with updated parameters

  @component
  Scenario: Deleting an existing driver
    Given User wants to delete an existing driver with id 'd1e04703-1af4-43ee-aae7-4ee06881ee59'
    When he performs delete of existing driver via request
    Then response should have 204 status, minus one driver in database

  @component
  Scenario: Retrieving an existing driver by id
    Given User wants to get details about an existing driver with id 'd1e04703-1af4-43ee-aae7-4ee06881ee59'
    When he performs search driver by id via request
    Then response should have 200 status, json content type, contain driver with requested id

  @component
  Scenario: Retrieving an existing driver by email
    Given User wants to get details about an existing driver with email 'john.doe@mail.com'
    When he performs search driver by email via request
    Then response should have 200 status, json content type, contain driver with requested email

  @component
  Scenario: Retrieving existing drivers by default
    Given User wants to get details about existing drivers
    When he performs request with no request parameters
    Then response should have 200 status, json content type, contain info about 4 drivers

  @component
  Scenario: Retrieving existing drivers filtered by name and email
    Given User wants to get details about existing drivers
    When he performs request with parameters: 'name'='Doe' and 'email'='com'
    Then response should have 200 status, json content type, contain info about 3 drivers found by name and email

  @component
  Scenario: Retrieving available drivers by default
    Given User wants to get details about available drivers
    When he performs request with no request parameters to available drivers url
    Then response should have 200 status, json content type, contain info about 2 available drivers
