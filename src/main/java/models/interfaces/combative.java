package models.interfaces;

import models.Tile;
import models.units.Unit;

public interface combative {
    public int getHitPointsLeft();
    public void reduceHitPoints(int amount);
    public void setHitPoints(int amount);
    public Tile getLocation();
}
