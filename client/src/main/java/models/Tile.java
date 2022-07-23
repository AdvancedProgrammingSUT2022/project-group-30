package models;

import com.google.gson.annotations.SerializedName;
import controllers.GameController;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.interfaces.Workable;
import models.resources.BonusResource;
import models.resources.Resource;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitType;
import models.works.Work;
import utilities.Debugger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Tile implements Workable, TileImage, TurnHandler {

    private final int id;
    public  int getId() {
        return id;
    }

    private static int newAvailableId = 0;

    private TerrainType terrainType;
    private HashMap<Resource, Integer> resources = new HashMap<>();
    private ArrayList<Improvement> improvements = new ArrayList<>();
    private Ruins ruins;
    private Work work;
    private Output output;
    private ArrayList<Feature> features = new ArrayList<>();

    @SerializedName("type")
    private String typeName;

    public Tile(TerrainType terrainType,
                HashMap<Resource, Integer> resources, Ruins ruins) {
        this.typeName = getClass().getName();
        this.id = newAvailableId;
        newAvailableId++;
        this.output = new Output(0, 0, 0);
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(terrainType, new ArrayList<>());
        this.resources = resources;
        this.ruins = ruins;
    }

    public TileHistory createTileHistory() {
        TileHistory history = new TileHistory();
        Tile tile = new Tile(terrainType, new HashMap<Resource, Integer>(resources), null);
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
        int numberOfRivers = 0;
        for (int i = 0; i < numberOfRivers; i++) {
            this.getOutput().add(new Output(1, 0, 0));
        }
    }

    public int numberOfRivers() {
        int count = 0;
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if (river.getFirstTile().equals(this) || river.getSecondTile().equals(this))
                count++;
        }
        return count;
    }

    public ArrayList<Resource> calculateCollectibeResourcesOutput() {
        ArrayList<Resource> collectibleResourceOutput = new ArrayList<>();
        for (Resource resource : resources.keySet()) {
            if (!(resource instanceof BonusResource) && resources.get(resource) > 0 && resource.canBeExploited(this)) {
                collectibleResourceOutput.add(resource);
            }
        }
        return collectibleResourceOutput;
    }

    public Output calculateOutput() {
        Output output = new Output(0, 0, 0);
        if (!this.hasCitizen())
            return output;
        output.add(this.output);
        for (Resource resource : this.resources.keySet()) {
            if (this.isImprovementTypeAccessible(resource.getPrerequisiteImprovement())) {
                for (int i = 0; i < this.resources.get(resource); i++)
                    output.add(resource.getOutput());
            }
        }
        for (Improvement improvement : this.getImprovements()) {
            output.add(improvement.getType().getOutput());
        }
        return output;
    }

    public Output calculateTheoreticalOutput(){
        Output output = new Output(0, 0, 0);
        output.add(this.output);
        for (Resource resource : this.resources.keySet()) {
            if (this.isImprovementTypeAccessible(resource.getPrerequisiteImprovement())) {
                for (int i = 0; i < this.resources.get(resource); i++)
                    output.add(resource.getOutput());
            }
        }
        for (Improvement improvement : this.getImprovements()) {
            output.add(improvement.getType().getOutput());
        }
        return output;
    }

    public City getCityOfTile() {
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (city.getTerritories().contains(this))
                return city;
        }
        return null;
    }

    public boolean doesPackingLetUnitEnter(Unit unit) {
        if (unit.isCivilian()) {
            if (GameController.getGameController().getCivilianUnitInTile(this) != null) {
                return false;
            }
        } else {
            if (GameController.getGameController().getMilitaryUnitInTile(this) != null) {
                return false;
            }
        }
        return true;
    }

    public boolean doesPackingLetUnitEnter(UnitType unitType) {
        if (unitType.getCombatType() == CombatType.CIVILIAN) {
            if (GameController.getGameController().getCivilianUnitInTile(this) != null) {
                return false;
            }
        } else {
            if (GameController.getGameController().getMilitaryUnitInTile(this) != null) {
                return false;
            }
        }
        return true;
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

    public Improvement getNonRouteImprovement() {
        for (Improvement improvement : improvements) {
            if (improvement.getType() != ImprovementType.ROAD && improvement.getType() != ImprovementType.RAILROAD) {
                return improvement;
            }
        }
        return null;
    }

    public void removeImprovement(Improvement improvement) {
        improvements.remove(improvement);
    }

    public void removeAllImprovements() {
        improvements = new ArrayList<>();
    }

    public boolean isOfType(TerrainProperty property) {
        if (this.terrainType.equals(property) || this.features.contains(property))
            return true;
        return false;
    }

    // returns -1 if the Tile is not in the map
    public int findTileXCoordinateInMap() {
        GameMap map = GameMap.getGameMap();
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[i].length; j++) {
                if (map.getMap()[i][j] == this) {
                    return j;
                }
            }
        }
        return -1;
    }

    public void addFeatureAndApplyChanges(Feature feature) {
        if (this.features.contains(feature)) {
            Debugger.debug("feature already exists");
            return;
        }
        ArrayList<Feature> featuresCopy = new ArrayList<>(features);
        featuresCopy.add(feature);
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(this.terrainType, featuresCopy);
    }

    public void removeFeatureAndApplyChanges(Feature feature) {
        if (!this.features.contains(feature)) {
            Debugger.debug("feature does not exist");
            return;
        }
        ArrayList<Feature> featuresCopy = new ArrayList<>(features);
        featuresCopy.remove(feature);
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(this.terrainType, featuresCopy);
    }

    public void removeAllFeaturesAndApplyChanges() {
        this.setTerrainTypeAndFeaturesAndApplyOutputChanges(this.terrainType, new ArrayList<Feature>());
    }


    public int findTileYCoordinateInMap() {
        GameMap map = GameMap.getGameMap();
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[i].length; j++) {
                if (map.getMap()[i][j] == this) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Improvement getRoadOfTile() {
        for (Improvement improvement : this.improvements) {
            if (improvement.getType() == ImprovementType.ROAD)
                return improvement;
        }
        return null;
    }

    public Improvement getRailRoadOfTile() {
        for (Improvement improvement : this.improvements) {
            if (improvement.getType() == ImprovementType.RAILROAD)
                return improvement;
        }
        return null;
    }

    public boolean isImprovementTypeAccessible(ImprovementType type) {
        if (this.containsImprovment(type)) {
            for (Improvement improvement : improvements) {
                if (improvement.getType() == type && improvement.getIsPillaged())
                    return true;
            }
        }
        return false;
    }

    public void addImprovement(Improvement improvement) {
        this.improvements.add(improvement);
    }

    public void removeImprovement(ImprovementType improvementType) {
        for (int i = 0; i < this.improvements.size(); i++) {
            if (this.improvements.get(i).getType() == improvementType) {
                this.improvements.remove(i);
                return;
            }
        }
    }

    public Improvement getImprovementByType(ImprovementType type) {
        for (Improvement improvement : this.improvements) {
            if (improvement.getType() == type)
                return improvement;
        }
        Debugger.debug("There is no improvement with this type");
        return null;
    }

    public void removeWork() {
        this.work = null;
    }

    public void goToNextTurn() {
        if (work != null) {
            work.goToNextTurn();
        }
    }

    public boolean hasCitizen() {
        City city = this.getCityOfTile();
        for (Citizen citizen : city.getCitizens()) {
            if (this == citizen.getWorkPlace()) {
                return true;
            }
        }
        return false;
    }

    public TerrainType getTerrainType() {
        return this.terrainType;
    }

    public ArrayList<Feature> getFeatures() {
        return this.features;
    }

    public Civilization getCivilization() {
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (city.getTerritories().contains(this)) {
                return city.getOwner();
            }
        }
        return null;
    }

    public HashMap<Resource, Integer> getResources() {
        return this.resources;
    }

    public ArrayList<Resource> getResourcesAsArrayList() {
        ArrayList<Resource> result = new ArrayList<>();
        for (Resource resource : resources.keySet()) {
            if (resources.get(resource) > 0) {
                result.add(resource);
            }
        }
        return result;
    }

    public boolean hasExploitableResource(Resource resource) {  // check if the tile has a resource and the proper improvement to use it
        if (hasResource(resource) && resource.canBeExploited(this)) {
            return true;
        }
        return false;
    }

    public boolean hasResource(Resource resource) {
        if (resources.containsKey(resource) && resources.get(resource) > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Improvement> getImprovements() {
        return this.improvements;
    }

    public ArrayList<Improvement> getUnpillagedImprovements() {
        ArrayList<Improvement> result = new ArrayList<Improvement>();
        for (Improvement improvement : improvements) {
            if (!improvement.getIsPillaged()) {
                result.add(improvement);
            }
        }
        return result;
    }

    public Ruins getRuins() {
        return this.ruins;
    }

    public Work getWork() {
        return this.work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Output getOutput() {
        return this.output;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public int calculateDistance(Tile tile) {
        int distance = 0;
        HashSet<Tile> checkedTiles = new HashSet<>();
        checkedTiles.add(this);
        while (!checkedTiles.contains(tile)) {
            distance++;
            HashSet<Tile> newTiles = new HashSet<>();
            for (Tile checkedTile : checkedTiles) {
                newTiles.addAll(GameController.getGameController().getAdjacentTiles(checkedTile));
            }
            checkedTiles.addAll(newTiles);
        }
        return distance;
    }

    public String getInfo() {
        String result = "Y: " + findTileYCoordinateInMap() + ", X: " + findTileXCoordinateInMap();
        result += "\n" + terrainType.getName();
        return result;
    }
}
