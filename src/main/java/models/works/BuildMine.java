package models.works;

import controllers.GameController;
import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.units.Unit;

public class BuildMine extends BuildImprovement {
    public BuildMine(Unit worker) {
        super(ImprovementType.MINE, worker);
    }

    @Override
    public void applyChange() {
        // TODO
        Tile myLocation = this.findLocation();
        myLocation.addImprovement(new Improvement(ImprovementType.MINE, worker.getOwner()));
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        // TODO
        return 0;
    }
}
