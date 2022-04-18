package models;

import models.interfaces.TerrainProperty;
import models.utilities.Debugger;

public enum Feature implements TerrainProperty {
    FLOOD_PLAINS(new Output(0, 2, 0), -33, 1),
    FOREST(new Output(0, 1, 1), 25, 2),
    ICE(new Output(0, 0, 0), 0, Integer.MAX_VALUE),
    JUNGLE(new Output(0, 1, -1), 25, 2),
    MARSH(new Output(0, -1, 0), -33, 2),
    OASIS(new Output(1, 3, 0), -33, 1);

    private Output output;
    private int combatModifier;
    private int movementCost;

    private Feature(Output output, int combatModifier, int movementCost) {
        this.output = output;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
    }

    public boolean isTileCompatibleWithFeature(Feature feature, Tile tile) {
        if (feature.equals(FLOOD_PLAINS)) {
            if (tile.isNearTheRiver())
                return true;
            return false;
        }

        if (feature.equals(OASIS)) {
            if (tile.getTerrainType().equals(TerrainType.DESERT))
                return true;
            return false;
        }
        return true;
    }

    public void plantFeatureOnTileAndApplyOutputChanges(Feature feature, Tile tile) {
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

    public void removeFeatureOnTile(Tile tile) {
        if (tile.getFeature() != null)
            tile.setFeature(null);
    }

    public void setOutput(Output output) {
        this.output = output;
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

}
