package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class DiplomaticRelationsMap extends Diplomacy {
    double friendliness;

    public DiplomaticRelationsMap(CivilizationPair pair) {
        this.friendliness = 0;
        this.pair = pair;
    }

    public DiplomaticRelationsMap(Civilization civ1, Civilization civ2) {
        this.friendliness = 0;
        this.pair = new CivilizationPair(civ1, civ2);
    }

    public double getFriendliness() {
        return this.friendliness;
    }

    public void setFriendliness(double friendliness) {
        this.friendliness = friendliness;
    }
}
