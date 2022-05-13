package models.works;

import models.Feature;
import models.units.Unit;
import utilities.Debugger;

public class ClearFeature extends Work {

    private Feature feature;

    public ClearFeature(Feature feature, Unit worker){
        this.feature = feature;
        this.worker = worker;
        turnsRemaining = calculateRequiredTurns();
        isInProgress = true;
    }

    @Override
    public void applyChange() {
        // TODO
    }

    @Override
    public int calculateRequiredTurns() {
        if (this.feature == Feature.FOREST)
            return 4;
        if (this.feature == Feature.JUNGLE)
            return 7;
        if (this.feature == Feature.MARSH)
            return 6;
        Debugger.debug("You can't clear this feature");
        return 0;
    }

    public Feature getFeature() {
        return this.feature;
    }

}
