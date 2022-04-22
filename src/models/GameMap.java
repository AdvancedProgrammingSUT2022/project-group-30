package models;

import java.util.ArrayList;

import models.units.Unit;
import utilities.Debugger;

public class GameMap {
    public static final int NUMBER_OF_TILES = 100;
    private Tile[][] map;
    private ArrayList<RiverSegment> rivers;
    private Tile frameBase;

    public GameMap() {
        map = new Tile[NUMBER_OF_TILES][NUMBER_OF_TILES];
        rivers = new ArrayList<>();
    }

    public Tile getTile(int x, int y) {
        if (y >= map.length || y < 0 || x >= map[y].length || x < 0) {
            Debugger.debug("getTile of GameMap called with invalid indices x:" + x + ", y: " + y);
            return null;
        }
        return map[y][x];
    }

    public boolean areCoordinatesValid(int x, int y) {
        if (y >= map.length || y < 0 || x >= map[y].length || x < 0) {
            return false;
        }
        return true;
    }


    public ArrayList<Tile> findClosestPath(Tile origin, Tile destination){
        //TODO
        return null;
    }

    public int findMovementCost(Unit unit, Tile destination) {
        // TODO
        return 0;
    }

    public Tile findTileToAppendToCity(City city) {
        // TODO
        return null;
    }

    public ArrayList<Tile> getZOCTilesForCivilization(Civilization civilization) {
        // TODO
        return null;
    }

    public Tile[][] getMap() {
        return this.map;
    }

    public ArrayList<RiverSegment> getRivers() {
        return this.rivers;
    }

    public Tile getFrameBase() {
        return this.frameBase;
    }
}
