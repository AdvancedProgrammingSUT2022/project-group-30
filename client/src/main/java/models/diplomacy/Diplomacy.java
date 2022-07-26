package models.diplomacy;

import models.CivilizationPair;

public abstract class Diplomacy {
    protected final int id;
    protected static int nextAvailableId = 0;
    protected CivilizationPair pair;

    public  int getId() {
        return id;
    }

    public Diplomacy() {
        this.id = nextAvailableId;
        nextAvailableId++;
    }

    public Diplomacy(Diplomacy diplomacy) {
        this.id = diplomacy.id;
    }

    public CivilizationPair getPair() {
        return this.pair;
    }
}
