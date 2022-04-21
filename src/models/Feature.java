package models;

import java.util.ArrayList;
import java.util.Arrays;

import models.interfaces.TerrainProperty;
import utilities.Debugger;

public enum Feature implements TerrainProperty {
    FLOOD_PLAINS(new Output(0, 2, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.DESERT)), -33, 1),
    FOREST(new Output(0, 1, 1),
            new ArrayList<TerrainType>(
                    Arrays.asList(TerrainType.GRASSLAND, TerrainType.HILLS, TerrainType.PLAINS, TerrainType.TUNDRA)),
            25, 2),
    ICE(new Output(0, 0, 0), new ArrayList<TerrainType>(), 0, 100000),
    JUNGLE(new Output(0, 1, -1), new ArrayList<TerrainType>(Arrays.asList(TerrainType.HILLS, TerrainType.PLAINS)), 25,
            2),
    MARSH(new Output(0, -1, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.GRASSLAND)), -33, 2),
    OASIS(new Output(1, 3, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.DESERT)), -33, 1);

    private Output output;
    private ArrayList<TerrainType> terrainTypes;
    private int combatModifier;
    private int movementCost;

    private Feature(Output output, ArrayList<TerrainType> terrainTypes, int combatModifier, int movementCost) {
        this.output = output;
        this.terrainTypes = terrainTypes;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
    }

    public static boolean isTileCompatibleWithFeature(Feature feature, Tile tile) {
        if (feature.equals(FLOOD_PLAINS) && feature.getTerrainTypes().contains(tile.getTerrainType())) {
            if (tile.isNearTheRiver())
                return true;
            return false;
        }

        if (feature.equals(OASIS) && feature.getTerrainTypes().contains(tile.getTerrainType())) {
            if (tile.getTerrainType().equals(TerrainType.DESERT))
                return true;
            return false;
        } else if (feature.getTerrainTypes().contains(tile.getTerrainType()))
            return true;
        return false;
    }

    public static void plantFeatureOnTileAndApplyOutputChanges(Feature feature, Tile tile) {
        if (isTileCompatibleWithFeature(feature, tile))
            tile.setFeature(feature);
        else {
            Debugger.debug("the feature is not compatible with the tile!");
            return;
        }

        if (feature.equals(FOREST)) {
            tile.getOutput().setFood(feature.getOutput().getFood());
            tile.getOutput().setGold(feature.getOutput().getGold());
            tile.getOutput().setProduction(feature.getOutput().getProduction());
        }

        else {
            tile.getOutput().setFood(tile.getOutput().getFood() + feature.getOutput().getFood());
            tile.getOutput().setGold(tile.getOutput().getGold() + feature.getOutput().getGold());
            tile.getOutput().setProduction(tile.getOutput().getProduction() + feature.getOutput().getProduction());
        }
    }

    public static void removeFeatureOnTile(Tile tile) {
        if (tile.getFeature() != null)
            tile.setFeature(null);
    }

    public Output getOutput() {
        return this.output;
    }

    public void setCombatModifier(int combatModifier) {
        this.combatModifier = combatModifier;
    }

    public int getCombatModifier() {
        return this.combatModifier;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }

    public int getMovementCost() {
        return this.movementCost;
    }

    public ArrayList<TerrainType> getTerrainTypes() {
        return this.terrainTypes;
    }

}
