package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.interfaces.TerrainProperty;
import models.resources.Resources;

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
        //TODO .. change following command
        this.terrainType = terrainType;
        feature.plantFeatureOnTileAndApplyOutputChanges(feature, this);
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
            if (city.getTerritory().contains(this))
                return city;
        }
        return null;
    }

    public boolean isNearTheRiver() {
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if (river.getFisrtTile().equals(this) || river.getSecondTile().equals(this))
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
        if (this.terrainType == property || this.feature == property)
            return true;
        return false;
    }

    public void goToNextTurn() {
        // TODO
    }

    public TerrainType getTerrainType() {
        return this.terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Civilization getCivilization() {
        return this.civilization;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public HashMap<Resources, Integer> getResources() {
        return this.resources;
    }

    public void setResources(HashMap<Resources, Integer> resources) {
        this.resources = resources;
    }

    public ArrayList<Improvement> getImprovements() {
        return this.improvements;
    }

    public void setImprovements(ArrayList<Improvement> improvements) {
        this.improvements = improvements;
    }

    public ArrayList<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
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

    public void setRuins(Ruins ruins) {
        this.ruins = ruins;
    }

    public ArrayList<Work> getWorks() {
        return this.works;
    }

    public void setWorks(ArrayList<Work> works) {
        this.works = works;
    }

    public Output getOutput(){
        return this.output;
    }
}
