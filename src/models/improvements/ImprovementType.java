package models.improvements;

import java.util.ArrayList;
import java.util.Arrays;

import models.Output;
import models.Technology;
import models.TechnologyType;
import models.TerrainType;
import models.Tile;
import models.Feature;
import models.interfaces.TerrainProperty;

public enum ImprovementType {
    CAMP(new Output(0, 0, 0), TechnologyType.TRAPPING,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(Feature.FOREST, TerrainType.TUNDRA, TerrainType.HILLS, TerrainType.PLAINS))),
    FARM(new Output(0, 1, 0), TechnologyType.AGRICULTURE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.GRASSLAND, TerrainType.DESERT))),
    LUMBER_MILL(new Output(0, 0, 1), TechnologyType.ENGINEERING,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST))),
    MINE(new Output(0, 0, 1), TechnologyType.MINING, new ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.TUNDRA, TerrainType.SNOW, TerrainType.HILLS, Feature.FOREST, Feature.JUNGLE, Feature.OASIS))),
     PASTURE(new Output(0, 0, 0), TechnologyType.ANIMAL_HUSBANDRY, new
     ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.TUNDRA, TerrainType.HILLS))),
    PLANTATION(new Output(0, 0, 0), TechnologyType.CALENDAR, new
    ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, Feature.FOREST, Feature.JUNGLE, Feature.OASIS, Feature.MARSH))),
    QUARRY(new Output(0, 0, 0), TechnologyType.MASONRY, new
     ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.TUNDRA, TerrainType.HILLS))),
     TRADING_POST(new Output(2, 0, 0), TechnologyType.TRAPPING, new
     ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.TUNDRA))),
    FORT(new Output(0, 0, 2), TechnologyType.ENGINEERING, new
    ArrayList<TerrainProperty>(Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.TUNDRA, TerrainType.SNOW))),
   //??output of road & railroad
    ROAD(new Output(0, 0, 0), TechnologyType.THE_WHEEL, new
     ArrayList<TerrainProperty>(Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS, TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA, Feature.FLOOD_PLAINS, Feature.FOREST, Feature.JUNGLE, Feature.MARSH, Feature.OASIS))),
     RAILROAD(new Output(0, 0, 0), ??, new
     ArrayList<TerrainProperty>(Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS, TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA, Feature.FLOOD_PLAINS, Feature.FOREST, Feature.JUNGLE, Feature.MARSH, Feature.OASIS)));
   
     private Output output;
    private Technology prerequisiteTechnology;
    private ArrayList<TerrainProperty> terrainProperties;
    private boolean isPillaged;
    private double maintenanceCost;

    private ImprovementType(Output output, Technology prerequisiteTechnology,
            ArrayList<TerrainProperty> terrainProperties, double maintenanceCost) {
        this.output = output;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.terrainProperties = terrainProperties;
        this.isPillaged = false;
        this.maintenanceCost = maintenanceCost;
    }

}
