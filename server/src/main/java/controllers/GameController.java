package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.*;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.diplomacy.*;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.MPCostInterface;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.interfaces.combative;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.technology.Technology;
import models.technology.TechnologyMap;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import models.works.Work;
import utilities.Debugger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameController {
    private static GameController gameController;

    private GameDataBase gameDataBase = GameDataBase.getGameDataBase();

    private ProgramDatabase programDatabase;

    private GameController() {
        gameDataBase = GameDataBase.getGameDataBase();
    }

    public static GameController getGameController() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }

    public void initializeGame(int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        GameMap.getGameMap().loadMapFromFile(mapHeight, mapWidth, startingYPosition, startingXPosition);
        assignCivsToPlayersAndInitializePrimaryUnits(mapHeight, mapWidth, startingYPosition, startingXPosition);
        gameDataBase.setCurrentPlayer(gameDataBase.getPlayers().get(0).getCivilization());
    }

    public void addCivilizationToPairs(Civilization newCivilization) {
        for (Civilization civilization : getGameDataBase().getCivilizations()) {
            gameDataBase.getDiplomaticRelations().add(new DiplomaticRelation(civilization, newCivilization));
        }
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits(int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        File main = new File("src", "main");
        File resources = new File(main, "resources");
        File textFiles = new File(resources, "textFiles");
        File civilizationsFile = new File(textFiles, "civilizations.txt");
        try {
            Scanner scanner = new Scanner(civilizationsFile);
            ArrayList<String> fileLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
            fileLines = getAppropriateStartingPoints(fileLines, mapHeight, mapWidth, startingYPosition, startingXPosition);
            for (int i = 0; i < fileLines.size(); i++) {
                System.out.println(fileLines.get(i));
            }
            Random rand = new Random();
            for (int i = 0; i < this.gameDataBase.getPlayers().size(); i++) {
                int fileLineIndex = rand.nextInt(fileLines.size());
                String tokens[] = fileLines.get(fileLineIndex).split("-");
                Civilization civilization = new Civilization(tokens[2]);
                System.out.println(tokens[0]);
                System.out.println(tokens[1]);
                addCivilizationToPairs(civilization);
                this.gameDataBase.getPlayers().get(i).setCivilization(civilization);
                Tile settlerTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - startingXPosition, Integer.parseInt(tokens[0]) - startingYPosition);
                System.out.println(settlerTile.getTerrainType().getName());
                Tile warriorTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 1 - startingXPosition, Integer.parseInt(tokens[0]) - startingYPosition);
                createUnit(UnitType.SETTLER, civilization, settlerTile);
                createUnit(UnitType.WARRIOR, civilization, warriorTile);
                int yFixDifference = 0;
                while (Integer.parseInt(tokens[0]) - 1 - startingYPosition - yFixDifference + 9 > GameMap.getGameMap().getMap().length - 1) {
                    yFixDifference += 1;
                }
                int xFixDifference = 0;
                while (Integer.parseInt(tokens[1]) - 3 - startingXPosition - xFixDifference + 19 > GameMap.getGameMap().getMap()[0].length - 1) {
                    xFixDifference += 1;
                }
                civilization.setFrameBase(GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 3 - startingXPosition - xFixDifference, Integer.parseInt(tokens[0]) - 1 - startingYPosition - yFixDifference));
                System.out.println(civilization.getFrameBase().findTileXCoordinateInMap());
                System.out.println(civilization.getFrameBase().findTileYCoordinateInMap());
                fileLines.remove(fileLineIndex);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAppropriateStartingPoints(ArrayList<String> fileLines, int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        ArrayList<String> appropriateLines = new ArrayList<>();
        for (int i = 0; i < fileLines.size(); i++) {
            String tokens[] = fileLines.get(i).split("-");
            int xPosition = Integer.parseInt(tokens[1]);
            int yPosition = Integer.parseInt(tokens[0]);
            if (!(yPosition < startingYPosition || yPosition > startingYPosition + mapHeight - 1 || xPosition < startingXPosition || xPosition > startingXPosition + mapWidth - 1)) {
                appropriateLines.add(fileLines.get(i));
            }
        }
        return appropriateLines;
    }

    public void createUnit(UnitType type, Civilization owner, Tile location) {
        Unit newUnit = new Unit(owner, type, location);
        gameDataBase.getUnits().add(newUnit);
        setMapImageOfCivilization(owner);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location, int initialXP) {
        Unit newUnit = new Unit(owner, type, location);
        gameDataBase.getUnits().add(newUnit);
        setMapImageOfCivilization(owner);
    }

    public void addPlayers(User[] players) {
        gameDataBase.addPlayers(players);
    }

    public boolean areCoordinatesValid(int x, int y) {
        return gameDataBase.getMap().areCoordinatesValid(x, y);
    }

    public Tile getTileByCoordinates(int x, int y) {
        return gameDataBase.getMap().getTile(x, y);
    }

    public boolean canWorkerFixImprovement(Unit worker) {
        if (isWorkerWorking(worker)) {
            return false;
        }
        if (worker.getLocation().getCityOfTile() == null || worker.getLocation().getCityOfTile().getOwner() != worker.getOwner()) {
            return false;
        }
        if (getTypeOfPillagedImprovement(worker) != null) return true;
        return false;
    }

    public ImprovementType getTypeOfPillagedImprovement(Unit worker) {
        for (Improvement improvement : worker.getLocation().getImprovements()) {
            if (improvement.getIsPillaged() && (improvement.getType() != ImprovementType.RAILROAD || improvement.getType() != ImprovementType.ROAD)) {
                return improvement.getType();
            }
        }
        return null;
    }

    public boolean canWorkerFixRoute(Unit worker) {
        if (isWorkerWorking(worker)) {
            return false;
        }
        if (worker.getLocation().getCityOfTile() == null || worker.getLocation().getCityOfTile().getOwner() != worker.getOwner()) {
            return false;
        }
        for (Improvement improvement : worker.getLocation().getImprovements()) {
            if (improvement.getIsPillaged() && (improvement.getType() == ImprovementType.RAILROAD || improvement.getType() == ImprovementType.ROAD)) {
                return true;
            }
        }
        return false;
    }

    public boolean canWorkerClearRoutes(Unit worker) {
        if (isWorkerWorking(worker)) {
            return false;
        }
        if (!(worker.getLocation().isImprovementTypeAccessible(ImprovementType.ROAD) ||
                worker.getLocation().isImprovementTypeAccessible(ImprovementType.RAILROAD))) {
            return false;
        }
        return true;
    }

    public boolean canWorkerClearFeature(Unit worker, Feature feature) {
        if (isWorkerWorking(worker)) {
            return false;
        }
        if (!worker.getLocation().getFeatures().contains(feature)) {
            return false;
        }
        if (worker.getLocation().getCityOfTile() == null || worker.getLocation().getCityOfTile().getOwner() != worker.getOwner()) {
            return false;
        }
        return true;
    }

    public boolean canWorkerBuildFarmOrMine(Unit worker, ImprovementType type) {
        Tile location = worker.getLocation();
        Civilization owner = worker.getOwner();

        if (isWorkerWorking(worker)) {
            return false;
        }
        if (worker.getLocation().containsImprovment(type)) {
            return false;
        }
        if (location.getCityOfTile() == null || location.getCityOfTile().getOwner() != owner) {
            return false;
        }
        if (!worker.getOwner().hasTechnology(type.getPrerequisiteTechnology())) {
            return false;
        }
        if ((location.getFeatures().contains(Feature.FOREST) && !owner.hasTechnology(Technology.MINING)) ||
                (location.getFeatures().contains(Feature.JUNGLE) && !owner.hasTechnology(Technology.BRONZE_WORKING)) ||
                (location.getFeatures().contains(Feature.MARSH) && !owner.hasTechnology(Technology.MASONRY))) {
            return false;
        }

        if (!type.isCompatibleWithTile(worker.getLocation())) {
            if (!((location.getFeatures().contains(Feature.FOREST) && owner.hasTechnology(Technology.MINING)) ||
                    (location.getFeatures().contains(Feature.JUNGLE) && owner.hasTechnology(Technology.BRONZE_WORKING)) ||
                    (location.getFeatures().contains(Feature.MARSH) && owner.hasTechnology(Technology.MASONRY)))) {
                return false;
            }
        }
        for (Resource resource : location.getResourcesAsArrayList()) {
            if (resource.getPrerequisiteImprovement() == type) {
                return true;
            }
        }
        return false;
    }

    public void startResearch(Civilization civilization, Technology researchProject) {
        if (civilization.getResearchReserve().containsKey(researchProject)) {
            civilization.setBeakerCount(civilization.getResearchReserve().get(researchProject));
            civilization.getResearchReserve().remove(researchProject);
        }
        civilization.setResearchProject(researchProject);
    }

    public void stopResearch(Civilization civilization) {
        Technology researchProject = civilization.getResearchProject();
        civilization.getResearchReserve().put(researchProject, civilization.getBeakerCount());
        civilization.setResearchProject(null);
        civilization.setBeakerCount(0);
    }

    public void stopPreviousResearchAndStartNext(Civilization civilization, Technology next) {
        stopResearch(civilization);
        startResearch(civilization, next);
    }

    public Improvement findImprovementById(int id) {
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            for (Improvement improvement : tile.getImprovements()) {
                if (improvement.getId() == id) {
                    return improvement;
                }
            }
        }
        return null;
    }

    public Building findBuildingById(int id) {
        for (City city : gameDataBase.getCities()) {
            for (Building building : city.getBuildings()) {
                if (building.getId() == id) {
                    return building;
                }
            }
        }
        return null;
    }

    public DiplomaticRelation findDiplomacyById(int id) {
        for (DiplomaticRelation diplomaticRelation : gameDataBase.getDiplomaticRelations()) {
            if (diplomaticRelation.getId() == id) {
                return diplomaticRelation;
            }
        }
        return null;
    }

    public Message findMessageById(int id) {
        // TODO
        return null;
    }

    public TechnologyMap findTechnologyMapById(int id) {
        for (Civilization civilization : gameDataBase.getCivilizations()) {
            if (civilization.getTechnologies().getId() == id) {
                return civilization.getTechnologies();
            }
        }
        return null;
    }

    public Work findWorkById(int id) {
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            if (tile.getWork() != null && tile.getWork().getId() == id) {
                return tile.getWork();
            }
        }
        return null;
    }

    public Citizen findCitizenById(int id) {
        for (City city : gameDataBase.getCities()) {
            for (Citizen citizen : city.getCitizens()) {
                if (citizen.getId() == id) {
                    return citizen;
                }
            }
        }
        return null;
    }

    public Notification findNotificationById(int id) {
        for (Civilization civilization : gameDataBase.getCivilizations()) {
            for (Notification notification : civilization.getNotifications()) {
                if (notification.getId() == id) {
                    return notification;
                }
            }
        }
        return null;
    }

    public Player findPlayerById(int id) {
        for (Player player : gameDataBase.getPlayers()) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public RiverSegment findRiverSegmentById(int id) {
        for (RiverSegment river : GameMap.getGameMap().getRivers()) {
            if (river.getId() == id) {
                return river;
            }
        }
        return null;
    }

    public boolean canWorkerBuildImprovement(Unit worker, ImprovementType improvementType) {
        Tile location = worker.getLocation();
        Civilization owner = worker.getOwner();

        if (isWorkerWorking(worker)) {
            return false;
        }
        if (worker.getLocation().containsImprovment(improvementType)) {
            return false;
        }
        if (location.getCityOfTile() == null || location.getCityOfTile().getOwner() != owner) {
            return false;
        }
        if (!worker.getOwner().hasTechnology(improvementType.getPrerequisiteTechnology())) {
            return false;
        }
        if (!improvementType.isCompatibleWithTile(worker.getLocation())) {
            return false;
        }
        for (Resource resource : location.getResourcesAsArrayList()) {
            if (resource.getPrerequisiteImprovement() == improvementType) {
                return true;
            }
        }
        return false;
    }

    public boolean canWorkerBuildRoute(Unit worker, ImprovementType routeType) {
        if (isWorkerWorking(worker)) {
            return false;
        }
        if (worker.getLocation().containsImprovment(ImprovementType.ROAD) ||
                worker.getLocation().containsImprovment(ImprovementType.RAILROAD)) {
            return false;
        }
        if (!worker.getOwner().hasTechnology(routeType.getPrerequisiteTechnology())) {
            return false;
        }
        return true;
    }

    public boolean isWorkerWorking(Unit worker) {
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            if (tile.getWork() != null && tile.getWork().getWorker() == worker && tile.getWork().isInProgress()) {
                return true;
            }
        }
        return false;
    }

    public Work getWorkersWork(Unit worker) {
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            if (tile.getWork() != null && tile.getWork().getWorker() == worker) {
                return tile.getWork();
            }
        }
        return null;
    }

    public TileVisibility getTileVisibilityForPlayer(Tile tile) { // returns Visible, Fog of War, or Revealed
        return gameDataBase.getCurrentPlayer().getTileVisibility(tile);
    }

    public void moveUnit(Unit unit, Tile destination) { // doesn't check packing and mp conditions, doesn't cost mp. updates fog of war
        unit.setLocation(destination);
        setMapImageOfCivilization(unit.getOwner());
        awakeAllNearAlertedUnits(unit);
        unit.resetInactivityDuration();
    }

    public boolean canUnitTeleportToTile(UnitType unit, Civilization owner, Tile tile) {
        if (isTileImpassable(tile)) {
            return false;
        }
        Unit militaryUnitInTile = getMilitaryUnitInTile(tile);
        Unit civilianUnitInTile = getCivilianUnitInTile(tile);
        City city = getCityCenteredInTile(tile);
        if (militaryUnitInTile != null) {
            if (militaryUnitInTile.getOwner() != owner) {
                return false;
            } else if (!unit.isCivilian()) {
                return false;
            }
        }
        if (civilianUnitInTile != null) {
            if (civilianUnitInTile.getOwner() != owner) {
                return false;
            } else if (unit.isCivilian()) {
                return false;
            }
        }
        if (city != null && city.getOwner() != owner) {
            return false;
        }
        return true;
    }

    private void awakeAllNearAlertedUnits(Unit unit) {
        Tile tile = unit.getLocation();
        ArrayList<Tile> adjacetTiles = this.getAdjacentTiles(tile);
        for (int i = 0; i < adjacetTiles.size(); i++) {
            ArrayList<Unit> units = this.getUnitsInTile(adjacetTiles.get(i));
            for (int j = 0; j < units.size(); j++) {
                if (units.get(j).getOwner() != unit.getOwner() && units.get(j).getState() == UnitState.ALERT) {
                    units.get(j).setState(UnitState.AWAKE);
                }
            }
        }
    }

    public void moveUnitAlongItsPath(Unit unit) {
        ArrayList<Tile> path = unit.getPath();
        if (path == null || path.size() < 2) {
            return;
        }
        Tile farthest = findFarthesestTileByMPCost(unit);
        if (farthest == null) {
            return;
        }
        int index = path.indexOf(farthest);
        Tile destination = null;
        for (int i = index; i > 0; i--) {
            ArrayList<Unit> units = getUnitsInTile(path.get(i));
            Unit generalUnit = (units.isEmpty() == false) ? units.get(0) : null;
            City city = getCityCenteredInTile(path.get(i));
            if (generalUnit != null && generalUnit.getOwner() != unit.getOwner()) {
                continue;
            } else if (generalUnit != null && generalUnit.getOwner() == unit.getOwner()) {
                if ((generalUnit.isCivilian() && unit.isCivilian()) || (!generalUnit.isCivilian() && !unit.isCivilian())) {
                    continue;
                }
            }

            if (city != null && city.getOwner() != unit.getOwner()) {   // if there is a hostile city on the path
                continue;
            }

            if (city != null && city.getOwner() == unit.getOwner()) {
                if (city.getEntityInProduction() instanceof UnitType) {
                    UnitType producible = (UnitType) city.getEntityInProduction();
                    if (producible.isCivilian() == unit.getType().isCivilian()) {
                        continue;
                    }
                }
            }

            destination = path.get(i);
            break;
        }

        if (destination != null) {
            moveUnit(unit, destination);
            if (unit.getType().needsAssmbly()) {
                unit.disassemble();
            }
            expendMPForMovementAlongPath(unit, destination);
            updateUnitPath(unit, destination);
        } else {
            unit.setPath(null);
        }
    }

    private void updateUnitPath(Unit unit, Tile destination) {
        ArrayList<Tile> newPath = new ArrayList<>(unit.getPath());
        int index = newPath.indexOf(destination);
        for (int i = 0; i < index; i++) {
            newPath.remove(0);
        }
        if (newPath.size() == 1) {
            unit.setPath(null);
        } else {
            unit.setPath(newPath);
        }
    }

    private void expendMPForMovementAlongPath(Unit unit, Tile destination) {
        int costInt = 0;
        for (int i = 1; i <= unit.getPath().indexOf(destination); i++) {
            MPCostInterface cost = calculateRequiredMps(unit, unit.getPath().get(i - 1), unit.getPath().get(i));
            if (cost == MPCostEnum.EXPENSIVE) {
                unit.setMovePointsLeft(0);
                costInt = 0;
                break;
            } else {
                costInt += ((MPCostClass) cost).getCost();
            }
        }
        unit.setMovePointsLeft(Math.max(0, unit.getMovePointsLeft() - costInt));
    }

    private Tile findFarthesestTileByMPCost(Unit unit) {
        int MPsLeft = unit.getMovePointsLeft();
        if (MPsLeft <= 0 || unit.getPath() == null || unit.getPath().size() < 2) {
            return null;
        }

        for (int i = 1; i < unit.getPath().size(); i++) {
            MPCostInterface cost = calculateRequiredMps(unit, unit.getPath().get(i - 1), unit.getPath().get(i));
            if (cost == MPCostEnum.IMPASSABLE) {
                return unit.getPath().get(i - 1);
            } else if (cost == MPCostEnum.EXPENSIVE) {
                return unit.getPath().get(i);
            } else {
                MPsLeft -= ((MPCostClass) cost).getCost();
                if (MPsLeft <= 0 || i == unit.getPath().size() - 1) {
                    return unit.getPath().get(i);
                }
            }
        }
        Debugger.debug("find Farthest path is faulty!");
        return null;
    }

    public City getCivsCityInTile(Tile tile, Civilization civilization) {
        for (City city : gameDataBase.getCities()) {
            if (city.getCentralTile() == tile & city.getOwner() == civilization) {
                return city;
            }
        }
        return null;
    }

    public boolean doesTileContainEnemyCombative(Tile tile, Civilization reference) {
        ArrayList<Unit> tileUnits = getUnitsInTile(tile);
        for (Unit unit : tileUnits) {
            if (unit.getOwner() != reference) {
                return true;
            }
        }
        if (getCityCenteredInTile(tile) != null) {
            return true;
        }
        return false;
    }

    public combative getPriorityTargetInTile(Tile tile, Civilization reference) {
        City city = getCityCenteredInTile(tile);
        if (city != null && city.getOwner() != reference) {
            return city;
        }

        Unit militaryUnit = getMilitaryUnitInTile(tile);
        if (militaryUnit != null && militaryUnit.getOwner() != reference) {
            return militaryUnit;
        }
        Unit civilianUnit = getCivilianUnitInTile(tile);
        if (civilianUnit != null && civilianUnit.getOwner() != reference) {
            return civilianUnit;
        }
        return null;
    }

    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit unit : gameDataBase.getUnits()) {
            if (unit.getLocation() == tile) {
                units.add(unit);
            }
        }
        return units;
    }

    public Unit getMilitaryUnitInTile(Tile tile) {
        for (Unit unit : gameDataBase.getUnits()) {
            if (unit.getLocation() == tile && unit.getType().getCombatType() != CombatType.CIVILIAN) {
                return unit;
            }
        }
        return null;
    }

    public Unit getCivilianUnitInTile(Tile tile) {
        for (Unit unit : gameDataBase.getUnits()) {
            if (unit.getLocation() == tile && unit.getType().getCombatType() == CombatType.CIVILIAN) {
                return unit;
            }
        }
        return null;
    }

    public Unit getCivsUnitInTile(Tile tile, Civilization civilization) {  // returns null if civ doesn't have units in the tile, and returns the military unit if both a military and a civilian unit of the civ are in the tile
        Unit militaryUnit = getCivsMilitaryUnitInTile(tile, civilization);
        Unit civilianUnit = getCivsCivilianUnitInTile(tile, civilization);
        if (militaryUnit != null) {
            return militaryUnit;
        } else if (civilianUnit != null) {
            return civilianUnit;
        } else {
            return null;
        }
    }

    public Unit getCivsMilitaryUnitInTile(Tile tile, Civilization civilization) {
        ArrayList<Unit> units = getUnitsInTile(tile);
        for (Unit unit : units) {
            if (unit.getOwner() == civilization && unit.getType().getCombatType() != CombatType.CIVILIAN) {
                return unit;
            }
        }
        return null;
    }

    public Unit getCivsCivilianUnitInTile(Tile tile, Civilization civilization) {
        ArrayList<Unit> units = getUnitsInTile(tile);
        for (Unit unit : units) {
            if (unit.getOwner() == civilization && unit.getType().getCombatType() == CombatType.CIVILIAN) {
                return unit;
            }
        }
        return null;
    }

    public boolean isTileTooNearCity(Tile tile) {
        for (City city : gameDataBase.getCities()) {
            if (city.getCentralTile().calculateDistance(tile) < 4) {
                return true;
            }
        }
        return false;
    }

    public void foundCityWithSettler(Unit unit) {
        City newCity = new City(unit.getOwner(), unit.getLocation());
        newCity.addCitizen();
        ArrayList<Tile> territory = getAdjacentTiles(newCity.getCentralTile());
        for (Tile tile : territory) {
            newCity.addTileToTerritory(tile);
        }
        newCity.getBuildings().add(new Building(BuildingType.PALACE));
        gameDataBase.getCities().add(newCity);
        if (unit.getOwner().getOriginCapital() == null) {
            unit.getOwner().changeCapital(newCity);
        }

        removeUnit(unit);
    }

    public void checkVictoryByDominion() {
        Civilization dominator = null;
        for (Civilization civilization : gameDataBase.getCivilizations()) {
            if (doesCivilizationHaveItsOriginalCapital(civilization)) {
                if (dominator == null) {
                    dominator = civilization;
                } else {
                    return;
                }
            }
        }
        makeCivilizationWin(dominator);
    }

    public boolean doesCivilizationHaveItsOriginalCapital(Civilization civilization) {
        if (civilization.getCapital() != null && civilization.getCapital() == civilization.getOriginCapital()) {
            return true;
        } else {
            return false;
        }
    }

    public void defeatCivilization(Civilization civilization) {
        civilization.setDefeated(true);
        civilization.setScore(0);
    }

    public void makeCivilizationWin(Civilization civilization) {
        for (Civilization civ : gameDataBase.getCivilizations()) {
            if (civ != civilization) {
                defeatCivilization(civ);
            }
        }

        civilization.setScore(calculateScoreForCivilization(civilization) * (2050 - gameDataBase.calculateYear()) / 500);
        // TODO : end game and stuff
    }

    public void endGameByTime() {
        Civilization winner = null;
        int winnerScore = 0;
        for (Civilization civilization : gameDataBase.getCivilizations()) {
            int score = calculateScoreForCivilization(civilization);
            if (score > winnerScore || winner == null) {
                winner = civilization;
                winnerScore = score;
            }
        }
        makeCivilizationWin(winner);
    }

    public void disableTurnBreak() {
        Civilization player = getCurrentPlayer();
        player.setisTurnBreakDisabled(true);
    }

    public void deleteUnit(Unit unit) {     // deletes unit and REFUNDS ITS OWNER
        unit.getOwner().setGoldCount(unit.getOwner().getGoldCount() + (double) unit.getType().getCost() / (double) 10);
        for (StrategicResource strategicResource : unit.getType().getPrerequisiteResources().keySet()) {
            unit.getOwner().addStrategicResource(strategicResource, unit.getType().getPrerequisiteResources().get(strategicResource));
        }
        removeUnit(unit);
    }

    public void removeUnit(Unit unit) {     // just removes the unit, updates fog of war, and nothing else
        if (unit.getOwner().getSelectedEntity() == unit) {
            unit.getOwner().setSelectedEntity(null);
        }
        if (unit.getType() == UnitType.WORKER && getWorkersWork(unit) != null) {
            getWorkersWork(unit).stopWork();
        }
        gameDataBase.getUnits().remove(unit);
        setMapImageOfCivilization(unit.getOwner());
    }

    public void destroyCity(City city) {
        if (city.getOwner().getSelectedEntity() == city) {
            city.getOwner().setSelectedEntity(null);
        }
        for (Tile tile : city.getTerritories()) {
            tile.removeAllImprovements();
        }
        gameDataBase.getCities().remove(city);
        setMapImageOfCivilization(city.getOwner());
    }

    public ArrayList<Unit> getCurrentPlayersUnitsWaitingForCommand() {    // returns an arraylist of all units waiting for commands for the current player
        ArrayList<Unit> result = new ArrayList<>();
        Civilization player = getCurrentPlayer();
        ArrayList<Unit> playersUnits = player.getUnits();
        for (Unit unit : playersUnits) {
            if (unit.isWaitingForCommand()) {
                result.add(unit);
            }
        }
        return result;
    }

    public void goToNextTurn() {
        gameDataBase.setTurnNumber(gameDataBase.getTurnNumber() + 1);
        if (gameDataBase.calculateYear() >= 2050) {
            endGameByTime();
        }
        getGameDataBase().setCurrentPlayer(gameDataBase.getPlayers().get(0).getCivilization());

        for (Unit unit : gameDataBase.getUnits()) {
            unit.goToNextTurn();
        }
        for (City city : gameDataBase.getCities()) {
            city.goToNextTurn();
        }
        for (Civilization civilization : gameDataBase.getCivilizations()) {
            civilization.goToNextTurn();
        }
        for (Tile tile : GameMap.getGameMap().getAllMapTiles()) {
            tile.goToNextTurn();
        }
        for (Diplomacy diplomaticRelation : gameDataBase.getDiplomaticRelations()) {
            if (diplomaticRelation instanceof TurnHandler) {
                ((TurnHandler) diplomaticRelation).goToNextTurn();
            }
        }
    }

    public void goToNextPlayer() {
        Civilization nextPlayer = getNextPlayer();
        if (nextPlayer == null) {
            goToNextTurn();
        } else {
            gameDataBase.setCurrentPlayer(nextPlayer);
        }
    }

    public Civilization getNextPlayer() {   // returns the next player, return null if it is currently the last player's turn
        ArrayList<Player> players = gameDataBase.getPlayers();
        Player currentPlayer = getCurrentPlayer().getPlayer();

        int currentIndex = gameDataBase.getPlayers().indexOf(currentPlayer);
        if (currentIndex < players.size() - 1) {
            return players.get(currentIndex + 1).getCivilization();
        } else {
            return null;
        }
    }

    public void setProgramDatabase() {
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase() {
        return this.programDatabase;
    }

    public void setGameDataBase() {
        this.gameDataBase = GameDataBase.getGameDataBase();
    }

    public GameDataBase getGameDataBase() {
        return this.gameDataBase;
    }

    public City getCityCenteredInTile(Tile tile) {
        for (City city : gameDataBase.getCities()) {
            if (city.getCentralTile() == tile) {
                return city;
            }
        }
        return null;
    }

    public boolean canUnitMove(Unit unit) {
        if (unit.getMovePointsLeft() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canUnitPillage(Unit unit) {
        if (unit.isCivilian()) {
            return false;
        }
        if (unit.getMovePointsLeft() == 0) {
            return false;
        }
        if (unit.getLocation().getUnpillagedImprovements().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean canUnitSetUpForRangedAttack(Unit unit) {
        return (unit.getType().getCombatType() == CombatType.SIEGE &&
                !unit.isAssembled() && unit.getMovePointsLeft() >= 1 &&
                unit.getState().waitsForCommand());
    }

    public boolean canUnitMeleeAttack(Unit unit) {
        if (!unit.getState().waitsForCommand() || unit.isCivilian() || unit.getType().isRanged() ||
                unit.getMovePointsLeft() == 0 || unit.hasAttackedThisTurn()) {
            return false;
        }
        ArrayList<Tile> unitVicinity = getAdjacentTiles(unit.getLocation());
        for (Tile tile : unitVicinity) {
            if (doesTileContainEnemyCombative(tile, unit.getOwner())) {
                return true;
            }
        }
        return false;
    }

    public boolean canUnitRangedAttack(Unit unit) {
        if (!unit.getState().waitsForCommand || unit.isCivilian() || !unit.getType().isRanged() ||
                unit.getMovePointsLeft() == 0 || !unit.isAssembled() || unit.hasAttackedThisTurn()) {
            return false;
        }
        return true;
    }

    public void addImprovementToTile(Tile tile, ImprovementType improvementType) {  // Ignores stacking and everything else!
        Improvement newImprovement = new Improvement(improvementType, getCurrentPlayer());
        tile.addImprovement(newImprovement);
    }

    public void pillageUnitsTile(Unit unit) {
        for (Improvement improvement : unit.getLocation().getImprovements()) {
            improvement.setIsPillaged(true);
        }
        unit.setMovePointsLeft(0);
    }

    public City whoseTerritoryIsTileInButIsNotTheCenterOf(Tile tile) {     // If the tile is located in the citie's territory, returns the city(city center does not count)
        for (City city : gameDataBase.getCities()) {
            if (city.getTerritories().contains(tile) && city.getCentralTile() != tile) {
                return city;
            }
        }
        return null;
    }

    public City whoseTerritoryIsTileIn(Tile tile) {     // If the tile is located in the citie's territory, returns the city(city center counts)
        for (City city : gameDataBase.getCities()) {
            if (city.getTerritories().contains(tile)) {
                return city;
            }
        }
        return null;
    }

    public void addTileToCityTerritory(City city, Tile tile) {
        city.addTileToTerritory(tile);
        setMapImageOfCivilization(city.getOwner());
    }

    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        int x = tile1.findTileXCoordinateInMap();
        int y = tile1.findTileYCoordinateInMap();
        int x2 = tile2.findTileXCoordinateInMap();
        int y2 = tile2.findTileYCoordinateInMap();
        if (Math.abs(x - x2) > 1 || Math.abs(y - y2) > 1) return false;
        if (x % 2 == 0) {
            if (y2 - y == 1 && x != x2) return false;
            return true;
        } else {
            if (y - y2 == 1 && x != x2) return false;
            return true;
        }
    }

    public boolean hasCommonRiver(Tile tile1, Tile tile2) {
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if ((river.getFirstTile().equals(tile1) && river.getSecondTile().equals(tile2)) || (river.getFirstTile().equals(tile2) && river.getSecondTile().equals(tile1)))
                return true;
        }
        return false;
    }

    public boolean hasCommonRoadOrRailRoad(Tile tile1, Tile tile2) {
        boolean hasRoad = false;
        boolean hasRailRoad = false;
        for (Improvement improvement : tile1.getImprovements()) {
            if (improvement.getType().equals(ImprovementType.ROAD)) hasRoad = true;
            if (improvement.getType().equals(ImprovementType.RAILROAD)) hasRailRoad = true;
        }

        for (Improvement improvement : tile2.getImprovements()) {
            if (hasRoad && improvement.getType().equals(ImprovementType.ROAD)) return true;
            if (hasRailRoad && improvement.getType().equals(ImprovementType.RAILROAD)) return true;
        }
        return false;
    }

    public boolean isInZOC(Unit unit, Tile tile) {
        for (Unit unit2 : GameDataBase.getGameDataBase().getUnits()) {
            if (areTwoTilesAdjacent(tile, unit2.getLocation()) && !unit.getOwner().equals(unit2.getOwner()) && unit2.getType().getCombatType() != CombatType.CIVILIAN)
                return true;
        }
        return false;
    }

    public Civilization getCurrentPlayer() {
        return gameDataBase.getCurrentPlayer();
    }

    public void declareWar(Civilization attacker, Civilization defender) {
        DiplomaticRelation relation = gameDataBase.getDiplomaticRelation(attacker, defender);
        if (relation != null) {
            relation.setAreAtWar(true);
        }
    }

    public void declarePeace(Civilization attacker, Civilization defender) {
        DiplomaticRelation relation = gameDataBase.getDiplomaticRelation(attacker, defender);
        if (relation != null) {
            relation.setAreAtWar(false);
        }
    }

    public boolean isTileImpassable(Tile tile) {
        if (tile.getTerrainType().equals(TerrainType.OCEAN)
                || tile.getTerrainType().equals(TerrainType.MOUNTAIN)
                || tile.getFeatures().contains(Feature.ICE)) {
            return true;
        }
        return false;
    }


    public MPCostInterface calculateRequiredMps(Unit unit, Tile sourceTile, Tile destinationTile) {
        int MPs = 0;
        boolean hasCommonRoadOrRailRoad = false;
        if (!areTwoTilesAdjacent(destinationTile, sourceTile)) {
            Debugger.debug("Two tile are not adjacent!");
            return null;
        }
        if (destinationTile.getTerrainType().equals(TerrainType.OCEAN) || destinationTile.getTerrainType().equals(TerrainType.MOUNTAIN) || destinationTile.getFeatures().contains(Feature.ICE))
            return MPCostEnum.IMPASSABLE;
        if (hasCommonRoadOrRailRoad(sourceTile, destinationTile) && ((getDiplomaticRelationsMap(new CivilizationPair(unit.getOwner(), sourceTile.getRoadOfTile().getFounder())).getFriendliness() >= 0 && getDiplomaticRelationsMap(new CivilizationPair(unit.getOwner(), destinationTile.getRoadOfTile().getFounder())).getFriendliness() >= 0) || (getDiplomaticRelationsMap(new CivilizationPair(unit.getOwner(), sourceTile.getRailRoadOfTile().getFounder())).getFriendliness() >= 0 && getDiplomaticRelationsMap(new CivilizationPair(unit.getOwner(), destinationTile.getRailRoadOfTile().getFounder())).getFriendliness() >= 0)))
            hasCommonRoadOrRailRoad = true;
        if (hasCommonRiver(sourceTile, destinationTile) && !(hasCommonRoadOrRailRoad && unit.getOwner().getTechnologies().getLearnedTechnologies().contains(Technology.CONSTRUCTION)))
            return MPCostEnum.EXPENSIVE;
        if (isInZOC(unit, sourceTile) && isInZOC(unit, destinationTile)) return MPCostEnum.EXPENSIVE;
        if (!unit.getType().equals(UnitType.SCOUT))
            MPs += ((MPCostClass) destinationTile.getTerrainType().getMovementCost()).getCost();
        for (Feature feature : destinationTile.getFeatures())
            MPs += ((MPCostClass) feature.getMovementCost()).getCost();
        if (hasCommonRoadOrRailRoad) MPs = (int) Math.max(MPs * 0.5, 1);
        return new MPCostClass(MPs);
    }

    public ArrayList<Tile> getAdjacentTiles(Tile tile) {
        int x = tile.findTileXCoordinateInMap();
        int y = tile.findTileYCoordinateInMap();
        ArrayList<Tile> tiles = new ArrayList<>();
        if (GameMap.getGameMap().areCoordinatesValid(x, y - 1)) {
            tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x, y - 1));
        }
        if (GameMap.getGameMap().areCoordinatesValid(x, y + 1)) {
            tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x, y + 1));
        }
        if (GameMap.getGameMap().areCoordinatesValid(x - 1, y)) {
            tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x - 1, y));
        }
        if (GameMap.getGameMap().areCoordinatesValid(x + 1, y)) {
            tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x + 1, y));
        }
        if (x % 2 == 1) {
            if (GameMap.getGameMap().areCoordinatesValid(x - 1, y + 1)) {
                tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x - 1, y + 1));
            }
            if (GameMap.getGameMap().areCoordinatesValid(x + 1, y + 1)) {
                tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x + 1, y + 1));
            }
        } else {
            if (GameMap.getGameMap().areCoordinatesValid(x - 1, y - 1)) {
                tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x - 1, y - 1));
            }
            if (GameMap.getGameMap().areCoordinatesValid(x + 1, y - 1)) {
                tiles.add(GameDataBase.getGameDataBase().getMap().getTile(x + 1, y - 1));
            }
        }
        return tiles;
    }

    public boolean isTileBlocker(Tile tile) {
        if (tile.getTerrainType().equals(TerrainType.HILLS) || tile.getTerrainType().equals(TerrainType.MOUNTAIN) || tile.getFeatures().contains(Feature.FOREST))
            return true;
        return false;
    }

    public ArrayList<Tile> getVisibleTilesFromTile(Tile tile, int distance) {
        if (distance != 1 && distance != 2) {
            Debugger.debug("Distance is invalid");
            return null;
        }
        ArrayList<Tile> tempTiles = new ArrayList<>();
        ArrayList<Tile> waitingTiles = new ArrayList<>();
        ArrayList<Tile> finalTiles = new ArrayList<>();
        tempTiles = getAdjacentTiles(tile);
        for (Tile tile2 : tempTiles) {
            if (isTileBlocker(tile2) && !tile.getTerrainType().equals(TerrainType.HILLS)) waitingTiles.add(tile2);
            else finalTiles.add(tile2);
        }

        if (distance == 1) {
            finalTiles.addAll(waitingTiles);
            return finalTiles;
        }
        int size = finalTiles.size();
        for (int i = 0; i < size; i++) {
            finalTiles.addAll(getAdjacentTiles(finalTiles.get(i)));
        }
        finalTiles.addAll(waitingTiles);
        finalTiles.add(tile);
        return deleteRepetitiveElementsFromArrayList(finalTiles);
    }

    public ArrayList<Tile> deleteRepetitiveElementsFromArrayList(ArrayList<Tile> tiles) {
        Set<Tile> set = new LinkedHashSet<>();
        set.addAll(tiles);
        tiles.clear();
        tiles.addAll(set);
        return tiles;
    }

    public ArrayList<Tile> getVisibleTilesByCities(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (!city.getOwner().equals(civilization)) continue;
            for (Tile tile : city.getTerritories()) {
                tiles.addAll(getVisibleTilesFromTile(tile, 1));
            }
            tiles.add(city.getCentralTile());
        }
        return deleteRepetitiveElementsFromArrayList(tiles);
    }

    public ArrayList<Tile> getVisibleTilesByUnits(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Unit unit : GameDataBase.getGameDataBase().getUnits()) {
            if (unit.getOwner().equals(civilization))
                tiles.addAll(getVisibleTilesFromTile(unit.getLocation(), unit.getType().getCombatType().equals(CombatType.SIEGE) ? 1 : 2));
        }
        return deleteRepetitiveElementsFromArrayList(tiles);
    }

    public ArrayList<Tile> getVisibleTilesByUnit(Unit unit) {
        return getVisibleTilesFromTile(unit.getLocation(), unit.getType().getCombatType().equals(CombatType.SIEGE) ? 1 : 2);
    }

    public ArrayList<Tile> getVisibleTilesByCivilization(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.addAll(getVisibleTilesByCities(civilization));
        tiles.addAll(getVisibleTilesByUnits(civilization));
        return deleteRepetitiveElementsFromArrayList(tiles);
    }

    public void setMapImageOfCivilization(Civilization civilization) {
        if (civilization.isEverythingVisibleCheatCodeInEffect()) {
            return;
        }
        HashMap<Tile, TileImage> newMapImage = new HashMap<>();
        ArrayList<Tile> visibleTiles = getVisibleTilesByCivilization(civilization);
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) == null) {
                newMapImage.put(tile, null);
                discoverCivsInTile(tile);
            } else if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) instanceof TileHistory)
                newMapImage.put(tile, civilization.getMapImage().get(tile));
            else if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) instanceof Tile)
                newMapImage.put(tile, tile.createTileHistory());
            else newMapImage.put(tile, tile);
        }
        civilization.getMapImage().clear();
        civilization.getMapImage().putAll(newMapImage);
    }

    public void makeEverythingVisible() {
        Civilization player = getCurrentPlayer();
        for (Tile tile : player.getMapImage().keySet()) {
            discoverCivsInTile(tile);
            player.getMapImage().put(tile, tile);
        }
        player.setEverythingVisibleCheatCodeInEffect(true);
    }

    public void discoverCivsInTile(Tile tile) {
        for (Unit unit : getUnitsInTile(tile)) {
            DiplomaticRelation relation = gameDataBase.getDiplomaticRelation(getCurrentPlayer(), unit.getOwner());
            if (relation != null) {
                relation.setAreMutuallyVisible(true);
            }
        }
        City city = whoseTerritoryIsTileIn(tile);
        if (city != null) {
            DiplomaticRelation relation = gameDataBase.getDiplomaticRelation(getCurrentPlayer(), city.getOwner());
            if (relation != null) {
                relation.setAreMutuallyVisible(true);
            }
        }
    }

    public int getMapWidth() {
        return GameMap.getGameMap().getMap()[0].length;
    }

    public int getMapHeight() {
        return GameMap.getGameMap().getMap().length;
    }

    public ArrayList<Tile> findPath(Unit unit, Tile sourceTile, Tile destinationTile) {
        ArrayList<Tile> pathTiles = new ArrayList<>();
        TileGraph graph = this.makeTilesGraph(unit, sourceTile, destinationTile);
        GraphNode destinationNode = graph.getNodeByTile(destinationTile);
        for (GraphNode graphNode : destinationNode.getShortestPath()) {
            pathTiles.add(graphNode.getTile());
        }
        pathTiles.add(destinationTile);
        return pathTiles;
    }

    private TileGraph calculateShortestPathFromSourceTile(TileGraph graph, GraphNode source) {
        source.setDistance(0);
        HashSet<GraphNode> settledNodes = new HashSet<>();
        HashSet<GraphNode> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);
        while (!unsettledNodes.isEmpty()) {
            GraphNode currentNode = this.getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<GraphNode, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                if (!settledNodes.contains(adjacencyPair.getKey()) && currentNode.getDistance() + adjacencyPair.getValue() < adjacencyPair.getKey().getDistance()) {
                    adjacencyPair.getKey().setDistance(currentNode.getDistance() + adjacencyPair.getValue());
                    List<GraphNode> shortestPath = new LinkedList<>(currentNode.getShortestPath());
                    shortestPath.add(currentNode);
                    adjacencyPair.getKey().setShortestPath(shortestPath);
                    unsettledNodes.add(adjacencyPair.getKey());
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private GraphNode getLowestDistanceNode(HashSet<GraphNode> unsettledNodes) {
        GraphNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (GraphNode unsettledNode : unsettledNodes) {
            if (unsettledNode.getDistance() < lowestDistance) {
                lowestDistance = unsettledNode.getDistance();
                lowestDistanceNode = unsettledNode;
            }
        }
        return lowestDistanceNode;
    }

    private TileGraph makeTilesGraph(Unit unit, Tile origin, Tile destination) {
        TileGraph graph = new TileGraph();
        GraphNode sourceNode = new GraphNode(origin);
        graph.addNode(sourceNode);
        while (!graph.isTileAddedToGraph(destination)) {
            ArrayList<GraphNode> nodes = new ArrayList<>(graph.getNodes());
            for (int i = 0; i < nodes.size(); i++) {
                GraphNode node = nodes.get(i);
                ArrayList<Tile> adjacentTiles = this.getAdjacentTiles(node.getTile());
                for (Tile adjacentTile : adjacentTiles) {
                    if (!graph.isTileAddedToGraph(adjacentTile)) {
                        MPCostInterface mpCost;
                        if ((mpCost = this.calculateRequiredMps(unit, node.getTile(), adjacentTile)) != MPCostEnum.IMPASSABLE) {
                            GraphNode newNode = new GraphNode(adjacentTile);
                            int requiredMp = 2;
                            if (mpCost instanceof MPCostClass) {
                                requiredMp = ((MPCostClass) mpCost).getCost();
                            }
                            node.addDestination(newNode, requiredMp);
                            graph.addNode(newNode);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            ArrayList<GraphNode> nodes = new ArrayList<>(graph.getNodes());
            for (int j = 0; j < nodes.size(); j++) {
                GraphNode node = nodes.get(j);
                ArrayList<Tile> adjacentTiles = this.getAdjacentTiles(node.getTile());
                for (Tile adjacentTile : adjacentTiles) {
                    if (!graph.isTileAddedToGraph(adjacentTile)) {
                        MPCostInterface mpCost;
                        if ((mpCost = this.calculateRequiredMps(unit, node.getTile(), adjacentTile)) != MPCostEnum.IMPASSABLE) {
                            GraphNode newNode = new GraphNode(adjacentTile);
                            int requiredMp = 2;
                            if (mpCost instanceof MPCostClass) {
                                requiredMp = ((MPCostClass) mpCost).getCost();
                            }
                            node.addDestination(newNode, requiredMp);
                            graph.addNode(newNode);
                        }
                    }
                }
            }
        }
        graph = this.calculateShortestPathFromSourceTile(graph, sourceNode);
        return graph;
    }

    /*Diplomacy functions*/
    public DiplomaticRelation getDiplomaticRelationsMap(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof DiplomaticRelation && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                return (DiplomaticRelation) diplomacy;
        }
        //it will never return null
        return null;
    }

    public ArrayList<ScientificTreaty> getScientificTreaties(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        ArrayList<ScientificTreaty> scientificTreaties = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof ScientificTreaty && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                scientificTreaties.add((ScientificTreaty) diplomacy);
        }
        return scientificTreaties;
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContracts(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public WarInfo getWarInfos(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof WarInfo && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                return (WarInfo) diplomacy;
        }
        return null;
    }

    public ArrayList<DiplomaticRelation> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
        ArrayList<DiplomaticRelation> diplomaticRelationsMaps = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof DiplomaticRelation && diplomacy.getPair().containsCivilization(civilization))
                diplomaticRelationsMaps.add((DiplomaticRelation) diplomacy);
        }
        return diplomaticRelationsMaps;
    }

    public ArrayList<ScientificTreaty> getScientificTreatiesOfCivilization(Civilization civilization) {
        ArrayList<ScientificTreaty> scientificTreaties = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof ScientificTreaty && diplomacy.getPair().containsCivilization(civilization))
                scientificTreaties.add((ScientificTreaty) diplomacy);
        }
        return scientificTreaties;
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationPayer(Civilization civilization) {
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && ((StepWiseGoldTransferContract) diplomacy).getPayer() == civilization)
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationRecipient(Civilization civilization) {
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && ((StepWiseGoldTransferContract) diplomacy).getRecipient() == civilization)
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public ArrayList<WarInfo> getWarInfoMapOfCivilization(Civilization civilization) {
        ArrayList<WarInfo> warInfos = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
            if (diplomacy instanceof WarInfo && diplomacy.getPair().containsCivilization(civilization))
                warInfos.add((WarInfo) diplomacy);
        }
        return warInfos;
    }

    public ArrayList<Notification> getCivilizationNewNotification() {
        ArrayList<Notification> allNotifications = this.getCurrentPlayer().getNotifications();
        ArrayList<Notification> newNotifications = new ArrayList<>();
        for (int i = 0; i < allNotifications.size(); i++) {
            if (!allNotifications.get(i).getIsSeen()) {
                newNotifications.add(allNotifications.get(i));
            }
        }
        return newNotifications;
    }

    public void seenAllNotifications() {
        ArrayList<Notification> notifications = this.getCurrentPlayer().getNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            notifications.get(i).setIsSeen(true);
        }
    }

    public int calculateScoreForCivilization(Civilization civilization) {
        int score = 0;
        score += this.calculateCivilizationTerritorySize(civilization) * 10;
        score += this.calculateSumOfMilitaryUnitsCostsForCivilization(civilization);
        score += this.calculateEffectOfOutputOnScore(civilization);
        score += civilization.getGoldCount();
        score += this.calculateNumberOfResourcesForCivilization(civilization) * 10;
        score += civilization.getTechnologies().getLearnedTechnologies().size() * 10;
        score += civilization.getAllImprovements().size() * 5;
        return score;
    }

    public int calculateCivilizationTerritorySize(Civilization civilization) {
        ArrayList<City> cities = civilization.getCities();
        int territorySize = 0;
        for (City city : cities) {
            territorySize += city.getTerritories().size();
        }
        return territorySize;
    }

    public int calculateSumOfMilitaryUnitsCostsForCivilization(Civilization civilization) {
        ArrayList<Unit> units = civilization.getMilitaryUnits();
        int sum = 0;
        for (Unit unit : units) {
            sum += unit.getType().getCost();
        }
        return sum;
    }

    public int calculateEffectOfOutputOnScore(Civilization civilization) {
        return (int) civilization.calculateNetGoldProduction() * 10 + (int) civilization.calculateTotalBeakers() * 10 + civilization.calculateTotalFoodFromCities() * 10;
    }

    public int calculateNumberOfResourcesForCivilization(Civilization civilization) {
        HashMap<LuxuryResource, Integer> luxuryResources = civilization.getLuxuryResources();
        HashMap<StrategicResource, Integer> strategicResources = civilization.getStrategicResources();
        int count = 0;
        for (Map.Entry<LuxuryResource, Integer> entry : luxuryResources.entrySet()) {
            if (entry.getValue() > 0) {
                count += entry.getValue();
            }
        }
        for (Map.Entry<StrategicResource, Integer> entry : strategicResources.entrySet()) {
            if (entry.getValue() > 0) {
                count += entry.getValue();
            }
        }
        return count;
    }

    public void sortPlayersInOrderOfScore() {
        GameDataBase gameDatabase = GameDataBase.getGameDataBase();
        for (int i = 0; i < gameDatabase.getPlayers().size(); i++) {
            for (int j = i + 1; j < gameDatabase.getPlayers().size(); j++) {
                Player firstPlayer = gameDatabase.getPlayers().get(i);
                Player secondPlayer = gameDatabase.getPlayers().get(j);
                int firstCivScore = this.calculateScoreForCivilization(firstPlayer.getCivilization());
                int secondCivScore = this.calculateScoreForCivilization(secondPlayer.getCivilization());
                if (firstCivScore < secondCivScore) {
                    Player temp = firstPlayer;
                    firstPlayer = secondPlayer;
                    secondPlayer = temp;
                }
            }
        }
    }

    public int calculateHighestScore() {
        ArrayList<Player> players = GameDataBase.getGameDataBase().getPlayers();
        int highestScore = 0;
        for (Player player : players) {
            if (this.calculateScoreForCivilization(player.getCivilization()) > highestScore) {
                highestScore = this.calculateScoreForCivilization(player.getCivilization());
            }
        }
        return highestScore;
    }

    public int calculateLowestScore() {
        ArrayList<Player> players = GameDataBase.getGameDataBase().getPlayers();
        int lowestScore = this.calculateScoreForCivilization(players.get(0).getCivilization());
        for (Player player : players) {
            if (this.calculateScoreForCivilization(player.getCivilization()) < lowestScore) {
                lowestScore = this.calculateScoreForCivilization(player.getCivilization());
            }
        }
        return lowestScore;
    }

    public double calculateAverageScore() {
        ArrayList<Player> players = GameDataBase.getGameDataBase().getPlayers();
        int averageScore = 0;
        for (Player player : players) {
            averageScore += this.calculateScoreForCivilization(player.getCivilization());
        }
        averageScore /= players.size();
        return averageScore;
    }

    public HashMap<String, ArrayList<String>> loadMeta() throws IOException {
        File file = new File("meta.json");
        HashMap<String, ArrayList<String>> meta = new HashMap<>();
        if (!file.exists())
            return meta;
        FileInputStream fileInputStream = new FileInputStream(file);
        String data = new String(fileInputStream.readAllBytes());
        meta = new Gson().fromJson(data, new TypeToken<HashMap<String, ArrayList<String>>>(){}.getType());
        fileInputStream.close();
        return meta;
    }

    public void saveMeta(HashMap<String, ArrayList<String>> input) throws IOException {
        String data = new Gson().toJson(input);
        FileOutputStream fileOutputStream = new FileOutputStream("meta.json");
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();
    }

    public void saveGame(String fileName) {
        try {
            if (GameDataBase.getGameDataBase().getPlayers().isEmpty())
                return;
            FileOutputStream fileOutputStream = new FileOutputStream("save/" + fileName + ".sav");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(GameDataBase.getGameDataBase());
            objectOutputStream.flush();
            objectOutputStream.close();

            HashMap<String, ArrayList<String>> meta = loadMeta();
            meta.put(fileName + ".sav", GameDataBase.getGameDataBase().getAllPlayersUsername());
            saveMeta(meta);
        } catch (Exception e) {
            Debugger.debug("Serialization");
            e.printStackTrace();
        }
    }

    public void loadGame(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream("save/" + fileName + ".sav");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GameDataBase.setGameDataBaseWithForce((GameDataBase) objectInputStream.readObject());
            objectInputStream.close();
        } catch (Exception e) {
            Debugger.debug("Deserialization");
        }
    }

    public void saveGameManually(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyـMMـddـhhـmmـss");
        saveGame("manual_save_" + dateTimeFormatter.format(now));
    }

    public ArrayList<String> listSavedFiles(boolean isAuto) throws IOException {
        HashMap<String, ArrayList<String>> meta = loadMeta();
        String name = ProgramDatabase.getProgramDatabase().getLoggedInUser().getUsername();
        File file = new File("save");
        String[] files = file.list();
        ArrayList<String> filesArrayList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if ((isAuto && files[i].startsWith("auto")) || (!isAuto && files[i].startsWith("manual"))) {
                if (meta.get(files[i]).contains(name))
                    filesArrayList.add(files[i]);
            }
        }
        return filesArrayList;
    }

}
