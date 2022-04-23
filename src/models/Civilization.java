package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import models.interfaces.Producible;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.technology.Technology;
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

    public void goToNextTurn() {
        // TODO
    }

    public void setNextResearchProject(Technology technology) {
        // MINETODO
    }

    private void updateStrategicResources() {
        // TODO
    }

    public double calculateNetFoodProduction() {
        // TODO
        return 0;
    }

    public double calculateNetFoodConsumption() {
        // TODO
        return 0;
    }

    public double calculateNetProduction() {
        // TODO
        return 0;
    }

    public double calculateNetGoldProduction() {
        // TODO
        return 0;
    }

    public double calculateTotalCosts() {
        // TODO
        return 0;
    }

    public double calculateNetScienceProduction() {
        // MINETODO
        return 0;
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
}
