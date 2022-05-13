package models.works;

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

    @Override
    public void applyChange() {
        Tile location = this.findLocation();
        location.addImprovement(new Improvement(improvementType, worker.getOwner()));
        ArrayList<Feature> clearableFeatures = new ArrayList<>(Arrays.asList(Feature.FOREST, Feature.JUNGLE, Feature.MARSH));
        for (Feature feature : clearableFeatures) {
            if (location.getFeatures().contains(feature)) {
                location.removeFeatureAndApplyChanges(feature);
            }
        }
        this.findLocation().removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        Tile location = this.findLocation();
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
}
