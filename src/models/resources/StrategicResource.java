package models.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import models.Civilization;
import models.ImprovementType;
import models.Output;
import models.Technology;
import models.TerrainProperty;
import models.TerrainType;

public class StrategicResource extends Resource {
    private Technology prerequisiteTechnology;

    public static StrategicResource COAL = new StrategicResource(new Output(0, 0, 1),
            ImprovementType.MINE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.HILLS, TerrainType.GRASSLAND)),
            Technology.SCIENTIFIC_THEORY);
    public static StrategicResource HORSE = new StrategicResource(new Output(0, 0, 1),
            ImprovementType.PASTURE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.TUNDRA, TerrainType.PLAINS, TerrainType.GRASSLAND)),
            Technology.ANIMAL_HUSBANDRY);
    public static StrategicResource IRON = new StrategicResource(new Output(0, 0, 1),
            ImprovementType.MINE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.TUNDRA, TerrainType.PLAINS, TerrainType.GRASSLAND, TerrainType.DESERT,
                            TerrainType.SNOW, TerrainType.HILLS)),
            Technology.IRON_WORKING);

    public StrategicResource(Output output, ImprovementType prerequisiteImprovement,
            ArrayList<TerrainProperty> allowedTerrains, Technology prerequisTechnology) {
        super(output, prerequisiteImprovement, allowedTerrains);
        this.prerequisiteTechnology = prerequisTechnology;
    }

    @Override
    public boolean isDiscoverable(Civilization civilization) {
        if (civilization.hasTechnology(prerequisiteTechnology)) {
            return true;
        }
        return false;
    }

    public static HashMap<StrategicResource, Integer> makeRawHashMap() {
        HashMap<StrategicResource, Integer> result = new HashMap<StrategicResource, Integer>();
        result.put(COAL, 0);
        result.put(HORSE, 0);
        result.put(IRON, 0);
        return result;
    }
}
