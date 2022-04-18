package models.technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utilities.Debugger;

/*
IMPORTANT NOTE:
an "unlocked" tech is one that can be researched, but is not yet learned, and the civilization does not yet "own" it, but needs to research
and learn it.
A "learned" tech is one that has been researched and added to the civilization's tech map. the civilization "owns" it.

the difference between unlocking and learning are important, because the methods related to them have precise names.
*/

public enum Technology {
    AGRICULTURE(0, new ArrayList<Technology>()),
    ANIMAL_HUSBANDRY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE))),
    ARCHERY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE))),
    MINING(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE))),
    BRONZE_WORKING(0, new ArrayList<Technology>(Arrays.asList(MINING))),
    POTTERY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE))),
    CALENDAR(0, new ArrayList<Technology>(Arrays.asList(POTTERY))),
    MASONRY(0, new ArrayList<Technology>(Arrays.asList(MINING))),
    THE_WHEEL(0, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY))),
    TRAPPING(0, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY))),
    WRITING(0, new ArrayList<Technology>(Arrays.asList(POTTERY))),
    CONSTRUCTION(0, new ArrayList<Technology>(Arrays.asList(MASONRY))),
    HORSEBACK_RIDING(0, new ArrayList<Technology>(Arrays.asList(THE_WHEEL))),
    IRON_WORKING(0, new ArrayList<Technology>(Arrays.asList(BRONZE_WORKING))),
    MATHEMATICS(0, new ArrayList<Technology>(Arrays.asList(THE_WHEEL, ARCHERY))),
    PHILOSOPHY(0, new ArrayList<Technology>(Arrays.asList(WRITING))),
    CIVIL_SERVICE(0, new ArrayList<Technology>(Arrays.asList(PHILOSOPHY, TRAPPING))),
    CURRENCY(0, new ArrayList<Technology>(Arrays.asList(MATHEMATICS))),
    CHIVALRY(0, new ArrayList<Technology>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY))),
    ENGINEERING(0, new ArrayList<Technology>(Arrays.asList(MATHEMATICS, CONSTRUCTION))),
    MACHINERY(0, new ArrayList<Technology>(Arrays.asList(ENGINEERING))),
    METAL_CASTING(0, new ArrayList<Technology>(Arrays.asList(IRON_WORKING))),
    PHYSICS(0, new ArrayList<Technology>(Arrays.asList(ENGINEERING, METAL_CASTING))),
    STEEL(0, new ArrayList<Technology>(Arrays.asList(METAL_CASTING))),
    THEOLOGY(0, new ArrayList<Technology>(Arrays.asList(CALENDAR, PHILOSOPHY))),
    EDUCATION(0, new ArrayList<Technology>(Arrays.asList(THEOLOGY))),
    ACOUSTICS(0, new ArrayList<Technology>(Arrays.asList(EDUCATION))),
    ARCHAEOLOGY(0, new ArrayList<Technology>(Arrays.asList(ACOUSTICS))),
    BANKING(0, new ArrayList<Technology>(Arrays.asList(EDUCATION, CHIVALRY))),
    GUNPOWDER(0, new ArrayList<Technology>(Arrays.asList(PHYSICS, STEEL))),
    CHEMISTRY(0, new ArrayList<Technology>(Arrays.asList(GUNPOWDER))),
    PRINTING_PRESS(0, new ArrayList<Technology>(Arrays.asList(MACHINERY, PHYSICS))),
    ECONOMICS(0, new ArrayList<Technology>(Arrays.asList(BANKING, PRINTING_PRESS))),
    FERTILIZER(0, new ArrayList<Technology>(Arrays.asList(CHEMISTRY))),
    METALLURGY(0, new ArrayList<Technology>(Arrays.asList(GUNPOWDER))),
    MILITARY_SCIENCE(0, new ArrayList<Technology>(Arrays.asList(ECONOMICS, CHEMISTRY))),
    RIFLING(0, new ArrayList<Technology>(Arrays.asList(METALLURGY))),
    SCIENTIFIC_THEORY(0, new ArrayList<Technology>(Arrays.asList(ACOUSTICS))),
    BIOLOGY(0, new ArrayList<Technology>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY))),
    STEAM_POWER(0, new ArrayList<Technology>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE))),
    DYNAMITE(0, new ArrayList<Technology>(Arrays.asList(FERTILIZER, RIFLING))),
    ELECTRICITY(0, new ArrayList<Technology>(Arrays.asList(BIOLOGY, STEAM_POWER))),
    RADIO(0, new ArrayList<Technology>(Arrays.asList(ELECTRICITY))),
    RAILROAD(0, new ArrayList<Technology>(Arrays.asList(STEAM_POWER))),
    REPLACEABLE_PARTS(0, new ArrayList<Technology>(Arrays.asList(STEAM_POWER))),
    COMBUSTION(0, new ArrayList<Technology>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE))),
    TELEGRAPH(0, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)));


    class Relations {
        public static HashMap<Technology, ArrayList<Technology>> forwardRelations = new HashMap<Technology, ArrayList<Technology>>();
        public static HashMap<Technology, ArrayList<Technology>> backwardRelations = new HashMap<Technology, ArrayList<Technology>>();
    }

    private int cost;

    private Technology(int cost, ArrayList<Technology> prerequisiteTechnologies) {
        if (prerequisiteTechnologies == null) {
            Debugger.debug("Error in Technology class : cannot pass null arrayList to constructor!");
            return;
        }

        Relations.backwardRelations.put(this, prerequisiteTechnologies);
        if (Relations.forwardRelations.get(this) == null) {
            Relations.forwardRelations.put(this, new ArrayList<Technology>());
        }
        for (Technology tech : prerequisiteTechnologies) {
            if (Relations.forwardRelations.get(tech) == null) {
                Relations.forwardRelations.put(tech, new ArrayList<Technology>(Arrays.asList(this)));
            } else {
                Relations.forwardRelations.get(tech).add(this);
            }
        }
    }

    public ArrayList<Technology> getDependentTechnologies() {
        return Relations.forwardRelations.get(this);
    }
    public ArrayList<Technology> getPrerequisiteTechnologies() {
        return Relations.backwardRelations.get(this);
    }

    public int getCost() {
        return cost;
    }
}