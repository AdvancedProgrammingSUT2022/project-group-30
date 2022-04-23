package controllers;

import java.util.ArrayList;

import models.City;
import models.Civilization;
import models.GameDataBase;
import models.Tile;
import models.TileVisibility;
import models.interfaces.Selectable;
import models.units.CombatType;
import models.units.Unit;
import models.works.Work;

public class GameController {
    private static GameController gameController;
    private GameController() {
        database = GameDataBase.getGameDataBase();
    }
    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    private GameDataBase database;

    public void startGame() {

    }

    public Tile findWorksLocation(Work work) {  // gets a work and finds the tile that owns it
        // TODO
        return null;
    }

    public boolean areCoordinatesValid(int x, int y) {
        return database.getMap().areCoordinatesValid(x, y);
    }

    public Tile getTileByCoordinates(int x, int y) {
        return database.getMap().getTile(x, y);
    }

    public TileVisibility getTileVisibilityForPlayer(Tile tile) {  // returns Visible, Fog of War, or Revealed
        return database.getCurrentPlayer().getTileVisibility(tile);
    }

    public Unit getMilitaryUnitInTile(Tile tile) {
        ArrayList<Unit> units = database.getUnits();
        for (Unit unit : units) {
            if (unit.getLocation() == tile && unit.getType().getCombatType() != CombatType.CIVILIAN) {
                return unit;
            }
        }
        return null;
    }
    
    public Unit getMilitaryUnitInTile(Tile tile, Civilization owner) {
        ArrayList<Unit> units = database.getUnits();
        for (Unit unit : units) {
            if (unit.getLocation() == tile && unit.getType().getCombatType() != CombatType.CIVILIAN && unit.getOwner() == owner) {
                return unit;
            }
        }
        return null;
    }

    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        // TODO
        return null;
    }

    public City getCityInTile(Tile tile) {
        for (City city : database.getCities()) {
            if (city.getCentralTile() == tile) {
                return city;
            }
        }
        return null;
    }

    public void setSelectedEntity(Selectable selectable) {
        database.getSelectedEntity();
    }

    public Selectable getSelectedEntity() {
        return database.getSelectedEntity();
    }

    public Civilization getCurrentPlayer() {
        return database.getCurrentPlayer();
    }
}