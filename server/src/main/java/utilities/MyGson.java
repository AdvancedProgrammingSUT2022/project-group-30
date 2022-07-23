package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.interfaces.TerrainProperty;
import models.interfaces.TileImage;
import models.resources.Resource;
import netPackets.JsonDeserializerWithInheritance;

public class MyGson {

    private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(TerrainProperty.class, new JsonDeserializerWithInheritance<TerrainProperty>())
            .registerTypeAdapter(Resource.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(TileImage.class, new JsonDeserializerWithInheritance<TileImage>())
            .setPrettyPrinting()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }
}
