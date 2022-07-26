package models.works;

import com.google.gson.annotations.SerializedName;
import models.Tile;
import models.improvements.ImprovementType;
import models.units.Unit;

public class FixPillage extends Work {
    private ImprovementType improvementType;

    public FixPillage(ImprovementType improvementType, Unit worker) {
        this.improvementType = improvementType;
        this.worker = worker;
        turnsRemaining = calculateRequiredTurns();
        isInProgress = true;
    }

    @Override
    public void applyChange() {
        Tile myLocation = this.findLocation();
        if (myLocation.containsImprovment(improvementType))
            myLocation.getImprovementByType(improvementType).setIsPillaged(false);
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        return 3;
    }

    public ImprovementType getImprovementType() {
        return improvementType;
    }

    @Override
    public String getTitle() {
        String result = "Fixing " + improvementType.getName().toLowerCase();
        return result;
    }
}
