Feature: Passenger Payment Controller End to End

  @e2e
  Scenario: Saving a new passenger payment
    Given User wants to save a new passenger payment for the ride with id 'd2847bda-9285-4b52-a905-ef9b872f1c9c', passenger id '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', driver id 'd9343856-ad27-4256-9534-4c59fa5e6422', amount 100, payment method 'BANK_CARD' and card number '5538411806914853'
    When he performs request to save a new passenger payment
    Then response should have 201 status, json content type, contain passenger payment for ride 'd2847bda-9285-4b52-a905-ef9b872f1c9c' and id

  @e2e
  Scenario: Retrieving an existing passenger payment by id
    Given User wants to get details about existing passenger payment
    When he performs request to search passenger payment with id 'cce748fb-1a3a-468e-a49e-08a26fe2a418'
    Then response should have 200 status, json content type, contain passenger payment with requested id

  @e2e
  Scenario: Retrieving existing passenger payments by default
    Given User wants to get details about existing passenger payments
    When he performs request to get passenger payments with no request parameters
    Then response should have 200 status, json content type, contain at least 4 passenger payments

  @e2e
  Scenario: Retrieving existing passenger payments filtered by cash payment method
    Given User wants to get details about existing passenger payments filtered by cash payment method
    When he performs request to get passenger payments with request parameter 'paymentMethod'='CASH'
    Then response should have 200 status, json content type, contain at least 2 filtered passenger payments
