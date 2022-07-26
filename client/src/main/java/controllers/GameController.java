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
        Request request = new Request("initializeGame", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addCivilizationToPairs(Civilization newCivilization) {
        Request request = new Request("addCivilizationToPairs", MyGson.toJson(newCivilization));
        NetworkController.getNetworkController().transferData(request);
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits(int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("assignCivsToPlayersAndInitializePrimaryUnits", MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        NetworkController.getNetworkController().transferData(request);
    }

    private ArrayList<String> getAppropriateStartingPoints(ArrayList<String> fileLines, int mapHeight, int mapWidth, int startingYPosition, int startingXPosition) {
        Request request = new Request("getAppropriateStartingPoints", MyGson.toJson(fileLines), MyGson.toJson(mapHeight), MyGson.toJson(mapWidth), MyGson.toJson(startingYPosition), MyGson.toJson(startingXPosition));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request, String[].class);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location) {
        Request request = new Request("createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location));
        NetworkController.getNetworkController().transferData(request);
    }

    public void createUnit(UnitType type, Civilization owner, Tile location, int initialXP) {
        Request request = new Request("createUnit", MyGson.toJson(type), MyGson.toJson(owner), MyGson.toJson(location), MyGson.toJson(initialXP));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addPlayers(User[] players) {
        Request request = new Request("addPlayers", MyGson.toJson(players));
        NetworkController.getNetworkController().transferData(request);
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
        NetworkController.getNetworkController().transferData(request);
    }

    public void stopResearch(Civilization civilization) {
        Request request = new Request("stopResearch", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void stopPreviousResearchAndStartNext(Civilization civilization, Technology next) {
        Request request = new Request("stopPreviousResearchAndStartNext", MyGson.toJson(civilization), MyGson.toJson(next));
        NetworkController.getNetworkController().transferData(request);
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
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean canUnitTeleportToTile(UnitType unit, Civilization owner, Tile tile) {
        Request request = new Request("canUnitTeleportToTile", MyGson.toJson(unit), MyGson.toJson(owner), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    private void awakeAllNearAlertedUnits(Unit unit) {
        Request request = new Request("awakeAllNearAlertedUnits", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void moveUnitAlongItsPath(Unit unit) {
        Request request = new Request("moveUnitAlongItsPath", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    private void updateUnitPath(Unit unit, Tile destination) {
        Request request = new Request("updateUnitPath", MyGson.toJson(unit), MyGson.toJson(destination));
        NetworkController.getNetworkController().transferData(request);
    }

    private void expendMPForMovementAlongPath(Unit unit, Tile destination) {
        Request request = new Request("expendMPForMovementAlongPath", MyGson.toJson(unit), MyGson.toJson(destination));
        NetworkController.getNetworkController().transferData(request);
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
        return (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request, Unit[].class);
    }

    public DiplomaticRelation getDiplomaticRelation(Civilization civ1, Civilization civ2) {
        Request request = new Request("getDiplomaticRelation", MyGson.toJson(civ1), MyGson.toJson(civ2));
        return (DiplomaticRelation) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Civilization> getDiscoveredCivilizations(Civilization civilization) {
        Request request = new Request("getDiscoveredCivilizations", MyGson.toJson(civilization));
        return (ArrayList<Civilization>) NetworkController.getNetworkController().transferData(request, Civilization[].class);
    }

    public void addGoldToCiv(Civilization civ, double amount) {
        Request request = new Request("addGoldToCiv", MyGson.toJson(civ), MyGson.toJson(amount));
        NetworkController.getNetworkController().transferData(request);
    }

    public void reduceGoldFromCiv(Civilization civ, int amount) {
        Request request = new Request("reduceGoldFromCiv", MyGson.toJson(civ), MyGson.toJson(amount));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addLuxuryResourceToCiv(Civilization civ, LuxuryResource resource, int amount) {
        Request request = new Request("addLuxuryResourceToCiv", MyGson.toJson(civ), MyGson.toJson(resource), MyGson.toJson(amount));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addStrategicResourceToCiv(Civilization civ, StrategicResource resource, int amount) {
        Request request = new Request("addStrategicResourceToCiv", MyGson.toJson(civ), MyGson.toJson(resource), MyGson.toJson(amount));
        NetworkController.getNetworkController().transferData(request);
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
        NetworkController.getNetworkController().transferData(request);
    }

    public void checkVictoryByDominion() {
        Request request = new Request("checkVictoryByDominion");
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesCivilizationHaveItsOriginalCapital(Civilization civilization) {
        Request request = new Request("doesCivilizationHaveItsOriginalCapital", MyGson.toJson(civilization));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void defeatCivilization(Civilization civilization) {
        Request request = new Request("defeatCivilization", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void makeCivilizationWin(Civilization civilization) {
        Request request = new Request("makeCivilizationWin", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void endGameByTime() {
        Request request = new Request("endGameByTime");
        NetworkController.getNetworkController().transferData(request);
    }

    public void disableTurnBreak() {
        Request request = new Request("disableTurnBreak");
        NetworkController.getNetworkController().transferData(request);
    }

    public void deleteUnit(Unit unit) {
        Request request = new Request("deleteUnit", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void removeUnit(Unit unit) {
        Request request = new Request("removeUnit", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
    }

    public void destroyCity(City city) {
        Request request = new Request("destroyCity", MyGson.toJson(city));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Unit> getCurrentPlayersUnitsWaitingForCommand() {
        Request request = new Request("getCurrentPlayersUnitsWaitingForCommand");
        return (ArrayList<Unit>) NetworkController.getNetworkController().transferData(request, Unit[].class);
    }

    public void goToNextTurn() {
        Request request = new Request("goToNextTurn");
        NetworkController.getNetworkController().transferData(request);
    }

    public void goToNextPlayer() {
        Request request = new Request("goToNextPlayer");
        NetworkController.getNetworkController().transferData(request);
    }

    public Civilization getNextPlayer() {
        Request request = new Request("getNextPlayer");
        return (Civilization) NetworkController.getNetworkController().transferData(request);
    }

    public void setProgramDatabase() {
        Request request = new Request("setProgramDatabase");
        NetworkController.getNetworkController().transferData(request);
    }

    public ProgramDatabase getProgramDatabase() {
        Request request = new Request("getProgramDatabase");
        return (ProgramDatabase) NetworkController.getNetworkController().transferData(request);
    }

    public void setGameDataBase() {
        Request request = new Request("setGameDataBase");
        NetworkController.getNetworkController().transferData(request);
    }

    public GameDataBase getGameDataBase() {
        Request request = new Request("getGameDataBase");
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
        NetworkController.getNetworkController().transferData(request);
    }

    public void pillageUnitsTile(Unit unit) {
        Request request = new Request("pillageUnitsTile", MyGson.toJson(unit));
        NetworkController.getNetworkController().transferData(request);
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
        NetworkController.getNetworkController().transferData(request);
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
        Request request = new Request("getCurrentPlayer");
        return (Civilization) NetworkController.getNetworkController().transferData(request);
    }

    public void declareWar(Civilization attacker, Civilization defender) {
        Request request = new Request("declareWar", MyGson.toJson(attacker), MyGson.toJson(defender));
        NetworkController.getNetworkController().transferData(request);
    }

    public void declarePeace(Civilization attacker, Civilization defender) {
        Request request = new Request("declarePeace", MyGson.toJson(attacker), MyGson.toJson(defender));
        NetworkController.getNetworkController().transferData(request);
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
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public void makeCivLearnAllTechnologiesWithCheat(Civilization civ) {
        Request request = new Request("makeCivLearnAllTechnologiesWithCheat", MyGson.toJson(civ));
        NetworkController.getNetworkController().transferData(request);
    }

    public void makeCivLearnTechnologyWithCheat(Civilization civ, Technology technology) {
        Request request = new Request("makeCivLearnTechnologyWithCheat", MyGson.toJson(civ), MyGson.toJson(technology));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileBlocker(Tile tile) {
        Request request = new Request("isTileBlocker", MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getVisibleTilesFromTile(Tile tile, int distance) {
        Request request = new Request("getVisibleTilesFromTile", MyGson.toJson(tile), MyGson.toJson(distance));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public ArrayList<Tile> deleteRepetitiveElementsFromArrayList(ArrayList<Tile> tiles) {
        Request request = new Request("deleteRepetitiveElementsFromArrayList", MyGson.toJson(tiles));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public ArrayList<Tile> getVisibleTilesByCities(Civilization civilization) {
        Request request = new Request("getVisibleTilesByCities", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public ArrayList<Tile> getVisibleTilesByUnits(Civilization civilization) {
        Request request = new Request("getVisibleTilesByUnits", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public ArrayList<Tile> getVisibleTilesByUnit(Unit unit) {
        Request request = new Request("getVisibleTilesByUnit", MyGson.toJson(unit));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public ArrayList<Tile> getVisibleTilesByCivilization(Civilization civilization) {
        Request request = new Request("getVisibleTilesByCivilization", MyGson.toJson(civilization));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public void setMapImageOfCivilization(Civilization civilization) {
        Request request = new Request("setMapImageOfCivilization", MyGson.toJson(civilization));
        NetworkController.getNetworkController().transferData(request);
    }

    public void makeEverythingVisible() {
        Request request = new Request("makeEverythingVisible");
        NetworkController.getNetworkController().transferData(request);
    }

    public void discoverCivsInTile(Tile tile) {
        Request request = new Request("discoverCivsInTile", MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public int getMapWidth() {
        Request request = new Request("getMapWidth");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int getMapHeight() {
        Request request = new Request("getMapHeight");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> findPath(Unit unit, Tile sourceTile, Tile destinationTile) {
        Request request = new Request("findPath", MyGson.toJson(unit), MyGson.toJson(sourceTile), MyGson.toJson(destinationTile));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
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
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request, ScientificTreaty[].class);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContracts(CivilizationPair pair) {
        Request request = new Request("getStepWiseGoldTransferContracts", MyGson.toJson(pair));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request, StepWiseGoldTransferContract[].class);
    }

    public WarInfo getWarInfos(CivilizationPair pair) {
        Request request = new Request("getWarInfos", MyGson.toJson(pair));
        return (WarInfo) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<DiplomaticRelation> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
        Request request = new Request("getDiplomaticRelationsMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<DiplomaticRelation>) NetworkController.getNetworkController().transferData(request, DiplomaticRelation[].class);
    }

    public ArrayList<ScientificTreaty> getScientificTreatiesOfCivilization(Civilization civilization) {
        Request request = new Request("getScientificTreatiesOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<ScientificTreaty>) NetworkController.getNetworkController().transferData(request, ScientificTreaty[].class);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationPayer(Civilization civilization) {
        Request request = new Request("getStepWiseGoldTransferContractsOfCivilizationPayer", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request, StepWiseGoldTransferContract[].class);
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationRecipient(Civilization civilization) {
        Request request = new Request("getStepWiseGoldTransferContractsOfCivilizationRecipient", MyGson.toJson(civilization));
        return (ArrayList<StepWiseGoldTransferContract>) NetworkController.getNetworkController().transferData(request, StepWiseGoldTransferContract[].class);
    }

    public ArrayList<WarInfo> getWarInfoMapOfCivilization(Civilization civilization) {
        Request request = new Request("getWarInfoMapOfCivilization", MyGson.toJson(civilization));
        return (ArrayList<WarInfo>) NetworkController.getNetworkController().transferData(request, WarInfo[].class);
    }

    public ArrayList<Notification> getCivilizationNewNotification() {
        Request request = new Request("getCivilizationNewNotification");
        return (ArrayList<Notification>) NetworkController.getNetworkController().transferData(request, Notification[].class);
    }

    public void seenAllNotifications() {
        Request request = new Request("seenAllNotifications");
        NetworkController.getNetworkController().transferData(request);
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
        Request request = new Request("sortPlayersInOrderOfScore");
        NetworkController.getNetworkController().transferData(request);
    }

    public int calculateHighestScore() {
        Request request = new Request("calculateHighestScore");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateLowestScore() {
        Request request = new Request("calculateLowestScore");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public double calculateAverageScore() {
        Request request = new Request("calculateAverageScore");
        return (double) NetworkController.getNetworkController().transferData(request);
    }

    public HashMap<String, ArrayList<String>> loadMeta() throws IOException {
        Request request = new Request("loadMeta");
        return (HashMap<String, ArrayList<String>>) NetworkController.getNetworkController().transferData(request);
    }

    public void saveMeta(HashMap<String, ArrayList<String>> input) throws IOException {
        Request request = new Request("saveMeta", MyGson.toJson(input));
        NetworkController.getNetworkController().transferData(request);
    }

    public void saveGame(String fileName) {
        Request request = new Request("saveGame", MyGson.toJson(fileName));
        NetworkController.getNetworkController().transferData(request);
    }

    public void loadGame(String fileName) {
        Request request = new Request("loadGame", MyGson.toJson(fileName));
        NetworkController.getNetworkController().transferData(request);
    }

    public void saveGameManually() {
        Request request = new Request("saveGameManually");
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<String> listSavedFiles(boolean isAuto) throws IOException {
        Request request = new Request("listSavedFiles", MyGson.toJson(isAuto));
        return (ArrayList<String>) NetworkController.getNetworkController().transferData(request, String[].class);
    }

    public TileImage[][] getCivilizationImageToShowOnScene(Civilization civ) {
        Request request = new Request("getCivilizationImageToShowOnScene", MyGson.toJson(civ));
        return (TileImage[][]) NetworkController.getNetworkController().transferData(request);
    }

    public int findTileXCoordinateInMap(Tile tile) {
        Request request = new Request("findTileXCoordinateInMap", MyGson.toJson(tile));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int findTileYCoordinateInMap(Tile tile) {
        Request request = new Request("findTileYCoordinateInMap", MyGson.toJson(tile));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public void setPlayersSelectedEntity(Selectable selectable) {
        Request request = new Request("setPlayersSelectedEntity", MyGson.toJson(selectable));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isCityOwnerEqualToCurrentPlayer(City city) {
        Request request = new Request("isCityOwnerEqualToCurrentPlayer", MyGson.toJson(city));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isUnitOwnerEqualToCurrentPlayer(Unit unit) {
        Request request = new Request("isUnitOwnerEqualToCurrentPlayer", MyGson.toJson(unit));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<RiverSegment> getMapRivers() {
        Request request = new Request("getMapRivers");
        return (ArrayList<RiverSegment>) NetworkController.getNetworkController().transferData(request, RiverSegment[].class);
    }

    public String findRiverSegmentDirectionForTile(RiverSegment river, Tile tile) {
        Request request = new Request("findRiverSegmentDirectionForTile", MyGson.toJson(river), MyGson.toJson(tile));
        return (String) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isTileImageEqualToTile(Tile tile, TileImage tileImage) {
        Request request = new Request("isTileImageEqualToTile", MyGson.toJson(tile), MyGson.toJson(tileImage));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Tile getTileFromMap(int x, int y) {
        Request request = new Request("getTileFromMap", MyGson.toJson(x), MyGson.toJson(y));
        return (Tile) NetworkController.getNetworkController().transferData(request);
    }

    public void setCurrentPlayerFrameBase(Tile tile) {
        Request request = new Request("setCurrentPlayerFrameBase", MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public Output calculateTheoreticalOutputForTile(Tile tile) {
        Request request = new Request("calculateTheoreticalOutputForTile", MyGson.toJson(tile));
        return (Output) NetworkController.getNetworkController().transferData(request);
    }

    public boolean canResourceBeExploitedForTile(Resource resource, Tile tile) {
        Request request = new Request("canResourceBeExploitedForTile", MyGson.toJson(resource), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<City> getCivilizationCities(Civilization civ) {
        Request request = new Request("getCivilizationCities", MyGson.toJson(civ));
        return (ArrayList<City>) NetworkController.getNetworkController().transferData(request, City[].class);
    }

    public ArrayList<City> getCivilizationCitiesWaitingForProduction(Civilization civ) {
        Request request = new Request("getCivilizationCitiesWaitingForProduction", MyGson.toJson(civ));
        return (ArrayList<City>) NetworkController.getNetworkController().transferData(request, City[].class);
    }

    public void deselectCity(City city) {
        Request request = new Request("deselectCity", MyGson.toJson(city));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> findCityPurchasableTiles(City city) {
        Request request = new Request("findCityPurchasableTiles", MyGson.toJson(city));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public int calculateNextTilePriceForCity(City city) {
        Request request = new Request("calculateNextTilePriceForCity", MyGson.toJson(city));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public double getCityOwnerGoldCount(City city) {
        Request request = new Request("getCityOwnerGoldCount", MyGson.toJson(city));
        return (double) NetworkController.getNetworkController().transferData(request);
    }

    public void decreaseCivilizationGold(Civilization civ, int cost) {
        Request request = new Request("decreaseCivilizationGold", MyGson.toJson(civ), MyGson.toJson(cost));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isCityCapital(City city) {
        Request request = new Request("isCityCapital", MyGson.toJson(city));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isCityTileBeingWorked(City city, Tile tile) {
        Request request = new Request("isCityTileBeingWorked", MyGson.toJson(city), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Output calculateOutputForCity(City city) {
        Request request = new Request("calculateOutputForCity", MyGson.toJson(city));
        return (Output) NetworkController.getNetworkController().transferData(request);
    }

    public double calculateCityBeakerProduction(City city) {
        Request request = new Request("calculateCityBeakerProduction", MyGson.toJson(city));
        return (double) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateDistanceFromTile(Tile tile, Tile dest) {
        Request request = new Request("calculateDistanceFromTile", MyGson.toJson(tile), MyGson.toJson(dest));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public void executeRangedAttackCity(City city, combative comb) {
        Request request = new Request("executeRangedAttackCity", MyGson.toJson(city), MyGson.toJson(comb));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesCityHasTileInTerritory(City city, Tile tile) {
        Request request = new Request("doesCityHasTileInTerritory", MyGson.toJson(city), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateWorklessCitizenCountForCity(City city) {
        Request request = new Request("calculateWorklessCitizenCountForCity", MyGson.toJson(city));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public Citizen getCityWorklessCitizen(City city) {
        Request request = new Request("getCityWorklessCitizen", MyGson.toJson(city));
        return (Citizen) NetworkController.getNetworkController().transferData(request);
    }

    public void assignCitizenToWorkPlaceForCity(City city, Tile tile, Citizen citizen) {
        Request request = new Request("assignCitizenToWorkPlaceForCity", MyGson.toJson(city), MyGson.toJson(tile), MyGson.toJson(citizen));
        NetworkController.getNetworkController().transferData(request);
    }

    public void freeCityTile(City city, Tile tile) {
        Request request = new Request("freeCityTile", MyGson.toJson(city), MyGson.toJson(tile));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Tile> getCityWorkingTiles(City city) {
        Request request = new Request("getCityWorkingTiles", MyGson.toJson(city));
        return (ArrayList<Tile>) NetworkController.getNetworkController().transferData(request, Tile[].class);
    }

    public boolean doesCityWorkingTilesContainsTile(City city, Tile tile) {
        Request request = new Request("doesCityWorkingTilesContainsTile", MyGson.toJson(city), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean doesCityNonWorkingTilesContainsTile(City city, Tile tile) {
        Request request = new Request("doesCityNonWorkingTilesContainsTile", MyGson.toJson(city), MyGson.toJson(tile));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public Producible getCityEntityInProduction(City city) {
        Request request = new Request("getCityEntityInProduction", MyGson.toJson(city));
        return (Producible) NetworkController.getNetworkController().transferData(request);
    }

    public int calculateProductionHammerCost(Producible producible) {
        Request request = new Request("calculateProductionHammerCost", MyGson.toJson(producible));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<UnitType> calculateCityProductionReadyUnitTypes(City city) {
        Request request = new Request("calculateCityProductionReadyUnitTypes", MyGson.toJson(city));
        return (ArrayList<UnitType>) NetworkController.getNetworkController().transferData(request, UnitType[].class);
    }

    public ArrayList<BuildingType> calculateCityProductionReadyBuildingTypes(City city) {
        Request request = new Request("calculateCityProductionReadyBuildingTypes", MyGson.toJson(city));
        return (ArrayList<BuildingType>) NetworkController.getNetworkController().transferData(request, BuildingType[].class);
    }

    public ArrayList<BuildingType> calculateCityProductionReadyBuildingTypesFalseValue(City city) {
        Request request = new Request("calculateCityProductionReadyBuildingTypesFalseValue", MyGson.toJson(city));
        return (ArrayList<BuildingType>) NetworkController.getNetworkController().transferData(request, BuildingType[].class);
    }

    public boolean doesPackingLetUnitEnterCity(City city, UnitType type) {
        Request request = new Request("doesPackingLetUnitEnterCity", MyGson.toJson(city), MyGson.toJson(type));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void changeCityProduction(City city, Producible producible) {
        Request request = new Request("changeCityProduction", MyGson.toJson(city), MyGson.toJson(producible));
        NetworkController.getNetworkController().transferData(request);
    }

    public void stopCityProduction(City city) {
        Request request = new Request("stopCityProduction", MyGson.toJson(city));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<UnitType> calculateCityPurchasableUnitTypes(City city) {
        Request request = new Request("calculateCityPurchasableUnitTypes", MyGson.toJson(city));
        return (ArrayList<UnitType>) NetworkController.getNetworkController().transferData(request, UnitType[].class);
    }

    public ArrayList<BuildingType> calculateCityPurchasableBuildingTypes(City city) {
        Request request = new Request("calculateCityPurchasableBuildingTypes", MyGson.toJson(city));
        return (ArrayList<BuildingType>) NetworkController.getNetworkController().transferData(request, BuildingType[].class);
    }

    public void addBuildingToCity(City city, BuildingType type) {
        Request request = new Request("addBuildingToCity", MyGson.toJson(city), MyGson.toJson(type));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean isStateAllowedForUnit(Unit unit, UnitState state){
        return unit.getType().getCombatType().isStateAllowed(state);
    }

    public boolean isCityOwnerEqualToUnitOwner(City city, Unit unit){
        return city.getOwner().equals(unit.getOwner());
    }

    public void deselectUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
    }

    public void alertUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.ALERT);
    }

    public void sleepUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.ASLEEP);
    }

    public void fortifyUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFY);
    }

    public void garrisonUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.GARRISON);
    }

    public void fortifyUnitUntilHealed(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFYUNTILHEALED);
    }

    public void awakeUnit(Unit unit){
        unit.setState(UnitState.AWAKE);
    }

    public void deleteAUnit(Unit unit){
        unit.getOwner().setSelectedEntity(null);
        deleteUnit(unit);
    }

    public void cancelUnitMove(Unit unit){
        unit.setPath(null);
    }

    public Selectable getCivilizationSelectedEntity(Civilization civ){
        return civ.getSelectedEntity();
    }

    public void setUnitPath(Unit unit, Tile location, Tile destination){
        unit.setPath(findPath(unit, unit.getLocation(), destination));
    }

    public int getUnitMovePointsLeft(Unit unit){
        return unit.getMovePointsLeft();
    }

    public boolean areUnitAndTargetOwnerAtWar(combative target, Unit unit){
        return GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).areAtWar();
    }

    public void setUnitAndTargetOwnerAtWar(combative target, Unit unit, boolean atWar){
        GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).setAreAtWar(atWar);
    }

    public void executeMeleeAttackUnit(Unit unit, combative target){
        CombatController.getCombatController().executeMeleeAttack(unit, target);
    }

    public boolean isCityOriginalCapital(City city){
        return city.isOriginalCapital();
    }

    public boolean isCityFounderEqualToUnitOwner(City city, Unit unit){
        return city.getFounder().equals(unit.getOwner());
    }


}
