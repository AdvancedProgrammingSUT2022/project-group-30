package models;

import models.interfaces.MPCostInterface;
import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {
    DESERT(new Output(0, 0, 0), -33, new MPCostClass(1)),
    GRASSLAND(new Output(0, 2, 0), -33, new MPCostClass(1)),
    HILLS(new Output(0, 0, 2), 25, new MPCostClass(2)),
    MOUNTAIN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE),
    OCEAN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE),
    PLAINS(new Output(0, 1, 1), -33, new MPCostClass(1)),
    SNOW(new Output(0, 0, 0), -33, new MPCostClass(1)),
    TUNDRA(new Output(0, 1, 0), -33, new MPCostClass(1));

    private Output output;
    private double combatModifier;
    private MPCostInterface movementCost;

    private TerrainType(Output output, double combatModifier, MPCostInterface movementCost) {
        this.output = output;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
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

}
