package models;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.GameController;
import models.buildings.Building;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.interfaces.TileImage;
import models.interfaces.Workable;
import models.resources.Resources;
import models.units.Unit;
import models.works.Work;

public class Tile implements Workable, TileImage {
    private TerrainType terrainType;
    private Civilization civilization;
    private HashMap<Resources, Integer> resources = new HashMap<>();
    private ArrayList<Improvement> improvements = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private Ruins ruins;
    private ArrayList<Work> works;
    private Output output;
    private ArrayList<Feature> features = new ArrayList<>();

    public Tile(TerrainType terrainType, Civilization civilization,
            HashMap<Resources, Integer> resources, Ruins ruins) {
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(terrainType, null);
        this.civilization = civilization;
        this.resources = resources;
        this.ruins = ruins;
        this.output = new Output(0, 0, 0);
    }

    public TileHistory createTileHistory() {
        TileHistory history = new TileHistory();
        Tile tile = new Tile(terrainType, civilization, new HashMap<Resources, Integer>(resources), ruins);
        history.setTile(tile);
        ArrayList<Unit> units = GameController.getGameController().getUnitsInTile(this);
        for (Unit unit : units) {
            history.getUnits().add(unit.createImage());
        }
        for (Improvement improvement : improvements) {
            history.getImprovements().add(improvement.createImage());
        }
        for (Building building : buildings) {
            history.getBuildings().add(building.createImage());
        }
        City city = GameController.getGameController().getCityInTile(this);
        if (city == null) {
            history.setCity(null);
        } else {
            history.setCity(city.createImage());
        }
        
        return history;
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
