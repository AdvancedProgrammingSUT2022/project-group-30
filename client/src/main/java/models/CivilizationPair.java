package models;

import utilities.Debugger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class CivilizationPair {
    private Set<Civilization> civilizations = new LinkedHashSet<>();

    public CivilizationPair(Civilization civ1, Civilization civ2) {
        if (civ1.equals(civ2)) {
            Debugger.debug("Two civilizations are the same. Inputs of CivilianPair's constructor should be different!");
            return;
        }
        civilizations.add(civ1);
        civilizations.add(civ2);
    }

    public boolean containsCivilization(Civilization civilization) {
        if (civilizations.contains(civilization))
            return true;
        return false;
    }

    public Set<Civilization> getCivilizations() {
        return this.civilizations;
    }

    public ArrayList<Civilization> getCivilizationsArray() {
        ArrayList<Civilization> civilizations = new ArrayList<Civilization>(this.civilizations);
        return civilizations;
    }
}
