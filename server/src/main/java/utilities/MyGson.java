package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.MPCostEnum;
import models.TileVisibility;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.*;
import models.resources.Resource;
import models.technology.Technology;
import models.units.CombatType;
import models.units.UnitState;
import models.units.UnitType;
import models.works.Work;
import netPackets.JsonDeserializerWithInheritance;

public class MyGson {
    private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(TerrainProperty.class, new JsonDeserializerWithInheritance<TerrainProperty>())
            .registerTypeAdapter(Resource.class, new JsonDeserializerWithInheritance<Resource>())
            .registerTypeAdapter(Building.class, new JsonDeserializerWithInheritance<Building>())
            .registerTypeAdapter(Selectable.class, new JsonDeserializerWithInheritance<Selectable>())
            .registerTypeAdapter(Producible.class, new JsonDeserializerWithInheritance<Producible>())
            .registerTypeAdapter(combative.class, new JsonDeserializerWithInheritance<combative>())
            .registerTypeAdapter(Work.class, new JsonDeserializerWithInheritance<Work>())
//            .registerTypeAdapter(BuildingType.class, new JsonDeserializerWithInheritance<BuildingType>())
//            .registerTypeAdapter(ImprovementType.class, new JsonDeserializerWithInheritance<ImprovementType>())
//            .registerTypeAdapter(Technology.class, new JsonDeserializerWithInheritance<Technology>())
            .registerTypeAdapter(TileImage.class, new JsonDeserializerWithInheritance<TileImage>())
//            .registerTypeAdapter(MPCostEnum.class, new JsonDeserializerWithInheritance<MPCostEnum>())
//            .registerTypeAdapter(UnitType.class, new JsonDeserializerWithInheritance<UnitType>())
//            .registerTypeAdapter(UnitState.class, new JsonDeserializerWithInheritance<UnitState>())
//            .registerTypeAdapter(CombatType.class, new JsonDeserializerWithInheritance<CombatType>())
//            .registerTypeAdapter(TileVisibility.class, new JsonDeserializerWithInheritance<TileVisibility>())
            .registerTypeAdapter(Producible.class, new JsonDeserializerWithInheritance<Producible>())
            .setPrettyPrinting()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }
}
