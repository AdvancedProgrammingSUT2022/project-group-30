package models;

import controllers.GameController;
import models.buildings.Palace;
import models.diplomacy.StepWiseGoldTransferContract;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.technology.Technology;
import models.technology.TechnologyMap;
import models.units.CombatType;
import models.units.Unit;
import utilities.Debugger;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization implements TurnHandler {
    private final String name;
    private HashMap<Tile, TileImage> mapImage = new HashMap<>();
    private boolean isEverythingVisibleCheatCodeInEffect = false;
    private boolean isTurnBreakDisabled = false;
    private HashMap<LuxuryResource, Integer> luxuryResources = LuxuryResource.makeRawHashMap();
    private HashMap<StrategicResource, Integer> strategicResources = StrategicResource.makeRawHashMap();
    private TechnologyMap technologies = new TechnologyMap();
    private double goldCount;
    private double beakerCount;
    private Technology researchProject;
    private HashMap<Technology, Double> researchReserve = new HashMap<>();
    private double happiness;
    private double diplomaticCredit;
    private double score;
    private City capital;
    private City originCapital;
    private Tile frameBase;
    private Selectable selectedEntity;
    private ArrayList<Notification> notifications = new ArrayList<>();

    public Civilization(String name) {
        this.name = name;
        this.goldCount = 0;
        this.beakerCount = 0;
        this.happiness = 20;
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

    public ArrayList<Unit> getMilitaryUnits() {
        ArrayList<Unit> allUnits = this.getUnits();
        ArrayList<Unit> militaryUnits = new ArrayList<>();
        for (int i = 0; i < allUnits.size(); i++) {
            if (allUnits.get(i).getType().getCombatType() != CombatType.CIVILIAN) {
                militaryUnits.add(allUnits.get(i));
            }
        }
        return militaryUnits;
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

    public ArrayList<City> getCitiesWaitingForProduction() {
        ArrayList<City> result = new ArrayList<>();
        for (City city : getCities()) {
            if (city.getEntityInProduction() == null) {
                result.add(city);
            }
        }
        return result;
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
        this.beakerCount += this.calculateTotalBeakers();
        if (this.researchProject != null) {
            if (this.beakerCount >= this.researchProject.getCost()) {
                this.beakerCount -= this.researchProject.getCost();
                this.technologies.learnTechnology(this.researchProject);
                this.addNotificationForResearch(this.researchProject);
                this.researchProject = null;
            }
        }
        int goldChange = (int) calculateGoldChange();
        goldCount += goldChange;
        if (goldCount < 0) {
            beakerCount += goldCount;
            beakerCount = Math.max(0, beakerCount);
            goldCount = 0;
        }
        this.happiness += this.calculateHappinessChanges();
    }

    public void payStrategicResources(HashMap<StrategicResource, Integer> amount) {
        for (StrategicResource strategicResource : amount.keySet()) {
            int newValue = strategicResources.get(strategicResource) - amount.get(strategicResource);
            if (newValue < 0) {
                Debugger.debug("Civilization.java payStrategicResources method is making a value negative!");
                return;
            }
            strategicResources.put(strategicResource, newValue);
        }
    }

    public double calculateHappinessChanges() {
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
        int numberOfScientificTreaty = GameController.getGameController().getScientificTreatiesOfCivilization(this).size();
        if (numberOfScientificTreaty != 0)
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

    public boolean hasTechnology(Technology technology) {
        return technologies.isTechnologyLearned(technology);
    }

    public boolean hasStrategicResources(HashMap<StrategicResource, Integer> resources) {
        for (StrategicResource resource : resources.keySet()) {
            if (strategicResources.get(resource) < resources.get(resource)) {
                return false;
            }
        }
        return true;
    }

    public double getGoldCount() {
        return this.goldCount;
    }

    public void setGoldCount(double goldCount) {
        this.goldCount = goldCount;
    }

    public void reduceGold(double amount) {
        this.goldCount -= amount;
    }

    public void addGold(double amount) {
        this.goldCount += amount;
    }

    public HashMap<LuxuryResource, Integer> getLuxuryResources() {
        return luxuryResources;
    }

    public void addLuxuryResource(LuxuryResource resource) {
        luxuryResources.put(resource, luxuryResources.get(resource) + 1);
    }

    public void addLuxuryResource(LuxuryResource resource, int amount) {
        luxuryResources.put(resource, luxuryResources.get(resource) + amount);
    }

    public void addStrategicResource(StrategicResource resource) {
        strategicResources.put(resource, strategicResources.get(resource) + 1);
    }

    public void addStrategicResource(StrategicResource resource, int amount) {
        strategicResources.put(resource, strategicResources.get(resource) + amount);
    }

    public void addGold(int amount) {
        goldCount += amount;
    }

    public void decreaseGold(int amount) {
        goldCount -= amount;
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

    public void changeCapital(City newCapital) {
        if (originCapital == null) {
            this.originCapital = newCapital;
        }

        // switch palace
        Palace palace = null;
        if (capital != null) {
            palace = capital.getPalace();
            capital.getBuildings().remove(palace);
        }
        if (palace == null) {
            palace = new Palace(this);
        }
        newCapital.getBuildings().add(palace);

        this.capital = newCapital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
    }

    public City getOriginCapital() {
        return originCapital;
    }

    public void setisTurnBreakDisabled(boolean isTurnBreakDisabled) {
        this.isTurnBreakDisabled = isTurnBreakDisabled;
    }

    public boolean isTurnBreakDisabled() {
        return isTurnBreakDisabled;
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

    public void addNotificationForResearch(Technology researchProject) {
        String technologyName = researchProject.getName();
        String notificationText = "You have learned " + technologyName + " technology!";
        Notification notification = new Notification(notificationText, false, GameDataBase.getGameDataBase().getTurnNumber());
        notifications.add(notification);
    }

    public void addNotificationForProduction(Producible production) {
        String productionName = production.getName();
        String notificationText = "City production is finished : " + productionName + " !";
        Notification notification = new Notification(notificationText, false, GameDataBase.getGameDataBase().getTurnNumber());
        notifications.add(notification);
    }

    public void addNotification(String text) {
        Notification notification = new Notification(text, false, GameDataBase.getGameDataBase().getTurnNumber());
        notifications.add(notification);
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<Notification> getNotifications() {
        return this.notifications;
    }

    public int calculateTotalFoodFromCities() {
        ArrayList<City> cities = this.getCities();
        int sum = 0;
        for (City city : cities) {
            sum += city.calculateOutput().getFood();
        }
        return sum;
    }

    public ArrayList<Improvement> getAllImprovements() {
        ArrayList<Improvement> improvements = new ArrayList<>();
        GameMap map = GameMap.getGameMap();
        Tile[][] tiles = map.getMap();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                ArrayList<Improvement> tilesImprovements = tiles[i][j].getImprovements();
                for (int k = 0; k < tilesImprovements.size(); k++) {
                    if (tilesImprovements.get(k).getFounder().equals(this)) {
                        improvements.add(tilesImprovements.get(k));
                    }
                }
            }
        }
        return improvements;
    }

    public void learnTechnologyWithCheat(Technology technology) {
        technologies.learnTechnologyAndPrerequisites(technology);
        if (researchProject != null && technologies.isTechnologyLearned(researchProject)) {
            researchProject = null;
        }
    }

    public void learnAllTechnologiesWithCheat() {
        technologies.learnAllTechnologies();
        researchProject = null;
    }


}
