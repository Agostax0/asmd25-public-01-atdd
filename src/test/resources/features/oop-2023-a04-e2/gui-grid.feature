Feature: Zigzag Path Selection Game
  As a user
  I want to select cells on a grid to create a zigzag path
  So that I can complete the game when connecting from the first row to the last row

  Background: The game is initialized
    Given An initialized game

  Scenario: Game initialization
    When the application starts
    Then a cell in the first row should be marked with an asterisk at a random position
    And all grid cells should be enabled and clickable

  Scenario: Ignore clicks on first row cells
    When I click on a cell in the first row
    Then that cell should not be selected
    And the cell selection count should remain unchanged

  Scenario: Select cells by clicking
    When I click on a cell in rows other than the first row
    Then that cell should be marked as selected
    And the cell should remain enabled

  Scenario: Prevent duplicate selections
    When I click on an already selected cell
    Then the cell should remain selected
    And nothing should change

  Scenario: Enable multiple selections before path completion
    When I click on multiple cells without forming a complete zigzag path
    Then all clicked cells should be marked as selected
    And all cells should remain enabled
    And the game should continue

  Scenario: Detect valid zigzag path with exact selection
    When I select cells that form a zigzag path connecting first and last rows
    And the zigzag path is exactly the selected cells with no extra selections
    Then all grid cells should be disabled

  Scenario: Complete game with extra selected cells
    When I select cells that include a valid zigzag path
    And there are additional selected cells beyond the zigzag path
    Then all grid cells should be disabled
