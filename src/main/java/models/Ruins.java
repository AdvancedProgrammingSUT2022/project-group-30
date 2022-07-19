package models;

import controllers.GameController;
import models.technology.Technology;

import java.util.ArrayList;
import java.util.Random;

public class Ruins {
    private boolean isUsed = false;

    public Ruins createImage() {
        return new Ruins();
    }
    public void run() {
        int number = (int) (Math.random()*5 + 1);

        //TODO

        this.isUsed = true;
    }

    public void learningTechnology(){
        ArrayList<Technology> unlockedTechnologies = this.getCivilization().getTechnologies().getUnlockedTechnologies();
        int randomNum = (int) (Math.random() * unlockedTechnologies.size());
        this.getCivilization().getTechnologies().learnTechnology(unlockedTechnologies.get(randomNum));
    }

    public void makingTilesVisible(){
        ArrayList<Tile> mapTiles = GameDataBase.getGameDataBase().getMap().getAllMapTiles();
        int randomNum = (int) (Math.random() * mapTiles.size());
        Tile centerTile = mapTiles.get(randomNum);
        ArrayList<Tile> tiles = GameController.getGameController().getAdjacentTiles(centerTile);
        for (Tile tile : tiles) {
            this.getCivilization().getMapImage().put(tile, tile);
        }
    }



    public Tile getTile(){
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            if(tile.getRuins() == this)
                return tile;
        }
        return null;
    }

    public Civilization getCivilization(){
        return this.getTile().getCivilization();
    }

    public boolean isUsed() {
        return isUsed;
    }
}
