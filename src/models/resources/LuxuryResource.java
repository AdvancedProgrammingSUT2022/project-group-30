package models.resources;

import java.util.ArrayList;
import java.util.Arrays;

import models.Feature;
import models.Output;
import models.interfaces.TerrainProperty;
import models.TerrainType;
import models.improvements.ImprovementType;

public class LuxuryResource extends Resource {
        public static LuxuryResource COTTON = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND)), "Cotton");
        public static LuxuryResource DYE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(Feature.JUNGLE, Feature.FOREST)), "Dye");
        public static LuxuryResource FUR = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.CAMP,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.TUNDRA, Feature.FOREST)), "Fur");
        public static LuxuryResource GEM = new LuxuryResource(new Output(3, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(Feature.JUNGLE, TerrainType.GRASSLAND, TerrainType.PLAINS,
                                                        TerrainType.DESERT, TerrainType.TUNDRA, TerrainType.HILLS)), "Gem");
        public static LuxuryResource GOLD = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS,
                                        TerrainType.DESERT, TerrainType.HILLS)), "Gold");
        public static LuxuryResource INCENSE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT)), "Incense");
        public static LuxuryResource IVORY = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.CAMP,
                        new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS)), "Ivory");
        public static LuxuryResource MARBLE = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.QUARRY,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.GRASSLAND, TerrainType.PLAINS, TerrainType.DESERT,
                                                        TerrainType.TUNDRA, TerrainType.HILLS)), "Marble");
        public static LuxuryResource SILK = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST)), "Silk");
        public static LuxuryResource SILVER = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.MINE,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.TUNDRA, TerrainType.DESERT, TerrainType.HILLS)), "Silver");
        public static LuxuryResource SUGAR = new LuxuryResource(new Output(2, 0, 0),
                        ImprovementType.PLANTATION,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(Feature.FLOOD_PLAINS, Feature.MARSH, TerrainType.HILLS)), "Sugar");

        public LuxuryResource(Output output, ImprovementType prerequisiteImprovement,
                        ArrayList<TerrainProperty> allowedTerrains, String name) {
                super(output, prerequisiteImprovement, allowedTerrains, name);
        }
}
