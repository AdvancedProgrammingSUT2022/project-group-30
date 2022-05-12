package models.works;

import controllers.GameController;
import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.units.Unit;

public class BuildImprovement extends Work {
    private ImprovementType improvementType;

    public BuildImprovement(ImprovementType improvementType, Unit worker) {
        this.improvementType = improvementType;
        this.worker = worker;
        turnsRemaining = calculateRequiredTurns();
        isInProgress = true;
    }

    @Override
    public void applyChange() {
        Tile myLocation = this.findLocation();
        Improvement improvement = new Improvement(improvementType, worker.getOwner());
        myLocation.addImprovement(improvement);
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        if (improvementType == ImprovementType.ROAD) {
            return 3;
        }
        // TODO
        return 0;
    }

    public ImprovementType getImprovement() {
        return improvementType;
    }
}
