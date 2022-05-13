package models.works;

import models.improvements.Improvement;

public class FixPillage extends Work {
    private Improvement improvement;

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
        String result = "Fixing " + improvement.getType().getName().toLowerCase();
        return result;
    }
}
