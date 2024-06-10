Feature: Ride Controller Component

  @component
  Scenario: Saving a new ride
    Given User wants to save a new ride with passenger id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2', payment method 'BANK_CARD', start address 'Bramcote Grove, 42' and destination address 'Llewellyn Street, 35'
    When he performs saving of a new ride via request
    Then response should have 201 status, json content type, contain ride with expected parameters and id

  @component
  Scenario: Canceling an existing ride
    Given User wants to cancel an existing ride
    When he performs a request to cancel ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54'
    Then response should have 200 status, json content type, contain canceled ride

  @component
  Scenario: Accepting an existing ride
    Given User wants to accept ride by existing driver
    When he performs a request to accept ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54' by available driver with id '4c2b3a93-1d97-4ccf-a7b8-824daea08671'
    Then response should have 200 status, json content type, contain accepted ride

  @component
  Scenario: Beginning an existing ride
    Given User wants to begin an accepted ride
    When he performs a request to begin ride with id 'a5b0a1f9-f45d-4287-bbad-ea6253976d0d'
    Then response should have 200 status, json content type, contain began ride

  @component
  Scenario: Ending an existing ride to be paid with bank card
    Given User wants to end a began ride with bank card payment method
    When he performs a request to end ride with id '72b20027-23d1-4651-865a-a2f7cd74fdb6'
    Then response should have 200 status, json content type, contain ended ride with bank card payment method

  @component
  Scenario: Ending an existing ride to be paid with cash
    Given User wants to end a began ride with cash payment method
    When he performs a request to end ride with id 'acd6f53f-a19b-4109-9b6f-1d15814db2f1'
    Then response should have 200 status, json content type, contain ended ride with cash payment method

  @component
  Scenario: Finishing an existing ride
    Given User wants to finish an ended ride
    When he performs a request to finish ride with id 'b4de939f-3a3f-441c-b081-dc604f6bed20'
    Then response should have 200 status, json content type, contain finished ride

  @component
  Scenario: Confirming payment with cash
    Given User wants to confirm payment with cash for an existing ride
    When he performs a request to confirm payment for the ride with id 'ffe34487-dfa3-4660-96dc-ed108e06ab77'
    Then response should have 200 status, json content type, contain finished ride with cash payment method

  @component
  Scenario: Confirming payment with bank card
    Given User wants to confirm payment with bank card for an existing ride
    When he performs a request to confirm payment for the ride with id '942d8839-7a16-4dfa-9443-179220135b3a'
    Then response should have 200 status, json content type, contain finished ride with bank card payment method

  @component
  Scenario: Applying promo code to an existing ride
    Given User wants to apply promo code to an existing ride
    When he performs a request to apply promo code 'BRILLIANT10' to the ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54'
    Then response should have 200 status, json content type, contain ride with applied promo code

  @component
  Scenario: Changing payment method
    Given User wants to change payment method for an existing ride
    When he performs a request to change payment method for the ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54'
    Then response should have 200 status, json content type, contain ride with changed payment method

  @component
  Scenario: Scoring a driver
    Given User wants to score a driver for an existing ride
    When he performs a request to score a driver with 5 for the ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 200 status, json content type, contain ride with driver score

  @component
  Scenario: Scoring a passenger
    Given User wants to score a passenger for an existing ride
    When he performs a request to score a passenger with 5 for the ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 200 status, json content type, contain ride with passenger score

  @component
  Scenario: Deleting an existing ride
    Given User wants to delete an existing ride
    When he performs a request to delete a ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 204 status, minus ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31' in the database

  @component
  Scenario: Retrieving an existing ride by id
    Given User wants to get details about an existing ride
    When he performs a request to get details about ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 200 status, json content type, contain ride with requested id

  @component
  Scenario: Retrieving existing rides by default
    Given User wants to get details about existing rides
    When he performs a request with no request parameters to get all rides
    Then response should have 200 status, json content type, contain info about 9 rides

  @component
  Scenario: Retrieving existing rides by passenger id as request parameter
    Given User wants to get details about existing rides for specific passenger
    When he performs a request with request parameter 'passengerId'='deecaeef-454b-487d-987c-54df212385b3' to get all rides for this passenger
    Then response should have 200 status, json content type, contain info about 3 rides for this passenger

  @component
  Scenario: Retrieving existing rides by passenger id as path variable
    Given User wants to get details about existing rides for specific passenger
    When he performs a request with passenger id 'deecaeef-454b-487d-987c-54df212385b3' to get all rides for this passenger
    Then response should have 200 status, json content type, contain info about 3 rides for this passenger

  @component
  Scenario: Retrieving existing rides by driver id as path variable
    Given User wants to get details about existing rides for specific driver
    When he performs a request with driver id 'd9343856-ad27-4256-9534-4c59fa5e6422' to get all rides for this driver
    Then response should have 200 status, json content type, contain info about 3 rides for this driver

  @component
  Scenario: Retrieving passenger rating
    Given User wants to get rating for an existing passenger
    When he performs a request with passenger id '072f635e-0ee7-461e-aa7e-1901ae3d0c5e' to get his rating
    Then response should have 200 status, json content type, contain rating for the passenger

  @component
  Scenario: Retrieving driver rating
    Given User wants to get rating for an existing driver
    When he performs a request with driver id '19ee7917-8e48-4b3e-8b21-28c3d1e53ca4' to get his rating
    Then response should have 200 status, json content type, contain rating for the driver
