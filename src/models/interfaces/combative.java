package models.interfaces;

import models.units.Unit;

public interface combative {
    public void attack(Unit target);

    public void defend(Unit attacker);
}
