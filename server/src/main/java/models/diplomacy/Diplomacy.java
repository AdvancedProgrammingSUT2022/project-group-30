package models.diplomacy;

import models.CivilizationPair;

public abstract class Diplomacy {
    protected final int id;
    public  int getId() {
        return id;
    }
    protected static int nextAvailableId = 0;

    public Diplomacy() {
        this.id = nextAvailableId;
        nextAvailableId++;
    }

    protected CivilizationPair pair;

    public CivilizationPair getPair() {
        return this.pair;
    }
}
