package models.works;

import com.google.gson.annotations.SerializedName;
import models.Feature;
import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.units.Unit;

import java.util.ArrayList;
import java.util.Arrays;

public class BuildImprovementAndRemoveFeature extends BuildImprovement {


    public BuildImprovementAndRemoveFeature(Unit worker, ImprovementType type) {
        super(type, worker);
    }

    private final ArrayList<Feature> clearableFeatures = new ArrayList<>(Arrays.asList(Feature.FOREST, Feature.JUNGLE, Feature.MARSH));

    @Override
    public void applyChange() {
        Tile location = this.findLocation();
        location.addImprovement(new Improvement(improvementType, worker.getOwner()));
        for (Feature feature : clearableFeatures) {
            if (location.getFeatures().contains(feature)) {
                location.removeFeatureAndApplyChanges(feature);
            }
        }
        this.findLocation().removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        Tile location = worker.getLocation();
        if (location.getFeatures().contains(Feature.FOREST)) {
            return 10;
        } else if (location.getFeatures().contains(Feature.JUNGLE)) {
            return 13;
        } else if (location.getFeatures().contains(Feature.MARSH)) {
            return 12;
        } else {
            return 6;
        }
    }

    @Override
    public String getTitle() {
        String result = "Building " + improvementType.getName();
        Tile location = this.findLocation();
        for (Feature feature : clearableFeatures) {
            if (location.getFeatures().contains(feature)) {
                result += " and clear " + feature.getName();
            }
        }
        return result;
    }
}
