package models.resources;

import models.Feature;
import models.Output;
import models.TerrainType;
import models.improvements.ImprovementType;
import models.interfaces.TerrainProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class BonusResource extends Resource {
    private static ArrayList<BonusResource> allTypes = new ArrayList<>();

    public static BonusResource BANANA = new BonusResource(new Output(0, 1, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.JUNGLE)), "Banana");

    public static BonusResource COW = new BonusResource(new Output(0, 1, 0),
            ImprovementType.PASTURE,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.GRASSLAND)), "Cow");

    public static BonusResource GAZELLE = new BonusResource(new Output(0, 1, 0),
            ImprovementType.CAMP,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.TUNDRA, TerrainType.HILLS, Feature.FOREST)), "Gazelle");

    public static BonusResource SHEEP = new BonusResource(new Output(0, 1, 0),
            ImprovementType.PASTURE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.GRASSLAND, TerrainType.DESERT,
                            TerrainType.HILLS)), "Sheep");

    public static BonusResource WHEAT = new BonusResource(new Output(0, 1, 0),
            ImprovementType.PASTURE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, Feature.FLOOD_PLAINS)), "Wheat");

    public BonusResource(Output output, ImprovementType prerequisiteImprovement,
                         ArrayList<TerrainProperty> allowedTerrains, String name) {
        super(output, prerequisiteImprovement, allowedTerrains, name);
        if (allTypes == null) {
            allTypes = new ArrayList<>();
        }
        allTypes.add(this);
    }

    public static ArrayList<BonusResource> getAllTypes() {
        return new ArrayList<BonusResource>(allTypes);
    }

    public static BonusResource getBonusResourceByName(String name) {
        for (BonusResource allType : allTypes) {
            if (allType.getName().equalsIgnoreCase(name)) {
                return allType;
            }
        }
        return null;
    }
}
