package models.works;

import models.GameDataBase;
import models.Tile;
import models.interfaces.TurnHandler;
import models.units.Unit;

public abstract class Work implements TurnHandler {
    //private or protected??
    private int turnsRemaining;
    protected Unit worker;
    private boolean isInProgress;

    public void goToNextTurn() {
        // TODO

        if (isInProgress) {
            turnsRemaining--;
        }
        if (turnsRemaining == 0) {
            applyChange();
        }
    }

    public abstract void applyChange();

    public void startWork() {
        // TODO
        isInProgress = true;
    }
    public void stopWork() {
        // TODO
        isInProgress = false;
    }

    public abstract int calculateRequiredTurns();

    public Tile findLocation() {
        for(Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()){
            if(tile.getWork() == this)
                return tile;
        }
        return null;
    }
}
