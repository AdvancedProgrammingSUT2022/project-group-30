package models.buildings;

import models.interfaces.Producible;
import models.interfaces.Workable;

public class Building implements Producible, Workable {
    private final BuildingType type;

    public Building(BuildingType type){
        this.type = type;
    }

    public Building createImage() { // create a deep copy of the object
        // TODO
        return null;
    }

    public BuildingType getType() {
        return type;
    }

}
