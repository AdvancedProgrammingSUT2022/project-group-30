package models;

import models.interfaces.TileImage;
import models.units.Unit;

import java.util.ArrayList;

public class TileHistory implements TileImage {
    private Tile tile;
    private ArrayList<Unit> units;
    private City city;

    public TileHistory() {
        units = new ArrayList<>();
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
