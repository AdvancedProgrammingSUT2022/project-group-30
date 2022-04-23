package models;

import java.util.ArrayList;

import models.improvements.Improvement;
import models.interfaces.TileImage;
import models.units.Unit;

public class TileHistory implements TileImage {
    // TODO : Save works in it, and save whether tile was in a city's territory or not
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
