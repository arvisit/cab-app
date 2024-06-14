Feature: Driver Payment Controller Component

  @component
  Scenario: Saving a new driver payment
    Given User wants to save a new driver payment for the driver with id 'd9343856-ad27-4256-9534-4c59fa5e6422', amount 20, operation type 'WITHDRAWAL' and card number '8972821332297027'
    When he performs request to save a new driver payment
    Then response should have 201 status, json content type, contain driver payment with expected parameters and id

  @component
  Scenario: Saving a new withdrawal payment
    Given User wants to save a new withdrawal
    When he performs request to save a new driver payment
    Then driver should have balance of 20 after that

  @component
  Scenario: Saving a new repayment payment
    Given User wants to save a new repayment
    When he performs request to save a new driver payment
    Then driver should have balance of 60 after that

  @component
  Scenario: Retrieving an existing driver payment by id
    Given User wants to get details about existing driver payment
    When he performs request to search driver payment with id 'd470f2cd-468a-496c-9298-7035c7ca7495'
    Then response should have 200 status, json content type, contain driver payment with requested id

  @component
  Scenario: Retrieving existing driver payments by default
    Given User wants to get details about existing driver payments
    When he performs request to get driver payments with no request parameters
    Then response should have 200 status, json content type, contain 3 driver payments

  @component
  Scenario: Retrieving existing driver payments filtered by driver id and repayment operation type
    Given User wants to get details about existing driver payments filtered by driver id and repayment operation type
    When he performs request to get driver payments with request parameters 'driverId'='d9343856-ad27-4256-9534-4c59fa5e6422' and 'operation'='REPAYMENT'
    Then response should have 200 status, json content type, contain 1 filtered driver payment

  @component
  Scenario: Retrieving account balance for existing driver
    Given User wants to get account balance for existing driver
    When he performs request to get account balance for the driver with id 'd9343856-ad27-4256-9534-4c59fa5e6422'
    Then response should have 200 status, json content type, contain account balance with expected parameters
