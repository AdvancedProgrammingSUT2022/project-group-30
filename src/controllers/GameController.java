package controllers;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import models.City;
import models.Civilization;
import models.Feature;
import models.GameDataBase;
import models.GameMap;
import models.ProgramDatabase;
import models.MPCost;
import models.MPCostClass;
import models.RiverSegment;
import models.TerrainType;
import models.Tile;
import models.TileHistory;
import models.TileVisibility;
import models.User;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.MPCostInterface;
import models.interfaces.TileImage;
import models.technology.Technology;
import models.units.CombatType;
import models.units.Unit;
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
        GameMap.getGameMap();
        assignCivsToPlayersAndInitializePrimaryUnits();
        gameDataBase.setCurrentPlayer(gameDataBase.getPlayers().get(0).getCivilization());
    }

    private void assignCivsToPlayersAndInitializePrimaryUnits() {
        File civilizationsFile = new File("resources", "civilizations.txt");
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
                Tile settlerTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]),
                        Integer.parseInt(tokens[0]));
                Tile warriorTile = GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 1,
                        Integer.parseInt(tokens[0]));
                createUnit(UnitType.SETTLER, civilization, settlerTile);
                createUnit(UnitType.WARRIOR, civilization, warriorTile);
                civilization.setFrameBase(
                        GameMap.getGameMap().getTile(Integer.parseInt(tokens[1]) - 3, Integer.parseInt(tokens[0]) - 1));
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
        // TODO TOROKKKHOOODAAA : update visibility for owner tile
    }

    public Tile findWorksLocation(Work work) { // gets a work and finds the tile that owns it
        // TODO
        return null;
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

    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit unit : gameDataBase.getUnits()) {
            if (unit.getLocation() == tile) {
                units.add(unit);
            }
        }
        return units;
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

    public City getCityInTile(Tile tile) {
        for (City city : gameDataBase.getCities()) {
            if (city.getCentralTile() == tile) {
                return city;
            }
        }
        return null;
    }

    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        // TODO
        return true;
    }

    public boolean hasCommonRiver(Tile tile1, Tile tile2) {
        for (RiverSegment river : GameDataBase.getGameDataBase().getMap().getRivers()) {
            if ((river.getFirstTile().equals(tile1) && river.getSecondTile().equals(tile2))
                    || (river.getFirstTile().equals(tile2) && river.getSecondTile().equals(tile1)))
                return true;
        }
        return false;
    }

    public boolean hasCommonRoadOrRailRoad(Tile tile1, Tile tile2) {
        boolean hasRoad = false;
        boolean hasRailRoad = false;
        for (Improvement improvement : tile1.getImprovements()) {
            if (improvement.getType().equals(ImprovementType.ROAD))
                hasRoad = true;
            if (improvement.getType().equals(ImprovementType.RAILROAD))
                hasRailRoad = true;
        }

        for (Improvement improvement : tile2.getImprovements()) {
            if (hasRoad && improvement.getType().equals(ImprovementType.ROAD))
                return true;
            if (hasRailRoad && improvement.getType().equals(ImprovementType.RAILROAD))
                return true;
        }
        return false;
    }

    public boolean isInZOC(Unit unit, Tile tile) {
        for (Unit unit2 : GameDataBase.getGameDataBase().getUnits()) {
            if (areTwoTilesAdjacent(tile, unit2.getLocation())
                    && !unit.getOwner().equals(unit2.getOwner()) &&
                    unit2.getType().getCombatType() != CombatType.CIVILIAN)
                return true;
        }
        return false;
    }

    public Civilization getCurrentPlayer() {
        return gameDataBase.getCurrentPlayer();
    }

    public MPCostInterface calculateRequiredMps(Unit unit, Tile destinationTile) {
        int MPs = 0;
        Tile sourceTile = unit.getLocation();
        boolean hasCommonRoadOrRailRoad = false;
        if (!areTwoTilesAdjacent(destinationTile, sourceTile)) {
            Debugger.debug("Two tile are not adjacent!");
            return null;
        }
        if (destinationTile.getTerrainType().equals(TerrainType.OCEAN)
                || destinationTile.getTerrainType().equals(TerrainType.MOUNTAIN)
                || destinationTile.getFeatures().contains(Feature.ICE)) {
            return MPCost.IMPASSABLE;
        }
        if (hasCommonRoadOrRailRoad(sourceTile, destinationTile)) // MINETODO add relation effects
            hasCommonRoadOrRailRoad = true;
        if (hasCommonRiver(sourceTile, destinationTile) && !(hasCommonRoadOrRailRoad
                && unit.getOwner().getTechnologies().contains(Technology.CONSTRUCTION)))
            return MPCost.EXPENSIVE;
        if (isInZOC(unit, sourceTile) && isInZOC(unit, destinationTile))
            return MPCost.EXPENSIVE;
        if (!unit.getType().equals(UnitType.SCOUT))
            MPs += destinationTile.getTerrainType().getMovementCost();
        for (Feature feature : destinationTile.getFeatures()) {
            MPs += feature.getMovementCost();
        }
        if (hasCommonRoadOrRailRoad)
            MPs = (int) Math.max(MPs * 0.5, 1);
        return new MPCostClass(MPs);
    }

    public ArrayList<Tile> getAdjacentTiles(Tile tile) {
        // TODO
        return null;
    }

    public boolean isTileBlocker(Tile tile) {
        if (tile.getTerrainType().equals(TerrainType.HILLS)
                || tile.getTerrainType().equals(TerrainType.MOUNTAIN)
                || tile.getFeatures().contains(Feature.FOREST))
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
            if (isTileBlocker(tile2) && !tile.getTerrainType().equals(TerrainType.HILLS))
                waitingTiles.add(tile2);
            else
                finalTiles.add(tile2);
        }

        if (distance == 1) {
            finalTiles.addAll(waitingTiles);
            return finalTiles;
        }

        for (Tile visibleTile : finalTiles) {
            finalTiles.addAll(getAdjacentTiles(visibleTile));
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

    public ArrayList<Tile> getVisiblTilesByCities(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (!city.getOwner().equals(civilization))
                continue;
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
                tiles.addAll(getVisibleTilesFromTile(unit.getLocation(),
                        unit.getType().getCombatType().equals(CombatType.SIEGE) ? 1 : 2));
        }
        return deleteRepetitiveElementsFromArrayList(tiles);
    }

    public ArrayList<Tile> getVisibleTilesByCivilization(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.addAll(getVisiblTilesByCities(civilization));
        tiles.addAll(getVisibleTilesByUnits(civilization));
        return deleteRepetitiveElementsFromArrayList(tiles);
    }

    public void setMapImageOfCivilization(Civilization civilization) {
        HashMap<Tile, TileImage> newMapImage = new HashMap<>();
        ArrayList<Tile> visibleTiles = getVisibleTilesByCivilization(civilization);
        for (Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()) {
            if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) == null)
                newMapImage.put(tile, null);
            else if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) instanceof TileHistory)
                newMapImage.put(tile, civilization.getMapImage().get(tile));
            else if (!visibleTiles.contains(tile) && civilization.getMapImage().get(tile) instanceof Tile)
                newMapImage.put(tile, tile.createTileHistory());
            else
                newMapImage.put(tile, tile);
        }
        civilization.getMapImage().clear();
        civilization.getMapImage().putAll(newMapImage);
    }
}
