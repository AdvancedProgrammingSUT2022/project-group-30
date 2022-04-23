package controllers;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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
import models.TileVisibility;
import models.User;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.MPCostInterface;
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

    // public Diplomacy findDiplomacy(CivilizationPair pair, Diplomacy
    // diplomacyType){
    // for(Diplomacy diplomacy2:
    // GameDataBase.getGameDataBase().getAllDiplomaticRelations()){
    // if(diplomacy2 instanceof Diplomacy && diplomacy2.getPair().equals(pair))
    // return diplomacy2;
    // }
    // return null;
    // }

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
}
