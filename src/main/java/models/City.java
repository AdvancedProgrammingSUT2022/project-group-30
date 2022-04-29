package models;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import controllers.GameController;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TurnHandler;
import models.interfaces.Workable;
import models.interfaces.combative;
import models.resources.Resource;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import utilities.Debugger;

public class City implements Selectable, TurnHandler, combative {
    private final Civilization founder;
    private Civilization owner;
    // Should I delete following field??
    private boolean isPuppet;
    private final Tile centralTile;
    private ArrayList<Building> buildings = new ArrayList<>();
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

    public City(Civilization founder, Tile tile) {
        this.founder = founder;
        this.owner = founder;
        this.isPuppet = false;
        this.centralTile = tile;
        this.territories.add(tile);
        this.hammerCount = 0;
        this.foodCount = 0;
        this.combatStrength = 8;
        this.rangedCombatStrength = 5;
        this.hitPoints = 20;
        this.range = 2;
        this.populationGrowthLimit = 10;
        this.populationShrinkageLimit = -10;
        this.expansionLimit = 1;
        this.populationProgress = 0;
        this.expansionProgress = 0;
        this.expansionProgress = 0;
    }

    public City createImage() {
        City image = new City(founder, centralTile);
        image.setOwner(owner);
        image.setIsPuppet(isPuppet);
        image.territories = new ArrayList<>(territories);
        image.setHammerCount(hammerCount);
        image.setFoodCount(foodCount);
        image.setHitPoints(hitPoints);
        for (Building building : buildings) {
            image.getBuildings().add(building.createImage());
        }
        return image;
    }

    public void goToNextTurn() {
        int production = calculateOutput().getProduction();
        hammerCount += production;
        if (entityInProduction != null && hammerCount >= entityInProduction.calculateHammerCost()) {
            finishProduction();
        }

        foodCount += calculateFoodChange();
        if (foodCount <= populationShrinkageLimit) {
            killACitizen();
            foodCount = 0;
        }
        if (foodCount >= populationGrowthLimit) {
            addCitizen();
            foodCount = 0;
        }
        if (owner.getHappiness() >= 0) {
            expansionProgress += 1;
            while (expansionProgress >= expansionLimit) {
                expansionProgress -= expansionLimit;
                growTerritory();
            }
        }
    }

    private void finishProduction() {
        if (entityInProduction instanceof  BuildingType) {
            addBuilding((BuildingType) entityInProduction);
        } else if (entityInProduction instanceof UnitType) {
            if (centralTile.doesPackingLetUnitEnter((UnitType) entityInProduction)) {
                GameController.getGameController().createUnit((UnitType) entityInProduction, owner, centralTile);
            } else {
                Debugger.debug("finishProduction of City.java: central tile shouldn't be full!");
                return;
            }
        }

        hammerCount -= entityInProduction.calculateHammerCost();
        entityInProduction = null;

        // TODO : send notification to the player informing them of the end of production
    }

    public ArrayList<Resource> calculateCollectibleResourceOutput() {
        ArrayList<Resource> collectibleResources = new ArrayList<>();
        for (Tile territory : territories) {
            collectibleResources.addAll(territory.calculateCollectibeResourcesOutput());
        }

        return collectibleResources;
    }

    public ArrayList<UnitType> calculatePurchasableUnitTypes() {    // DOESN'T CHECK IF CITY HAS ENOUGH MONEY TO PURCHASE UNIT
        ArrayList<UnitType> result = new ArrayList<>();
        for (UnitType type : UnitType.values()) {
            if (type == UnitType.SETTLER) {
                if (citizens.size() < 2 || owner.calculateHappiness() < 0) {
                    continue;
                }
            }
            if (owner.hasTechnology(type.getPrerequisitTechnology())) {
                result.add(type);
            }
        }
        return result;
    }

    public ArrayList<BuildingType> calculatePurchasableBuildingTypes() {    // DOESN'T CHECK IF CITY HAS ENOUGH GOLD TO PURCHASE BUILDING
        return calculateProductionReadyBuildingTypes();
    }

    public ArrayList<UnitType> calculateProductionReadyUnitTypes() {
        ArrayList<UnitType> result = new ArrayList<>();
        for (UnitType type : UnitType.values()) {
            if (type == UnitType.SETTLER) {
                if (citizens.size() < 2 || owner.calculateHappiness() < 0) {
                    continue;
                }
            }
            if (owner.hasTechnology(type.getPrerequisitTechnology()) && owner.hasStrategicResources(type.getPrerequisiteResources())) {
                result.add(type);
            }
        }
        return result;
    }

    public ArrayList<BuildingType> calculateProductionReadyBuildingTypes() {
        ArrayList<BuildingType> result = new ArrayList<BuildingType>();
        for (BuildingType type : BuildingType.values()) {
            if (owner.hasTechnology(type.getPrerequisiteTechnology()) && hasBuildingType(type) == false) {
                result.add(type);
            }
        }
        return result;
    }

    public void changeProduction(Producible producible) {
        if (entityInProduction == null) {
            if (productionReserve.containsKey(producible)) {
                hammerCount += productionReserve.get(producible);
                productionReserve.remove(producible);
            }
        } else {
            productionReserve.put(entityInProduction, (int) hammerCount);
            hammerCount = 0;
            entityInProduction = null;
            changeProduction(producible);
        }
        entityInProduction = producible;
        if (hammerCount >= producible.calculateHammerCost()) {
            finishProduction();
        }
    }

    public void stopProduction() {
        productionReserve.put(entityInProduction, (int) hammerCount);
        entityInProduction = null;
        hammerCount = 0;
    }

    public void addBuilding(BuildingType type) {
        if (hasBuildingType(type)) {
            return;
        }
        buildings.add(new Building(type));
    }

    private void growTerritory() {
        for (Tile territory : territories) {
            ArrayList<Tile> adjacentTiles = GameController.getGameController().getAdjacentTiles(territory);
            Collections.shuffle(adjacentTiles);
            for (Tile adjacentTile : adjacentTiles) {
                if (territories.contains(adjacentTile) == false && adjacentTile.getCityOfTile() == null) {
                    territories.add(adjacentTile);
                    return;
                }
            }
        }
    }

    public boolean isTileBeingWorked(Tile tile) {
        for (Citizen citizen : citizens) {
            if (citizen.getWorkPlace() == tile) {
                return true;
            }
        }
        return false;
    }

    public Unit getGarrisoningUnit() {      // return null if city is not garrisoned, return the garrisoning unit otherwise
        for (Unit unit : GameDataBase.getGameDataBase().getUnits()) {
            if (unit.getLocation() == centralTile && unit.getOwner() == owner && unit.getState() == UnitState.GARRISON) {
                return unit;
            }
        }
        return null;
    }

    public Output calculateOutputOfBuildings() {
        Output output = new Output(0, 0, 0);
        if (this.hasBuildingType(BuildingType.GRANARY)) output.add(new Output(0, 2, 0));
        if (this.hasBuildingType(BuildingType.WATER_MILL)) output.add(new Output(0, 2, 0));
        if (this.hasBuildingType(BuildingType.MINT)) {
            if (this.hasResourceByName("Gold"))
                output.add(new Output((int) (3 * this.findNumberOfResourceByName("Gold")), 0, 0));
            if (this.hasResourceByName("Silver"))
                output.add(new Output((int) (3 * this.findNumberOfResourceByName("Silver")), 0, 0));
        }
        if (this.hasBuildingType(BuildingType.PALACE)) output.add(new Output(2, 0, 2));
        return output;
    }

    public Output calculateBuildingsEffectsOnPercentageOfCityOutput() {
        Output output = new Output(0, 0, 0);
        if (this.hasBuildingType(BuildingType.MARKET)) output.add(new Output(25, 0, 0));
        if (this.hasBuildingType(BuildingType.BANK)) output.add(new Output(25, 0, 0));
        if (this.hasBuildingType(BuildingType.SATRAPS_COURT)) output.add(new Output(25, 0, 0));
        if (this.hasBuildingType(BuildingType.WINDMILL)) output.add(new Output(0, 0, 15));
        if (this.hasBuildingType(BuildingType.FACTORY)) output.add(new Output(0, 0, 50));
        if (this.hasBuildingType(BuildingType.STOCK_EXCHANGE)) output.add(new Output(33, 0, 0));
        return output;
    }

    public Output calculateOutput() {
        Output output = new Output(0, 0, 0);
        for (Tile tile : this.territories) {
            output.add(tile.calculateOutput());
        }
        for (Citizen citizen : this.citizens) {
            if (citizen.isWorkless()) {
                output.add(new Output(0, 0, 1));
            }
        }
        output.add(this.calculateOutputOfBuildings());
        output.times(this.calculateBuildingsEffectsOnPercentageOfCityOutput());
        return output;
    }

    public double calculateRequiredFood() {
        if (this.hasBuildingType(BuildingType.HOSPITAL)) return this.citizens.size() * 1;
        return this.citizens.size() * 2;
    }

    public double calculateFoodChange() {
        double amount = this.calculateOutput().getFood();
        amount -= calculateRequiredFood();
        if (this.owner.getHappiness() < 0) amount = amount * 33.0 / 100;
        return amount;
    }

    public double calculateBeakerProduction() {
        double count = 3;//3 beakers per turn for capital(palace)
        double percentage = 100;
        for (City city : this.founder.getCities()) {
            count += city.getCitizens().size();
        }
        if (this.hasBuildingType(BuildingType.LIBRARY)) count += 2;
        if (this.hasBuildingType(BuildingType.UNIVERSITY)) percentage += 50;
        if (this.hasBuildingType((BuildingType.PUBLIC_SCHOOL))) percentage += 50;
        return count * percentage / 100.0;
    }

    public double calculateTotalGoldCosts() {
        double maintenanceCost = 0;
        for (Building building : this.buildings) {
            maintenanceCost += building.getType().getMaintenanceCost();
        }
        return maintenanceCost;
    }

    public double calculateHappiness() {
        double happiness = 0;
        for (Building building : this.buildings) {
            happiness += building.getType().getHappiness();
        }
        happiness -= this.citizens.size() * 0.33;
        if (this.hasBuildingType(BuildingType.COURTHOUSE) && happiness < 0)
            happiness = 0;
        return happiness;
    }

    public boolean hasBuildingType(BuildingType type) {
        for (Building building : this.buildings) {
            if (building.getType() == type) return true;
        }
        return false;
    }

    public boolean isNearTheRiver() {
        for (Tile tile : this.getTerritories()) {
            if (tile.isNearTheRiver()) return true;
        }
        return false;
    }

    public boolean hasResourceByName(String name) {
        for (Tile tile : this.territories) {
            for (Resource resource : tile.getResources().keySet()) {
                if (resource.getName().equals(name)) return true;
            }
        }
        return false;
    }

    public double findNumberOfResourceByName(String name) {
        double count = 0;
        for (Tile tile : this.territories) {
            for (Resource resource : tile.getResources().keySet()) {
                if (resource.getName().equals(name)) count += tile.getResources().get(resource);
            }
        }
        return count;
    }

    public int calculateWorklessCitizenCount() {
        int count = 0;
        for (Citizen citizen : citizens) {
            if (citizen.isWorkless()) {
                count++;
            }
        }
        return count;
    }

    public Citizen getWorklessCitizen() {
        for (Citizen citizen : citizens) {
            if (citizen.isWorkless()) {
                return citizen;
            }
        }
        return null;
    }

    public ArrayList<Tile> getUnworkedTiles() {
        ArrayList<Tile> unworkedTiles = new ArrayList<>();
        for (Tile tile : territories) {
            if (!isWorkableWorked(tile)) {
                unworkedTiles.add(tile);
            }
        }
        return unworkedTiles;
    }

    public ArrayList<Building> getUnworkedBuildings() {
        ArrayList<Building> unworkedBuildings = new ArrayList<>();
        for (Building building : buildings) {
            if (!isWorkableWorked(building)) {
                unworkedBuildings.add(building);
            }
        }
        return unworkedBuildings;
    }

    public boolean isWorkableWorked(Workable workable) {
        for (Citizen citizen : citizens) {
            if (citizen.getWorkPlace() == workable) {
                return true;
            }
        }
        return false;
    }

    public void addTileToTerritory(Tile tile) {
        if (!territories.contains(tile)) {
            territories.add(tile);
        }
    }

    public boolean isCapital() {
        return (owner.getCapital() == this);
    }

    public void attack(Unit target) {
        // TODO
    }

    public void defend(Unit attacker) {
        // TODO
    }

    public double calculateEffectiveCombatStrength() {
        // TODO
        //TODO +5 Defense if the city has Walls  -> you can use "if(this.hasBuildingType(BuildingType.WALLS))"
        //TODO +7.5 Defense if the city has Castle
        return 0;
    }

    public double calculateEffectiveRangedCombatStrength() {
        // TODO
        return 0;
    }

    public void addCitizen() {
        // MINETODO check it
        //TODO
        this.citizens.add(new Citizen());
    }

    public void killACitizen() {
        // MINETODO check it
        // which one??
        //TODO

        if (citizens.isEmpty()) {
            return;
        }
        citizens.remove(0);
    }

    public void removeCitizenFromWork(Citizen citizen) {
        // MineTODO check it & check errors
        citizen.setWorkPlace(null);
    }

    public void emptyWorkable(Workable workable) {
        // MINETODO
    }

    public void assignCitizenToWorkplace(Workable workPlace, Citizen citizen) {
        // MINETODO check it check errors...    Amir: errors have been checked in view: the citizen passed is guaranteed to be workless and the tile is guaranteed to be unworked
        citizen.setWorkPlace(workPlace);
    }

    public Citizen getCitizenAssignedToTile(Tile tile) {
        for (Citizen citizen : citizens) {
            if (citizen.getWorkPlace() == tile) {
                return citizen;
            }
        }
        return null;
    }

    private void expandSelf() {
        // TODO
    }

    public boolean isDestructible() {
        // MINETODO check it
        if (!owner.equals(founder)) return true;
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

    public ArrayList<Building> getBuildings() {
        return buildings;
    }
}
