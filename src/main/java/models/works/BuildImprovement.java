package models.works;

import controllers.GameController;
import models.Tile;
import models.improvements.Improvement;

public class BuildImprovement extends Work {
    private Improvement improvement;

    @Override
    public void applyChange() {
        // TODO
        Tile myLocation = this.findLocation();
        myLocation.addImprovement(improvement);
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        // TODO
        return 0;
    }
}
