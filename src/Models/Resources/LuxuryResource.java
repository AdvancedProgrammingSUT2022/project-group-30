package models.resources;

import java.util.ArrayList;
import java.util.Arrays;

import models.Feature;
import models.ImprovementType;
import models.Output;
import models.TerrainProperty;
import models.TerrainType;

public class LuxuryResource extends Resource {
    LuxuryResource COTTON = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND)));
    LuxuryResource DYE = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.JUNGLE, Feature.FOREST)));
    LuxuryResource FUR = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.CAMP,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.TUNDRA, Feature.FOREST)));
    LuxuryResource GEM = new LuxuryResource(new Output(3, 0, 0),
            ImprovementType.MINE,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.JUNGLE, TerrainType.GRASSLAND, TerrainType.PLAINS,
                    TerrainType.DESERT, TerrainType.TUNDRA, TerrainType.HILLS)));
    LuxuryResource GOLD = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.MINE,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS,
                    TerrainType.DESERT, TerrainType.HILLS)));
    LuxuryResource INCENSE = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT)));
    LuxuryResource IVORY = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.CAMP,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS)));
    LuxuryResource MARBLE = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.QUARRY,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS, TerrainType.DESERT,
                    TerrainType.TUNDRA, TerrainType.HILLS)));
    LuxuryResource SILK = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST)));
    LuxuryResource SILVER = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.MINE,
            new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.TUNDRA, TerrainType.DESERT, TerrainType.HILLS)));
    LuxuryResource SUGAR = new LuxuryResource(new Output(2, 0, 0),
            ImprovementType.PLANTATION,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.FLOODPLAINS, Feature.MARSH, TerrainType.HILLS)));

            
    public LuxuryResource(Output output, ImprovementType prerequisiteImprovement,
            ArrayList<TerrainProperty> allowedTerrains) {
        super(output, prerequisiteImprovement, allowedTerrains);
    }
}
