package models;

import java.util.ArrayList;
import java.util.HashMap;

import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TurnHandler;
import models.interfaces.Workable;
import models.units.Unit;

public class City implements Selectable, TurnHandler {
    private final Civilization founder;
    private Civilization owner;
    // Should I delete following field??
    private boolean isPuppet;
    private final Tile centralTile;
    // should I add it in constructor??
    private ArrayList<Tile> territories = new ArrayList<>();
    private HashMap<Producible, Integer> productionReserve = new HashMap<>();
    private Producible entityInProduction;
    private double hammerCount;
    private double foodCount;
    private double combatStrength;
    private double rangedCombatStrength;
    private double range;
    private double hitPoints;
    // TODO initialize 5 following fields with proper number
    private double expansionProgress;
    private double expansionLimit;
    private double populationProgress;
    private double populationGrowthLimit;
    private double populationShrinkageLimit;
    private ArrayList<Citizen> citizens = new ArrayList<>();

    // MINETODO where is the appropriate place to initialze combatStrength and
    // rangedCombatStrength...(Maybe instead of get, you should calculate them)
    public City(Civilization founder, Tile tile, double combatStrength, double rangedCombatStrength) {  // WARNING : if you change this, apply the same changes to the createImage method, he makes copies :)
        this.founder = founder;
        this.owner = founder;
        this.isPuppet = false;
        this.centralTile = tile;
        this.territories.add(tile);
        this.hammerCount = 0;
        this.foodCount = 0;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.hitPoints = 20;
        this.range = 2;
    }

    public City createImage() {
        City image = new City(founder, centralTile, combatStrength, rangedCombatStrength);
        image.setOwner(owner);
        image.setIsPuppet(isPuppet);
        image.territories = new ArrayList<>(territories);
        image.setHammerCount(hammerCount);
        image.setFoodCount(foodCount);
        image.setHitPoints(hitPoints);
        return image;
    }

    public void goToNextTurn() {
        // TODO
    }

    public double calculateFoodProduction() {
        // TODO
        return 0;
    }

    public double calculateGoldProduction() {
        // TODO
        return 0;
    }

    public double calculateProduction() {
        // TODO
        return 0;
    }

    public double getScienceProduction() {
        // TODO
        return 0;
    }

    public void attack(Unit target) {
        // TODO
    }

    public void defend(Unit attacker) {
        // TODO
    }

    public double calculateEffectiveCombatStrength() {
        // TODO
        return 0;
    }

    public double calculateEffectiveRangedCombatStrength() {
        // TODO
        return 0;
    }

    public void addCitizen() {
        // MINETODO check it
        this.citizens.add(new Citizen());
    }

    public void killACitizen() {
        // MINETODO check it
        // which one??
    }

    public void removeCitizenFromWork(Citizen citizen) {
        // MineTODO check it & check errors
        citizen.setWorkPlace(null);
    }

    public void emptyWorkable(Workable workable) {
        // MINETODO
    }

    public void assignCitizenToWorkplace(Workable workPlace, Citizen citizen) {
        // MINETODO check it check errors...
        citizen.setWorkPlace(workPlace);
    }

    private void expandSelf() {
        // TODO
    }

    public boolean isDestructible() {
        // MINETODO check it
        if (!owner.equals(founder))
            return true;
        return false;
    }

    public Civilization getFounder() {
        return founder;
    }

    public Civilization getOwner() {
        return owner;
    }

    public void setOwner(Civilization owner) {
        this.owner = owner;
    }

    public boolean isIsPuppet() {
        return isPuppet;
    }

    public void setIsPuppet(boolean isPuppet) {
        this.isPuppet = isPuppet;
    }

    public Tile getCentralTile() {
        return centralTile;
    }

    public void setTerritories(ArrayList<Tile> territories) {
        this.territories = territories;
    }

    public HashMap<Producible, Integer> getProductionReserve() {
        return productionReserve;
    }

    public void setProductionReserve(HashMap<Producible, Integer> productionReserve) {
        this.productionReserve = productionReserve;
    }

    public Producible getEntityInProduction() {
        return entityInProduction;
    }

    public void setEntityInProduction(Producible entityInProduction) {
        this.entityInProduction = entityInProduction;
    }

    public double getCombatStrength() {
        return combatStrength;
    }

    public void setCombatStrength(double combatStrength) {
        this.combatStrength = combatStrength;
    }

    public double getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public void setRangedCombatStrength(double rangedCombatStrength) {
        this.rangedCombatStrength = rangedCombatStrength;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public double getExpansionProgress() {
        return expansionProgress;
    }

    public void setExpansionProgress(double expansionProgress) {
        this.expansionProgress = expansionProgress;
    }

    public double getExpansionLimit() {
        return expansionLimit;
    }

    public void setExpansionLimit(double expansionLimit) {
        this.expansionLimit = expansionLimit;
    }

    public double getPopulationProgress() {
        return populationProgress;
    }

    public void setPopulationProgress(double populationProgress) {
        this.populationProgress = populationProgress;
    }

    public double getPopulationGrowthLimit() {
        return populationGrowthLimit;
    }

    public void setPopulationGrowthLimit(double populationGrowthLimit) {
        this.populationGrowthLimit = populationGrowthLimit;
    }

    public double getPopulationShrinkageLimit() {
        return populationShrinkageLimit;
    }

    public void setPopulationShrinkageLimit(double populationShrinkageLimit) {
        this.populationShrinkageLimit = populationShrinkageLimit;
    }

    public ArrayList<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(ArrayList<Citizen> citizens) {
        this.citizens = citizens;
    }

    public double getHammerCount() {
        return hammerCount;
    }

    public double getFoodCount() {
        return foodCount;
    }

    public ArrayList<Tile> getTerritories() {
        return territories;
    }

    public void setHammerCount(double hammerCount) {
        this.hammerCount = hammerCount;
    }

    public void setFoodCount(double foodCount) {
        this.foodCount = foodCount;
    }

}
