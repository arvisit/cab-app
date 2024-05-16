Feature: Passenger Controller End to End

  @e2e
  Scenario: Saving a new passenger
    Given User wants to save a new passenger with name 'Jack Black', email 'jack.black@mail.com' and card number '0000111122223333'
    When he performs saving via request
    Then response should have 201 status, json content type, contain passenger with expected parameters and id

  @e2e
  Scenario: Updating an existing passenger
    Given User wants to update an existing passenger with new name 'Jack White', email 'jack.white@mail.com' and card number '0000111122223333' values
    When he performs update of existing passenger with id '51334f37-482c-4498-ad0f-122929edc0ff' via request
    Then response should have 200 status, json content type, contain passenger with name 'Jack White', email 'jack.white@mail.com' and card number '0000111122223333'

  @e2e
  Scenario: Deleting an existing passenger
    Given User wants to delete an existing passenger with id '51334f37-482c-4498-ad0f-122929edc0ff'
    When he performs delete of existing passenger via request
    Then response should have 204 status, minus one passenger in database

  @e2e
  Scenario: Retrieving an existing passenger by id
    Given User wants to get details about an existing passenger with id '44012418-4211-4e65-aac6-c1f19f25715c'
    When he performs search passenger by id via request
    Then response should have 200 status, json content type, contain passenger with requested id

  @e2e
  Scenario: Retrieving an existing passenger by email
    Given User wants to get details about an existing passenger with email 'johnny.doe@yahoo.de'
    When he performs search passenger by email via request
    Then response should have 200 status, json content type, contain passenger with requested email
