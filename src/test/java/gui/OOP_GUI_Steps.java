package gui;

import io.cucumber.java.en.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OOP_GUI_Steps {
    private final int width = 7;
    private UserActions gui ;
    private final String ASTERISK = "*";
    private final int INITIAL_ROW = 0;

    @When("the application starts")
    public void theApplicationStarts() {
        this.gui = new MockGUI(width);
    }

    @Given("An initialized game")
    public void anInitializedGame() {
        this.gui = new MockGUI(width);
    }

    @Then("a cell in the first row should be marked with an asterisk at a random position")
    public void aCellInTheFirstRowShouldBeMarkedWithAnAsteriskAtARandomPosition() {
        assert(this.gui.getCells().get(INITIAL_ROW).stream()
                .filter(Cell::containsAsterisk).toList().size() == 1);
    }

    @And("all grid cells should be enabled and clickable")
    public void allGridCellsShouldBeEnabledAndClickable() {
        assert(this.gui.getCells().values()
                .stream().allMatch(it->
                        it.stream().allMatch(Cell::isClickable))
        );
    }

    @When("I click on a cell in the first row")
    public void iClickOnACellInTheFirstRow() {
        this.gui = this.gui.click(new Pair<>(0, new Random().nextInt(width)));
    }

    @Then("that cell should not be selected")
    public void thatCellShouldNotBeSelected() {
        assert(this.gui.getCells().get(INITIAL_ROW).stream().filter(Cell::containsAsterisk).toList().size() == 1);
    }

    @And("the cell selection count should remain unchanged")
    public void theCellSelectionCountShouldRemainUnchanged() {
        assert(this.gui.getCells().get(INITIAL_ROW).stream().filter(Cell::containsAsterisk).toList().size() == 1);
    }

    @When("I click on a cell in rows other than the first row")
    public void iClickOnACellInRowsOtherThanTheFirstRow() {
        this.gui = this.gui.click(new Pair<>(new Random().nextInt(width), new Random().nextInt(width)));
    }

    @Then("that cell should be marked as selected")
    public void thatCellShouldBeMarkedAsSelected() {
        assert(this.gui.getCells().entrySet().stream().filter(it -> it.getValue().stream().anyMatch(Cell::containsAsterisk)).count() == 2);
    }

    @And("the cell should remain enabled")
    public void theCellShouldRemainEnabled() {
        assert(this.gui.getCells().values().stream().allMatch(it -> it.stream().allMatch(Cell::isClickable)));
    }

    @When("I click on an already selected cell")
    public void iClickOnAnAlreadySelectedCell() {
        // To exclude the first row
        Pair<Integer, Integer> pos = new Pair<>(new Random().nextInt(1,width), new Random().nextInt(1,width));
        this.gui = this.gui.click(pos);
        this.gui = this.gui.click(pos);
    }

    @Then("the cell should remain selected")
    public void theCellShouldRemainSelected() {
        assert(this.gui.getCells().values().stream().allMatch(it -> it.stream().allMatch(Cell::isClickable)));
    }

    @And("nothing should change")
    public void nothingShouldChange() {
        assert(this.gui.getCells().values().stream().allMatch(it -> it.stream().allMatch(Cell::isClickable)));
        assert(this.gui.getCells().entrySet().stream().filter(it -> it.getValue().stream().anyMatch(Cell::containsAsterisk)).count() == 2);
    }

    @When("I click on multiple cells without forming a complete zigzag path")
    public void iClickOnMultipleCellsWithoutFormingACompleteZigzagPath() {
        this.gui = this.gui.click(new Pair<>(1, width-1)); // far right
        this.gui = this.gui.click(new Pair<>(1, 0)); // far left
    }

    @Then("all clicked cells should be marked as selected")
    public void allClickedCellsShouldBeMarkedAsSelected() {
        assert(this.gui.getCells().get(1).stream().filter(Cell::containsAsterisk).count() == 2);
    }

    @And("all cells should remain enabled")
    public void allCellsShouldRemainEnabled() {
        assert(this.gui.getCells().values().stream().allMatch(it -> it.stream().allMatch(Cell::isClickable)));
    }

    @And("the game should continue")
    public void theGameShouldContinue() {
        assert(this.gui.getCells().values().stream().allMatch(it -> it.stream().allMatch(Cell::isClickable)));
    }

    private Cell getStartingCell(){
        return this.gui.getCells().get(0).stream().filter(Cell::containsAsterisk).toList().getFirst();
    }

    private Cell getCellAtPos(int row, int col){
        return this.gui.getCells().get(row).get(col);
    }

    List<Pair<Integer, Integer>> validZigzagPath(){
        List<Pair<Integer, Integer>> path = new ArrayList<>();

        Cell startingCell = getStartingCell();
        for(int i = 1; i < this.width; i++){
            if(startingCell.col() - 1 > 0){
                path.add(new Pair<>(i, startingCell.col() - 1));
                startingCell = getCellAtPos(i, startingCell.col() - 1);
            }
            else {
                path.add(new Pair<>(i, startingCell.col() + 1));
                startingCell = getCellAtPos(i, startingCell.col() + 1);
            }
        }

        return path;
    }

    @When("I select cells that form a zigzag path connecting first and last rows")
    public void iSelectCellsThatFormAZigzagPathConnectingFirstAndLastRows() {
        for(var pos : validZigzagPath()){
            this.gui = this.gui.click(pos);
        }
    }

    @And("the zigzag path is exactly the selected cells with no extra selections")
    public void theZigzagPathIsExactlyTheSelectedCellsWithNoExtraSelections() {
        assert(this.gui.getCells().values().stream().map(it -> it.stream().filter(Cell::containsAsterisk).count()).allMatch(it -> it == 1));
    }

    @Then("all grid cells should be disabled")
    public void allGridCellsShouldBeDisabled() {
        assert(this.gui.getCells().values().stream().noneMatch(it -> it.stream().noneMatch(Cell::isClickable)));
    }

    @When("I select cells that include a valid zigzag path")
    public void iSelectCellsThatIncludeAValidZigzagPath() {
        List<Pair<Integer, Integer>> validPath = validZigzagPath();
        for(int i = 0; i < 3; i++){
            Pair<Integer, Integer> newPos;
            do {
                newPos = new Pair<>(new Random().nextInt(1, width), new Random().nextInt(1, width));
            }while(!validPath.contains(newPos));
            validPath.add(newPos);
        }

        for(var pos: validPath){
            this.gui = this.gui.click(pos);
        }
    }

    @And("there are additional selected cells beyond the zigzag path")
    public void thereAreAdditionalSelectedCellsBeyondTheZigzagPath() {
        assert(this.gui.getCells().values().stream().map(it -> it.stream().filter(Cell::containsAsterisk).count()).reduce(Long::sum).get() > width);
    }
}
