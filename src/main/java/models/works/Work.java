package models.works;

import models.GameDataBase;
import models.Tile;
import models.interfaces.TurnHandler;
import models.units.Unit;

public abstract class Work implements TurnHandler {
    //private or protected??
    protected int turnsRemaining;
    protected Unit worker;
    protected boolean isInProgress;

    public void goToNextTurn() {
        if (isInProgress) {
            turnsRemaining--;
        }
        if (turnsRemaining == 0) {
            applyChange();
        }
    }

    public abstract void applyChange();

    public void startWork(Unit newWorker) {
        worker = newWorker;
        isInProgress = true;
    }
    public void startWork() {
        // TODO
        isInProgress = true;
    }
    public void stopWork() {
        // TODO
        isInProgress = false;
    }

    public abstract int calculateRequiredTurns();
    public abstract String getTitle();

    public Tile findLocation() {
        for(Tile tile : GameDataBase.getGameDataBase().getMap().getAllMapTiles()){
            if(tile.getWork() == this)
                return tile;
        }
        return null;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    public void setTurnsRemaining(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    public Unit getWorker() {
        return worker;
    }

    public void setWorker(Unit worker) {
        this.worker = worker;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }
}
