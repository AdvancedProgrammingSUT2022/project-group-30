package models.technology;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;
import utilities.Debugger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
IMPORTANT NOTE:
an "unlocked" tech is one that can be researched, but is not yet learned, and the civilization does not yet "own" it, but needs to research
and learn it.
A "learned" tech is one that has been researched and added to the civilization's tech map. the civilization "owns" it.

the difference between unlocking and learning are important, because the methods related to them have precise names.
*/

public enum Technology implements EnumInterface {
    @SerializedName("Enum models.technology.Technology Agriculture")
    AGRICULTURE(10, new ArrayList<Technology>(), "Agriculture"),
    @SerializedName("Enum models.technology.Technology Animal Husbandry")
    ANIMAL_HUSBANDRY(1000, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Animal Husbandry"),
    @SerializedName("Enum models.technology.Technology Archery")
    ARCHERY(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Archery"),
    @SerializedName("Enum models.technology.Technology Mining")
    MINING(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Mining"),
    @SerializedName("Enum models.technology.Technology Bronze Working")
    BRONZE_WORKING(10, new ArrayList<Technology>(Arrays.asList(MINING)), "Bronze Working"),
    @SerializedName("Enum models.technology.Technology Pottery")
    POTTERY(10, new ArrayList<Technology>(Arrays.asList(AGRICULTURE)), "Pottery"),
    @SerializedName("Enum models.technology.Technology Calendar")
    CALENDAR(10, new ArrayList<Technology>(Arrays.asList(POTTERY)), "Calendar"),
    @SerializedName("Enum models.technology.Technology Masonry")
    MASONRY(10, new ArrayList<Technology>(Arrays.asList(MINING)), "Masonry"),
    @SerializedName("Enum models.technology.Technology The Wheel")
    THE_WHEEL(10, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), "The Wheel"),
    @SerializedName("Enum models.technology.Technology Trapping")
    TRAPPING(10, new ArrayList<Technology>(Arrays.asList(ANIMAL_HUSBANDRY)), "Trapping"),
    @SerializedName("Enum models.technology.Technology Writing")
    WRITING(10, new ArrayList<Technology>(Arrays.asList(POTTERY)), "Writing"),
    @SerializedName("Enum models.technology.Technology Construction")
    CONSTRUCTION(10, new ArrayList<Technology>(Arrays.asList(MASONRY)), "Construction"),
    @SerializedName("Enum models.technology.Technology Horseback Riding")
    HORSEBACK_RIDING(10, new ArrayList<Technology>(Arrays.asList(THE_WHEEL)), "Horseback Riding"),
    @SerializedName("Enum models.technology.Technology Iron Working")
    IRON_WORKING(10, new ArrayList<Technology>(Arrays.asList(BRONZE_WORKING)), "Iron Working"),
    @SerializedName("Enum models.technology.Technology Mathematics")
    MATHEMATICS(10, new ArrayList<Technology>(Arrays.asList(THE_WHEEL, ARCHERY)), "Mathematics"),
    @SerializedName("Enum models.technology.Technology Philosophy")
    PHILOSOPHY(10, new ArrayList<Technology>(Arrays.asList(WRITING)), "Philosophy"),
    @SerializedName("Enum models.technology.Technology Civil Service")
    CIVIL_SERVICE(10, new ArrayList<Technology>(Arrays.asList(PHILOSOPHY, TRAPPING)), "Civil Service"),
    @SerializedName("Enum models.technology.Technology Currency")
    CURRENCY(10, new ArrayList<Technology>(Arrays.asList(MATHEMATICS)), "Currency"),
    @SerializedName("Enum models.technology.Technology Chivalry")
    CHIVALRY(10, new ArrayList<Technology>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)), "Chivalry"),
    @SerializedName("Enum models.technology.Technology Engineering")
    ENGINEERING(10, new ArrayList<Technology>(Arrays.asList(MATHEMATICS, CONSTRUCTION)), "Engineering"),
    @SerializedName("Enum models.technology.Technology Machinery")
    MACHINERY(10, new ArrayList<Technology>(Arrays.asList(ENGINEERING)), "Machinery"),
    @SerializedName("Enum models.technology.Technology Metal Casting")
    METAL_CASTING(10, new ArrayList<Technology>(Arrays.asList(IRON_WORKING)), "Metal Casting"),
    @SerializedName("Enum models.technology.Technology Physics")
    PHYSICS(10, new ArrayList<Technology>(Arrays.asList(ENGINEERING, METAL_CASTING)), "Physics"),
    @SerializedName("Enum models.technology.Technology Steel")
    STEEL(10, new ArrayList<Technology>(Arrays.asList(METAL_CASTING)), "Steel"),
    @SerializedName("Enum models.technology.Technology Theology")
    THEOLOGY(10, new ArrayList<Technology>(Arrays.asList(CALENDAR, PHILOSOPHY)), "Theology"),
    @SerializedName("Enum models.technology.Technology Education")
    EDUCATION(10, new ArrayList<Technology>(Arrays.asList(THEOLOGY)), "Education"),
    @SerializedName("Enum models.technology.Technology Acoustics")
    ACOUSTICS(10, new ArrayList<Technology>(Arrays.asList(EDUCATION)), "Acoustics"),
    @SerializedName("Enum models.technology.Technology Archaeology")
    ARCHAEOLOGY(10, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "Archaeology"),
    @SerializedName("Enum models.technology.Technology Banking")
    BANKING(10, new ArrayList<Technology>(Arrays.asList(EDUCATION, CHIVALRY)), "Banking"),
    @SerializedName("Enum models.technology.Technology Gunpowder")
    GUNPOWDER(10, new ArrayList<Technology>(Arrays.asList(PHYSICS, STEEL)), "Gunpowder"),
    @SerializedName("Enum models.technology.Technology Chemistry")
    CHEMISTRY(10, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "Chemistry"),
    @SerializedName("Enum models.technology.Technology Printing Press")
    PRINTING_PRESS(10, new ArrayList<Technology>(Arrays.asList(MACHINERY, PHYSICS)), "Printing Press"),
    @SerializedName("Enum models.technology.Technology Economics")
    ECONOMICS(10, new ArrayList<Technology>(Arrays.asList(BANKING, PRINTING_PRESS)), "Economics"),
    @SerializedName("Enum models.technology.Technology Fertilizer")
    FERTILIZER(10, new ArrayList<Technology>(Arrays.asList(CHEMISTRY)), "Fertilizer"),
    @SerializedName("Enum models.technology.Technology Metallurgy")
    METALLURGY(10, new ArrayList<Technology>(Arrays.asList(GUNPOWDER)), "Metallurgy"),
    @SerializedName("Enum models.technology.Technology Military Science")
    MILITARY_SCIENCE(10, new ArrayList<Technology>(Arrays.asList(ECONOMICS, CHEMISTRY)), "Military Science"),
    @SerializedName("Enum models.technology.Technology Rifling")
    RIFLING(10, new ArrayList<Technology>(Arrays.asList(METALLURGY)), "Rifling"),
    @SerializedName("Enum models.technology.Technology Scientific Theory")
    SCIENTIFIC_THEORY(10, new ArrayList<Technology>(Arrays.asList(ACOUSTICS)), "Scientific Theory"),
    @SerializedName("Enum models.technology.Technology Biology")
    BIOLOGY(10, new ArrayList<Technology>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)), "Biology"),
    @SerializedName("Enum models.technology.Technology Steam Powder")
    STEAM_POWER(10, new ArrayList<Technology>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)), "Steam Powder"),
    @SerializedName("Enum models.technology.Technology Dynamite")
    DYNAMITE(10, new ArrayList<Technology>(Arrays.asList(FERTILIZER, RIFLING)), "Dynamite"),
    @SerializedName("Enum models.technology.Technology Electricity")
    ELECTRICITY(10, new ArrayList<Technology>(Arrays.asList(BIOLOGY, STEAM_POWER)), "Electricity"),
    @SerializedName("Enum models.technology.Technology Radio")
    RADIO(10, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "Radio"),
    @SerializedName("Enum models.technology.Technology Railroad")
    RAILROAD(10, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "Railroad"),
    @SerializedName("Enum models.technology.Technology Replaceable Parts")
    REPLACEABLE_PARTS(10, new ArrayList<Technology>(Arrays.asList(STEAM_POWER)), "Replaceable Parts"),
    @SerializedName("Enum models.technology.Technology Combustion")
    COMBUSTION(10, new ArrayList<Technology>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)), "Combustion"),
    @SerializedName("Enum models.technology.Technology Telegraph")
    TELEGRAPH(10, new ArrayList<Technology>(Arrays.asList(ELECTRICITY)), "Telegraph");

    private static class Relations {
        public static HashMap<Technology, ArrayList<Technology>> forwardRelations = new HashMap<Technology, ArrayList<Technology>>();
        public static HashMap<Technology, ArrayList<Technology>> backwardRelations = new HashMap<Technology, ArrayList<Technology>>();
    }

    private int cost;
    private String name;
    private int grade;


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

        // set grade
        grade = 0;
        for (Technology prerequisiteTechnology : prerequisiteTechnologies) {
            if (grade <= prerequisiteTechnology.getGrade()) {
                grade = prerequisiteTechnology.getGrade() + 1;
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

    public String getName() {
        return this.name;
    }

    public int getGrade() {
        return grade;
    }

    public static Technology getTechnologyByName(String name) {
        for (Technology technology : values()) {
            if (technology.getName().equalsIgnoreCase(name)) {
                return technology;
            }
        }
        return null;
    }

    public static int findMaxGrade() {
        int result = 0;
        for (Technology value : values()) {
            if (value.getGrade() > result) {
                result = value.getGrade();
            }
        }
        return result;
    }

    public static int findMostPopulousGradesPopulation() {
        int maxPopulation = 0;
        for (int i = 0; i <= findMaxGrade(); i++) {
            int population = 0;
            for (Technology value : values()) {
                if (value.getGrade() == i) {
                    population++;
                }
            }
            if (population > maxPopulation) {
                maxPopulation = population;
            }
        }
        return maxPopulation;
    }
}