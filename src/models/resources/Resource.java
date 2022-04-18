package models.resources;

import java.util.ArrayList;

import models.Civilization;
import models.Output;
import models.interfaces.TerrainProperty;
import models.Tile;
import models.improvements.ImprovementType;


public abstract class Resource {
    private Output output;
    private ArrayList<TerrainProperty> allowedTerrains;
    private ImprovementType prerequisiteImprovement;

    public Resource(Output output, ImprovementType prerequisiteImprovement, ArrayList<TerrainProperty> allowedTerrains) {
        this.output = output;
        this.prerequisiteImprovement = prerequisiteImprovement;
        this.allowedTerrains = allowedTerrains;
    }


    public boolean isTileValid(Tile tile) {
        for (TerrainProperty terrainProperty : allowedTerrains) {
            if (tile.isOfType(terrainProperty)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDiscoverable(Civilization civilization) {
        return true;
    }

    public boolean canBeExploited(Tile tile) {
        if (tile.containsImprovment(prerequisiteImprovement)) {
            return true;
        } else {
            return false;
        }
    }

    
       
    public Output getOutput() {
        return output;
    }
}