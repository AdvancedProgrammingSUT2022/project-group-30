package models;

import controllers.GameController;
import models.technology.Technology;
import models.units.UnitType;

import java.util.ArrayList;

public class Ruins {
    private boolean isUsed = false;

    public Ruins createImage() {
        return new Ruins();
    }

    public void run() {
        int randomNumber = (int) (Math.random() * 5 + 1);

        switch (randomNumber) {
            case 1 -> learningTechnology();
            case 2 -> makingTilesVisible();
            case 3 -> addPopulation();
            case 4 -> addGold();
            case 5 -> createSettlerAndWorker();
            default -> {
            }
        }

        this.isUsed = true;
    }

    public void learningTechnology() {
        ArrayList<Technology> unlockedTechnologies = this.getCivilization().getTechnologies().getUnlockedTechnologies();
        int randomNum = (int) (Math.random() * unlockedTechnologies.size());
        this.getCivilization().getTechnologies().learnTechnology(unlockedTechnologies.get(randomNum));
    }

    public void makingTilesVisible() {
        ArrayList<Tile> mapTiles = GameDataBase.getGameDataBase().getMap().getAllMapTiles();
        int randomNum = (int) (Math.random() * mapTiles.size());
        ArrayList<Tile> tiles = GameController.getGameController().getAdjacentTiles(mapTiles.get(randomNum));
        for (Tile tile : tiles) {
            this.getCivilization().getMapImage().put(tile, tile);
        }
    }

    public void addPopulation() {
        this.getCivilization().getCapital().addCitizen();
    }

    public void addGold() {
        this.getCivilization().addGold(100);
    }

    public void createSettlerAndWorker() {
        ArrayList<Tile> allTiles = GameController.getGameController().getAdjacentTiles(this.getTile());
        allTiles.add(0, this.getTile());
        for (Tile tile : allTiles) {
            if (tile.doesPackingLetUnitEnter(UnitType.SETTLER)) {
                GameController.getGameController().createUnit(UnitType.SETTLER, this.getCivilization(), tile);
                break;
            }
        }
        for (Tile tile : allTiles) {
            if (tile.doesPackingLetUnitEnter(UnitType.WORKER)) {
                GameController.getGameController().createUnit(UnitType.WORKER, this.getCivilization(), tile);
                break;
            }
        }
    }

    public Tile getTile() {
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            if (tile.getRuins() == this)
                return tile;
        }
        return null;
    }

    public Civilization getCivilization() {
        return this.getTile().getCivilization();
    }

    public boolean isUsed() {
        return isUsed;
    }
}
