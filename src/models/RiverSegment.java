package models;

public class RiverSegment {
    private final Tile firstTile;
    private final Tile secondTile;

    public RiverSegment(Tile firstTile, Tile secondTile) {
        this.firstTile = firstTile;
        this.secondTile = secondTile;
    }

    public Tile getFirstTile() {
        return this.firstTile;
    }

    public Tile getSecondTile() {
        return this.secondTile;
    }
}
