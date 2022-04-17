package models.features;

import models.Output;
import models.Tile;

public abstract class Feature {
    protected Output output;
    protected int combatModifier;
    protected int movementCost;

    public Feature(int food, int gold, int production, int combatModifier, int movementCost){
        this.output = new Output(gold, food, production);
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
    }

    public abstract boolean isTileValid(Tile tile);

    public abstract void modifyOutput(Output output);

    public void setOutput(Output output){
        this.output = output;
    }

    public Output getOutput(){
        return this.output;
    }

    public void setCombatModifier(int combatModifier){
        this.combatModifier = combatModifier;
    }

    public int getCombatModifier(){
        return this.combatModifier;
    }

    public void setMovementCost(int movementCost){
        this.movementCost = movementCost;
    }

    public int getMovementCost(){
        return this.movementCost;
    }
    
}
