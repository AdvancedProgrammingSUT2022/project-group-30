package models;

import models.interfaces.MPCostInterface;
import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {
    DESERT(new Output(0, 0, 0), -33, new MPCostClass(1), "Desert"),
    GRASSLAND(new Output(0, 2, 0), -33, new MPCostClass(1), "Grassland"),
    HILLS(new Output(0, 0, 2), 25, new MPCostClass(2), "Hills"),
    MOUNTAIN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE, "Mountain"),
    OCEAN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE, "Ocean"),
    PLAINS(new Output(0, 1, 1), -33, new MPCostClass(1), "Plains"),
    SNOW(new Output(0, 0, 0), -33, new MPCostClass(1), "Snow"),
    TUNDRA(new Output(0, 1, 0), -33, new MPCostClass(1), "Tundra");

    private Output output;
    private double combatModifier;
    private MPCostInterface movementCost;
    private final String name;


    private TerrainType(Output output, double combatModifier, MPCostInterface movementCost, String name) {
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

    public void setMovementCost(MPCostInterface movementCost) {
        this.movementCost = movementCost;
    }

    public MPCostInterface getMovementCost() {
        return this.movementCost;
    }

    public String getName() {
        return name;
    }

}
