package netPackets;

import com.google.gson.Gson;
import models.GameMap;

public class Response {

    private GameMap gameMap;

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Response formJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Response.class);
    }

    public void setGameMap(GameMap gameMap){
        this.gameMap = gameMap;
    }

    public GameMap getGameMap(){
        return this.gameMap;
    }
}
