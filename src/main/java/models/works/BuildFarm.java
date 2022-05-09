package models.works;

import controllers.GameController;
import models.Tile;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.units.Unit;

public class BuildFarm extends BuildImprovement {
    public BuildFarm(Unit worker) {
        super(ImprovementType.FARM, worker);
    }

    @Override
    public void applyChange() {
        this.findLocation().addImprovement(new Improvement(ImprovementType.FARM, worker.getOwner()));
        this.findLocation().removeWork();
    }

    @Override
    public int calculateRequiredTurns() {
        Tile locationTile = this.findLocation();
        //MINETODO
       // if(locationTile.getFeatures().contains())
        return 0;
    }
}
