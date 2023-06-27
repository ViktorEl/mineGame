package sk.tuke.mines.core;

import java.util.Random;

public class MineField {
    private final int rowCount;

    private final int columnCount;

    private final int mineCount;

    private FieldState state = FieldState.PLAYING;

    private Tile[][] tiles;

    private int openTilesCount;

    public MineField(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        this.tiles = new Tile[rowCount][columnCount];
        generate();
    }

    private void generate() {
        generateMines();
        fillWithClues();
    }

    private void generateMines() {
        var random = new Random();

        var minesStored = 0;
        while (minesStored < this.mineCount) {
            var row = random.nextInt(rowCount);
            var column = random.nextInt(columnCount);

            if (tiles[row][column] == null) {
                tiles[row][column] = new Mine();
                minesStored++;
            }
        }
    }

    private void fillWithClues() {
        for (var row = 0; row < rowCount; row++) {
            for (var column = 0; column < columnCount; column++) {
                if (tiles[row][column] == null)
                    tiles[row][column] = new Clue(countNeighboursMines(row, column));
            }
        }
    }

    private int countNeighboursMines(int row, int column) {
        var mines = 0;

        if (row > 0) {
            if (column > 0 && tiles[row - 1][column - 1] instanceof Mine)
                mines++;
            if (tiles[row - 1][column] instanceof Mine)
                mines++;
            if (column + 1 < columnCount && tiles[row - 1][column + 1] instanceof Mine)
                mines++;
        }

        if (column > 0 && tiles[row][column - 1] instanceof Mine)
            mines++;
        if (column + 1 < columnCount && tiles[row][column + 1] instanceof Mine)
            mines++;

        if (row + 1 < rowCount) {
            if (column > 0 && tiles[row + 1][column - 1] instanceof Mine)
                mines++;
            if (tiles[row + 1][column] instanceof Mine)
                mines++;
            if (column + 1 < columnCount && tiles[row + 1][column + 1] instanceof Mine)
                mines++;
        }

        return mines;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public FieldState getState() {
        return state;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public void markTile(int row, int column) {
        var tile = tiles[row][column];
        if (tile.getState() == TileState.CLOSED)
            tile.setState(TileState.MARKED);
        else if (tile.getState() == TileState.MARKED)
            tile.setState(TileState.CLOSED);
    }

    public void openTile(int row, int column) {
        var tile = tiles[row][column];
        if (tile.getState() == TileState.CLOSED) {
            tile.setState(TileState.OPEN);
            openTilesCount++;

            if (tile instanceof Mine) {
                state = FieldState.FAILED;
                return;
            }

            if (tile instanceof Clue && ((Clue) tile).getValue() == 0)
                openNeighbourTiles(row, column);

            if (isSolved())
                state = FieldState.SOLVED;
        }
    }

    private void openNeighbourTiles(int row, int column) {
        if (row > 0) {
            if (column > 0)
                openTile(row - 1, column - 1);
            openTile(row - 1, column);
            if (column + 1 < columnCount)
                openTile(row - 1, column + 1);
        }

        if (column > 0)
            openTile(row, column - 1);
        if (column + 1 < columnCount)
            openTile(row, column + 1);;

        if (row + 1 < rowCount) {
            if (column > 0)
                openTile(row + 1, column - 1);
            openTile(row + 1, column);
            if (column + 1 < columnCount)
                openTile(row + 1, column + 1);
        }
    }

    private boolean isSolved() {
        return rowCount * columnCount - mineCount == openTilesCount;
    }
}