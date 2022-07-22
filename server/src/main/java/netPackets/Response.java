package netPackets;

import com.google.gson.Gson;
import models.GameMap;

public class Response {
    private String json;

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Response fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Response.class);
    }

    public String getJson() {
        return json;
    }
}
