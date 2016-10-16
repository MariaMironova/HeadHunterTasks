package island;

import java.util.*;


public class Island {
    private final int MAX_HEIGHT = 1000;
    private int height;
    private int width;
    private Cell[][] matrix;

    public Island(int[][] matrix) {
        height = matrix.length + 2;
        width = matrix[0].length + 2;
        this.matrix = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            this.matrix[i][0] = new Cell(i, 0, 0);
            this.matrix[i][width - 1] = new Cell(i, width - 1, 0);
        }
        for (int i = 0; i < width; i++) {
            this.matrix[0][i] = new Cell(0, i, 0);
            this.matrix[height - 1][i] = new Cell(height - 1, i, 0);
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                this.matrix[i + 1][j + 1] = new Cell(i + 1, j + 1, matrix[i][j]);
            }
        }
    }


    public void flood() {
        Queue<Cell> queue = new LinkedList<>();

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                queue.add(matrix[i][j]);
            }
        }

        Cell cell;
        while ((cell = queue.poll())!= null) {
            if (cell.isLowland() && !cell.isChecked()) {
                addWater(cell);
            }
        }
    }

    private void addWater(Cell cell) {
        Queue<Cell> lowland = new LinkedList<>();
        lowland.add(cell);

        Set<Cell> visitedCells = new HashSet<>();
        int minValue = MAX_HEIGHT + 1;
        while (!lowland.isEmpty()) {
            Cell currentCell = lowland.poll();

            if (!visitedCells.contains(currentCell)) {
                for (Cell nb : currentCell.getNeighbours()) {
                    int currentCellFloodedWaterLevel = currentCell.getFloodedWaterLevel();
                    int nbFloodedWaterLevel = nb.getFloodedWaterLevel();
                    if (nbFloodedWaterLevel < currentCellFloodedWaterLevel) {
                        return;
                    }
                    if (nbFloodedWaterLevel < minValue && nbFloodedWaterLevel != currentCellFloodedWaterLevel) {
                        minValue = nbFloodedWaterLevel;
                    }
                    if (nbFloodedWaterLevel == currentCellFloodedWaterLevel) {
                        lowland.add(nb);
                    }
                }

                visitedCells.add(currentCell);
            }
        }

        final int finalMinValue = minValue;
        visitedCells.forEach(cell1 -> cell1.setFloodedWaterLevel(finalMinValue));
        visitedCells.forEach(cell1 -> cell1.setChecked(true));
        addWater(cell);
    }

    public int getResult() {
        int sum = 0;
        for (int i = 2; i < height - 2; i++) {
            for (int j = 2; j < width - 2; j++) {
                sum += matrix[i][j].getFloodedWaterLevel() - matrix[i][j].getCellHeight();
            }
        }
        return sum;
    }

    private class Cell {
        private int x;
        private int y;
        private int cellHeight;
        private int floodedWaterLevel;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Cell(int x, int y, int cellHeight) {
            this.x = x;
            this.y = y;
            this.cellHeight = cellHeight;
            this.floodedWaterLevel = cellHeight;
        }

        public ArrayList<Cell> getNeighbours() {
            ArrayList<Cell> nb = new ArrayList<>();
            if (x != 0) nb.add(matrix[x-1][y]);
            if (x != height - 1) nb.add(matrix[x+1][y]);
            if (y != 0) nb.add(matrix[x][y-1]);
            if (y != width - 1) nb.add(matrix[x][y+1]);

            return nb;
        }

        public int getCellHeight() {
            return cellHeight;
        }

        public int getFloodedWaterLevel() {
            return floodedWaterLevel;
        }

        public void setFloodedWaterLevel(int floodedWaterLevel) {
            this.floodedWaterLevel = floodedWaterLevel;
        }

        public boolean isLowland() {
            List<Cell> nbs = getNeighbours();
            if (nbs.size() != 4) return false;
            for (Cell nb : nbs) {
                if (this.floodedWaterLevel > nb.floodedWaterLevel) {
                    return false;
                }
            }
            return true;
        }
    }

    public void printFloodedIsland() {
        System.out.println("----------------------------------");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(matrix[i][j].getFloodedWaterLevel() + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
    }
}
