package models.works;

import models.Tile;
import models.improvements.ImprovementType;
import models.units.Unit;

public class ClearRoutes extends Work {
    public ClearRoutes(Unit worker) {
        this.worker = worker;
        turnsRemaining = calculateRequiredTurns();
        isInProgress = true;
    }

    @Override
    public void applyChange() {
        Tile myLocation = this.findLocation();
        if (this.worker.getLocation().containsImprovment(ImprovementType.ROAD))
            myLocation.removeImprovement(ImprovementType.ROAD);
        if (this.worker.getLocation().containsImprovment(ImprovementType.RAILROAD))
            myLocation.removeImprovement(ImprovementType.RAILROAD);
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        if (this.worker.getLocation().containsImprovment(ImprovementType.ROAD) && this.worker.getLocation().containsImprovment(ImprovementType.RAILROAD))
            return 6;
        return 3;
    }
}
