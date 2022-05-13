package controllers;

import models.City;
import models.interfaces.combative;
import models.units.Unit;

public class CombatController {
    private static CombatController combatController = null;
    public static CombatController getCombatController() {
        if (combatController == null) {
            combatController = new CombatController();
        }
        return combatController;
    }

    public int calculateEffectiveMeleeCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        return 0;
    }

    public int calculateEffectiveMeleeCombatStrengthForCity(Unit unit, combative target) {
        // TODO
        return 0;
    }

    public int calculateEffectiveRangedCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        return 0;
    }

    public int calculateEffectiveRangedCombatStrengthForCity(City city, combative target) {
        // TODO
        return 0;
    }

    public int calculateNetDefensiveBonusForUnit(Unit unit, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        return 0;
    }

    public int calculateNetDefensiveBonusForCity(City city, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        return 0;
    }

    public void executeMeleeAttack(Unit attacker, combative defender) {
        // TODO
        /*
        NOTES:
        - HANDLE BOTH DYING AT THE SAME TIME

        if defender is civilian, have it capture -> turn it into worker and move into the tile
        calculate combat strengths and defensive bonuses
        apply damages and check HPs
        kill first one to reach 0 HPs -> if it's a unit, just kill it and move the other one into it.
                                      -> if it's a city, destroy it and all units contained in it.
        modify unit's hasAttackedField
        modify unit's inactivityDuration field and make sure the same thing is done with move
        drain unit's MPs (doesn't apply to some types)
         */
    }

    public void executeUnitRangedAttack(Unit attacker, combative defender) {
        // TODO
        /*
        NOTES:
        - HANDLE BOTH DYING AT THE SAME TIME

        if defender is civilian, do a normal attack
        calculate ranged combat strength and defensive bonus
        apply damage and check HP -> don't let city hp fall below 1
        if defender reached 0 HPs -> it's a unit, just kill it and move the other one into it.
        modify unit's hasAttackedField
        modify unit's inactivityDuration field and make sure the same thing is done with move
        drain Unit's MPs (doesn't apply to some types)
         */
    }
}
