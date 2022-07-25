package models;

import controllers.GameController;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.interfaces.*;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import utilities.Debugger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class City implements Selectable, TurnHandler, combative, java.io.Serializable{
    private static final int MAXHITPOINTS = 20;
    private final Civilization founder;
    private Civilization owner;
    private final Tile centralTile;
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Tile> territories = new ArrayList<>();
    private HashMap<Producible, Integer> productionReserve = new HashMap<>();
    private Producible entityInProduction;
    private double hammerCount;
    private double foodCount;
    private double combatStrength;
    private double rangedCombatStrength;
    private double range;
    private double hitPoints;
    private boolean hasAttackedThisTurn;
    private double expansionProgress;
    private double expansionLimit;
    private double populationProgress;
    private double populationGrowthLimit;
    private double populationShrinkageLimit;
    private ArrayList<Citizen> citizens = new ArrayList<>();
    private boolean isDefeated;


    public City(Civilization founder, Tile tile) {
        this.founder = founder;
        this.owner = founder;
        this.centralTile = tile;
        this.territories.add(tile);
        this.hammerCount = 0;
        this.foodCount = 0;
        this.hasAttackedThisTurn = false;
        this.combatStrength = 8;
        this.rangedCombatStrength = 5;
        this.hitPoints = MAXHITPOINTS;
        this.range = 2;
        this.populationGrowthLimit = 10;
        this.populationShrinkageLimit = -10;
        this.expansionLimit = 1;
        this.populationProgress = 0;
        this.expansionProgress = 0;
    }

    public City createImage() {
        City image = new City(founder, centralTile);
        image.setOwner(owner);
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
        double production = calculateOutput().getProduction();
        production *= calculateBuildingEffectCoefficientForProduction();
        hammerCount += (int) production;
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
        for (Tile tile : territories) {
            for (Resource resource : tile.calculateCollectibeResourcesOutput()) {
                if (resource instanceof LuxuryResource) {
                    owner.addLuxuryResource((LuxuryResource) resource);
                } else if (resource instanceof StrategicResource) {
                    owner.addStrategicResource((StrategicResource) resource);
                }
            }
        }
        hasAttackedThisTurn = false;
        hitPoints = Math.min(hitPoints + 1, MAXHITPOINTS);
    }

    private double calculateBuildingEffectCoefficientForProduction() {
        double coeff = 1;
        if (hasBuildingType(BuildingType.STABLE) && entityInProduction instanceof UnitType &&
                ((UnitType) entityInProduction).getCombatType() == CombatType.MOUNTED) {
            coeff += 0.25;
        }
        if (hasBuildingType(BuildingType.FORGE) && entityInProduction instanceof UnitType &&
                ((UnitType) entityInProduction).getCombatType() == CombatType.MELEE) {
            coeff += 0.15;
        }
        if (hasBuildingType(BuildingType.WORKSHOP) && entityInProduction instanceof BuildingType) {
            coeff += 0.20;
        }
        if (hasBuildingType(BuildingType.ARSENAL) && entityInProduction instanceof UnitType &&
                ((UnitType) entityInProduction).getCombatType() == CombatType.MELEE) {
            coeff += 0.20;
        }
        return coeff;
    }

    private void finishProduction() {
        if (entityInProduction instanceof BuildingType) {
            addBuilding((BuildingType) entityInProduction);
        } else if (entityInProduction instanceof UnitType) {
            if (centralTile.doesPackingLetUnitEnter((UnitType) entityInProduction)) {
                GameController.getGameController().createUnit((UnitType) entityInProduction, owner, centralTile,
                        calculateInitialXPForUnitType((UnitType) entityInProduction));
            } else {
                Debugger.debug("finishProduction of City.java: central tile shouldn't be full!");
                return;
            }
        }

        hammerCount -= entityInProduction.calculateHammerCost();
        this.owner.addNotificationForProduction(entityInProduction);
        entityInProduction = null;
    }

    private int calculateInitialXPForUnitType(UnitType type) {
        int result = 0;
        if (type.getCombatType() == CombatType.MELEE) {
            if (hasBuildingType(BuildingType.BARRACKS)) {
                result += 15;
            }
            if (hasBuildingType(BuildingType.ARMORY)) {
                result += 15;
            }
            if (hasBuildingType(BuildingType.MILITARY_ACADEMY)) {
                result += 15;
            }
        }
        return result;
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
                if (citizens.size() < 2 || owner.getHappiness() < 0) {
                    continue;
                }
            }
            if (owner.getTechnologies().isTechnologyLearned(type.getPrerequisitTechnology())) {
                result.add(type);
            }
        }
        return result;
    }

    public ArrayList<BuildingType> calculatePurchasableBuildingTypes() {    // DOESN'T CHECK IF CITY HAS ENOUGH GOLD TO PURCHASE BUILDING
        return calculateProductionReadyBuildingTypes(true);
    }

    public ArrayList<UnitType> calculateProductionReadyUnitTypes() {
        ArrayList<UnitType> result = new ArrayList<>();
        for (UnitType type : UnitType.values()) {
            if (type == UnitType.SETTLER) {
                if (citizens.size() < 2 || owner.getHappiness() < 0) {
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
        return calculateProductionReadyBuildingTypes(false);
    }

    public ArrayList<BuildingType> calculateProductionReadyBuildingTypes(boolean isForPurchase) {
        ArrayList<BuildingType> result = new ArrayList<BuildingType>();
        for (BuildingType type : BuildingType.values()) {
            if (owner.hasTechnology(type.getPrerequisiteTechnology()) && hasBuildingType(type) == false) {
                if (type.shouldBeNearRiver()) {
                    if (!isNearTheRiver()) {
                        continue;
                    }
                }
                if (type == BuildingType.STOCK_EXCHANGE) {
                    if (!(hasBuildingType(BuildingType.BANK) || hasBuildingType(BuildingType.SATRAPS_COURT))) {
                        continue;
                    }
                }
                if (type == BuildingType.CIRCUS) {
                    if (!(hasExploitableResourceNearby(StrategicResource.HORSE) || hasExploitableResourceNearby(LuxuryResource.IVORY))) {
                        continue;
                    }
                }
                if (type == BuildingType.STABLE) {
                    if (!hasExploitableResourceNearby(StrategicResource.HORSE)) {
                        continue;
                    }
                }
                if (type == BuildingType.FORGE) {
                    if (!hasExploitableResourceNearby(StrategicResource.IRON)) {
                        continue;
                    }
                }
                if (type == BuildingType.WINDMILL) {
                    if (centralTile.isOfType(TerrainType.HILLS)) {
                        continue;
                    }
                }

                if (!isForPurchase && type == BuildingType.FACTORY) {
                    if (!owner.hasStrategicResources(StrategicResource.getRequiredResourceHashMap(StrategicResource.COAL))) {
                        continue;
                    }
                }

                for (BuildingType prerequisite : type.getPrerequisiteBuildingTypes()) {
                    if (!hasBuildingType(prerequisite)) {
                        continue;
                    }
                }

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
            } else {
                if (producible instanceof UnitType) {
                    owner.payStrategicResources(((UnitType) producible).getPrerequisiteResources());
                } else if (producible == BuildingType.FACTORY) {
                    owner.payStrategicResources(StrategicResource.getRequiredResourceHashMap(StrategicResource.COAL));
                }
            }
        } else {
            stopProduction();
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

    public int calculateNextTilePrice() {
        int price = 50;
        price *= territories.size();
        return price;
    }

    public ArrayList<Tile> findPurchasableTiles() {
        ArrayList<Tile> result = new ArrayList<>();
        for (Tile territory : territories) {
            for (Tile adjacentTile : GameController.getGameController().getAdjacentTiles(territory)) {
                if (!territories.contains(adjacentTile) && GameController.getGameController().whoseTerritoryIsTileIn(adjacentTile) == null) {
                    result.add(adjacentTile);
                }
            }
        }
        return result;
    }

    public boolean hasExploitableResourceNearby(Resource resource) {    // check if a tile in the territory has the resource and the required improvement to use it
        for (Tile territory : territories) {
            if (territory.hasExploitableResource(resource)) {
                return true;
            }
        }
        return false;
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

    public void addCitizen() {
        this.citizens.add(new Citizen());
    }

    public void killACitizen() {
        if (citizens.isEmpty()) {
            return;
        }
        citizens.remove(0);
    }

    public void removeCitizenFromWork(Citizen citizen) {
        citizen.setWorkPlace(null);
    }

    public void assignCitizenToWorkplace(Workable workPlace, Citizen citizen) {
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

    public Civilization getFounder() {
        return founder;
    }

    public Civilization getOwner() {
        return owner;
    }

    public void setOwner(Civilization owner) {
        this.owner = owner;
    }

    public Tile getCentralTile() {
        return centralTile;
    }

    @Override
    public Tile getLocation() {
        return getCentralTile();
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

    @Override
    public int getHitPointsLeft() {
        return (int) this.hitPoints;
    }

    @Override
    public void setHitPoints(int hitPointsLeft) {
        this.hitPoints = hitPointsLeft;
    }

    @Override
    public void reduceHitPoints(int amount) {
        hitPoints -= amount;
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

    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }
}
