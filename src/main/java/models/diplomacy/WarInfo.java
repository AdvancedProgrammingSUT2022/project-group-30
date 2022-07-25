package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class WarInfo extends Diplomacy  implements java.io.Serializable{
    public WarInfo(CivilizationPair pair) {
        this.pair = pair;
    }

    public WarInfo(Civilization civ1, Civilization civ2) {
        this.pair = new CivilizationPair(civ1, civ2);
    }
}
