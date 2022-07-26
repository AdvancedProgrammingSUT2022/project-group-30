package models;

import com.google.gson.annotations.SerializedName;
import models.interfaces.MPCostInterface;
import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {

    @SerializedName("Enum models.TerrainType Desert")
    DESERT(new Output(0, 0, 0), -33, new MPCostClass(1), "Desert"),
    @SerializedName("Enum models.TerrainType Grassland")
    GRASSLAND(new Output(0, 2, 0), -33, new MPCostClass(1), "Grassland"),
    @SerializedName("Enum models.TerrainType Hills")
    HILLS(new Output(0, 0, 2), 25, new MPCostClass(2), "Hills"),
    @SerializedName("Enum models.TerrainType Mountain")
    MOUNTAIN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE, "Mountain"),
    @SerializedName("Enum models.TerrainType Ocean")
    OCEAN(new Output(0, 0, 0), 25, MPCostEnum.IMPASSABLE, "Ocean"),
    @SerializedName("Enum models.TerrainType Plains")
    PLAINS(new Output(0, 1, 1), -33, new MPCostClass(1), "Plains"),
    @SerializedName("Enum models.TerrainType Snow")
    SNOW(new Output(0, 0, 0), -33, new MPCostClass(1), "Snow"),
    @SerializedName("Enum models.TerrainType Tundra")
    TUNDRA(new Output(0, 1, 0), -33, new MPCostClass(1), "Tundra");


    private Output output;
    private double combatModifier;
    private MPCostInterface movementCost;
    private final String name;

    @SerializedName("type")
    private String typeName;


    private TerrainType(Output output, double combatModifier, MPCostInterface movementCost, String name) {
        this.typeName = getClass().getName();
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
