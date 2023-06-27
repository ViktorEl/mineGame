package sk.tuke.mines;

import sk.tuke.mines.core.Clue;
import sk.tuke.mines.core.Mine;
import sk.tuke.mines.core.MineField;
import sk.tuke.mines.core.Tile;

public class Main {
    public static void main(String[] args) {
        var field = new MineField(1, 2, 1);

        renderField(field);

        field.openTile(0, 0);

        System.out.println("-----------------------------");
        renderField(field);
    }

    private static void renderField(MineField field) {
        System.out.println(field.getState());

        for (var row = 0; row < field.getRowCount(); row++) {
            for (var column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);

                switch (tile.getState()) {
                    case OPEN:
                        if (tile instanceof Mine)
                            System.out.print("X");
                        else if (tile instanceof Clue) {
                            Clue clue = ((Clue) tile);
                            System.out.print(clue.getValue());
                        }
                        break;
                    case MARKED:
                        System.out.print("M");
                        break;
                    case CLOSED:
                        System.out.print("-");
                        break;
                }
            }
            System.out.println();
        }
    }
}