Feature: Promo Code Controller End to End

  @e2e
  Scenario: Saving a new promo code
    Given User wants to save a new promo code with keyword 'PROMO' and discount percent 20
    When he performs saving of a new promo code via request
    Then response should have 201 status, json content type, contain promo code with expected parameters and id

  @e2e
  Scenario: Updating an existing promo code
    Given User wants to update an existing promo code with keyword 'NEWPROMO' and discount percent 20
    When he performs update of existing promo code with id 3 via request
    Then response should have 200 status, json content type, contain promo code with keyword 'NEWPROMO' and discount percent 20

  @e2e
  Scenario: Deactivating an existing promo code
    Given User wants to deactivate an existing promo code
    When he performs deactivation of existing promo code with id 1 via request
    Then response should have 200 status, json content type, contain deactivated promo code

  @e2e
  Scenario: Deleting an existing promo code
    Given User wants to delete an existing promo code with id 3
    When he performs delete of existing promo code via request
    Then response should have 204 status, minus one promo code in database

  @e2e
  Scenario: Retrieving an existing promo code by id
    Given User wants to get details about an existing promo code with id 4
    When he performs search promo code by id via request
    Then response should have 200 status, json content type, contain promo code with requested id

#  @e2e
#  Scenario: Retrieving existing promo codes by default
#    Given User wants to get details about existing promo codes
#    When he performs request with no request parameters
#    Then response should have 200 status, json content type, contain info about 4 promo codes
#
#  @e2e
#  Scenario: Retrieving active promo codes by default
#    Given User wants to get details about active promo codes
#    When he performs request with no request parameters to active promo codes url
#    Then response should have 200 status, json content type, contain info about 2 active promo codes
