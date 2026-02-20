package gui;

import java.util.*;

interface UserActions {
    MockGUI click(Pair<Integer, Integer> pos);
    Map<Integer, List<Cell>> getCells();
}


public class MockGUI implements UserActions {
    private final Map<Integer, List<Cell>> grid = new HashMap<>();
    private final int width;
    MockGUI(final int width){
        this.width = width;
        for(int i = 0; i < width; i++){
            grid.putIfAbsent(i, new ArrayList<>());
            for(int j = 0; j < width; j ++){
                grid.get(i).add(new Cell(i, j, true, false));
            }
        }

        final int randomIndex = new Random().nextInt(width);
        final Cell startCell = grid.get(0).get(randomIndex);
        updateCell(0, randomIndex, startCell.isClickable(), true);
    }

    private void updateCell(int row, int col, boolean isClickable, boolean containsAsterisk){
        grid.get(row).set(col, new Cell(row,col,isClickable,containsAsterisk));
    }

    @Override
    public MockGUI click(Pair<Integer, Integer> pos) {
        if(pos.getX() != 0){
            updateCell(pos.getX(), pos.getY(), true, true);
        }

        if(dfsGrid()){
            disableAll();
        }

        return this;
    }

    private void disableAll(){
        for(int row = 0; row < width; row++){
            for(int col = 0; col < width; col++){
                Cell cell = this.grid.get(row).get(col);
                updateCell(row, col, false, cell.containsAsterisk());
            }
        }
    }

    private boolean dfsGrid(){
        Cell startingCell = this.grid.get(0).stream().filter(Cell::containsAsterisk).toList().getFirst();
        return dfs(startingCell);
    }

    private boolean dfs(Cell cell){
        if(cell.row() == this.width - 1){
            return true;
        }
        else {
            return this.grid.getOrDefault(cell.row() + 1, List.of()).stream()
                    .filter(it-> it.col() == cell.col() - 1 || it.col() == cell.col() + 1)
                    .anyMatch(this::dfs);
        }
    }

    @Override
    public Map<Integer, List<Cell>> getCells() {
        return this.grid;
    }
}
