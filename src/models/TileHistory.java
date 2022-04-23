package models;

import java.util.ArrayList;

import models.buildings.Building;
import models.improvements.Improvement;
import models.units.Unit;

public class TileHistory {
    private Tile tile;
    private ArrayList<Unit> units;
    private ArrayList<Improvement> improvements;
    private ArrayList<Building> buildings;
    private City city;

    public TileHistory() {
        units = new ArrayList<>();
        improvements = new ArrayList<>();
        buildings = new ArrayList<>();
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

    public ArrayList<Improvement> getImprovements() {
        return improvements;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }
}
