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
    AGRICULTURE(0, new ArrayList<Technology>(), "agriculture"),
    ANIMAL_HUSBANDRY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "animal husbandry"),
    ARCHERY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "archery"),
    MINING(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "mining"),
    BRONZE_WORKING(0, new ArrayList<Technology>(Arrays.asList(MINING)), "bronze working"),
    POTTERY(0, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "pottery"),
    CALENDAR(0, new ArrayList<Technology>(Arrays.asList(POTTERY)), "calendar"),
    MASONRY(0, new ArrayList<Technology>(Arrays.asList(MINING)), "masonry"),
    THE_WHEEL(0, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), " the wheel"),
    TRAPPING(0, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), "trapping"),
    WRITING(0, new ArrayList<Technology>(Arrays.asList(POTTERY)), "writing"),
    CONSTRUCTION(0, new ArrayList<Technology>(Arrays.asList(MASONRY)), "construction"),
    HORSEBACK_RIDING(0, new ArrayList<Technology>(Arrays.asList(THE_WHEEL)), "horseback riding"),
    IRON_WORKING(0, new ArrayList<Technology>(Arrays.asList(BRONZE_WORKING)), "iron working"),
    MATHEMATICS(0, new ArrayList<Technology>(Arrays.asList(THE_WHEEL, ARCHERY)), "mathematics"),
    PHILOSOPHY(0, new ArrayList<Technology>(Arrays.asList(WRITING)), "philosophy"),
    CIVIL_SERVICE(0, new ArrayList<Technology>(Arrays.asList(PHILOSOPHY, TRAPPING)), "civil service"),
    CURRENCY(0, new ArrayList<Technology>(Arrays.asList(MATHEMATICS)), "currency"),
    CHIVALRY(0, new ArrayList<Technology>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)), "chivalry"),
    ENGINEERING(0, new ArrayList<Technology>(Arrays.asList(MATHEMATICS, CONSTRUCTION)), "engineering"),
    MACHINERY(0, new ArrayList<Technology>(Arrays.asList(ENGINEERING)), "machinery"),
    METAL_CASTING(0, new ArrayList<Technology>(Arrays.asList(IRON_WORKING)), "metal casting"),
    PHYSICS(0, new ArrayList<Technology>(Arrays.asList(ENGINEERING, METAL_CASTING)), "physics"),
    STEEL(0, new ArrayList<Technology>(Arrays.asList(METAL_CASTING)), "steel"),
    THEOLOGY(0, new ArrayList<Technology>(Arrays.asList(CALENDAR, PHILOSOPHY)), "theology"),
    EDUCATION(0, new ArrayList<Technology>(Arrays.asList(THEOLOGY)), "education"),
    ACOUSTICS(0, new ArrayList<Technology>(Arrays.asList(EDUCATION)), "acoustics"),
    ARCHAEOLOGY(0, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "archaeology"),
    BANKING(0, new ArrayList<Technology>(Arrays.asList(EDUCATION, CHIVALRY)), "banking"),
    GUNPOWDER(0, new ArrayList<Technology>(Arrays.asList(PHYSICS, STEEL)), "gunpowder"),
    CHEMISTRY(0, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "chemistry"),
    PRINTING_PRESS(0, new ArrayList<Technology>(Arrays.asList(MACHINERY, PHYSICS)), "printing press"),
    ECONOMICS(0, new ArrayList<Technology>(Arrays.asList(BANKING, PRINTING_PRESS)), "economics"),
    FERTILIZER(0, new ArrayList<Technology>(Arrays.asList(CHEMISTRY)), "fertilizer"),
    METALLURGY(0, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "metallurgy"),
    MILITARY_SCIENCE(0, new ArrayList<Technology>(Arrays.asList(ECONOMICS, CHEMISTRY)), "military science"),
    RIFLING(0, new ArrayList<Technology>(Arrays.asList(METALLURGY)), "rifling"),
    SCIENTIFIC_THEORY(0, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "scientific theory"),
    BIOLOGY(0, new ArrayList<Technology>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)), "biology"),
    STEAM_POWER(0, new ArrayList<Technology>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)), "steam powder"),
    DYNAMITE(0, new ArrayList<Technology>(Arrays.asList(FERTILIZER, RIFLING)), "dynamite"),
    ELECTRICITY(0, new ArrayList<Technology>(Arrays.asList(BIOLOGY, STEAM_POWER)), "electricity"),
    RADIO(0, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "radio"),
    RAILROAD(0, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "railroad"),
    REPLACEABLE_PARTS(0, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "replaceable parts"),
    COMBUSTION(0, new ArrayList<Technology>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)), "combustion"),
    TELEGRAPH(0, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "telegraph");

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
}