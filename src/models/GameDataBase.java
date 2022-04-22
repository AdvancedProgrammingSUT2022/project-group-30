package models;

import java.util.ArrayList;

public class GameDataBase {
    private static GameDataBase gameDataBase = null;
    private ArrayList<City> cities;
    private GameMap map = GameMap.getGameMap();
    
    private GameDataBase()  {
        map = GameMap.getGameMap();
    }

    public static GameDataBase getGameDataBase()  {
        if(gameDataBase == null)
            gameDataBase = new GameDataBase();
        return gameDataBase;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<City> getCities(){
        return this.cities;
    }

    public GameMap getMap(){
        return this.map;
    }
}
