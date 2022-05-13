package models.interfaces;

import models.units.Unit;

public interface combative {
    public void attack(Unit target);
    public void defend(Unit attacker);
    public int getHitPointsLeft();
    public void reduceHitPoints(int amount);
    public void setHitPoints(int amount);
}
