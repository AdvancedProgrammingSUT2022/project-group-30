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
    AGRICULTURE(10, new ArrayList<Technology>(), "Agriculture"),
    ANIMAL_HUSBANDRY(1000, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Animal Husbandry"),
    ARCHERY(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Archery"),
    MINING(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Mining"),
    BRONZE_WORKING(10, new ArrayList<Technology>(Arrays.asList(MINING)), "Bronze Working"),
    POTTERY(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Pottery"),
    CALENDAR(10, new ArrayList<Technology>(Arrays.asList(POTTERY)), "Calendar"),
    MASONRY(10, new ArrayList<Technology>(Arrays.asList(MINING)), "Masonry"),
    THE_WHEEL(10, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), "The Wheel"),
    TRAPPING(10, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), "Trapping"),
    WRITING(10, new ArrayList<Technology>(Arrays.asList(POTTERY)), "Writing"),
    CONSTRUCTION(10, new ArrayList<Technology>(Arrays.asList(MASONRY)), "Construction"),
    HORSEBACK_RIDING(10, new ArrayList<Technology>(Arrays.asList(THE_WHEEL)), "Horseback Riding"),
    IRON_WORKING(10, new ArrayList<Technology>(Arrays.asList(BRONZE_WORKING)), "Iron Working"),
    MATHEMATICS(10, new ArrayList<Technology>(Arrays.asList(THE_WHEEL, ARCHERY)), "Mathematics"),
    PHILOSOPHY(10, new ArrayList<Technology>(Arrays.asList(WRITING)), "Philosophy"),
    CIVIL_SERVICE(10, new ArrayList<Technology>(Arrays.asList(PHILOSOPHY, TRAPPING)), "Civil Service"),
    CURRENCY(10, new ArrayList<Technology>(Arrays.asList(MATHEMATICS)), "Currency"),
    CHIVALRY(10, new ArrayList<Technology>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)), "Chivalry"),
    ENGINEERING(10, new ArrayList<Technology>(Arrays.asList(MATHEMATICS, CONSTRUCTION)), "Engineering"),
    MACHINERY(10, new ArrayList<Technology>(Arrays.asList(ENGINEERING)), "Machinery"),
    METAL_CASTING(10, new ArrayList<Technology>(Arrays.asList(IRON_WORKING)), "Metal Casting"),
    PHYSICS(10, new ArrayList<Technology>(Arrays.asList(ENGINEERING, METAL_CASTING)), "Physics"),
    STEEL(10, new ArrayList<Technology>(Arrays.asList(METAL_CASTING)), "Steel"),
    THEOLOGY(10, new ArrayList<Technology>(Arrays.asList(CALENDAR, PHILOSOPHY)), "Theology"),
    EDUCATION(10, new ArrayList<Technology>(Arrays.asList(THEOLOGY)), "Education"),
    ACOUSTICS(10, new ArrayList<Technology>(Arrays.asList(EDUCATION)), "Acoustics"),
    ARCHAEOLOGY(10, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "Archaeology"),
    BANKING(10, new ArrayList<Technology>(Arrays.asList(EDUCATION, CHIVALRY)), "Banking"),
    GUNPOWDER(10, new ArrayList<Technology>(Arrays.asList(PHYSICS, STEEL)), "Gunpowder"),
    CHEMISTRY(10, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "Chemistry"),
    PRINTING_PRESS(10, new ArrayList<Technology>(Arrays.asList(MACHINERY, PHYSICS)), "Printing Press"),
    ECONOMICS(10, new ArrayList<Technology>(Arrays.asList(BANKING, PRINTING_PRESS)), "Economics"),
    FERTILIZER(10, new ArrayList<Technology>(Arrays.asList(CHEMISTRY)), "Fertilizer"),
    METALLURGY(10, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "Metallurgy"),
    MILITARY_SCIENCE(10, new ArrayList<Technology>(Arrays.asList(ECONOMICS, CHEMISTRY)), "Military Science"),
    RIFLING(10, new ArrayList<Technology>(Arrays.asList(METALLURGY)), "Rifling"),
    SCIENTIFIC_THEORY(10, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "Scientific Theory"),
    BIOLOGY(10, new ArrayList<Technology>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)), "Biology"),
    STEAM_POWER(10, new ArrayList<Technology>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)), "Steam Powder"),
    DYNAMITE(10, new ArrayList<Technology>(Arrays.asList(FERTILIZER, RIFLING)), "Dynamite"),
    ELECTRICITY(10, new ArrayList<Technology>(Arrays.asList(BIOLOGY, STEAM_POWER)), "Electricity"),
    RADIO(10, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "Radio"),
    RAILROAD(10, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "Railroad"),
    REPLACEABLE_PARTS(10, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "Replaceable Parts"),
    COMBUSTION(10, new ArrayList<Technology>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)), "Combustion"),
    TELEGRAPH(10, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "Telegraph");

    private static class Relations {
        public static HashMap<Technology, ArrayList<Technology>> forwardRelations = new HashMap<Technology, ArrayList<Technology>>();
        public static HashMap<Technology, ArrayList<Technology>> backwardRelations = new HashMap<Technology, ArrayList<Technology>>();
    }

    private int cost;
    private String name;

    private Technology(int cost, ArrayList<Technology> prerequisiteTechnologies, String name) {
        this.name = name;
        this.cost = cost;
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

    public String getName(){
        return this.name;
    }

    public static Technology getTechnologyByName(String name) {
        for (Technology technology : values()) {
            if (technology.getName().equalsIgnoreCase(name)) {
                return technology;
            }
        }
        return null;
    }
}