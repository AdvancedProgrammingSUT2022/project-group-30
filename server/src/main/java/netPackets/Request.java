package netPackets;

import com.google.gson.Gson;
import models.GameMap;

import java.util.ArrayList;
import java.util.Arrays;

public class Request {


    private String methodName;
    private ArrayList<String> arguments;

    public Request(String methodName, String... arguments){
        this.methodName = methodName;
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Request fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Request.class);
    }

    public String getMethodName(){
        return this.methodName;
    }

    public ArrayList<String> getArguments(){
        return this.arguments;
    }

}
