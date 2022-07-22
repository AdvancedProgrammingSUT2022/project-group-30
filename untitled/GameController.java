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
        Request request = new Request("initializeGame", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
    }

    public void addCivilizationToPairs(Civilization newCivilization) {
        Request request = new Request("addCivilizationToPairs", MyGson.toJson(newCivilization));
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits(int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("assignCivsToPlayersAndInitializePrimaryUnits", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
    }

    private ArrayList<String> getAppropriateStartingPoints(ArrayList<String> fileLines, int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("getAppropriateStartingPoints", MyGson.toJson(fileLines), MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location) {
        Request request = new Request("createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location));
    }

    public void createUnit(UnitType type, Civilization owner, Tile location, int initialXP) {
        Request request = new Request("createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location), MyGson.toJson(initialXP));
    }

    public void addPlayers(User[] players) {
        Request request = new Request("addPlayers", MyGson.toJson(players));
    }

    public boolean areCoordinatesValid(int x, int y) {
        Request request = new Request("areCoordinatesValid", MyGson.toJson(x), MyGson.toJson(y));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Tile getTileByCoordinates(int x, int y) {
        Request request = new Request("getTileByCoordinates", MyGson.toJson(x), MyGson.toJson(y));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerFixImprovement(Unit worker) {
        Request request = new Request("canWorkerFixImprovement", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ImprovementType getTypeOfPillagedImprovement(Unit worker) {
        Request request = new Request("getTypeOfPillagedImprovement", MyGson.toJson(worker));
        return (ImprovementType) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerFixRoute(Unit worker) {
        Request request = new Request("canWorkerFixRoute", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerClearRoutes(Unit worker) {
        Request request = new Request("canWorkerClearRoutes", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerClearFeature(Unit worker, Feature feature) {
        Request request = new Request("canWorkerClearFeature", MyGson.toJson(worker), MyGson.toJson(feature));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerBuildFarmOrMine(Unit worker, ImprovementType type) {
        Request request = new Request("canWorkerBuildFarmOrMine", MyGson.toJson(worker), MyGson.toJson(type));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void startResearch(Civilization civilization, Technology researchProject) {
        Request request = new Request("startResearch", MyGson.toJson(civilization), MyGson.toJson(researchProject));
    }

    public void stopResearch(Civilization civilization) {
        Request request = new Request("stopResearch", MyGson.toJson(civilization));
    }

    public void stopPreviousResearchAndStartNext(Civilization civilization, Technology next) {
        Request request = new Request("stopPreviousResearchAndStartNext", MyGson.toJson(civilization), MyGson.toJson(next));
    }

    public boolean canWorkerBuildImprovement(Unit worker, ImprovementType improvementType) {
        Request request = new Request("canWorkerBuildImprovement", MyGson.toJson(worker), MyGson.toJson(improvementType));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canWorkerBuildRoute(Unit worker, ImprovementType routeType) {
        Request request = new Request("canWorkerBuildRoute", MyGson.toJson(worker), MyGson.toJson(routeType));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isWorkerWorking(Unit worker) {
        Request request = new Request("isWorkerWorking", MyGson.toJson(worker));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Work getWorkersWork(Unit worker) {
        Request request = new Request("getWorkersWork", MyGson.toJson(worker));
        return (Work) NetworkController.getNetworkController().transferData(request);
    }

    public TileVisibility getTileVisibilityForPlayer(Tile tile) {
        Request request = new Request("getTileVisibilityForPlayer", MyGson.toJson(tile));
        return (TileVisibility) NetworkController.getNetworkController().transferData(request);
    }

    public void moveUnit(Unit unit, Tile destination) {
        Request request = new Request("moveUnit", MyGson.toJson(unit), MyGson.toJson(destination));
    }

    public boolean canUnitTeleportToTile(UnitType unit, Civilization owner, Tile tile) {
        Request request = new Request("canUnitTeleportToTile", MyGson.toJson(unit), MyGson.toJson(owner), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    private void awakeAllNearAlertedUnits(Unit unit) {
        Request request = new Request("awakeAllNearAlertedUnits", MyGson.toJson(unit));
    }

    public void moveUnitAlongItsPath(Unit unit) {
        Request request = new Request("moveUnitAlongItsPath", MyGson.toJson(unit));
    }

    private void updateUnitPath(Unit unit, Tile destination) {
        Request request = new Request("updateUnitPath", MyGson.toJson(unit), MyGson.toJson(destination));
    }

    private void expendMPForMovementAlongPath(Unit unit, Tile destination) {
        Request request = new Request("expendMPForMovementAlongPath", MyGson.toJson(unit), MyGson.toJson(destination));
    }

    private Tile findFarthesestTileByMPCost(Unit unit) {
        Request request = new Request("findFarthesestTileByMPCost", MyGson.toJson(unit));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public City getCivsCityInTile(Tile tile, Civilization civilization) {
        Request request = new Request("getCivsCityInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesTileContainEnemyCombative(Tile tile, Civilization reference) {
        Request request = new Request("doesTileContainEnemyCombative", MyGson.toJson(tile), MyGson.toJson(reference));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public combative getPriorityTargetInTile(Tile tile, Civilization reference) {
        Request request = new Request("getPriorityTargetInTile", MyGson.toJson(tile), MyGson.toJson(reference));
        return (combative) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        Request request = new Request("getUnitsInTile", MyGson.toJson(tile));
        return (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getMilitaryUnitInTile(Tile tile) {
        Request request = new Request("getMilitaryUnitInTile", MyGson.toJson(tile));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivilianUnitInTile(Tile tile) {
        Request request = new Request("getCivilianUnitInTile", MyGson.toJson(tile));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("getCivsUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsMilitaryUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("getCivsMilitaryUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public Unit getCivsCivilianUnitInTile(Tile tile, Civilization civilization) {
        Request request = new Request("getCivsCivilianUnitInTile", MyGson.toJson(tile), MyGson.toJson(civilization));
        return (Unit) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileTooNearCity(Tile tile) {
        Request request = new Request("isTileTooNearCity", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void foundCityWithSettler(Unit unit) {
        Request request = new Request("foundCityWithSettler", MyGson.toJson(unit));
    }

    public void checkVictoryByDominion() {
        Request request = new Request("checkVictoryByDominion", MyGson.toJson());
    }

    public boolean doesCivilizationHaveItsOriginalCapital(Civilization civilization) {
        Request request = new Request("doesCivilizationHaveItsOriginalCapital", MyGson.toJson(civilization));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void defeatCivilization(Civilization civilization) {
        Request request = new Request("defeatCivilization", MyGson.toJson(civilization));
    }

    public void makeCivilizationWin(Civilization civilization) {
        Request request = new Request("makeCivilizationWin", MyGson.toJson(civilization));
    }

    public void endGameByTime() {
        Request request = new Request("endGameByTime", MyGson.toJson());
    }

    public void disableTurnBreak() {
        Request request = new Request("disableTurnBreak", MyGson.toJson());
    }

    public void deleteUnit(Unit unit) {
        Request request = new Request("deleteUnit", MyGson.toJson(unit));
    }

    public void removeUnit(Unit unit) {
        Request request = new Request("removeUnit", MyGson.toJson(unit));
    }

    public void destroyCity(City city) {
        Request request = new Request("destroyCity", MyGson.toJson(city));
    }

    public ArrayList<Unit> getCurrentPlayersUnitsWaitingForCommand() {
        Request request = new Request("getCurrentPlayersUnitsWaitingForCommand", MyGson.toJson());
        return (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request);
    }

    public void goToNextTurn() {
        Request request = new Request("goToNextTurn", MyGson.toJson());
    }

    public void goToNextPlayer() {
        Request request = new Request("goToNextPlayer", MyGson.toJson());
    }

    public Civilization getNextPlayer() {
        Request request = new Request("getNextPlayer", MyGson.toJson());
        return (Civilization) NetworkController.getNetworkController().transferData(request);
    }

    public void setProgramDatabase() {
        Request request = new Request("setProgramDatabase", MyGson.toJson());
    }

    public ProgramDatabase getProgramDatabase() {
        Request request = new Request("getProgramDatabase", MyGson.toJson());
        return (ProgramDatabase) NetworkController.getNetworkController().transferData(request);
    }

    public void setGameDataBase() {
        Request request = new Request("setGameDataBase", MyGson.toJson());
    }

    public GameDataBase getGameDataBase() {
        Request request = new Request("getGameDataBase", MyGson.toJson());
        return (GameDataBase) NetworkController.getNetworkController().transferData(request);
    }

    public City getCityCenteredInTile(Tile tile) {
        Request request = new Request("getCityCenteredInTile", MyGson.toJson(tile));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitMove(Unit unit) {
        Request request = new Request("canUnitMove", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitPillage(Unit unit) {
        Request request = new Request("canUnitPillage", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitSetUpForRangedAttack(Unit unit) {
        Request request = new Request("canUnitSetUpForRangedAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitMeleeAttack(Unit unit) {
        Request request = new Request("canUnitMeleeAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitRangedAttack(Unit unit) {
        Request request = new Request("canUnitRangedAttack", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void addImprovementToTile(Tile tile, ImprovementType improvementType) {
        Request request = new Request("addImprovementToTile", MyGson.toJson(tile), MyGson.toJson(improvementType));
    }

    public void pillageUnitsTile(Unit unit) {
        Request request = new Request("pillageUnitsTile", MyGson.toJson(unit));
    }

    public City whoseTerritoryIsTileInButIsNotTheCenterOf(Tile tile) {
        Request request = new Request("whoseTerritoryIsTileInButIsNotTheCenterOf", MyGson.toJson(tile));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public City whoseTerritoryIsTileIn(Tile tile) {
        Request request = new Request("whoseTerritoryIsTileIn", MyGson.toJson(tile));
        return (City) NetworkController.getNetworkController().transferData(request);
    }

    public void addTileToCityTerritory(City city, Tile tile) {
        Request request = new Request("addTileToCityTerritory", MyGson.toJson(city), MyGson.toJson(tile));
    }

    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        Request request = new Request("areTwoTilesAdjacent", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean hasCommonRiver(Tile tile1, Tile tile2) {
        Request request = new Request("hasCommonRiver", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean hasCommonRoadOrRailRoad(Tile tile1, Tile tile2) {
        Request request = new Request("hasCommonRoadOrRailRoad", MyGson.toJson(tile1), MyGson.toJson(tile2));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isInZOC(Unit unit, Tile tile) {
        Request request = new Request("isInZOC", MyGson.toJson(unit), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Civilization getCurrentPlayer() {
        Request request = new Request("getCurrentPlayer", MyGson.toJson());
        return (Civilization) NetworkController.getNetworkController().transferData(request);
    }

    public void declareWar(Civilization attacker, Civilization defender) {
        Request request = new Request("declareWar", MyGson.toJson(attacker), MyGson.toJson(defender));
    }

    public void declarePeace(Civilization attacker, Civilization defender) {
        Request request = new Request("declarePeace", MyGson.toJson(attacker), MyGson.toJson(defender));
    }

    public boolean isTileImpassable(Tile tile) {
        Request request = new Request("isTileImpassable", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }


    public MPCostInterface calculateRequiredMps(Unit unit, Tile sourceTile, Tile destinationTile) {
        Request request = new Request("calculateRequiredMps", MyGson.toJson(unit), MyGson.toJson(sourceTile), MyGson.toJson(destinationTile));
        return (MPCostInterface) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getAdjacentTiles(Tile tile) {
        Request request = new Request("getAdjacentTiles", MyGson.toJson(tile));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileBlocker(Tile tile) {
        Request request = new Request("isTileBlocker", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesFromTile(Tile tile, int distance) {
        Request request = new Request("getVisibleTilesFromTile", MyGson.toJson(tile), MyGson.toJson(distance));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> deleteRepetitiveElementsFromArrayList(ArrayList<Tile> tiles) {
        Request request = new Request("deleteRepetitiveElementsFromArrayList", MyGson.toJson(tiles));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByCities(Civilization civilization) {
        Request request = new Request("getVisibleTilesByCities", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByUnits(Civilization civilization) {
        Request request = new Request("getVisibleTilesByUnits", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByUnit(Unit unit) {
        Request request = new Request("getVisibleTilesByUnit", MyGson.toJson(unit));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesByCivilization(Civilization civilization) {
        Request request = new Request("getVisibleTilesByCivilization", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    public void setMapImageOfCivilization(Civilization civilization) {
        Request request = new Request("setMapImageOfCivilization", MyGson.toJson(civilization));
    }

    public void makeEverythingVisible() {
        Request request = new Request("makeEverythingVisible", MyGson.toJson());
    }

    public void discoverCivsInTile(Tile tile) {
        Request request = new Request("discoverCivsInTile", MyGson.toJson(tile));
    }

    public int getMapWidth() {
        Request request = new Request("getMapWidth", MyGson.toJson());
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int getMapHeight() {
        Request request = new Request("getMapHeight", MyGson.toJson());
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> findPath(Unit unit, Tile sourceTile, Tile destinationTile) {
        Request request = new Request("findPath", MyGson.toJson(unit), MyGson.toJson(sourceTile), MyGson.toJson(destinationTile));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request);
    }

    private TileGraph calculateShortestPathFromSourceTile(TileGraph graph, GraphNode source) {
        Request request = new Request("calculateShortestPathFromSourceTile", MyGson.toJson(graph), MyGson.toJson(source));
        return (TileGraph) NetworkController.getNetworkController().transferData(request);
    }

    private GraphNode getLowestDistanceNode(HashSet<GraphNode> unsettledNodes) {
        Request request = new Request("getLowestDistanceNode", MyGson.toJson(unsettledNodes));
        return (GraphNode) NetworkController.getNetworkController().transferData(request);
    }

    private TileGraph makeTilesGraph(Unit unit, Tile origin, Tile destination) {
        Request request = new Request("makeTilesGraph", MyGson.toJson(unit), MyGson.toJson(origin), MyGson.toJson(destination));
        return (TileGraph) NetworkController.getNetworkController().transferData(request);
    }

    /*Diplomacy functions*/
    public DiplomaticRelation getDiplomaticRelationsMap(CivilizationPair pair) {
        Request request = new Request("getDiplomaticRelationsMap", MyGson.toJson(pair));
        return (DiplomaticRelation) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<ScientificTreaty> getScientificTreaties(CivilizationPair pair) {
        Request request = new Request("getScientificTreaties", MyGson.toJson(pair));
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContracts(CivilizationPair pair) {
        Request request = new Request("getStepWiseGoldTransferContracts", MyGson.toJson(pair));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public WarInfo getWarInfos(CivilizationPair pair) {
        Request request = new Request("getWarInfos", MyGson.toJson(pair));
        return (WarInfo) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<DiplomaticRelation> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
        Request request = new Request("getDiplomaticRelationsMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<DiplomaticRelation>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<ScientificTreaty> getScientificTreatiesOfCivilization(Civilization civilization) {
        Request request = new Request("getScientificTreatiesOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationPayer(Civilization civilization) {
        Request request = new Request("getStepWiseGoldTransferContractsOfCivilizationPayer", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationRecipient(Civilization civilization) {
        Request request = new Request("getStepWiseGoldTransferContractsOfCivilizationRecipient", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<WarInfo> getWarInfoMapOfCivilization(Civilization civilization) {
        Request request = new Request("getWarInfoMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<WarInfo>) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Notification> getCivilizationNewNotification() {
        Request request = new Request("getCivilizationNewNotification", MyGson.toJson());
        return (ArrayList<Notification>) NetworkController.getNetworkController().transferData(request);
    }

    public void seenAllNotifications() {
        Request request = new Request("seenAllNotifications", MyGson.toJson());
    }

    public int calculateScoreForCivilization(Civilization civilization) {
        Request request = new Request("calculateScoreForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateCivilizationTerritorySize(Civilization civilization) {
        Request request = new Request("calculateCivilizationTerritorySize", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateSumOfMilitaryUnitsCostsForCivilization(Civilization civilization) {
        Request request = new Request("calculateSumOfMilitaryUnitsCostsForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateEffectOfOutputOnScore(Civilization civilization) {
        Request request = new Request("calculateEffectOfOutputOnScore", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateNumberOfResourcesForCivilization(Civilization civilization) {
        Request request = new Request("calculateNumberOfResourcesForCivilization", MyGson.toJson(civilization));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public void sortPlayersInOrderOfScore() {
        Request request = new Request("sortPlayersInOrderOfScore", MyGson.toJson());
    }

    public int calculateHighestScore() {
        Request request = new Request("calculateHighestScore", MyGson.toJson());
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateLowestScore() {
        Request request = new Request("calculateLowestScore", MyGson.toJson());
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public double calculateAverageScore() {
        Request request = new Request("calculateAverageScore", MyGson.toJson());
        return (double) NetworkController.getNetworkController().transferData(request);
    }

    public HashMap<String, ArrayList<String>> loadMeta() throws IOException {
        Request request = new Request("loadMeta", MyGson.toJson());
        return (HashMap<String, ArrayList<String>>) NetworkController.getNetworkController().transferData(request);
    }

    public void saveMeta(HashMap<String, ArrayList<String>> input) throws IOException {
        Request request = new Request("saveMeta", MyGson.toJson(HashMap < String), MyGson.toJson(input));
    }

    public void saveGame(String fileName) {
        Request request = new Request("saveGame", MyGson.toJson(fileName));
    }

    public void loadGame(String fileName) {
        Request request = new Request("loadGame", MyGson.toJson(fileName));
    }

    public void saveGameManually() {
        Request request = new Request("saveGameManually", MyGson.toJson());
    }

    public ArrayList<String> listSavedFiles(boolean isAuto) throws IOException {
        Request request = new Request("listSavedFiles", MyGson.toJson(isAuto));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request);
    }


}
