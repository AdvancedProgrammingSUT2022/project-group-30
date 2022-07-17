package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class DiplomaticRelation extends Diplomacy {
    double friendliness = 0;
    boolean areMutuallyVisible = false;

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

    public double getFriendliness() {
        return this.friendliness;
    }

    public void setFriendliness(double friendliness) {
        this.friendliness = friendliness;
    }
}
