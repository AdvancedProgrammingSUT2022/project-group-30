package models;

import models.interfaces.TerrainProperty;

public enum TerrainType implements TerrainProperty {
    DESERT(),
    GRASSLAND(),
    HILLS(),
    MOUNTAIN(),
    OCEAN(),
    PLAINS(),
    SNOW(),
    TUNDRA();
}
