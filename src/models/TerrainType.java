package models;

import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {
    DESERT(new Output(0, 0, 0), -33, 1),
    GRASSLAND(new Output(0, 2, 0), -33, 1),
    HILLS(new Output(0, 0, 2), 25, 2),
    MOUNTAIN(new Output(0, 0, 0), 25, Double.MAX_VALUE),
    OCEAN(new Output(0 ,0, 0), 25, Double.MAX_VALUE),
    PLAINS(new Output(0, 1, 1), -33, 1),
    SNOW(new Output(0, 0, 0), -33, 1),
    TUNDRA(new Output(0, 1, 0), -33, 1);

    private Output output;
    private double combatModifier;
    private double movementCost;
    
    private TerrainType(Output output, double combatModifier, double movementCost){
        this.output = output;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
    }

    public void setTerrainTypeToTileAndApllyOutputChanges(Tile tile){
        tile.getOutput().setFood(tile.getOutput().getFood() + this.getOutput().getFood());
        tile.getOutput().setGold(tile.getOutput().getGold() + this.getOutput().getGold());
        tile.getOutput().setProduction(tile.getOutput().getProduction() + this.getOutput().getProduction());
    }

    public void changeTerrainTypeOnTile(Tile tile, TerrainType newTerrainType){
        if(tile.getTerrainType() != null)
            //??okeye
            tile.getTerrainType().changeTerrainTypeOnTile(tile, this);
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
