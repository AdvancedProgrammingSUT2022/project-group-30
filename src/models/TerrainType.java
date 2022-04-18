package models;

import models.interfaces.TerrainProperty;
import utilities.Debugger;

public enum TerrainType implements TerrainProperty {
    DESERT(new Output(0, 0, 0), -33, 1),
    GRASSLAND(new Output(0, 2, 0), -33, 1),
    HILLS(new Output(0, 0, 2), 25, 2),
    MOUNTAIN(new Output(0, 0, 0), 25, Double.MAX_VALUE),
    OCEAN(new Output(0, 0, 0), 25, Double.MAX_VALUE),
    PLAINS(new Output(0, 1, 1), -33, 1),
    SNOW(new Output(0, 0, 0), -33, 1),
    TUNDRA(new Output(0, 1, 0), -33, 1);

    private Output output;
    private double combatModifier;
    private double movementCost;

    private TerrainType(Output output, double combatModifier, double movementCost) {
        this.output = output;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
    }

    public static void setTerrainTypeToTileAndApllyOutputChanges(Tile tile, TerrainType terrainType) {
        if (tile.getTerrainType() != null) {
            Debugger.debug("this tile has terrainType!");
            return;
        }
        tile.setTerrainType(terrainType);
        tile.getOutput().add(terrainType.getOutput());
    }

    public static void changeTerrainTypeOnTile(Tile tile, TerrainType newTerrainType) {
        tile.getOutput().subtract(tile.getTerrainType().getOutput());
        tile.setTerrainType(newTerrainType);
        tile.getOutput().add(tile.getTerrainType().getOutput());
    }

    public Output getOutput() {
        return this.output;
    }

    public void setCombatModifier(double combatModifier) {
        this.combatModifier = combatModifier;
    }

    public double getCombatModifier() {
        return this.combatModifier;
    }

    public void setMovementCost(double movementCost) {
        this.movementCost = movementCost;
    }

    public double getMovementCost() {
        return this.movementCost;
    }

}
