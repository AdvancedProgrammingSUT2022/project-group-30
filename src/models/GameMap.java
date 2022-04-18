package models;

import java.util.ArrayList;

public class GameMap {
    public static final int NUMBER_OF_TILES = 100;
    private Tile[][] map;
    private ArrayList<RiverSegment> rivers;
    private Tile frameBase;

    public GameMap() {
        map = new Tile[NUMBER_OF_TILES][NUMBER_OF_TILES];
        rivers = new ArrayList<>();
    }

    public ArrayList<Tile> findClosestPath(Tile origin, Tile destination){
        //TODO
        return null;
    }

    public int findMovementCost(Unit unit, Tile destination){
        //TODO
        return 0;
    }

    public Tile findTileToAppendToCity(City city){
        //TODO
        return null;
    }

    public ArrayList<Tile> getZOCTilesForCivilization(Civilization  civilization){
        //TODO
        return null;
    }

    public Tile[][] getMap(){
        return this.map;
    }

    public ArrayList<RiverSegment> getRivers (){
        return this.rivers;
    }

    public Tile getFrameBase(){
        return this.frameBase;
    }

    public void setFrameBase(Tile frameBase){
        this.frameBase = frameBase;
    }



}
