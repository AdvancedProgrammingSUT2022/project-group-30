package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utilities.Debugger;

public enum TechnologyType {
    AGRICULTURE(0, new ArrayList<TechnologyType>()),
    ANIMAL_HUSBANDRY(0, new ArrayList<TechnologyType>(Arrays.asList(AGRICULTURE))),
    ARCHERY(0, new ArrayList<TechnologyType>(Arrays.asList(AGRICULTURE))),
    MINING(0, new ArrayList<TechnologyType>(Arrays.asList(AGRICULTURE))),
    BRONZE_WORKING(0, new ArrayList<TechnologyType>(Arrays.asList(MINING))),
    POTTERY(0, new ArrayList<TechnologyType>(Arrays.asList(AGRICULTURE))),
    CALENDAR(0, new ArrayList<TechnologyType>(Arrays.asList(POTTERY))),
    MASONRY(0, new ArrayList<TechnologyType>(Arrays.asList(MINING))),
    THE_WHEEL(0, new ArrayList<TechnologyType>(Arrays.asList(ANIMAL_HUSBANDRY))),
    TRAPPING(0, new ArrayList<TechnologyType>(Arrays.asList(ANIMAL_HUSBANDRY))),
    WRITING(0, new ArrayList<TechnologyType>(Arrays.asList(POTTERY))),
    CONSTRUCTION(0, new ArrayList<TechnologyType>(Arrays.asList(MASONRY))),
    HORSEBACK_RIDING(0, new ArrayList<TechnologyType>(Arrays.asList(THE_WHEEL))),
    IRON_WORKING(0, new ArrayList<TechnologyType>(Arrays.asList(BRONZE_WORKING))),
    MATHEMATICS(0, new ArrayList<TechnologyType>(Arrays.asList(THE_WHEEL, ARCHERY))),
    PHILOSOPHY(0, new ArrayList<TechnologyType>(Arrays.asList(WRITING))),
    CIVIL_SERVICE(0, new ArrayList<TechnologyType>(Arrays.asList(PHILOSOPHY, TRAPPING))),
    CURRENCY(0, new ArrayList<TechnologyType>(Arrays.asList(MATHEMATICS))),
    CHIVALRY(0, new ArrayList<TechnologyType>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY))),
    ENGINEERING(0, new ArrayList<TechnologyType>(Arrays.asList(MATHEMATICS, CONSTRUCTION))),
    MACHINERY(0, new ArrayList<TechnologyType>(Arrays.asList(ENGINEERING))),
    METAL_CASTING(0, new ArrayList<TechnologyType>(Arrays.asList(IRON_WORKING))),
    PHYSICS(0, new ArrayList<TechnologyType>(Arrays.asList(ENGINEERING, METAL_CASTING))),
    STEEL(0, new ArrayList<TechnologyType>(Arrays.asList(METAL_CASTING))),
    THEOLOGY(0, new ArrayList<TechnologyType>(Arrays.asList(CALENDAR, PHILOSOPHY))),
    EDUCATION(0, new ArrayList<TechnologyType>(Arrays.asList(THEOLOGY))),
    ACOUSTICS(0, new ArrayList<TechnologyType>(Arrays.asList(EDUCATION))),
    ARCHAEOLOGY(0, new ArrayList<TechnologyType>(Arrays.asList(ACOUSTICS))),
    BANKING(0, new ArrayList<TechnologyType>(Arrays.asList(EDUCATION, CHIVALRY))),
    GUNPOWDER(0, new ArrayList<TechnologyType>(Arrays.asList(PHYSICS, STEEL))),
    CHEMISTRY(0, new ArrayList<TechnologyType>(Arrays.asList(GUNPOWDER))),
    PRINTING_PRESS(0, new ArrayList<TechnologyType>(Arrays.asList(MACHINERY, PHYSICS))),
    ECONOMICS(0, new ArrayList<TechnologyType>(Arrays.asList(BANKING, PRINTING_PRESS))),
    FERTILIZER(0, new ArrayList<TechnologyType>(Arrays.asList(CHEMISTRY))),
    METALLURGY(0, new ArrayList<TechnologyType>(Arrays.asList(GUNPOWDER))),
    MILITARY_SCIENCE(0, new ArrayList<TechnologyType>(Arrays.asList(ECONOMICS, CHEMISTRY))),
    RIFLING(0, new ArrayList<TechnologyType>(Arrays.asList(METALLURGY))),
    SCIENTIFIC_THEORY(0, new ArrayList<TechnologyType>(Arrays.asList(ACOUSTICS))),
    BIOLOGY(0, new ArrayList<TechnologyType>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY))),
    STEAM_POWER(0, new ArrayList<TechnologyType>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE))),
    DYNAMITE(0, new ArrayList<TechnologyType>(Arrays.asList(FERTILIZER, RIFLING))),
    ELECTRICITY(0, new ArrayList<TechnologyType>(Arrays.asList(BIOLOGY, STEAM_POWER))),
    RADIO(0, new ArrayList<TechnologyType>(Arrays.asList(ELECTRICITY))),
    RAILROAD(0, new ArrayList<TechnologyType>(Arrays.asList(STEAM_POWER))),
    REPLACEABLE_PARTS(0, new ArrayList<TechnologyType>(Arrays.asList(STEAM_POWER))),
    COMBUSTION(0, new ArrayList<TechnologyType>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE))),
    TELEGRAPH(0, new ArrayList<TechnologyType>(Arrays.asList(ELECTRICITY)));


    class Relations {
        public static HashMap<TechnologyType, ArrayList<TechnologyType>> forwardRelations = new HashMap<TechnologyType, ArrayList<TechnologyType>>();
        public static HashMap<TechnologyType, ArrayList<TechnologyType>> backwardRelations = new HashMap<TechnologyType, ArrayList<TechnologyType>>();
    }

    private int cost;

    private TechnologyType(int cost, ArrayList<TechnologyType> prerequisiteTechnologies) {
        if (prerequisiteTechnologies == null) {
            Debugger.debug("Error in Technology class : cannot pass null arrayList to constructor!");
            return;
        }

        Relations.backwardRelations.put(this, prerequisiteTechnologies);
        if (Relations.forwardRelations.get(this) == null) {
            Relations.forwardRelations.put(this, new ArrayList<TechnologyType>());
        }
        for (TechnologyType tech : prerequisiteTechnologies) {
            if (Relations.forwardRelations.get(tech) == null) {
                Relations.forwardRelations.put(tech, new ArrayList<TechnologyType>(Arrays.asList(this)));
            } else {
                Relations.forwardRelations.get(tech).add(this);
            }
        }
    }

    public ArrayList<TechnologyType> getUnlockedTechnologies() {
        return Relations.forwardRelations.get(this);
    }
    public ArrayList<TechnologyType> getPrerequisiteTechnologies() {
        return Relations.backwardRelations.get(this);
    }
}