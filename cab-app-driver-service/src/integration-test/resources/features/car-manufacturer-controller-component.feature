Feature: Car Manufacturer Controller Component

  @component
  Scenario: Retrieving existing car manufacturers by default
    Given User wants to get details about existing car manufacturers
    When he performs request for car manufacturers with no request parameters
    Then response should have 200 status, json content type, contain info about these car manufacturers from page 0:
      | 1  | Toyota  |
      | 2  | Volkswagen|
      | 3  | Ford|
      | 4  | Chevrolet|
      | 5  | Nissan|
      | 6  | Honda|
      | 7  | Hyundai|
      | 8  | BMW|
      | 9  | Mercedes-Benz|
      | 10 | Audi |

  @component
  Scenario: Retrieving existing car manufacturers from the second page
    Given User wants to get details about existing car manufacturers
    When he performs request for car manufacturers with request parameters: 'page'=1
    Then response should have 200 status, json content type, contain info about these car manufacturers from page 1:
      | 11 | Kia        |
      | 12 | Fiat       |
      | 13 | Peugeot    |
      | 14 | Renault    |
      | 15 | Skoda      |
      | 16 | Mazda      |
      | 17 | Subaru     |
      | 18 | Mitsubishi |
      | 19 | Opel       |
      | 20 | Citroën    |
