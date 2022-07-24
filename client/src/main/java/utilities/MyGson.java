package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.MPCostEnum;
import models.buildings.BuildingType;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;
import models.interfaces.TileImage;
import models.resources.Resource;
import models.technology.Technology;
import models.units.UnitState;
import models.units.UnitType;
import netPackets.JsonDeserializerWithInheritance;

public class MyGson {

    private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(TerrainProperty.class, new JsonDeserializerWithInheritance<TerrainProperty>())
            .registerTypeAdapter(Resource.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(BuildingType.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(ImprovementType.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(Technology.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(TileImage.class, new JsonDeserializerWithInheritance<TileImage>())
            .registerTypeAdapter(MPCostEnum.class, new JsonDeserializerWithInheritance<TileImage>())
            .registerTypeAdapter(UnitType.class, new JsonDeserializerWithInheritance<TileImage>())
            .registerTypeAdapter(UnitState.class, new JsonDeserializerWithInheritance<TileImage>())
            .setPrettyPrinting()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }
}
