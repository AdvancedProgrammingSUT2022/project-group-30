package models;

import java.util.ArrayList;

public class City {
    private int hammerCount;
    private int foodCount;
    private ArrayList<Tile> territory;

    public void setHammerCount(int hammerCount){
        this.hammerCount = hammerCount;
    }

    public int getHammerCount(){
        return this.hammerCount;
    }

    public void setTerritory(ArrayList<Tile> territoty){
        this.territory = territoty;
    }

    public ArrayList<Tile> getTerritory(){
        return this.territory;
    }

    public void setFoodCount(int foodCount){
        this.foodCount = foodCount;
    }

    public int getFoodCount(){
        return this.foodCount;
    }
    
}
