package models.buildings;

import models.Feature;
import models.Output;
import models.interfaces.Producible;
import models.resources.Resource;
import models.technology.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

public enum BuildingType implements Producible {
    BARRACKS(80, 1, Technology.BRONZE_WORKING, 0, "Barracks"),
    GRANARY(100, 1, Technology.POTTERY, 0, "Granary"),
    LIBRARY(80, 1, Technology.WRITING, 0, "Library"),
    MONUMENT(60, 1, null, 0, "Monument"),
    WALLS(100, 1, Technology.MASONRY, 0, "Walls"),
    WATER_MILL(120, 2, Technology.THE_WHEEL, 0, "Water Mill"),
    ARMORY(130, 3, Technology.IRON_WORKING, 0, "Armory"),
    BURIAL_TOMB(120, 0, Technology.PHILOSOPHY, 2, "Burial Tomb"),
    CIRCUS(150, 3, Technology.HORSEBACK_RIDING, 3, "Circus"),
    COLOSSEUM(150, 3, Technology.CONSTRUCTION, 4, "Colosseum"),
    COURTHOUSE(200, 5, Technology.MATHEMATICS, 0, "Courthouse"),
    STABLE(100, 1, Technology.HORSEBACK_RIDING, 0, "Stable"),
    TEMPLE(120, 2, Technology.PHILOSOPHY, 0, "Temple"),
    CASTLE(200, 3, Technology.CHIVALRY, 0, "Castle"),
    FORGE(150, 2, Technology.METAL_CASTING, 0, "Forge"),
    GARDEN(120, 2, Technology.THEOLOGY, 0, "Garden"),
    MARKET(120, 0, Technology.CURRENCY, 0, "Market"),
    MINT(120, 0, Technology.CURRENCY, 0, "Mint"),
    MONASTERY(120, 2, Technology.THEOLOGY, 0, "Monastery"),
    UNIVERSITY(200, 3, Technology.EDUCATION, 0, "University"),
    WORKSHOP(100, 2, Technology.METAL_CASTING, 0, "Workshop"),
    BANK(220, 0, Technology.BANKING, 0, "Bank"),
    MILITARY_ACADEMY(350, 3, Technology.MILITARY_SCIENCE, 0, "Military Academy"),
    MUSEUM(350, 3, Technology.ARCHAEOLOGY, 0, "Museum"),
    OPERA_HOUSE(220, 3, Technology.ACOUSTICS, 0, "Opera House"),
    PUBLIC_SCHOOL(350, 3, Technology.SCIENTIFIC_THEORY, 0, "Public School"),
    SATRAPS_COURT(220, 0, Technology.BANKING, 2, "Satrap's Court"),
    THEATER(300, 5, Technology.PRINTING_PRESS, 4, "Theater"),
    WINDMILL(180, 2, Technology.ECONOMICS, 0, "Windmill"),
    ARSENAL(350, 3, Technology.RAILROAD, 0, "Arsenal"),
    BROADCAST_TOWER(600, 3, Technology.RADIO, 0, "Broadcast Tower"),
    FACTORY(300, 3, Technology.STEAM_POWER, 0, "Factory"),
    HOSPITAL(400, 2, Technology.BIOLOGY, 0, "Hospital"),
    MILITARY_BASE(450, 4, Technology.TELEGRAPH, 0, "Military Base"),
    STOCK_EXCHANGE(650, 0, Technology.ELECTRICITY, 0, "Stock Exchange"),
    PALACE(0, 0, Technology.AGRICULTURE, 0, "Palace");

    private final double cost;
    private final double maintenanceCost;
    private final Technology prerequisiteTechnology;
    private final double happiness;
    private final String name;

    private BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, double happiness, String name) {
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
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
}
