package models.improvements;

import java.util.ArrayList;
import java.util.Arrays;

import models.Output;
import models.technology.Technology;
import models.TerrainType;
import models.Feature;
import models.interfaces.TerrainProperty;

public enum ImprovementType {
        CAMP(new Output(0, 0, 0), Technology.TRAPPING,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(Feature.FOREST, TerrainType.TUNDRA, TerrainType.HILLS,
                                                        TerrainType.PLAINS)),
                        0, "Camp"),
        FARM(new Output(0, 1, 0), Technology.AGRICULTURE,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.GRASSLAND, TerrainType.DESERT)),
                        0, "Farm"),
        LUMBER_MILL(new Output(0, 0, 1), Technology.ENGINEERING,
                        new ArrayList<TerrainProperty>(Arrays.asList(Feature.FOREST)), 0, "Lumber Mill"),
        MINE(new Output(0, 0, 1), Technology.MINING,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        TerrainType.TUNDRA,
                                                        TerrainType.SNOW, TerrainType.HILLS, Feature.FOREST,
                                                        Feature.JUNGLE, Feature.OASIS)),
                        0, "Mine"),
        PASTURE(new Output(0, 0, 0), Technology.ANIMAL_HUSBANDRY,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        TerrainType.TUNDRA, TerrainType.HILLS)),
                        0, "Pasture"),
        PLANTATION(new Output(0, 0, 0), Technology.CALENDAR,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        Feature.FOREST, Feature.JUNGLE, Feature.OASIS, Feature.MARSH)),
                        0, "Plantation"),
        QUARRY(new Output(0, 0, 0), Technology.MASONRY,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        TerrainType.TUNDRA, TerrainType.HILLS)),
                        0, "Quarry"),
        TRADING_POST(new Output(2, 0, 0), Technology.TRAPPING,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        TerrainType.TUNDRA)),
                        0, "Trading Post"),
        FORT(new Output(0, 0, 2), Technology.ENGINEERING,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.PLAINS, TerrainType.DESERT, TerrainType.GRASSLAND,
                                                        TerrainType.TUNDRA, TerrainType.SNOW)),
                        0, "Fort"),
        ROAD(new Output(0, 0, 0), Technology.THE_WHEEL,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS,
                                                        TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA,
                                                        Feature.FLOOD_PLAINS, Feature.FOREST,
                                                        Feature.JUNGLE, Feature.MARSH, Feature.OASIS)),
                        0, "Road"),
        RAILROAD(new Output(0, 0, 0), Technology.RAILROAD,
                        new ArrayList<TerrainProperty>(
                                        Arrays.asList(TerrainType.DESERT, TerrainType.GRASSLAND, TerrainType.HILLS,
                                                        TerrainType.PLAINS, TerrainType.SNOW, TerrainType.TUNDRA,
                                                        Feature.FLOOD_PLAINS, Feature.FOREST,
                                                        Feature.JUNGLE, Feature.MARSH, Feature.OASIS)),
                        0, "Rail Road");

        public static final int MAINTENANCE_COST_OF_ROAD_AND_RAILROAD = 10;
        private final Output output;
        private final Technology prerequisiteTechnology;
        private final ArrayList<TerrainProperty> terrainProperties;
        private boolean isPillaged;
        private final double maintenanceCost;
        private final String name;

        private ImprovementType(Output output, Technology prerequisiteTechnology,
                        ArrayList<TerrainProperty> terrainProperties, double maintenanceCost, String name) {
                this.output = output;
                this.prerequisiteTechnology = prerequisiteTechnology;
                this.terrainProperties = terrainProperties;
                this.isPillaged = false;
                this.maintenanceCost = maintenanceCost;
                this.name = name;
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

        public boolean getIsPillaged() {
                return this.isPillaged;
        }

        public void setIsPillaged(boolean isPillaged) {
                this.isPillaged = isPillaged;
        }

        public double getMaintenanceCost() {
                return this.maintenanceCost;
        }

        public String getName() {
                return name;
        }

}
