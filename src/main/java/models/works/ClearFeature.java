package models.works;

import models.Feature;

public class ClearFeature extends Work {
    private Feature feature;

    @Override
    public void applyChange() {
        // TODO
    }

    @Override
    public int calculateRequiredTurns() {
        // TODO
        return 0;
    }

    @Override
    public String getTitle() {
        String result = "Clearing " + feature.getName().toLowerCase();
        return result;
    }
}
