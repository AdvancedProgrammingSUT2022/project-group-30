package models;

import models.interfaces.Workable;

public class Citizen {
    private Workable workPlace;

    public Citizen() {
        workPlace = null;
    }

    public Citizen(Workable workPlace) {
        this.workPlace = workPlace;
    }

    public Workable getWorkPlace() {
        return workPlace;
    }

    public boolean isWorkless(){
        if(workPlace == null)
            return false;
        return true;
    }

    public void setWorkPlace(Workable workPlace) {
        this.workPlace = workPlace;
    }

}
