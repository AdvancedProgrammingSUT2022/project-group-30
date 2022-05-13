package controllers;

import models.City;
import models.Feature;
import models.Tile;
import models.buildings.BuildingType;
import models.interfaces.combative;
import models.units.Unit;

import java.util.ArrayList;

public class CombatController {
    private static CombatController combatController = null;
    public static CombatController getCombatController() {
        if (combatController == null) {
            combatController = new CombatController();
        }
        return combatController;
    }

    //1
    public int calculateEffectiveMeleeCombatStrengthForCity(City city, combative target) {
        // TODO
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for(Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        percentage = Math.max(-100, percentage);
        return (int) (city.getCombatStrength() * (percentage + 100) / 100);
    }

    //2
    public int calculateEffectiveRangedCombatStrengthForCity(City city, combative target) {
        // TODO
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for(Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        if(city.getOwner().getHappiness() < 0)
            percentage -= 25;
        percentage += city.getTerritories().size() * 2;
        percentage = Math.max(-100, percentage);
        return (int) (city.getRangedCombatStrength() * (percentage + 100) / 100);
    }

    //3
    public int calculateNetDefensiveBonusForCity(City city, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for(Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        if(city.getOwner().getHappiness() < 0)
            percentage -= 25;
        percentage += city.getTerritories().size() * 2;
        if(city.getGarrisoningUnit() != null)
            percentage += 33;
        percentage = Math.max(-50, percentage);
        percentage = Math.min(50, percentage);
        return (int)percentage;
    }
    public double calculateNumberOfNetDefensiveBonusForCity(City city, combative attacker) {
        double defenseBonus = 0;
        if(city.hasBuildingType(BuildingType.CASTLE))
            defenseBonus += 7.5;
        if(city.hasBuildingType(BuildingType.WALLS))
            defenseBonus += 5;
        return defenseBonus;
    }

    //4
    public int calculateEffectiveMeleeCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        Tile myLocation = unit.getLocation();
        double percentage = 0;
        for(Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        Tile targetTile;
        if(target instanceof City)
            targetTile = ((City)target).getCentralTile();
        else
            targetTile = ((Unit)target).getLocation();
        if(GameController.getGameController().hasCommonRiver(myLocation, targetTile))
            percentage -= 33;
        percentage = Math.max(-100, percentage);
        double effectOfHitPoints =  (10 - ((10 - unit.getHitPointsLeft()) / 2) ) * 10;
        return (int) (unit.getType().getCombatStrength() * (percentage + 100) / 100  * effectOfHitPoints);
    }

    //5
    public int calculateEffectiveRangedCombatStrengthForUnit(Unit unit, combative target) {
        // TODO
        Tile myLocation = unit.getLocation();
        double percentage = 0;
        for(Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        if(unit.getOwner().getHappiness() < 0)
            percentage -= 25;
        percentage = Math.max(-100, percentage);
        double effectOfHitPoints =  (10 - ((10 - unit.getHitPointsLeft()) / 2) ) * 10;
        return (int) (unit.getType().getRangedCombatStrength() * (percentage + 100) / 100  * effectOfHitPoints);
    }

    //6
    public int calculateNetDefensiveBonusForUnit(Unit unit, combative attacker) {
        // TODO: return percentage defensive bonus between -50 and 50
        return 0;
    }

    private int calculateEffectiveMeleeCombatStrength(combative attacker, combative defender) {
        if (attacker instanceof  Unit) {
            return calculateEffectiveMeleeCombatStrengthForUnit((Unit) attacker, defender);
        } else {
            return calculateEffectiveMeleeCombatStrengthForCity((City) attacker, defender);
        }
    }

    private int calculateEffectiveNetDefensiveBonus(combative defender, combative attacker) {
        if (defender instanceof City) {
            return calculateNetDefensiveBonusForCity((City) defender, attacker);
        } else {
            return calculateNetDefensiveBonusForUnit((Unit) defender, attacker);
        }
    }

    private void kill(City city) {
        // TODO
    }

    private void kill(Unit unit) {

    }

    private void kill(combative entityToKill) {
        if (entityToKill instanceof  Unit) {
            kill((Unit) entityToKill);
        } else {
            kill((City) entityToKill);
        }
    }

    public void executeMeleeAttack(Unit attacker, combative defender) {
        int attackerStrength = calculateEffectiveMeleeCombatStrengthForUnit(attacker, defender);
        int defenderStrength = calculateEffectiveMeleeCombatStrength(defender, attacker);
        int defenderDefensiveBonus = calculateEffectiveNetDefensiveBonus(defender, attacker);
        int attackerDefensiveBonus = calculateNetDefensiveBonusForUnit(attacker, defender);
        int damageDoneToDefender = attackerStrength * (100 - defenderDefensiveBonus) / 100;
        int damageDoneToAttacker = defenderStrength * (100 - attackerDefensiveBonus) / 100;
        if (defender.getHitPointsLeft() < damageDoneToDefender) {
            kill(defender);

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
