package models.resources;

import java.util.ArrayList;
import java.util.Arrays;

import models.Feature;
import models.Output;
import models.interfaces.TerrainProperty;
import models.TerrainType;
import models.improvements.ImprovementType;

public class LuxuryResource extends Resource {
        LuxuryResource COTTON = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND)), "Cotton");
        LuxuryResource DYE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(Feature.JUNGLE, Feature.FOREST)), "Dye");
        LuxuryResource FUR = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.CAMP,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.TUNDRA, Feature.FOREST)), "Fur");
        LuxuryResource GEM = new LuxuryResource(new Output(3, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(Feature.JUNGLE, TerrainType.GRASSLAND, TerrainType.PLAINS,
                                                        TerrainType.DESERT, TerrainType.TUNDRA, TerrainType.HILLS)), "Gem");
        LuxuryResource GOLD = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS,
                                        TerrainType.DESERT, TerrainType.HILLS)), "Gold");
        LuxuryResource INCENSE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT)), "Incense");
        LuxuryResource IVORY = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.CAMP,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS)), "Ivory");
        LuxuryResource MARBLE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.QUARRY,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS, TerrainType.DESERT,
                                                        TerrainType.TUNDRA, TerrainType.HILLS)), "Marble");
        LuxuryResource SILK = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST)), "Silk");
        LuxuryResource SILVER = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.TUNDRA, TerrainType.DESERT, TerrainType.HILLS)), "Silver");
        LuxuryResource SUGAR = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(Feature.FLOOD_PLAINS, Feature.MARSH, TerrainType.HILLS)), "Sugar");

        public LuxuryResource(Output output, ImprovementType prerequisiteImprovement,
                        ArrayList<TerrainProperty> allowedTerrains, String name) {
                super(output, prerequisiteImprovement, allowedTerrains, name);
        }
}
