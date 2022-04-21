package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.buildings.Building;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.resources.Resources;
import models.works.Work;

public class Tile {
    private TerrainType terrainType;
    private Feature feature;
    private Civilization civilization;
    private HashMap<Resources, Integer> resources;
    private ArrayList<Improvement> improvements;
    private ArrayList<Building> buildings;
    private boolean isPillaged;
    private Ruins ruins;
    private ArrayList<Work> works;
    private Output output;

    public Tile(TerrainType terrainType, Feature feature, Civilization civilization,
            HashMap<Resources, Integer> resources, Ruins ruins) {
        TerrainType.setTerrainTypeToTileAndApllyOutputChanges(this, terrainType);
        Feature.plantFeatureOnTileAndApplyOutputChanges(feature, this);
        this.civilization = civilization;
        this.resources = resources;
        this.improvements = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.isPillaged = false;
        this.ruins = ruins;
        this.works = new ArrayList<>();
        this.output = new Output(0, 0, 0);
    }

    public Output calculateOutput(Output output) {
        // TODO
        return null;
    }

    public City getCityOfTile() {
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (city.getTerritories().contains(this))
                return city;
        }
        return null;
    }

    public boolean isNearTheRiver() {
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if (river.getFirstTile().equals(this) || river.getSecondTile().equals(this))
                return true;
        }
        return false;
    }

    public boolean isCityOfTileNearTheRiver() {
        City city = this.getCityOfTile();
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if (city.getTerritories().contains(river.getFisrtTile())
                    || city.getTerritories().contains(river.getSecondTile()))
                return true;
        }
        return false;
    }

    public boolean containsImprovment(ImprovementType type) {
        for (Improvement improvement : this.improvements) {
            if (improvement.getType() == type)
                return true;
        }
        return false;
    }

    public boolean isOfType(TerrainProperty property) {
        if (this.terrainType.equals(property) || this.feature == property)
            return true;
        return false;
    }

    public void addImprovement(Improvement improvement) {
        // TODO
    }

    public void removeWork() {
        // TODO
    }

    public void goToNextTurn() {
        // TODO
    }

    public TerrainType getTerrainType() {
        return this.terrainType;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public Civilization getCivilization() {
        return this.civilization;
    }

    public HashMap<Resources, Integer> getResources() {
        return this.resources;
    }

    public ArrayList<Improvement> getImprovements() {
        return this.improvements;
    }

    public ArrayList<Building> getBuildings() {
        return this.buildings;
    }

    public boolean getIsPillaged() {
        return this.isPillaged;
    }

    public void setIsPillaged(boolean isPillaged) {
        this.isPillaged = isPillaged;
    }

    public Ruins getRuins() {
        return this.ruins;
    }

    public ArrayList<Work> getWorks() {
        return this.works;
    }

    public Output getOutput() {
        return this.output;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
}
