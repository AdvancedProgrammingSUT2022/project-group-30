package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class DiplomaticRelation extends Diplomacy {
    private boolean areMutuallyVisible = false;
    private boolean areAtWar = false;
    private int friendliness = 0;

    public DiplomaticRelation(Civilization civ1, Civilization civ2) {
        this.pair = new CivilizationPair(civ1, civ2);
    }

    public boolean areMutuallyVisible() {
        return areMutuallyVisible;
    }

    public void setAreMutuallyVisible(boolean areMutuallyVisible) {
        this.areMutuallyVisible = areMutuallyVisible;
    }

    public boolean areAtWar() {
        return areAtWar;
    }

    public void setAreAtWar(boolean areAtWar) {
        this.areAtWar = areAtWar;
    }

    public void setFriendliness(int friendliness) {
        this.friendliness = friendliness;
    }

    public int getFriendliness() {
        return friendliness;
    }

    public DiplomaticRelation(CivilizationPair pair) {
        this.pair = pair;
    }

}
