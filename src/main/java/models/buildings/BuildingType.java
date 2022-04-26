package models.buildings;

import models.Feature;
import models.Output;
import models.resources.Resource;
import models.technology.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

public enum BuildingType {
    BARRACKS(80, 1, Technology.BRONZE_WORKING, 0),
    GRANARY(100, 1 ,Technology.POTTERY, 0),
    LIBRARY(80, 1, Technology.WRITING, 0),
    MONUMENT(60, 1, null, 0),
    WALLS(100, 1, Technology.MASONRY, 0),
    WATER_MILL(120, 2, Technology.THE_WHEEL, 0),
    ARMORY(130, 3, Technology.IRON_WORKING, 0),
    BURIAL_TOMB(120, 0, Technology.PHILOSOPHY,2),
    CIRCUS(150, 3, Technology.HORSEBACK_RIDING, 3),
    COLOSSEUM(150, 3, Technology.CONSTRUCTION, 4),
    COURTHOUSE(200, 5, Technology.MATHEMATICS, 0),
    STABLE(100, 1, Technology.HORSEBACK_RIDING, 0),
    TEMPLE(120, 2, Technology.PHILOSOPHY, 0),
    CASTLE(200, 3, Technology.CHIVALRY, 0),
    FORGE(150, 2, Technology.METAL_CASTING, 0),
    GARDEN(120 ,2, Technology.THEOLOGY, 0),
    MARKET(120, 0 , Technology.CURRENCY, 0),
    MINT(120, 0, Technology.CURRENCY, 0),
    MONASTERY(120 , 2, Technology.THEOLOGY, 0),
    UNIVERSITY(200, 3, Technology.EDUCATION, 0),
    WORKSHOP(100, 2, Technology.METAL_CASTING, 0),
    BANK(220, 0, Technology.BANKING, 0),
    MILITARY_ACADEMY(350, 3, Technology.MILITARY_SCIENCE, 0),
    MUSEUM(350, 3, Technology.ARCHAEOLOGY, 0),
    OPERA_HOUSE(220, 3, Technology.ACOUSTICS, 0),
    PUBLIC_SCHOOL(350, 3, Technology.SCIENTIFIC_THEORY, 0),
    SATRAPS_COURT(220, 0 , Technology.BANKING, 2),
    THEATER(300, 5, Technology.PRINTING_PRESS, 4),
    WINDMILL(180, 2, Technology.ECONOMICS, 0),
    ARSENAL(350, 3, Technology.RAILROAD, 0),
    BROADCAST_TOWER(600, 3, Technology.RADIO, 0),
    FACTORY(300, 3, Technology.STEAM_POWER, 0),
    HOSPITAL(400, 2, Technology.BIOLOGY, 0),
    MILITARY_BASE(450, 4, Technology.TELEGRAPH, 0),
    STOCK_EXCHANGE(650, 0, Technology.ELECTRICITY, 0);

    private final double cost;
    private final double maintenanceCost;
    private final Technology prerequisiteTechnology;
    private final double happiness;

    public BuildingType(double cost, double maintenanceCost, Technology prerequisiteTechnology, double happiness){
        this.cost = cost;
        this.maintenanceCost = maintenanceCost;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.happiness = happiness;
    }

    public double getCost() {
        return cost;
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
}
