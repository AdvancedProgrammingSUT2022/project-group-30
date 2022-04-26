package models.buildings;

import models.City;
import models.Tile;
import models.interfaces.Producible;
import models.interfaces.Workable;
import models.resources.Resource;

public class Building implements Producible, Workable {
    private final BuildingType type;

    public Building(BuildingType type) {
        this.type = type;
    }

    public Building createImage() { // create a deep copy of the object
        // TODO
        return null;
    }

    public BuildingType getType() {
        return type;
    }


    public static boolean isTileCompatibleWithBuildingType(City city, BuildingType type) {
        if (type == BuildingType.WATER_MILL) {
            if (city.isNearTheRiver())
                return true;
        } else if (type == BuildingType.ARMORY) {
            if (city.hasBuildingType(BuildingType.BARRACKS))
                return true;
        } else if (type == BuildingType.CIRCUS) {
            boolean hasHorse = false;
            boolean hasIvory = false;
            for (Tile tile : city.getTerritories()) {
                //MINETODO tell me more about resources Mr.Kooshky
                for (Resource resource : tile.getResources().keySet()) {
                    if (resource.getName().equals("Horse"))
                        hasHorse = true;
                    if (resource.getName().equals("Ivory"))
                        hasIvory = true;
                }
            }
            if (hasHorse && hasIvory)
                return true;
        } else if (type == BuildingType.STABLE) {
            for (Tile tile : city.getTerritories()) {
                for (Resource resource : tile.getResources().keySet()) {
                    if (resource.getName().equals("Horse"))
                        return true;
                }
            }
        } else if (type == BuildingType.TEMPLE) {
            if(city.hasBuildingType(BuildingType.MONUMENT))
                return true;
        }else if(type == )
        else if (true) {
            return true;
        }
        return false;
    }
}
