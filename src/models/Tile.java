package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.buildings.Building;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.interfaces.Workable;
import models.resources.Resources;
import models.works.Work;
import utilities.Debugger;

public class Tile implements Workable {
    private TerrainType terrainType;
    private Civilization civilization;
    private HashMap<Resources, Integer> resources = new HashMap<>();
    private ArrayList<Improvement> improvements = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private boolean isPillaged;
    private Ruins ruins;
    private ArrayList<Work> works;
    private Output output;
    private ArrayList<Feature> features = new ArrayList<>();

    public Tile(TerrainType terrainType, Civilization civilization,
            HashMap<Resources, Integer> resources, Ruins ruins) {
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(terrainType, null);
        this.civilization = civilization;
        this.resources = resources;
        this.isPillaged = false;
        this.ruins = ruins;
        this.output = new Output(0, 0, 0);
    }

    public void setTerrainTypeAndFeaturesAndApplyOutputChanges(TerrainType terrainType, ArrayList<Feature> features) {
        this.setTerrainType(terrainType);
        if (features == null || features.isEmpty() || !features.contains(Feature.FOREST))
            this.getOutput().add(terrainType.getOutput());
        if (features == null)
            return;
        this.features = features;
        for (Feature feature : features) {
            this.getOutput().add(feature.getOutput());
        }
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
            if (city.getTerritories().contains(river.getFirstTile())
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
        if (this.terrainType.equals(property) || this.features.contains(property))
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

    public ArrayList<Feature> getFeatures() {
        return this.features;
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

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
}
