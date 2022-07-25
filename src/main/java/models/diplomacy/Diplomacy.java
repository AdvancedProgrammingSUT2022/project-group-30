package models.diplomacy;

import models.CivilizationPair;

public abstract class Diplomacy  implements java.io.Serializable{
    protected CivilizationPair pair;

    public CivilizationPair getPair() {
        return this.pair;
    }
}
