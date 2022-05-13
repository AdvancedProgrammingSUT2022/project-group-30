package models.works;

import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.units.Unit;

public class BuildImprovement extends Work {
    protected ImprovementType improvementType;

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
        return improvementType.getConstructionDuration();
    }

    @Override
    public String getTitle() {
        String result = "Building " + improvementType.getName().toLowerCase();
        return result;
    }

    public ImprovementType getImprovement() {
        return improvementType;
    }
}
