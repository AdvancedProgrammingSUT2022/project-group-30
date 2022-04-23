package models;

import java.util.Set;

public class CivilizationPair {
    Set<Civilization> civilizations;

    public CivilizationPair(Civilization civ1, Civilization civ2) {
        civilizations.add(civ1);
        civilizations.add(civ2);
    }

    public boolean containsCivilization(Civilization civilization) {
        if (civilizations.contains(civilization))
            return true;
        return false;
    }

    public Set<Civilization> getCivilizations(){
        return this.civilizations;
    }
}
