package models.improvements;

import models.Feature;
import models.Output;
import models.TerrainType;
import models.Tile;
import models.interfaces.TerrainProperty;
import models.technology.Technology;

import java.util.ArrayList;
import java.util.Arrays;

public enum ImprovementType {
    CAMP(new Output(0, 0, 0), Technology.TRAPPING,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(Feature.FOREST, TerrainType.TUNDRA, TerrainType.HILLS,
                            TerrainType.PLAINS)),
            0, "Camp", 4),
    FARM(new Output(0, 1, 0), Technology.AGRICULTURE,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.GRASSLAND, TerrainType.DESERT)),
            0, "Farm", 6),
    LUMBER_MILL(new Output(0, 0, 1), Technology.ENGINEERING,
            new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST)), 0, "Lumber Mill", 5),
    MINE(new Output(0, 0, 1), Technology.MINING,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            TerrainType.TUNDRA,
                            TerrainType.SNOW, TerrainType.HILLS, Feature.FOREST,
                            Feature.JUNGLE, Feature.OASIS)),
            0, "Mine", 6),
    PASTURE(new Output(0, 0, 0), Technology.ANIMAL_HUSBANDRY,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            TerrainType.TUNDRA, TerrainType.HILLS)),
            0, "Pasture", 6),
    PLANTATION(new Output(0, 0, 0), Technology.CALENDAR,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            Feature.FOREST, Feature.JUNGLE, Feature.OASIS, Feature.MARSH)),
            0, "Plantation", 5),
    QUARRY(new Output(0, 0, 0), Technology.MASONRY,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            TerrainType.TUNDRA, TerrainType.HILLS)),
            0, "Quarry", 6),
    TRADING_POST(new Output(2, 0, 0), Technology.TRAPPING,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            TerrainType.TUNDRA)),
            0, "Trading Post", 6),
    MANUFACTORY(new Output(0, 0, 2), Technology.ENGINEERING,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                            TerrainType.TUNDRA, TerrainType.SNOW)),
            0, "Manufactory", 7),
    ROAD(new Output(0, 0, 0), Technology.THE_WHEEL,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS,
                            TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA,
                            Feature.FLOOD_PLAINS, Feature.FOREST,
                            Feature.JUNGLE, Feature.MARSH, Feature.OASIS)),
            0, "Road", 3),
    RAILROAD(new Output(0, 0, 0), Technology.RAILROAD,
            new ArrayList<TerrainProperty>(
                    Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS,
                            TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA,
                            Feature.FLOOD_PLAINS, Feature.FOREST,
                            Feature.JUNGLE, Feature.MARSH, Feature.OASIS)),
            0, "Rail Road", 3);

    public static final int MAINTENANCE_COST_OF_ROAD_AND_RAILROAD = 1;
    private final Output output;
    private final Technology prerequisiteTechnology;
    private final ArrayList<TerrainProperty> terrainProperties;
    private final double maintenanceCost;
    private final String name;
    private final int constructionDuration;

    private ImprovementType(Output output, Technology prerequisiteTechnology,
                            ArrayList<TerrainProperty> terrainProperties, double maintenanceCost, String name, int constructionDuration) {
        this.output = output;
        this.prerequisiteTechnology = prerequisiteTechnology;
        this.terrainProperties = terrainProperties;
        this.maintenanceCost = maintenanceCost;
        this.name = name;
        this.constructionDuration = constructionDuration;
    }

    public Output getOutput() {
        return this.output;
    }

    public Technology getPrerequisiteTechnology() {
        return this.prerequisiteTechnology;
    }

    public ArrayList<TerrainProperty> getTerrainProperties() {
        return this.terrainProperties;
    }

    public boolean isCompatibleWithTile(Tile tile) {
        if (terrainProperties.contains(tile.getTerrainType())) {
            return true;
        }
        for (Feature feature : tile.getFeatures()) {
            if (terrainProperties.contains(feature)) {
                return true;
            }
        }
        return false;
    }

    public double getMaintenanceCost() {
        return this.maintenanceCost;
    }

    public String getName() {
        return name;
    }

    public static ImprovementType getImprovementTypeByName(String name) {
        for (ImprovementType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public int getConstructionDuration() {
        return constructionDuration;
    }
}
