package models;

import models.interfaces.MPCostInterface;

public class MPCostClass implements MPCostInterface{
    private int cost = 0;

    public MPCostClass(int cost){
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
