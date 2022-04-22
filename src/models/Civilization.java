package models;

import java.util.HashMap;

import models.technology.Technology;
import utilities.Debugger;

public class Civilization {
    private HashMap<Tile, TileVisibility> visibilityMap;

    public boolean hasTechnology(Technology technology) {
        //TODO
        return true;
    }
    private int goldCount;


    public int getGoldCount(){
        return this.goldCount;
    }

    public void setGoldCount(int goldCount){
        this.goldCount = goldCount;
    }

    public TileVisibility getTileVisibility(Tile tile) {
        if (visibilityMap.containsKey(tile) == false) {
            Debugger.debug("Civilization visibilityMap doesn't contain the tile whose visibility is queried");
            return TileVisibility.FOG_OF_WAR;
        }
        return visibilityMap.get(tile);
    }
}

