package models;

import java.util.ArrayList;

import models.interfaces.Selectable;

public class City implements Selectable{
    private int hammerCount;
    private int foodCount;
    private ArrayList<Tile> territories;

    public void setHammerCount(int hammerCount){
        this.hammerCount = hammerCount;
    }

    public int getHammerCount(){
        return this.hammerCount;
    }

    public void setTerritory(ArrayList<Tile> territoties){
        this.territories = territoties;
    }

    public ArrayList<Tile> getTerritories(){
        return this.territories;
    }

    public void setFoodCount(int foodCount){
        this.foodCount = foodCount;
    }

    public int getFoodCount(){
        return this.foodCount;
    }
    
}
