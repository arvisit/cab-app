Feature: Passenger Service

  Scenario: Saving a new passenger
    Given I want to save a new passenger with name 'Vivienne Gutierrez', email 'vivienne.gutierrez@yahoo.com.ar' and card number '7853471929691513'
    When I perform saving
    Then a new passenger should be saved
    And have name 'Vivienne Gutierrez', email 'vivienne.gutierrez@yahoo.com.ar' and card number '7853471929691513'
    And also have an id

  Scenario: Updating an existing passenger
    Given I want to update an existing passenger with new name 'Jane Doe', email 'jane.doe@mail.com' and card number '1111222233334444' values
    When I perform update of existing passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2'
    Then changes should be saved
    And I should receive response for passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2' and applied name 'Jane Doe', email 'jane.doe@mail.com' and card number '1111222233334444'

  Scenario: Deleting an existing passenger
    Given I want to delete an existing passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2'
    When I delete a passenger
    Then passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2' should be deleted

  Scenario: Retrieving an existing passenger by id
    Given I want to get details about existing passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2'
    When I search for the passenger with this id
    Then I should receive a response with details for passenger with id '3abcc6a1-94da-4185-aaa1-8a11c1b8efd2'

  Scenario: Retrieving an existing passenger by email
    Given I want to get details about existing passenger with email 'vivienne.gutierrez@yahoo.com.ar'
    When I search for the passenger with this email
    Then I should receive a response with details for passenger with email 'vivienne.gutierrez@yahoo.com.ar'

  Scenario: Retrieving existing passengers
    Given there are two existing passengers
      | 3abcc6a1-94da-4185-aaa1-8a11c1b8efd2 | Vivienne Gutierrez | vivienne.gutierrez@yahoo.com.ar | 7853471929691513 |
      | 1abcc8a1-54da-4185-a3a1-8a11c1a8efa2 | Jane Doe           | jane.doe@mail.com               | 1111222233334444 |
    When I try to get all passengers from the first page of size 10 without sorting
    Then I should receive a response with pageable details and list of all passengers
