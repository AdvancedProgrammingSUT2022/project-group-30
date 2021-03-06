package models;

import models.interfaces.Workable;

public class Citizen {
    private final int id;
    public  int getId() {
        return id;
    }

    private static int nextAvailableId = 0;

    private Workable workPlace;

    public Citizen() {
        this.id = nextAvailableId;
        nextAvailableId++;
        workPlace = null;
    }

    public Citizen(Workable workPlace) {
        this.id = nextAvailableId;
        nextAvailableId++;
        this.workPlace = workPlace;
    }

    public Workable getWorkPlace() {
        return workPlace;
    }

    public boolean isWorkless() {
        if (workPlace == null)
            return true;
        return false;
    }

    public void setWorkPlace(Workable workPlace) {
        this.workPlace = workPlace;
    }

}
