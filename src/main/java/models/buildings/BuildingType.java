package models.buildings;

import models.interfaces.Producible;
import models.technology.Technology;

import java.util.ArrayList;

public enum BuildingType implements Producible {
    BARRACKS(80, 1, Technology.BRONZE_WORKING, 0, "Barracks"),
    GRANARY(100, 1, Technology.POTTERY, 0, "Granary"),
    LIBRARY(80, 1, Technology.WRITING, 0, "Library"),
    MONUMENT(60, 1, null, 0, "Monument"),
    WALLS(100, 1, Technology.MASONRY, 0, "Walls"),
    WATER_MILL(120, 2, Technology.THE_WHEEL, true,0, "Water Mill"),
    ARMORY(130, 3, Technology.IRON_WORKING, createArrayList(BARRACKS), 0, "Armory"),
    BURIAL_TOMB(120, 0, Technology.PHILOSOPHY, 2, "Burial Tomb"),
    CIRCUS(150, 3, Technology.HORSEBACK_RIDING, 3, "Circus"),
    COLOSSEUM(150, 3, Technology.CONSTRUCTION, 4, "Colosseum"),
    COURTHOUSE(200, 5, Technology.MATHEMATICS, 0, "Courthouse"),
    STABLE(100, 1, Technology.HORSEBACK_RIDING, 0, "Stable"),
    TEMPLE(120, 2, Technology.PHILOSOPHY, createArrayList(MONUMENT), 0, "Temple"),
    CASTLE(200, 3, Technology.CHIVALRY, createArrayList(WALLS), 0, "Castle"),
    FORGE(150, 2, Technology.METAL_CASTING, 0, "Forge"),
    GARDEN(120, 2, Technology.THEOLOGY, true, 0, "Garden"),
    MARKET(120, 0, Technology.CURRENCY, 0, "Market"),
    MINT(120, 0, Technology.CURRENCY, 0, "Mint"),
    MONASTERY(120, 2, Technology.THEOLOGY, 0, "Monastery"),
    UNIVERSITY(200, 3, Technology.EDUCATION, createArrayList(LIBRARY), 0, "University"),
    WORKSHOP(100, 2, Technology.METAL_CASTING, 0, "Workshop"),
    BANK(220, 0, Technology.BANKING, createArrayList(MARKET), 0, "Bank"),
    MILITARY_ACADEMY(350, 3, Technology.MILITARY_SCIENCE, createArrayList(BARRACKS), 0, "Military Academy"),
    OPERA_HOUSE(220, 3, Technology.ACOUSTICS, createArrayList(TEMPLE, BURIAL_TOMB),0, "Opera House"),
    MUSEUM(350, 3, Technology.ARCHAEOLOGY, createArrayList(OPERA_HOUSE), 0, "Museum"),
    PUBLIC_SCHOOL(350, 3, Technology.SCIENTIFIC_THEORY, createArrayList(UNIVERSITY), 0, "Public School"),
    SATRAPS_COURT(220, 0, Technology.BANKING, createArrayList(MARKET), 2, "Satrap's Court"),
    THEATER(300, 5, Technology.PRINTING_PRESS, createArrayList(COLOSSEUM), 4, "Theater"),
    WINDMILL(180, 2, Technology.ECONOMICS, 0, "Windmill"),
    ARSENAL(350, 3, Technology.RAILROAD, createArrayList(MILITARY_ACADEMY), 0, "Arsenal"),
    BROADCAST_TOWER(600, 3, Technology.RADIO, createArrayList(MUSEUM), 0, "Broadcast Tower"),
    FACTORY(300, 3, Technology.STEAM_POWER, 0, "Factory"),
    HOSPITAL(400, 2, Technology.BIOLOGY, 0, "Hospital"),
    MILITARY_BASE(450, 4, Technology.TELEGRAPH, createArrayList(CASTLE), 0, "Military Base"),
    STOCK_EXCHANGE(650, 0, Technology.ELECTRICITY, 0, "Stock Exchange"),
    PALACE(0, 0, Technology.AGRICULTURE, 0, "Palace");

    private final double cost;
    private final double maintenanceCost;
    private final Technology prerequisiteTechnology;
    private final double happiness;
    private final String name;
    private final ArrayList<BuildingType> prerequisiteBuildingTypes;
    private final boolean shouldBeNearRiver;

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, double happiness, String name) {
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.prerequisiteBuildingTypes = new ArrayList<>();
        this.shouldBeNearRiver = false;
        this.happiness = happiness;
        this.name = name;
    }

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, ArrayList<BuildingType> prerequisiteBuildingType,
                         double happiness, String name) {
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.prerequisiteBuildingTypes = prerequisiteBuildingType;
        this.shouldBeNearRiver = false;
        this.happiness = happiness;
        this.name = name;
    }

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology,  boolean shouldBeNearRiver,
                         double happiness, String name) {
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.prerequisiteBuildingTypes = null;
        this.shouldBeNearRiver = false;
        this.happiness = happiness;
        this.name = name;
    }

    public int getCost() {
        return (int) cost;
    }

    public Technology getPrerequisiteTechnology() {
        return prerequisiteTechnology;
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    public double getHappiness() {
        return happiness;
    }

    public String getName() {
        return name;
    }

    public int calculateHammerCost() {
        return ((int) cost / 10);
    }

    public boolean shouldBeNearRiver() {
        return shouldBeNearRiver;
    }

    public ArrayList<BuildingType> getPrerequisiteBuildingTypes() {
        return new ArrayList<>(prerequisiteBuildingTypes);
    }

    private static ArrayList<BuildingType> createArrayList(BuildingType... types) {
        ArrayList<BuildingType> result = new ArrayList<>();
        for (BuildingType type : types) {
            result.add(type);
        }
        return result;
    }
}
