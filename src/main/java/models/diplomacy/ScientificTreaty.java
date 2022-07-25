package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class ScientificTreaty extends Diplomacy  implements java.io.Serializable{
    public ScientificTreaty(CivilizationPair pair) {
        this.pair = pair;
    }

    public ScientificTreaty(Civilization civ1, Civilization civ2) {
        this.pair = new CivilizationPair(civ1, civ2);
    }
}
