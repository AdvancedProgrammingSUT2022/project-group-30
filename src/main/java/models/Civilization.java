package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.technology.Technology;
import models.units.Unit;
import utilities.Debugger;

public class Civilization implements TurnHandler {
    private final String name;
    private HashMap<Tile, TileImage> mapImage = new HashMap<>();
    private HashMap<LuxuryResource, Integer> luxuryResources;
    private HashMap<StrategicResource, Integer> strategicResources = new HashMap<>();
    private ArrayList<Technology> technologies = new ArrayList<>();
    private double goldCount;
    private double beakerCount;
    private Technology researchProject;
    private HashMap<Technology, Integer> researchReserve = new HashMap<>();
    private double happiness;
    private double diplomaticCredit;
    private double score;
    private City capital;
    private final City originCapital;
    private Tile frameBase;
    private Selectable selectedEntity;

    public Civilization(String name) {
        this.name = name;
        this.goldCount = 0;
        this.beakerCount = 0;
        // 20?? manteghie?
        this.happiness = 20;
        // 20??
        this.diplomaticCredit = 20;
        this.score = 0;
        this.capital = null;
        this.originCapital = null;
        mapImage = new HashMap<>();
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            mapImage.put(tile, null);
        }
        this.frameBase = GameMap.getGameMap().getTile(0, 0);
    }

    public TileImage getTileImage(Tile tile) {
        if (mapImage.containsKey(tile) == false) {
            Debugger.debug("The tile passed to Civilization's getTileImage method is not in the GameMap: " + tile);
            return null;
        }
        return mapImage.get(tile);
    }

    public TileVisibility getTileVisibility(Tile tile) {
        if (mapImage.containsKey(tile) == false) {
            Debugger.debug("The tile passed to Civilization's getTileVisibility method is not in the GameMap: " + tile);
            return null;
        }
        if (mapImage.get(tile) == null) {
            return TileVisibility.FOG_OF_WAR;
        } else if (mapImage.get(tile) instanceof TileHistory) {
            return TileVisibility.REVEALED;
        } else if (mapImage.get(tile) instanceof Tile) {
            return TileVisibility.VISIBLE;
        } else {
            return null;
        }
    }

    public ArrayList<Unit> getUnits() {
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit unit : GameDataBase.getGameDataBase().getUnits()) {
            if (unit.getOwner() == this)
                units.add(unit);
        }
        return units;
    }

    public ArrayList<City> getCities() {
        ArrayList<City> cities = new ArrayList<>();
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (city.getOwner() == this) {
                cities.add(city);
            }
        }
        return cities;
    }

    public double getNumberOfRoads() {
        double count = 0;
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            for (Improvement improvement : tile.getImprovements()) {
                if (improvement.getType() == ImprovementType.ROAD && improvement.getFounder() == this)
                    count++;
            }
        }
        return count;
    }

    public double getNumberOfRailRoads() {
        double count = 0;
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            for (Improvement improvement : tile.getImprovements()) {
                if (improvement.getType() == ImprovementType.RAILROAD && improvement.getFounder() == this)
                    count++;
            }
        }
        return count;
    }

    public void goToNextTurn() {
        // TODO
    }

    public void setNextResearchProject(Technology technology) {
        //TODO
    }

    private void updateStrategicResources() {
        // TODO
    }

    public double calculateNetGoldProduction() {
        double gold = 0;
        for (City city : this.getCities()) {
            gold += city.calculateOutput().getGold();
        }
        return 0;
    }

    public double calculateTotalCosts() {
        double cost = 0;
        for (City city : this.getCities()) {
            cost += city.calculateTotalGoldCosts();
        }
        for (Unit unit : this.getUnits()) {
            cost += unit.getType().getCost() * 0.1;
        }
        cost += this.getNumberOfRoads() * 1;/*1 gold per turn for each unit*/
        cost += this.getNumberOfRailRoads();
        //MINETODO ... add "stepwisegold..." effects
        return cost;
    }

/*    public double calculateHappiness(){
        double happiness = 0;

    }*/

    public double calculateGoldConsumption() {
        return this.calculateNetGoldProduction() - this.calculateTotalCosts();
    }

    public ArrayList<Producible> findUnlockeProducibles() {
        // TODO
        return null;
    }

    public double calculateTotalPopulation() {
        // TODO
        return 0;
    }

    public double calculateCityCount() {
        // TODO
        return 0;
    }

    public double calculateAnnexedCityCount() {
        // TODO
        return 0;
    }

    public double calculateLuxuryResourceType() {
        // TODO
        return 0;
    }

    public boolean hasTechnology(Technology technology) {
        // TODO
        return true;
    }

    public double getGoldCount() {
        return this.goldCount;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    public HashMap<LuxuryResource, Integer> getLuxuryResources() {
        return luxuryResources;
    }

    public void setLuxuryResources(HashMap<LuxuryResource, Integer> luxuryResources) {
        this.luxuryResources = luxuryResources;
    }

    public HashMap<StrategicResource, Integer> getStrategicResources() {
        return strategicResources;
    }

    public void setStrategicResources(HashMap<StrategicResource, Integer> strategicResources) {
        this.strategicResources = strategicResources;
    }

    public ArrayList<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(ArrayList<Technology> technologies) {
        this.technologies = technologies;
    }

    public double getBeakerCount() {
        return beakerCount;
    }

    public void setBeakerCount(double beakerCount) {
        this.beakerCount = beakerCount;
    }

    public Technology getResearchProject() {
        return researchProject;
    }

    public void setResearchProject(Technology researchProject) {
        this.researchProject = researchProject;
    }

    public HashMap<Technology, Integer> getResearchReserve() {
        return researchReserve;
    }

    public void setResearchReserve(HashMap<Technology, Integer> researchReserve) {
        this.researchReserve = researchReserve;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public double getDiplomaticCredit() {
        return diplomaticCredit;
    }

    public void setDiplomaticCredit(double diplomaticCredit) {
        this.diplomaticCredit = diplomaticCredit;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public City getCapital() {
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
    }

    public City getOriginCapital() {
        return originCapital;
    }

    public void setFrameBase(Tile tile) {
        this.frameBase = tile;
    }

    public Tile getFrameBase() {
        return this.frameBase;
    }

    public String getName() {
        return name;
    }

    public HashMap<Tile, TileImage> getMapImage() {
        return this.mapImage;
    }

    public Selectable getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Selectable selectedEntity) {
        this.selectedEntity = selectedEntity;
    }
}
