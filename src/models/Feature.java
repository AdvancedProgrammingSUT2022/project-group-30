package models;

import java.util.ArrayList;
import java.util.Arrays;

import models.interfaces.TerrainProperty;

public enum Feature implements TerrainProperty {
    FLOOD_PLAINS(new Output(0, 2, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.DESERT)), -33, 1, "Flood Plains"),
    FOREST(new Output(0, 1, 1),
            new ArrayList<TerrainType>(
                    Arrays.asList(TerrainType.GRASSLAND, TerrainType.HILLS, TerrainType.PLAINS, TerrainType.TUNDRA)),
            25, 2, "Forest"),
    ICE(new Output(0, 0, 0), new ArrayList<TerrainType>(), 0, GameMap.EXPENSIVE_MOVEMENT_COST, "Ice"),
    JUNGLE(new Output(0, 1, -1), new ArrayList<TerrainType>(Arrays.asList(TerrainType.HILLS, TerrainType.PLAINS)), 25,
            2, "Jungle"),
    MARSH(new Output(0, -1, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.GRASSLAND)), -33, 2, "Marsh"),
    OASIS(new Output(1, 3, 0), new ArrayList<TerrainType>(Arrays.asList(TerrainType.DESERT)), -33, 1, "Oasis");

    private Output output;
    private ArrayList<TerrainType> terrainTypes;
    private int combatModifier;
    private int movementCost;
    private final String name;

    private Feature(Output output, ArrayList<TerrainType> terrainTypes, int combatModifier, int movementCost, String name) {
        this.output = output;
        this.terrainTypes = terrainTypes;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
        this.name = name;
    }

    public static boolean isTileCompatibleWithFeature(Feature feature, Tile tile)   {
        if (feature == Feature.FLOOD_PLAINS && feature.getTerrainTypes().contains(tile.getTerrainType())) {
            if (tile.isNearTheRiver())
                return true;
            return false;
        } else if (feature.getTerrainTypes().contains(tile.getTerrainType())) {
            return true;
        }
        return false;
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

    public String getName() {
        return name;
    }
}
