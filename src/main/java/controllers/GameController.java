package controllers;

import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

import models.*;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.diplomacy.*;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.MPCostInterface;
import models.interfaces.TileImage;
import models.interfaces.TurnHandler;
import models.technology.Technology;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import models.works.Work;
import utilities.Debugger;

public class GameController {
    private static GameController gameController;

    private GameDataBase gameDataBase = GameDataBase.getGameDataBase();

    private ProgramDatabase programDatabase;

    private GameController() {
        gameDataBase = GameDataBase.getGameDataBase();
    }

    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public void initializeGame() {
        GameMap.getGameMap().loadMapFromFile();
        assignCivsToPlayersAndInitializePrimaryUnits();
        gameDataBase.setCurrentPlayer(gameDataBase.getPlayers().get(0).getCivilization());
    }

    public void addCivilization(Civilization newCivilization) {
        for (Civilization civilization : getGameDataBase().getCivilizations()) {
            gameDataBase.getCivilizationPairs().add(new CivilizationPair(civilization, newCivilization));
            gameDataBase.getAllDiplomaticRelations().add(new DiplomaticRelationsMap(civilization, newCivilization));
        }
        GameDataBase.getGameDataBase().getCivilizations().add(newCivilization);
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits() {
        File main = new File("src", "main");
        File java = new File(main, "java");
        File resources = new File(java, "resources");
        File civilizationsFile = new File(resources, "civilizations.txt");
        try {
            Scanner scanner = new Scanner(civilizationsFile);
            ArrayList<String> fileLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
            Random rand = new Random();
            for (int i = 0; i < this.gameDataBase.getPlayers().size(); i++) {
                int fileLineIndex = rand.nextInt(fileLines.size());
                String tokens[] = fileLines.get(fileLineIndex).split("-");
                Civilization civilization = new Civilization(tokens[2]);
                this.gameDataBase.getPlayers().get(i).setCivilization(civilization);
                Tile settlerTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]));
                Tile warriorTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[0]));
                createUnit(UnitType.SETTLER, civilization, settlerTile);
                createUnit(UnitType.WARRIOR, civilization, warriorTile);
                civilization.setFrameBase(GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 3, Integer.parseInt(tokens[0]) - 1));
                fileLines.remove(fileLineIndex);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createUnit(UnitType type, Civilization owner, Tile location) {
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

    public TileVisibility getTileVisibilityForPlayer(Tile tile) { // returns Visible, Fog of War, or Revealed
        return gameDataBase.getCurrentPlayer().getTileVisibility(tile);
    }

    public void moveUnit(Unit unit, Tile destination) { // doesn't check packing and mp conditions, doesn't cost mp. updates fog of war
        unit.setLocation(destination);
        setMapImageOfCivilization(unit.getOwner());
        awakeAllNearAlertedUnits(unit);
    }

    private void awakeAllNearAlertedUnits(Unit unit){
        Tile tile = unit.getLocation();
        ArrayList<Tile> adjacetTiles = this.getAdjacentTiles(tile);
        for(int i = 0; i < adjacetTiles.size(); i++){
            ArrayList<Unit> units = this.getUnitsInTile(adjacetTiles.get(i));
            for(int j = 0; j < units.size(); j++){
                if(!units.get(j).getOwner().equals(unit.getOwner()) && units.get(i).getState() == UnitState.ALERT) {
                    units.get(i).setState(UnitState.AWAKE);
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
                if (unit.isCivilian() == false && i == path.size() - 1) {
                    // TODO : attack that tile
                }
                continue;
            } else if (generalUnit != null && generalUnit.getOwner() == unit.getOwner()) {
                if ((generalUnit.isCivilian() && unit.isCivilian()) || (!generalUnit.isCivilian() && !unit.isCivilian())) {
                    continue;
                }
            }


            if (city != null && city.getOwner() != unit.getOwner()) {   // if there is a hostile city on the path
                if (unit.isCivilian() != true && i == path.size() - 1) {    // attack it if it was the destination, cancel if not
                    // TODO : attack that tile
                }
                continue;
            }
            destination = path.get(i);
            break;
        }

        if (destination != null) {
            moveUnit(unit, destination);
            expendMPForMovementAlongPath(unit, destination);
            updateUnitPath(unit, destination);
        } else {
            unit.setPath(null);
        }
    }

    private void updateUnitPath(Unit unit, Tile destination) {
        ArrayList<Tile> newPath = new ArrayList<>(unit.getPath());
        int index = newPath.indexOf(destination);
        for(int i = 0; i < index; i++){
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
        // TODO : Create palace in city
        newCity.getBuildings().add(new Building(BuildingType.PALACE));
        gameDataBase.getCities().add(newCity);
        if (unit.getOwner().getOriginCapital() == null) {
            unit.getOwner().setCapital(newCity);
        }

        deleteUnit(unit);
    }

    private void deleteUnit(Unit unit) {
        if (unit.getOwner().getSelectedEntity() == unit) {
            unit.getOwner().setSelectedEntity(null);
        }
        gameDataBase.getUnits().remove(unit);
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
        for (Diplomacy diplomaticRelation : gameDataBase.getAllDiplomaticRelations()) {
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

    public City whoseTerritoryIsTileIn(Tile tile) {     // If the tile is located in the citie's territory, returns the city(city center counts too)
        for (City city : gameDataBase.getCities()) {
            if (city.getTerritories().contains(tile) && city.getCentralTile() != tile) {
                return city;
            }
        }
        return null;
    }

    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        int x = tile1.findTileXCoordinateInMap();
        int y = tile1.findTileYCoordinateInMap();
        int x2 = tile2.findTileXCoordinateInMap();
        int y2 = tile2.findTileYCoordinateInMap();
        if (Math.abs(x - x2) > 1 || Math.abs(y - y2) > 1) {
            return false;
        }
        if (x % 2 == 0) {
            if (y2 - y == 1 && x != x2) {
                return false;
            }
            return true;
        } else {
            if (y - y2 == 1 && x != x2) {
                return false;
            }
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
        if (hasCommonRiver(sourceTile, destinationTile) && !(hasCommonRoadOrRailRoad && unit.getOwner().getTechnologies().contains(Technology.CONSTRUCTION)))
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
            if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) == null)
                newMapImage.put(tile, null);
            else if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) instanceof TileHistory)
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
            player.getMapImage().put(tile, tile);
        }
        player.setEverythingVisibleCheatCodeInEffect(true);
    }

    public int getMapWidth() {
        return GameMap.getGameMap().getMap()[0].length;
    }

    public int getMapHeight() {
        return GameMap.getGameMap().getMap().length;
    }

    public ArrayList<Tile> findPath(Unit unit, Tile sourceTile, Tile destinationTile){
        ArrayList<Tile> pathTiles = new ArrayList<>();
        TileGraph graph = this.makeTilesGraph(unit, sourceTile, destinationTile);
        GraphNode destinationNode = graph.getNodeByTile(destinationTile);
        for (GraphNode graphNode : destinationNode.getShortestPath()) {
            pathTiles.add(graphNode.getTile());
        }
        pathTiles.add(destinationTile);
        return pathTiles;
    }

    private TileGraph calculateShortestPathFromSourceTile(TileGraph graph, GraphNode source){
        source.setDistance(0);
        HashSet<GraphNode> settledNodes = new HashSet<>();
        HashSet<GraphNode> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);
        while(!unsettledNodes.isEmpty()){
            GraphNode currentNode = this.getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for(Map.Entry<GraphNode, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()){
                if(!settledNodes.contains(adjacencyPair.getKey()) && currentNode.getDistance() + adjacencyPair.getValue() < adjacencyPair.getKey().getDistance()){
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

    private GraphNode getLowestDistanceNode(HashSet<GraphNode> unsettledNodes){
        GraphNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (GraphNode unsettledNode : unsettledNodes) {
            if(unsettledNode.getDistance() < lowestDistance){
                lowestDistance = unsettledNode.getDistance();
                lowestDistanceNode = unsettledNode;
            }
        }
        return lowestDistanceNode;
    }

    private TileGraph makeTilesGraph(Unit unit, Tile origin, Tile destination){
        TileGraph graph = new TileGraph();
        GraphNode sourceNode = new GraphNode(origin);
        graph.addNode(sourceNode);
        while(!graph.isTileAddedToGraph(destination)){
            ArrayList<GraphNode> nodes = new ArrayList<>(graph.getNodes());
            for (int i = 0 ; i < nodes.size(); i++) {
                GraphNode node = nodes.get(i);
                ArrayList<Tile> adjacentTiles = this.getAdjacentTiles(node.getTile());
                for (Tile adjacentTile : adjacentTiles) {
                    if(!graph.isTileAddedToGraph(adjacentTile)) {
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
        for(int i = 0; i < 2; i++){
            ArrayList<GraphNode> nodes = new ArrayList<>(graph.getNodes());
            for (int j = 0 ; j < nodes.size(); j++) {
                GraphNode node = nodes.get(j);
                ArrayList<Tile> adjacentTiles = this.getAdjacentTiles(node.getTile());
                for (Tile adjacentTile : adjacentTiles) {
                    if(!graph.isTileAddedToGraph(adjacentTile)) {
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
    public DiplomaticRelationsMap getDiplomaticRelationsMap(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof DiplomaticRelationsMap && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                return (DiplomaticRelationsMap) diplomacy;
        }
        //it will never return null
        return null;
    }

    public ArrayList<ScientificTreaty> getScientificTreaties(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        ArrayList<ScientificTreaty> scientificTreaties = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
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
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public WarInfo getWarInfos(CivilizationPair pair) {
        ArrayList<Civilization> civilizations = pair.getCivilizationsArray();
        Civilization civ1 = civilizations.get(0);
        Civilization civ2 = civilizations.get(1);
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof WarInfo && diplomacy.getPair().containsCivilization(civ1) && diplomacy.getPair().containsCivilization(civ2))
                return (WarInfo) diplomacy;
        }
        return null;
    }

    public ArrayList<DiplomaticRelationsMap> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
        ArrayList<DiplomaticRelationsMap> diplomaticRelationsMaps = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof DiplomaticRelationsMap && diplomacy.getPair().containsCivilization(civilization))
                diplomaticRelationsMaps.add((DiplomaticRelationsMap) diplomacy);
        }
        return diplomaticRelationsMaps;
    }

    public ArrayList<ScientificTreaty> getScientificTreatiesOfCivilization(Civilization civilization) {
        ArrayList<ScientificTreaty> scientificTreaties = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof ScientificTreaty && diplomacy.getPair().containsCivilization(civilization))
                scientificTreaties.add((ScientificTreaty) diplomacy);
        }
        return scientificTreaties;
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationPayer(Civilization civilization) {
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && ((StepWiseGoldTransferContract) diplomacy).getPayer() == civilization)
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public ArrayList<StepWiseGoldTransferContract> getStepWiseGoldTransferContractsOfCivilizationRecipient(Civilization civilization) {
        // OVERWRITE THE FOLLOWING CHANGES WITH WHATEVER MAEDEH WRITES:
        ArrayList<StepWiseGoldTransferContract> stepWiseGoldTransferContracts = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof StepWiseGoldTransferContract && ((StepWiseGoldTransferContract) diplomacy).getRecipient() == civilization)
                stepWiseGoldTransferContracts.add((StepWiseGoldTransferContract) diplomacy);
        }
        return stepWiseGoldTransferContracts;
    }

    public ArrayList<WarInfo> getWarInfoMapOfCivilization(Civilization civilization) {
        ArrayList<WarInfo> warInfos = new ArrayList<>();
        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getAllDiplomaticRelations()) {
            if (diplomacy instanceof WarInfo && diplomacy.getPair().containsCivilization(civilization))
                warInfos.add((WarInfo) diplomacy);
        }
        return warInfos;
    }
}
