Feature: Passenger Service End-to-End

  @e2e
  Scenario: Saving a new passenger
    Given User wants to save a new passenger with name 'Jack Black', email 'jack.black@mail.com' and card number '0000111122223333'
    When he performs saving via request
    Then response should have 201 status, json content type, contain passenger with expected parameters and id

  #@e2e
  #Scenario: Updating an existing passenger
  #  Given User wants to update an existing passenger with new name 'Jack Black', email 'jack.black@mail.com' and card number '0000111122223333' values
  #  When he performs update of existing passenger with id '51334f37-482c-4498-ad0f-122929edc0ff' via request
  #  Then response should have 200 status, json content type, contain passenger with updated parameters
  #
  #@e2e
  #Scenario: Deleting an existing passenger
  #  Given User wants to delete an existing passenger with id '51334f37-482c-4498-ad0f-122929edc0ff'
  #  When he performs delete of existing passenger via request
  #  Then response should have 204 status, minus one passenger in database
  #
  #@e2e
  #Scenario: Retrieving an existing passenger by id
  #  Given User wants to get details about an existing passenger with id '51334f37-482c-4498-ad0f-122929edc0ff'
  #  When he performs search passenger by id via request
  #  Then response should have 200 status, json content type, contain passenger with requested id

  @e2e
  Scenario: Retrieving an existing passenger by email
    Given User wants to get details about an existing passenger with email 'jack.black@mail.com'
    When he performs search passenger by email via request
    Then response should have 200 status, json content type, contain passenger with requested email

  #@e2e
  #Scenario: Retrieving existing passengers by default
  #  Given User wants to get details about existing passengers
  #  When he performs request with no request parameters
  #  Then response should have 200 status, json content type, contain info about these passengers:
  #    | 44012418-4211-4e65-aac6-c1f19f25715c | Johnny Doe | johnny.doe@yahoo.de    | null             |
  #    | 51334f37-482c-4498-ad0f-122929edc0ff | John Doe   | john.doe@mail.com      | 8633928741544997 |
  #    | edeb6315-5408-46e6-a710-2666725c4dc7 | Janny Doe  | janny.doe@yahoo.com.br | null             |
  #    | 3abcc6a1-94da-4185-aaa1-8a11c1b8efd2 | Jane Doe   | jane.doe@yahoo.com.ar  | 7853471929691513 |
  #
  #@e2e
  #Scenario: Retrieving existing passengers filtered by name and email
  #  Given User wants to get details about existing passengers
  #  When he performs request with parameters: 'name'='Doe' and 'email'='com'
  #  Then response should have 200 status, json content type, contain info about these passengers found by name and email:
  #    | 51334f37-482c-4498-ad0f-122929edc0ff | John Doe   | john.doe@mail.com      | 8633928741544997 |
  #    | edeb6315-5408-46e6-a710-2666725c4dc7 | Janny Doe  | janny.doe@yahoo.com.br | null             |
  #    | 3abcc6a1-94da-4185-aaa1-8a11c1b8efd2 | Jane Doe   | jane.doe@yahoo.com.ar  | 7853471929691513 |
