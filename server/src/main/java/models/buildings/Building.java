package models.buildings;

import models.City;
import models.TerrainType;
import models.interfaces.Workable;

public class Building implements Workable {
    private final int id;
    public  int getId() {
        return id;
    }

    protected static int nextAvailableId = 0;

    private final BuildingType type;

    public Building(BuildingType type) {
        this.id = nextAvailableId;
        nextAvailableId++;
        this.type = type;
    }

    public Building createImage() { // create a deep copy of the object
        return null;
    }

    public BuildingType getType() {
        return type;
    }

    public static boolean isCityCompatibleWithBuildingType(City city, BuildingType type) {
        if (!((type.getPrerequisiteTechnology() == null) || city.getOwner().getTechnologies().getLearnedTechnologies().contains(type.getPrerequisiteTechnology())))
            return false;
        if (type == BuildingType.WATER_MILL) {
            if (city.isNearTheRiver())
                return true;
        } else if (type == BuildingType.ARMORY) {
            if (city.hasBuildingType(BuildingType.BARRACKS))
                return true;
        } else if (type == BuildingType.CIRCUS) {
            if (city.hasResourceByName("Horse") && city.hasResourceByName("Ivory"))
                return true;
        } else if (type == BuildingType.STABLE) {
            if (city.hasResourceByName("Horse"))
                return true;
        } else if (type == BuildingType.TEMPLE) {
            if (city.hasBuildingType(BuildingType.MONUMENT))
                return true;
        } else if (type == BuildingType.CASTLE) {
            if (city.hasBuildingType(BuildingType.WALLS))
                return true;
        } else if (type == BuildingType.FORGE) {
            if (city.hasResourceByName("Horse"))
                return true;
        } else if (type == BuildingType.GARDEN) {
            if (city.isNearTheRiver())
                return true;
        } else if (type == BuildingType.UNIVERSITY) {
            if (city.hasBuildingType(BuildingType.LIBRARY))
                return true;
        } else if (type == BuildingType.BANK) {
            if (city.hasBuildingType(BuildingType.MARKET))
                return true;
        } else if (type == BuildingType.MILITARY_ACADEMY) {
            if (city.hasBuildingType(BuildingType.BARRACKS))
                return true;
        } else if (type == BuildingType.MUSEUM) {
            if (city.hasBuildingType(BuildingType.OPERA_HOUSE))
                return true;
        } else if (type == BuildingType.OPERA_HOUSE) {
            if (city.hasBuildingType(BuildingType.TEMPLE) && city.hasBuildingType(BuildingType.BURIAL_TOMB))
                return true;
        } else if (type == BuildingType.PUBLIC_SCHOOL) {
            if (city.hasBuildingType(BuildingType.UNIVERSITY))
                return true;
        } else if (type == BuildingType.SATRAPS_COURT) {
            if (city.hasBuildingType(BuildingType.MARKET))
                return true;
        } else if (type == BuildingType.THEATER) {
            if (city.hasBuildingType(BuildingType.COLOSSEUM))
                return true;
        } else if (type == BuildingType.WINDMILL) {
            if (!(city.getCentralTile().getTerrainType() == TerrainType.HILLS))
                return true;
        } else if (type == BuildingType.ARSENAL) {
            if (city.hasBuildingType(BuildingType.MILITARY_ACADEMY))
                return true;
        } else if (type == BuildingType.BROADCAST_TOWER) {
            if (city.hasBuildingType(BuildingType.MUSEUM))
                return true;
        } else if (type == BuildingType.FACTORY) {
            if (city.hasResourceByName("Iron"))
                return true;
        } else if (type == BuildingType.MILITARY_BASE) {
            if (city.hasBuildingType(BuildingType.CASTLE))
                return true;
        } else if (type == BuildingType.STOCK_EXCHANGE) {
            if (city.hasBuildingType(BuildingType.BANK) || city.hasBuildingType(BuildingType.SATRAPS_COURT))
                return true;
        } else {//there is no more condition to check for this specific type
            return true;
        }
        return false;
    }
}
