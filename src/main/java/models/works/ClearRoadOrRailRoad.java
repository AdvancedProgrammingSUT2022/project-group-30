package models.works;

import models.Tile;
import models.improvements.ImprovementType;
import models.units.Unit;

public class ClearRoadOrRailRoad extends Work{
    private ImprovementType improvementType;

    public ClearRoadOrRailRoad(ImprovementType improvementType, Unit worker) {
        this.improvementType = improvementType;
        this.worker = worker;
        turnsRemaining = calculateRequiredTurns();
        isInProgress = true;
    }

    @Override
    public void applyChange() {
        Tile myLocation = this.findLocation();
        myLocation.removeImprovement(improvementType);
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        return 3;
    }

    public ImprovementType getImprovement() {
        return improvementType;
    }
}
