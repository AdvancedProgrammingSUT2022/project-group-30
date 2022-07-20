package utilities;

import com.google.gson.Gson;

public class MyGson {

    private static Gson gson = new Gson();

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }
}
