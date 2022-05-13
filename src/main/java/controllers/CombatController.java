package controllers;

import models.City;
import models.interfaces.combative;
import models.units.Unit;
import models.units.UnitType;

import java.util.ArrayList;

public class CombatController {
    private static CombatController combatController = null;
    public static CombatController getCombatController() {
        if (combatController == null) {
            combatController = new CombatController();
        }
        return combatController;
    }

    private CombatController() {
        gameController = GameController.getGameController();
    }

    private final GameController gameController;

    public int calculateEffectiveMeleeCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        return unit.getType().getCombatStrength();
    }

    public int calculateEffectiveMeleeCombatStrengthForCity(City city, combative target) {
        // TODO
        return (int) city.getCombatStrength();
    }

    private int calculateEffectiveMeleeCombatStrength(combative attacker, combative defender) {
        if (attacker instanceof  Unit) {
            return calculateEffectiveMeleeCombatStrengthForUnit((Unit) attacker, defender);
        } else {
            return calculateEffectiveMeleeCombatStrengthForCity((City) attacker, defender);
        }
    }

    public int calculateEffectiveRangedCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        return unit.getType().getRangedCombatStrength();
    }

    public int calculateEffectiveRangedCombatStrengthForCity(City city, combative target) {
        // TODO
        return (int) city.getRangedCombatStrength();
    }

    private int calculateEffectiveNetDefensiveBonus(combative defender, combative attacker) {
        if (defender instanceof City) {
            return calculateNetDefensiveBonusForCity((City) defender, attacker);
        } else {
            return calculateNetDefensiveBonusForUnit((Unit) defender, attacker);
        }
    }

    public int calculateNetDefensiveBonusForUnit(Unit unit, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        return 0;
    }

    public int calculateNetDefensiveBonusForCity(City city, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        return 0;
    }

    private void kill(City city) {
        ArrayList<Unit> units = gameController.getUnitsInTile(city.getCentralTile());
        for (Unit unit : units) {
            gameController.removeUnit(unit);
        }
        gameController.destroyCity(city);
    }

    private void kill(Unit unit) {
        gameController.removeUnit(unit);
    }

    private void kill(combative entityToKill) {
        if (entityToKill instanceof  Unit) {
            kill((Unit) entityToKill);
        } else {
            kill((City) entityToKill);
        }
    }

    private void captureUnit(Unit attacker, Unit defender) {
        gameController.removeUnit(defender);
        gameController.createUnit(UnitType.WORKER, attacker.getOwner(), defender.getLocation());
        gameController.moveUnit(attacker, defender.getLocation());
    }

    public void executeMeleeAttack(Unit attacker, combative defender) {
        if (defender instanceof Unit && ((Unit)defender).isCivilian()) {
            captureUnit(attacker, (Unit) defender);
        }

        int attackerStrength = calculateEffectiveMeleeCombatStrengthForUnit(attacker, defender);
        int defenderStrength = calculateEffectiveMeleeCombatStrength(defender, attacker);
        int defenderDefensiveBonus = calculateEffectiveNetDefensiveBonus(defender, attacker);
        int attackerDefensiveBonus = calculateNetDefensiveBonusForUnit(attacker, defender);
        int damageDoneToDefender = attackerStrength * (100 - defenderDefensiveBonus) / 100;
        int damageDoneToAttacker = defenderStrength * (100 - attackerDefensiveBonus) / 100;

        if (defender.getHitPointsLeft() <= damageDoneToDefender) {
            kill(defender);
            attacker.reduceHitPoints(damageDoneToAttacker);
            if (attacker.getHitPointsLeft() <= 0) {
                attacker.setHitPointsLeft(1);
            }
            gameController.moveUnit(attacker, defender.getLocation());
        } else if (attacker.getHitPointsLeft() <= damageDoneToAttacker) {
            kill(attacker);
            defender.reduceHitPoints(damageDoneToDefender);
            if (defender instanceof Unit) {
                gameController.moveUnit((Unit) defender, attacker.getLocation());
            }
        }

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
