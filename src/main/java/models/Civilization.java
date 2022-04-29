package models;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.GameController;
import models.diplomacy.StepWiseGoldTransferContract;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.technology.Technology;
import models.technology.TechnologyMap;
import models.units.Unit;
import utilities.Debugger;

public class Civilization implements TurnHandler {
    private final String name;
    private HashMap<Tile, TileImage> mapImage = new HashMap<>();
    private boolean isEverythingVisibleCheatCodeInEffect = false;
    private HashMap<LuxuryResource, Integer> luxuryResources = LuxuryResource.makeRawHashMap();
    private HashMap<StrategicResource, Integer> strategicResources = StrategicResource.makeRawHashMap();
    private TechnologyMap technologies = new TechnologyMap();
    private double goldCount;
    private double beakerCount; // NOTE TO MAHYAR: read goToNextTurn(): the part about gold.
    private Technology researchProject;
    private HashMap<Technology, Double> researchReserve = new HashMap<>();
    private double happiness;
    private double diplomaticCredit;
    private double score;
    private City capital;
    private City originCapital;
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

        beakerCount += this.calculateTotalBeakers();

        int goldChange = (int) calculateGoldChange();
        goldCount += goldChange;
        if (goldCount < 0) {
            beakerCount += goldCount;
            beakerCount = Math.max(0, beakerCount);
            goldCount = 0;
        }

        for (City city : getCities()) {
            ArrayList<Resource> collectibleResourcesInput = city.calculateCollectibleResourceOutput();
            for (Resource resource : collectibleResourcesInput) {
                if (resource instanceof  LuxuryResource) {
                    luxuryResources.put((LuxuryResource) resource, luxuryResources.get(resource) + 1);
                }
                if (resource instanceof  StrategicResource) {
                    strategicResources.put((StrategicResource) resource, strategicResources.get(resource) + 1);
                }
            }
        }
    }

    public void setNextResearchProject(Technology technology) {
        //TODO
    }

    private void updateStrategicResources() {
        // TODO
    }

    public double calculateHappiness() {
        double happiness = 0;
        for (City city : this.getCities()) {
            happiness += city.calculateHappiness();
        }
        happiness += this.getLuxuryResources().keySet().size() * 4;
        happiness -= this.getCities().size() * 0.5;
        return happiness;
    }

    public double calculateTotalBeakers() {
        double count = 0;
        for (City city : this.getCities()) {
            count += city.calculateBeakerProduction();
        }
        double numberOfScientificTreaty = GameController.getGameController().getScientificTreatiesOfCivilization(this).size();
        count += count * 15 * numberOfScientificTreaty / 100.0;
        return count;
    }

    public double calculateNetGoldProduction() {
        double gold = 0;
        for (City city : this.getCities()) {
            gold += city.calculateOutput().getGold();
        }
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = GameController.getGameController().getStepWiseGoldTransferContractsOfCivilizationRecipient(this);
        for (StepWiseGoldTransferContract stepWiseGoldTransferContract : stepWiseGoldTransferContracts) {
            gold += stepWiseGoldTransferContract.getTotalAmount() / stepWiseGoldTransferContract.getTotalTurns();
        }
        return gold;
    }

    public double calculateTotalCosts() {
        double cost = 0;
        for (City city : this.getCities()) {
            cost += city.calculateTotalGoldCosts();
        }
        cost += this.getUnits().size() * Unit.MAINTENANCE_COST_OF_UNIT;
        cost += this.getNumberOfRoads() * ImprovementType.MAINTENANCE_COST_OF_ROAD_AND_RAILROAD;
        cost += this.getNumberOfRailRoads() * ImprovementType.MAINTENANCE_COST_OF_ROAD_AND_RAILROAD;
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = GameController.getGameController().getStepWiseGoldTransferContractsOfCivilizationPayer(this);
        for (StepWiseGoldTransferContract stepWiseGoldTransferContract : stepWiseGoldTransferContracts) {
            cost += stepWiseGoldTransferContract.getTotalAmount() / stepWiseGoldTransferContract.getTotalTurns();
        }
        return cost;
    }

    public double calculateGoldChange() {
        return this.calculateNetGoldProduction() - this.calculateTotalCosts();
    }

    public ArrayList<Producible> findUnlockedProducibles() {
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

    public void setGoldCount(double goldCount) {
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

    public TechnologyMap getTechnologies() {
        return technologies;
    }

    public void setTechnologies(TechnologyMap technologies) {
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

    public HashMap<Technology, Double> getResearchReserve() {
        return researchReserve;
    }

    public void setResearchReserve(HashMap<Technology, Double> researchReserve) {
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
        if (originCapital == null) {
            this.originCapital = capital;
        }
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

    public boolean isEverythingVisibleCheatCodeInEffect() {
        return isEverythingVisibleCheatCodeInEffect;
    }

    public void setEverythingVisibleCheatCodeInEffect(boolean everythingVisibleCheatCodeInEffect) {
        isEverythingVisibleCheatCodeInEffect = everythingVisibleCheatCodeInEffect;
    }

    public void setSelectedEntity(Selectable selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public Player getPlayer() {
        ArrayList<Player> players = GameDataBase.getGameDataBase().getPlayers();
        for (Player player : players) {
            if (player.getCivilization() == this) {
                return player;
            }
        }
        return null;
    }
}
