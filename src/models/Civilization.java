package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.interfaces.Producible;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.technology.Technology;
import utilities.Debugger;

public class Civilization {
    private final User user;
    private final String name;
    private ArrayList<Tile> revealedTiles = new ArrayList<>();
    private ArrayList<Tile> visibleTiles = new ArrayList<>();
    private HashMap<LuxuryResource, Integer> luxuryResources = new HashMap<>();
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

    public Civilization(User user, String name, City originCapital) {
        this.user = user;
        this.name = name;
        this.goldCount = 0;
        this.beakerCount = 0;
        // 20?? manteghie?
        this.happiness = 20;
        // 20??
        this.diplomaticCredit = 20;
        this.score = 0;
        this.capital = originCapital;
        this.originCapital = originCapital;
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

    public TileVisibility getTileVisibility(Tile tile) {
        // TODO
        return null;
    }


    public ArrayList<Tile> getRevealedTiles() {
        return revealedTiles;
    }

    public void setRevealedTiles(ArrayList<Tile> revealedTiles) {
        this.revealedTiles = revealedTiles;
    }

    public ArrayList<Tile> getVisibleTiles() {
        return visibleTiles;
    }

    public void setVisibleTiles(ArrayList<Tile> visibleTiles) {
        this.visibleTiles = visibleTiles;
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

}
