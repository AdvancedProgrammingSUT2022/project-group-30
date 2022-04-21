package models.works;

import models.Worker;

public abstract class Work {
    protected int turnsRemaining;
    protected Worker worker;

    public boolean goToNextTurn(){
        //TODO ..
        return true;
    }

    public abstract void applyChange();

    public void startWork(Worker worker){
        //MINETODO
    }

    public void stopWork(){
        //MINETODO
    }

    public abstract int calculateRequiredTurnsCount();

    public int getTurnsRemaining(){
        return this.turnsRemaining;
    }

    public void setTurnsRemaining(int turnsRemaining){
        this.turnsRemaining = turnsRemaining;
    }

    public Worker getWorker(){
        return this.worker;
    }

    public void setWorker(Worker worker){
        this.worker = worker;
    }

}
