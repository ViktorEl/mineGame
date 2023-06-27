package sk.tuke.mines.core;

public abstract class Tile {
    private TileState state = TileState.CLOSED;

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }
}