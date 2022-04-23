package models.technology;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.Debugger;

public class TechnologyMap {
    private HashMap<Technology, Boolean> map = new HashMap<Technology, Boolean>();

    public TechnologyMap() {
        for (Technology type : Technology.values()) {
            map.put(type, false);
        }
        map.put(Technology.AGRICULTURE, true);
    }

    public void learnTechnology(Technology technology) {
        if (isTechnologyUnlocked(technology) == false) {
            Debugger.debug("You can't unlock this technology!, the necessary prerequisites are not present");
            return;
        }

        map.put(technology, true);
    }

    public void learnTechnologyAndPrerequisites(Technology technology) { // discovers the technology and any of its
                                                                         // prerequisites that are not yet unlocked
        for (Technology prerequisite : technology.getPrerequisiteTechnologies()) {
            if (map.get(prerequisite) == false) {
                learnTechnologyAndPrerequisites(prerequisite);
            }
        }
        map.put(technology, true);
    }

    public boolean isTechnologyLearned(Technology type) {
        return map.get(type);
    }

    public boolean isTechnologyUnlocked(Technology technology) {
        for (Technology prerequisite : technology.getPrerequisiteTechnologies()) {
            if (map.get(prerequisite) == false) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Technology> getLearnedTechnologies() {
        ArrayList<Technology> result = new ArrayList<Technology>();
        for (Technology technology : map.keySet()) {
            if (map.get(technology)) {
                result.add(technology);
            }
        }
        return result;
    }

    public ArrayList<Technology> getUnlockedTechnologies() {
        ArrayList<Technology> result = new ArrayList<Technology>();
        for (Technology technology : map.keySet()) {
            if (map.get(technology)) {
                for (Technology unlockable : technology.getDependentTechnologies()) {
                    if (map.get(unlockable) == false) {
                        result.add(unlockable);
                    }
                }
            }
        }
        return result;
    }
}