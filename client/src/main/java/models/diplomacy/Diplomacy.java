package models.diplomacy;

import models.CivilizationPair;

public abstract class Diplomacy {
    protected CivilizationPair pair;

    public CivilizationPair getPair() {
        return this.pair;
    }
}
