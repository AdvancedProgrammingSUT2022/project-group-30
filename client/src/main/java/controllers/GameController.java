package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.*;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.diplomacy.*;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.*;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.technology.Technology;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import models.works.Work;
import netPackets.Request;
import netPackets.Response;
import utilities.Debugger;
import utilities.MyGson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameController {

    private NetworkController networkController = NetworkController.getNetworkController();

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
        Request request = new Request("GameController", "GameController", "initializeGame", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addCivilizationToPairs(Civilization newCivilization) {
        Request request = new Request("GameController", "addCivilizationToPairs", MyGson.toJson(newCivilization));
        NetworkController.getNetworkController().transferData(request);
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits(int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("GameController", "assignCivsToPlayersAndInitializePrimaryUnits", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        NetworkController.getNetworkController().transferData(request);
    }

    private ArrayList<String> getAppropriateStartingPoints(ArrayList<String> fileLines, int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("GameController", "getAppropriateStartingPoints", MyGson.toJson(fileLines), MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location) {
        Request request = new Request("GameController", "createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location));
        NetworkController.getNetworkController().transferData(request);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location, int initialXP) {
        Request request = new Request("GameController", "createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location), MyGson.toJson(initialXP));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addPlayers(User[] players) {
        Request request = new Request("GameController", "addPlayers", MyGson.toJson(players));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean areCoordinatesValid(int x, int y) {
        Request request = new Request("GameController", "areCoordinatesValid", MyGson.toJson(x), MyGson.toJson(y));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Tile getTileByCoordinates(int x, int y) {
        Request request = new Request("GameController", "getTileByCoordinates", MyGson.toJson(x), MyGson.toJson(y));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerFixImprovement(Unit worker) {
        Request request = new Request("GameController", "canWorkerFixImprovement", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ImprovementType getTypeOfPillagedImprovement(Unit worker) {
        Request request = new Request("GameController", "getTypeOfPillagedImprovement", MyGson.toJson(worker));
        return (ImprovementType) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerFixRoute(Unit worker) {
        Request request = new Request("GameController", "canWorkerFixRoute", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerClearRoutes(Unit worker) {
        Request request = new Request("GameController", "canWorkerClearRoutes", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerClearFeature(Unit worker, Feature feature) {
        Request request = new Request("GameController", "canWorkerClearFeature", MyGson.toJson(worker), MyGson.toJson(feature));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerBuildFarmOrMine(Unit worker, ImprovementType type) {
        Request request = new Request("GameController", "canWorkerBuildFarmOrMine", MyGson.toJson(worker), MyGson.toJson(type));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void startResearch(Civilization civilization, Technology researchProject) {
        Request request = new Request("GameController", "startResearch", MyGson.toJson(civilization), MyGson.toJson(researchProject));
        NetworkController.getNetworkController().transferData(request);
    }

    public void stopResearch(Civilization civilization) {
        Request request = new Request("GameController", "stopResearch", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void stopPreviousResearchAndStartNext(Civilization civilization, Technology next) {
        Request request = new Request("GameController", "stopPreviousResearchAndStartNext", MyGson.toJson(civilization), MyGson.toJson(next));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerBuildImprovement(Unit worker, ImprovementType improvementType) {
        Request request = new Request("GameController", "canWorkerBuildImprovement", MyGson.toJson(worker), MyGson.toJson(improvementType));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerBuildRoute(Unit worker, ImprovementType routeType) {
        Request request = new Request("GameController", "canWorkerBuildRoute", MyGson.toJson(worker), MyGson.toJson(routeType));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isWorkerWorking(Unit worker) {
        Request request = new Request("GameController", "isWorkerWorking", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Work getWorkersWork(Unit worker) {
        Request request = new Request("GameController", "getWorkersWork", MyGson.toJson(worker));
        return (Work) NetworkController.getNetworkController().transferData(request);
    }

    public TileVisibility getTileVisibilityForPlayer(Tile tile) {
        Request request = new Request("GameController", "getTileVisibilityForPlayer", MyGson.toJson(tile));
        return (TileVisibility) NetworkController.getNetworkController().transferData(request);
    }

    public void moveUnit(Unit unit, Tile destination) {
        Request request = new Request("GameController", "moveUnit", MyGson.toJson(unit), MyGson.toJson(destination));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitTeleportToTile(UnitType unit, Civilization owner, Tile tile) {
        Request request = new Request("GameController", "canUnitTeleportToTile", MyGson.toJson(unit), MyGson.toJson(owner), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    private void awakeAllNearAlertedUnits(Unit unit) {
        Request request = new Request("GameController", "awakeAllNearAlertedUnits", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void moveUnitAlongItsPath(Unit unit) {
        Request request = new Request("GameController", "moveUnitAlongItsPath", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    private void updateUnitPath(Unit unit, Tile destination) {
        Request request = new Request("GameController", "updateUnitPath", MyGson.toJson(unit), MyGson.toJson(destination));
        NetworkController.getNetworkController().transferData(request);
    }

    private void expendMPForMovementAlongPath(Unit unit, Tile destination) {
        Request request = new Request("GameController", "expendMPForMovementAlongPath", MyGson.toJson(unit), MyGson.toJson(destination));
        NetworkController.getNetworkController().transferData(request);
    }

    private Tile findFarthesestTileByMPCost(Unit unit) {
        Request request = new Request("GameController", "findFarthesestTileByMPCost", MyGson.toJson(unit));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public City getCivsCityInTile(Tile tile, Civilization civilization) {
        Request request = new Request("GameController", "getCivsCityInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesTileContainEnemyCombative(Tile tile, Civilization reference) {
        Request request = new Request("GameController", "doesTileContainEnemyCombative", MyGson.toJson(tile), MyGson.toJson(reference));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public combative getPriorityTargetInTile(Tile tile, Civilization reference) {
        Request request = new Request("GameController", "getPriorityTargetInTile", MyGson.toJson(tile), MyGson.toJson(reference));
        return (combative) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        Request request = new Request("GameController", "getUnitsInTile", MyGson.toJson(tile));
        ArrayList<Unit> result = (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request, Unit[].class);
        return result;
    }

    public Unit getMilitaryUnitInTile(Tile tile) {
        Request request = new Request("GameController", "getMilitaryUnitInTile", MyGson.toJson(tile));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivilianUnitInTile(Tile tile) {
        Request request = new Request("GameController", "getCivilianUnitInTile", MyGson.toJson(tile));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("GameController", "getCivsUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsMilitaryUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("GameController", "getCivsMilitaryUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsCivilianUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("GameController", "getCivsCivilianUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileTooNearCity(Tile tile) {
        Request request = new Request("GameController", "isTileTooNearCity", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void foundCityWithSettler(Unit unit) {
        Request request = new Request("GameController", "foundCityWithSettler", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void checkVictoryByDominion() {
        Request request = new Request("GameController", "checkVictoryByDominion");
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesCivilizationHaveItsOriginalCapital(Civilization civilization) {
        Request request = new Request("GameController", "doesCivilizationHaveItsOriginalCapital", MyGson.toJson(civilization));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void defeatCivilization(Civilization civilization) {
        Request request = new Request("GameController", "defeatCivilization", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void makeCivilizationWin(Civilization civilization) {
        Request request = new Request("GameController", "makeCivilizationWin", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void endGameByTime() {
        Request request = new Request("GameController", "endGameByTime");
        NetworkController.getNetworkController().transferData(request);
    }

    public void disableTurnBreak() {
        Request request = new Request("GameController", "disableTurnBreak");
        NetworkController.getNetworkController().transferData(request);
    }

    public void deleteUnit(Unit unit) {
        Request request = new Request("GameController", "deleteUnit", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void removeUnit(Unit unit) {
        Request request = new Request("GameController", "removeUnit", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void destroyCity(City city) {
        Request request = new Request("GameController", "destroyCity", MyGson.toJson(city));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Unit> getCurrentPlayersUnitsWaitingForCommand() {
        Request request = new Request("GameController", "getCurrentPlayersUnitsWaitingForCommand");
        return (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request);
    }

    public void goToNextTurn() {
        Request request = new Request("GameController", "goToNextTurn");
        NetworkController.getNetworkController().transferData(request);
    }

    public void goToNextPlayer() {
        Request request = new Request("GameController", "goToNextPlayer");
        NetworkController.getNetworkController().transferData(request);
    }

    public Civilization getNextPlayer() {
        Request request = new Request("GameController", "getNextPlayer");
        return (Civilization) NetworkController.getNetworkController().transferData(request);
    }

    public void setProgramDatabase() {
        Request request = new Request("GameController", "setProgramDatabase");
        NetworkController.getNetworkController().transferData(request);
    }

    public ProgramDatabase getProgramDatabase() {
        Request request = new Request("GameController", "getProgramDatabase");
        return (ProgramDatabase) NetworkController.getNetworkController().transferData(request);
    }

    public void setGameDataBase() {
        Request request = new Request("GameController", "setGameDataBase");
        NetworkController.getNetworkController().transferData(request);
    }

    public GameDataBase getGameDataBase() {
        Request request = new Request("GameController", "getGameDataBase");
        return (GameDataBase) NetworkController.getNetworkController().transferData(request);
    }

    public City getCityCenteredInTile(Tile tile) {
        Request request = new Request("GameController", "getCityCenteredInTile", MyGson.toJson(tile));
        City result = (City) NetworkController.getNetworkController().transferData(request);;
        System.out.println(result);
        return result;
    }

    public boolean canUnitMove(Unit unit) {
        Request request = new Request("GameController", "canUnitMove", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitPillage(Unit unit) {
        Request request = new Request("GameController", "canUnitPillage", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitSetUpForRangedAttack(Unit unit) {
        Request request = new Request("GameController", "canUnitSetUpForRangedAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitMeleeAttack(Unit unit) {
        Request request = new Request("GameController", "canUnitMeleeAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitRangedAttack(Unit unit) {
        Request request = new Request("GameController", "canUnitRangedAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void addImprovementToTile(Tile tile, ImprovementType improvementType) {
        Request request = new Request("GameController", "addImprovementToTile", MyGson.toJson(tile), MyGson.toJson(improvementType));
        NetworkController.getNetworkController().transferData(request);
    }

    public void pillageUnitsTile(Unit unit) {
        Request request = new Request("GameController", "pillageUnitsTile", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public City whoseTerritoryIsTileInButIsNotTheCenterOf(Tile tile) {
        Request request = new Request("GameController", "whoseTerritoryIsTileInButIsNotTheCenterOf", MyGson.toJson(tile));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public City whoseTerritoryIsTileIn(Tile tile) {
        Request request = new Request("GameController", "whoseTerritoryIsTileIn", MyGson.toJson(tile));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public void addTileToCityTerritory(City city, Tile tile) {
        Request request = new Request("GameController", "addTileToCityTerritory", MyGson.toJson(city), MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        Request request = new Request("GameController", "areTwoTilesAdjacent", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean hasCommonRiver(Tile tile1, Tile tile2) {
        Request request = new Request("GameController", "hasCommonRiver", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean hasCommonRoadOrRailRoad(Tile tile1, Tile tile2) {
        Request request = new Request("GameController", "hasCommonRoadOrRailRoad", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isInZOC(Unit unit, Tile tile) {
        Request request = new Request("GameController", "isInZOC", MyGson.toJson(unit), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Civilization getCurrentPlayer() {
        Request request = new Request("GameController", "getCurrentPlayer");
        Civilization civ = (Civilization) NetworkController.getNetworkController().transferData(request);
        return civ;
    }

    public void declareWar(Civilization attacker, Civilization defender) {
        Request request = new Request("GameController", "declareWar", MyGson.toJson(attacker), MyGson.toJson(defender));
        NetworkController.getNetworkController().transferData(request);
    }

    public void declarePeace(Civilization attacker, Civilization defender) {
        Request request = new Request("GameController", "declarePeace", MyGson.toJson(attacker), MyGson.toJson(defender));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileImpassable(Tile tile) {
        Request request = new Request("GameController", "isTileImpassable", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }


    public MPCostInterface calculateRequiredMps(Unit unit, Tile sourceTile, Tile destinationTile) {
        Request request = new Request("GameController", "calculateRequiredMps", MyGson.toJson(unit), MyGson.toJson(sourceTile), MyGson.toJson(destinationTile));
        return (MPCostInterface) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getAdjacentTiles(Tile tile) {
        Request request = new Request("GameController", "getAdjacentTiles", MyGson.toJson(tile));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileBlocker(Tile tile) {
        Request request = new Request("GameController", "isTileBlocker", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesFromTile(Tile tile, int distance) {
        Request request = new Request("GameController", "getVisibleTilesFromTile", MyGson.toJson(tile), MyGson.toJson(distance));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> deleteRepetitiveElementsFromArrayList(ArrayList<Tile> tiles) {
        Request request = new Request("GameController", "deleteRepetitiveElementsFromArrayList", MyGson.toJson(tiles));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByCities(Civilization civilization) {
        Request request = new Request("GameController", "getVisibleTilesByCities", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByUnits(Civilization civilization) {
        Request request = new Request("GameController", "getVisibleTilesByUnits", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByUnit(Unit unit) {
        Request request = new Request("GameController", "getVisibleTilesByUnit", MyGson.toJson(unit));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByCivilization(Civilization civilization) {
        Request request = new Request("GameController", "getVisibleTilesByCivilization", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public void setMapImageOfCivilization(Civilization civilization) {
        Request request = new Request("GameController", "setMapImageOfCivilization", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void makeEverythingVisible() {
        Request request = new Request("GameController", "makeEverythingVisible");
        NetworkController.getNetworkController().transferData(request);
    }

    public void discoverCivsInTile(Tile tile) {
        Request request = new Request("GameController", "discoverCivsInTile", MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public int getMapWidth() {
        Request request = new Request("GameController", "getMapWidth");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int getMapHeight() {
        Request request = new Request("GameController", "getMapHeight");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> findPath(Unit unit, Tile sourceTile, Tile destinationTile) {
        Request request = new Request("GameController", "findPath", MyGson.toJson(unit), MyGson.toJson(sourceTile), MyGson.toJson(destinationTile));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    private TileGraph calculateShortestPathFromSourceTile(TileGraph graph, GraphNode source) {
        Request request = new Request("GameController", "calculateShortestPathFromSourceTile", MyGson.toJson(graph), MyGson.toJson(source));
        return (TileGraph) NetworkController.getNetworkController().transferData(request);
    }

    private GraphNode getLowestDistanceNode(HashSet<GraphNode> unsettledNodes) {
        Request request = new Request("GameController", "getLowestDistanceNode", MyGson.toJson(unsettledNodes));
        return (GraphNode) NetworkController.getNetworkController().transferData(request);
    }

    private TileGraph makeTilesGraph(Unit unit, Tile origin, Tile destination) {
        Request request = new Request("GameController", "makeTilesGraph", MyGson.toJson(unit), MyGson.toJson(origin), MyGson.toJson(destination));
        return (TileGraph) NetworkController.getNetworkController().transferData(request);
    }

    /*Diplomacy functions*/
    public DiplomaticRelation getDiplomaticRelationsMap(CivilizationPair pair) {
        Request request = new Request("GameController", "getDiplomaticRelationsMap", MyGson.toJson(pair));
        return (DiplomaticRelation) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<ScientificTreaty> getScientificTreaties(CivilizationPair pair) {
        Request request = new Request("GameController", "getScientificTreaties", MyGson.toJson(pair));
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContracts(CivilizationPair pair) {
        Request request = new Request("GameController", "getStepWiseGoldTransferContracts", MyGson.toJson(pair));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public WarInfo getWarInfos(CivilizationPair pair) {
        Request request = new Request("GameController", "getWarInfos", MyGson.toJson(pair));
        return (WarInfo) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<DiplomaticRelation> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
        Request request = new Request("GameController", "getDiplomaticRelationsMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<DiplomaticRelation>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<ScientificTreaty> getScientificTreatiesOfCivilization(Civilization civilization) {
        Request request = new Request("GameController", "getScientificTreatiesOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationPayer(Civilization civilization) {
        Request request = new Request("GameController", "getStepWiseGoldTransferContractsOfCivilizationPayer", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationRecipient(Civilization civilization) {
        Request request = new Request("GameController", "getStepWiseGoldTransferContractsOfCivilizationRecipient", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<WarInfo> getWarInfoMapOfCivilization(Civilization civilization) {
        Request request = new Request("GameController", "getWarInfoMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<WarInfo>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Notification> getCivilizationNewNotification() {
        Request request = new Request("GameController", "getCivilizationNewNotification");
        return (ArrayList<Notification>) NetworkController.getNetworkController().transferData(request);
    }

    public void seenAllNotifications() {
        Request request = new Request("GameController", "seenAllNotifications");
        NetworkController.getNetworkController().transferData(request);
    }

    public int calculateScoreForCivilization(Civilization civilization) {
        Request request = new Request("GameController", "calculateScoreForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateCivilizationTerritorySize(Civilization civilization) {
        Request request = new Request("GameController", "calculateCivilizationTerritorySize", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateSumOfMilitaryUnitsCostsForCivilization(Civilization civilization) {
        Request request = new Request("GameController", "calculateSumOfMilitaryUnitsCostsForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateEffectOfOutputOnScore(Civilization civilization) {
        Request request = new Request("GameController", "calculateEffectOfOutputOnScore", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateNumberOfResourcesForCivilization(Civilization civilization) {
        Request request = new Request("GameController", "calculateNumberOfResourcesForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public void sortPlayersInOrderOfScore() {
        Request request = new Request("GameController", "sortPlayersInOrderOfScore");
        NetworkController.getNetworkController().transferData(request);
    }

    public int calculateHighestScore() {
        Request request = new Request("GameController", "calculateHighestScore");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateLowestScore() {
        Request request = new Request("GameController", "calculateLowestScore");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public double calculateAverageScore() {
        Request request = new Request("GameController", "calculateAverageScore");
        return (double) NetworkController.getNetworkController().transferData(request);
    }

    public HashMap<String, ArrayList<String>> loadMeta() throws IOException {
        Request request = new Request("GameController", "loadMeta");
        return (HashMap<String, ArrayList<String>>) NetworkController.getNetworkController().transferData(request);
    }

    public void saveMeta(HashMap<String, ArrayList<String>> input) throws IOException {
        Request request = new Request("GameController", "saveMeta", MyGson.toJson(input));
        NetworkController.getNetworkController().transferData(request);
    }

    public void saveGame(String fileName) {
        Request request = new Request("GameController", "saveGame", MyGson.toJson(fileName));
        NetworkController.getNetworkController().transferData(request);
    }

    public void loadGame(String fileName) {
        Request request = new Request("GameController", "loadGame", MyGson.toJson(fileName));
        NetworkController.getNetworkController().transferData(request);
    }

    public void saveGameManually() {
        Request request = new Request("GameController", "saveGameManually");
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<String> listSavedFiles(boolean isAuto) throws IOException {
        Request request = new Request("GameController", "listSavedFiles", MyGson.toJson(isAuto));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request);
    }

    public TileImage[][] getCivilizationImageToShowOnScene(Civilization civ) {
        Request request = new Request("GameController", "getCivilizationImageToShowOnScene", MyGson.toJson(civ));
        return (TileImage[][]) NetworkController.getNetworkController().transferData(request);
    }

    public int findTileXCoordinateInMap(Tile tile) {
        Request request = new Request("GameController", "findTileXCoordinateInMap", MyGson.toJson(tile));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int findTileYCoordinateInMap(Tile tile) {
        Request request = new Request("GameController", "findTileYCoordinateInMap", MyGson.toJson(tile));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public void setPlayersSelectedEntity(Selectable selectable) {
        Request request = new Request("GameController", "setPlayersSelectedEntity", MyGson.toJson(selectable));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isCityOwnerEqualToCurrentPlayer(City city) {
        Request request = new Request("GameController", "isCityOwnerEqualToCurrentPlayer", MyGson.toJson(city));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isUnitOwnerEqualToCurrentPlayer(Unit unit) {
        Request request = new Request("GameController", "isUnitOwnerEqualToCurrentPlayer", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<RiverSegment> getMapRivers() {
        Request request = new Request("GameController", "getMapRivers");
        return (ArrayList<RiverSegment>) NetworkController.getNetworkController().transferData(request);
    }

    public String findRiverSegmentDirectionForTile(RiverSegment river, Tile tile) {
        Request request = new Request("GameController", "findRiverSegmentDirectionForTile", MyGson.toJson(river), MyGson.toJson(tile));
        return (String) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileImageEqualToTile(Tile tile, TileImage tileImage) {
        Request request = new Request("GameController", "isTileImageEqualToTile", MyGson.toJson(tile), MyGson.toJson(tileImage));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Tile getTileFromMap(int x, int y) {
        Request request = new Request("GameController", "getTileFromMap", MyGson.toJson(x), MyGson.toJson(y));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public void setCurrentPlayerFrameBase(Tile tile) {
        Request request = new Request("GameController", "setCurrentPlayerFrameBase", MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public Output calculateTheoreticalOutputForTile(Tile tile) {
        Request request = new Request("GameController", "calculateTheoreticalOutputForTile", MyGson.toJson(tile));
        return (Output) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canResourceBeExploitedForTile(Resource resource, Tile tile) {
        Request request = new Request("GameController", "canResourceBeExploitedForTile", MyGson.toJson(resource), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }


}
