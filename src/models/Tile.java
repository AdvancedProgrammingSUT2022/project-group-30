package models;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.GameController;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.interfaces.Workable;
import models.resources.Resource;
import models.units.Unit;
import models.works.Work;

public class Tile implements Workable, TileImage, TurnHandler {
    private TerrainType terrainType;
    private Civilization civilization;
    private HashMap<Resource, Integer> resources = new HashMap<>();
    private ArrayList<Improvement> improvements = new ArrayList<>();
    private Ruins ruins;
    private ArrayList<Work> works;  // shouldn't this be a field?
    private Output output;
    private ArrayList<Feature> features = new ArrayList<>();

    public Tile(TerrainType terrainType, Civilization civilization,
            HashMap<Resource, Integer> resources, Ruins ruins) {
        this.output = new Output(0, 0, 0);
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(terrainType, new ArrayList<>());
        this.civilization = civilization;
        this.resources = resources;
        this.ruins = ruins;
        this.works = new ArrayList<>();
    }

    public TileHistory createTileHistory() {
        // TODO : doesn't save works in history
        TileHistory history = new TileHistory();
        Tile tile = new Tile(terrainType, civilization, new HashMap<Resource, Integer>(resources), null);
        tile.setTerrainTypeAndFeaturesAndApplyOutputChanges(terrainType, features);
        if (this.ruins != null) {
            tile.ruins = this.ruins.createImage();
        }
        for (Improvement improvement : improvements) {
            tile.improvements.add(improvement.createImage());
        }
        history.setTile(tile);
        ArrayList<Unit> units = GameController.getGameController().getUnitsInTile(this);
        for (Unit unit : units) {
            history.getUnits().add(unit.createImage());
        }
        City city = GameController.getGameController().getCityCenteredInTile(this);
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

    public City getCityOfTile()   {
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (city.getTerritories().contains(this))
                return city;
        }
        return null;
    }

    public boolean isNearTheRiver()   {
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

    // returns -1 if the Tile is not in the map
    public int findTileXCoordinateInMap()  {
        GameMap map = GameMap.getGameMap();
        for(int i = 0; i < map.getMap().length; i++){
            for(int j = 0; j < map.getMap()[i].length; j++){
                if(map.getMap()[i][j] == this){
                    return j;
                }
            }
        }
        return -1;
    }

    public int findTileYCoordinateInMap()  {
        GameMap map = GameMap.getGameMap();
        for(int i = 0; i < map.getMap().length; i++){
            for(int j = 0; j < map.getMap()[i].length; j++){
                if(map.getMap()[i][j] == this){
                    return i;
                }
            }
        }
        return -1;
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

    public HashMap<Resource, Integer> getResources() {
        return this.resources;
    }

    public ArrayList<Improvement> getImprovements() {
        return this.improvements;
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
