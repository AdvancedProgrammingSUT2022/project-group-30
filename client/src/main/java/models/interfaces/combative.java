package models.interfaces;

import models.Civilization;
import models.Tile;

public interface combative {
    public int getHitPointsLeft();

    public void reduceHitPoints(int amount);

    public void setHitPoints(int amount);

    public Tile getLocation();

    public Civilization getOwner();
}
