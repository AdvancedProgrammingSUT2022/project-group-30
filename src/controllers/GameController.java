package controllers;

import models.Feature;
import models.GameDataBase;
import models.MPCost;
import models.MPCostClass;
import models.RiverSegment;
import models.TerrainType;
import models.Tile;
import models.TileVisibility;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.MPCostInterface;
import models.technology.Technology;
import models.units.Unit;
import models.units.UnitType;
import models.works.Work;
import utilities.Debugger;

public class GameController {
    private static GameController gameController;

    private GameController() {
        database = GameDataBase.getGameDataBase();
    }

    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    private GameDataBase database;

    public void startGame() {

    }

    public Tile findWorksLocation(Work work) { // gets a work and finds the tile that owns it
        // TODO
        return null;
    }

    public boolean areCoordinatesValid(int x, int y) {
        return database.getMap().areCoordinatesValid(x, y);
    }

    public Tile getTileByCoordinates(int x, int y) {
        return database.getMap().getTile(x, y);
    }

    public TileVisibility getTileVisibilityForPlayer(Tile tile) { // returns Visible, Fog of War, or Revealed
        return database.getCurrentPlayer().getTileVisibility(tile);
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
        // unit2 okeye??
        for (Unit unit2 : GameDataBase.getGameDataBase().getUnits()) {
            if (areTwoTilesAdjacent(tile, unit2.getLocation())
                    && !unit.getOwner().equals(unit2.getOwner())
                    && unit2.getOwner().equals(unit2.getLocation().getCivilization()))
                return true;
        }
        return false;
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
        if (hasCommonRoadOrRailRoad(sourceTile, destinationTile))// MINETODO add relation effects
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