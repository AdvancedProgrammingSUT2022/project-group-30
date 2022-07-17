package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class DiplomaticRelation extends Diplomacy {
    boolean areMutuallyVisible = false;
    boolean areAtWar = false;

    public DiplomaticRelation(CivilizationPair pair) {
        this.pair = pair;
    }

    public DiplomaticRelation(Civilization civ1, Civilization civ2) {
        this.pair = new CivilizationPair(civ1, civ2);
    }

    public boolean areMutuallyVisible() {
        return areMutuallyVisible;
    }

    public void setAreMutuallyVisible(boolean areMutuallyVisible) {
        this.areMutuallyVisible = areMutuallyVisible;
    }

    public boolean isAreAtWar() {
        return areAtWar;
    }

    public void setAreAtWar(boolean areAtWar) {
        this.areAtWar = areAtWar;
    }
}
