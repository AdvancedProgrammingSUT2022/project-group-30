package models.works;

import controllers.GameController;
import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;

public class BuildFarm extends BuildImprovement {
    @Override
    public void applyChange() {
        // TODO
        Tile myLocation = GameController.getGameController().findWorksLocation(this);
        myLocation.addImprovement(new Improvement(ImprovementType.FARM, worker.getOwner()));
        myLocation.removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        // TODO 
        return 0;
    }
}
