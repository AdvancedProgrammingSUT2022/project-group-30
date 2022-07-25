package models.buildings;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;
import models.interfaces.Producible;
import models.technology.Technology;

import java.util.ArrayList;

public enum BuildingType implements Producible, EnumInterface {
    @SerializedName("Enum models.buildings.BuildingType Barracks")
    BARRACKS(80, 1, Technology.BRONZE_WORKING, 0, "Barracks"),
    @SerializedName("Enum models.buildings.BuildingType Granary")
    GRANARY(100, 1, Technology.POTTERY, 0, "Granary"),
    @SerializedName("Enum models.buildings.BuildingType Library")
    LIBRARY(80, 1, Technology.WRITING, 0, "Library"),
    @SerializedName("Enum models.buildings.BuildingType Monument")
    MONUMENT(60, 1, Technology.AGRICULTURE, 0, "Monument"),
    @SerializedName("Enum models.buildings.BuildingType Walls")
    WALLS(100, 1, Technology.MASONRY, 0, "Walls"),
    @SerializedName("Enum models.buildings.BuildingType Water Mill")
    WATER_MILL(120, 2, Technology.THE_WHEEL, true, 0, "Water Mill"),
    @SerializedName("Enum models.buildings.BuildingType Armory")
    ARMORY(130, 3, Technology.IRON_WORKING, createArrayList(BARRACKS), 0, "Armory"),
    @SerializedName("Enum models.buildings.BuildingType Burial Tomb")
    BURIAL_TOMB(120, 0, Technology.PHILOSOPHY, 2, "Burial Tomb"),
    @SerializedName("Enum models.buildings.BuildingType Circus")
    CIRCUS(150, 3, Technology.HORSEBACK_RIDING, 3, "Circus"),
    @SerializedName("Enum models.buildings.BuildingType Colosseum")
    COLOSSEUM(150, 3, Technology.CONSTRUCTION, 4, "Colosseum"),
    @SerializedName("Enum models.buildings.BuildingType Courthouse")
    COURTHOUSE(200, 5, Technology.MATHEMATICS, 0, "Courthouse"),
    @SerializedName("Enum models.buildings.BuildingType Stable")
    STABLE(100, 1, Technology.HORSEBACK_RIDING, 0, "Stable"),
    @SerializedName("Enum models.buildings.BuildingType Temple")
    TEMPLE(120, 2, Technology.PHILOSOPHY, createArrayList(MONUMENT), 0, "Temple"),
    @SerializedName("Enum models.buildings.BuildingType Castle")
    CASTLE(200, 3, Technology.CHIVALRY, createArrayList(WALLS), 0, "Castle"),
    @SerializedName("Enum models.buildings.BuildingType Forge")
    FORGE(150, 2, Technology.METAL_CASTING, 0, "Forge"),
    @SerializedName("Enum models.buildings.BuildingType Garden")
    GARDEN(120, 2, Technology.THEOLOGY, true, 0, "Garden"),
    @SerializedName("Enum models.buildings.BuildingType Market")
    MARKET(120, 0, Technology.CURRENCY, 0, "Market"),
    @SerializedName("Enum models.buildings.BuildingType Mint")
    MINT(120, 0, Technology.CURRENCY, 0, "Mint"),
    @SerializedName("Enum models.buildings.BuildingType Monastery")
    MONASTERY(120, 2, Technology.THEOLOGY, 0, "Monastery"),
    @SerializedName("Enum models.buildings.BuildingType University")
    UNIVERSITY(200, 3, Technology.EDUCATION, createArrayList(LIBRARY), 0, "University"),
    @SerializedName("Enum models.buildings.BuildingType Workshop")
    WORKSHOP(100, 2, Technology.METAL_CASTING, 0, "Workshop"),
    @SerializedName("Enum models.buildings.BuildingType Bank")
    BANK(220, 0, Technology.BANKING, createArrayList(MARKET), 0, "Bank"),
    @SerializedName("Enum models.buildings.BuildingType Military Academy")
    MILITARY_ACADEMY(350, 3, Technology.MILITARY_SCIENCE, createArrayList(BARRACKS), 0, "Military Academy"),
    @SerializedName("Enum models.buildings.BuildingType Opera House")
    OPERA_HOUSE(220, 3, Technology.ACOUSTICS, createArrayList(TEMPLE, BURIAL_TOMB), 0, "Opera House"),
    @SerializedName("Enum models.buildings.BuildingType Museum")
    MUSEUM(350, 3, Technology.ARCHAEOLOGY, createArrayList(OPERA_HOUSE), 0, "Museum"),
    @SerializedName("Enum models.buildings.BuildingType Public School")
    PUBLIC_SCHOOL(350, 3, Technology.SCIENTIFIC_THEORY, createArrayList(UNIVERSITY), 0, "Public School"),
    @SerializedName("Enum models.buildings.BuildingType Satrap's Court")
    SATRAPS_COURT(220, 0, Technology.BANKING, createArrayList(MARKET), 2, "Satrap's Court"),
    @SerializedName("Enum models.buildings.BuildingType Theater")
    THEATER(300, 5, Technology.PRINTING_PRESS, createArrayList(COLOSSEUM), 4, "Theater"),
    @SerializedName("Enum models.buildings.BuildingType Windmill")
    WINDMILL(180, 2, Technology.ECONOMICS, 0, "Windmill"),
    @SerializedName("Enum models.buildings.BuildingType Arsenal")
    ARSENAL(350, 3, Technology.RAILROAD, createArrayList(MILITARY_ACADEMY), 0, "Arsenal"),
    @SerializedName("Enum models.buildings.BuildingType Broadcast Tower")
    BROADCAST_TOWER(600, 3, Technology.RADIO, createArrayList(MUSEUM), 0, "Broadcast Tower"),
    @SerializedName("Enum models.buildings.BuildingType Factory")
    FACTORY(300, 3, Technology.STEAM_POWER, 0, "Factory"),
    @SerializedName("Enum models.buildings.BuildingType Hospital")
    HOSPITAL(400, 2, Technology.BIOLOGY, 0, "Hospital"),
    @SerializedName("Enum models.buildings.BuildingType Military Base")
    MILITARY_BASE(450, 4, Technology.TELEGRAPH, createArrayList(CASTLE), 0, "Military Base"),
    @SerializedName("Enum models.buildings.BuildingType Stock Exchange")
    STOCK_EXCHANGE(650, 0, Technology.ELECTRICITY, 0, "Stock Exchange"),
    @SerializedName("Enum models.buildings.BuildingType Palace")
    PALACE(0, 0, Technology.AGRICULTURE, 0, "Palace");

    private final double cost;
    private final double maintenanceCost;
    private final Technology prerequisiteTechnology;
    private final double happiness;
    private final String name;
    private final ArrayList<BuildingType> prerequisiteBuildingTypes;
    private final boolean shouldBeNearRiver;

    @SerializedName("type")
    private String typeName;

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, double happiness, String name) {
        this.typeName = getClass().getName();
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
        this.typeName = getClass().getName();
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.prerequisiteBuildingTypes = prerequisiteBuildingType;
        this.shouldBeNearRiver = false;
        this.happiness = happiness;
        this.name = name;
    }

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, boolean shouldBeNearRiver,
                         double happiness, String name) {
        this.typeName = getClass().getName();
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.prerequisiteBuildingTypes = new ArrayList<>();
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

    @Override
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
