Feature: Color Controller Component

  @component
  Scenario: Retrieving existing colors by default
    Given User wants to get details about existing colors
    When he performs request for colors with no request parameters
    Then response should have 200 status, json content type, contain info about these colors from page 0:
      | 1  | White  |
      | 2  | Black  |
      | 3  | Gray   |
      | 4  | Silver |
      | 5  | Blue   |
      | 6  | Red    |
      | 7  | Brown  |
      | 8  | Green  |
      | 9  | Orange |
      | 10 | Beige  |

  @component
  Scenario: Retrieving existing colors from the second page
    Given User wants to get details about existing colors
    When he performs request for colors with request parameters: 'page'=1
    Then response should have 200 status, json content type, contain info about these colors from page 1:
      | 11 | Purple |
      | 12 | Gold   |
      | 13 | Yellow |
