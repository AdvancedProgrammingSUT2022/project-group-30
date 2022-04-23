package models;

import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {
    DESERT(new Output(0, 0, 0), -33, 1, "Desert"),
    GRASSLAND(new Output(0, 2, 0), -33, 1, "Grassland"),
    HILLS(new Output(0, 0, 2), 25, 2, "Hills"),
    MOUNTAIN(new Output(0, 0, 0), 25, GameMap.EXPENSIVE_MOVEMENT_COST, "Mountain"),
    OCEAN(new Output(0, 0, 0), 25, GameMap.EXPENSIVE_MOVEMENT_COST, "Ocean"),
    PLAINS(new Output(0, 1, 1), -33, 1, "Plains"),
    SNOW(new Output(0, 0, 0), -33, 1, "Snow"),
    TUNDRA(new Output(0, 1, 0), -33, 1, "Tundra");

    private Output output;
    private double combatModifier;
    private double movementCost;
    private final String name;

    private TerrainType(Output output, double combatModifier, double movementCost, String name) {
        this.output = output;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
        this.name = name;
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

    public String getName() {
        return name;
    }

}
