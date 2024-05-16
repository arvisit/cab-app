Feature: Ride Controller End to End

  @e2e
  Scenario: Saving a new ride with default acceptance
    Given User wants to save a new ride with passenger id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2', payment method 'BANK_CARD', start address 'Bramcote Grove, 42' and destination address 'Llewellyn Street, 35'
    When he performs saving of a new ride via request
    Then response should have 201 status, json content type, contain ride with expected parameters and id
    And after 10 seconds this ride should be accepted by driver

  @e2e
  Scenario: Canceling an accepted ride
    Given User wants to cancel an accepted ride
    When he performs a request to cancel ride with id 'a5b0a1f9-f45d-4287-bbad-ea6253976d0d'
    Then response should have 200 status, json content type, contain canceled ride
    And assigned driver should become available

  @e2e
  Scenario: Accepting an existing ride
    Given User wants to accept ride by existing driver
    When he performs a request to accept ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54' by available driver with id '4c2b3a93-1d97-4ccf-a7b8-824daea08671'
    Then response should have 200 status, json content type, contain accepted ride
    And driver with id '4c2b3a93-1d97-4ccf-a7b8-824daea08671' should become unavailable

  @e2e
  Scenario: Beginning an existing ride
    Given User wants to begin an accepted ride
    When he performs a request to begin ride with id '48c14ff2-f020-473a-a98b-651f203c3843'
    Then response should have 200 status, json content type, contain began ride

  @e2e
  Scenario: Ending an existing ride to be paid with bank card
    Given User wants to end a began ride with bank card payment method
    When he performs a request to end ride with id '72b20027-23d1-4651-865a-a2f7cd74fdb6'
    Then response should have 200 status, json content type, contain ended ride with bank card payment method
    And ride should become finished eventually
    And payment for ride should be created
    And assigned driver should become available

  @e2e
  Scenario: Ending an existing ride to be paid with cash
    Given User wants to end a began ride with cash payment method
    When he performs a request to end ride with id 'acd6f53f-a19b-4109-9b6f-1d15814db2f1'
    Then response should have 200 status, json content type, contain ended ride with cash payment method
    And ride would not become finished after timeout 3 seconds

  @e2e
  Scenario: Confirming payment with cash
    Given User wants to confirm payment with cash for an existing ride
    When he performs a request to confirm payment for the ride with id 'ffe34487-dfa3-4660-96dc-ed108e06ab77'
    Then response should have 200 status, json content type, contain finished ride with cash payment method
    And payment for ride should be created

  @e2e
  Scenario: Applying promo code to an existing ride
    Given User wants to apply promo code to an existing ride
    When he performs a request to apply promo code 'DIAMOND10' to the ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54'
    Then response should have 200 status, json content type, contain ride with applied promo code

  @e2e
  Scenario: Changing payment method
    Given User wants to change payment method for an existing ride
    When he performs a request to change payment method for the ride with id '69b509c6-20fe-4b70-a07e-31e52ea05a54'
    Then response should have 200 status, json content type, contain ride with changed payment method

  @e2e
  Scenario: Scoring a driver
    Given User wants to score a driver for an existing ride
    When he performs a request to score a driver with 5 for the ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 200 status, json content type, contain ride with driver score 5

  @e2e
  Scenario: Scoring a passenger
    Given User wants to score a passenger for an existing ride
    When he performs a request to score a passenger with 4 for the ride with id '9ea8a8b0-004b-4ded-8a3a-edc198c89e31'
    Then response should have 200 status, json content type, contain ride with passenger score 4

  @e2e
  Scenario: Deleting an existing ride
    Given User wants to delete an existing ride
    When he performs a request to delete a ride with id 'a5b0a1f9-f45d-4287-bbad-ea6253976d0d'
    Then response should have 204 status, minus ride with id 'a5b0a1f9-f45d-4287-bbad-ea6253976d0d' in the database

  @e2e
  Scenario: Retrieving an existing ride by id
    Given User wants to get details about an existing ride
    When he performs a request to get details about ride with id 'aba3e655-a8b1-4dca-a4b5-6566dd74a47e'
    Then response should have 200 status, json content type, contain ride with requested id

  @e2e
  Scenario: Retrieving passenger rating
    Given User wants to get rating for an existing passenger
    When he performs a request with passenger id '072f635e-0ee7-461e-aa7e-1901ae3d0c5e' to get his rating
    Then response should have 200 status, json content type, contain rating for the passenger

  @e2e
  Scenario: Retrieving driver rating
    Given User wants to get rating for an existing driver
    When he performs a request with driver id '19ee7917-8e48-4b3e-8b21-28c3d1e53ca4' to get his rating
    Then response should have 200 status, json content type, contain rating for the driver
