package netPackets;

import com.google.gson.Gson;
import models.GameMap;
import utilities.MyGson;

public class Response {
    private String json;

    public Response(String json){
        this.json = json;
    }

    public String toJson(){
        Gson gson = MyGson.getGson();
        return gson.toJson(this);
    }

    public static Response fromJson(String json){
        Gson gson = MyGson.getGson();
        return gson.fromJson(json, Response.class);
    }

    public String getJson() {
        return json;
    }
}
