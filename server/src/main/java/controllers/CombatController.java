package controllers;

import models.*;
import models.buildings.BuildingType;
import models.interfaces.combative;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
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

    public int calculateEffectiveMeleeCombatStrengthForCity(City city, combative target) {
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for (Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        percentage = Math.max(-100, percentage);
        return (int) (city.getCombatStrength() * (percentage + 100) / 100);
    }

    public int calculateEffectiveRangedCombatStrengthForCity(City city, combative target) {
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for (Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        if (city.getOwner().getHappiness() < 0)
            percentage -= 25;
        percentage += city.getTerritories().size() * 2;
        percentage = Math.max(-100, percentage);
        return (int) (city.getRangedCombatStrength() * (percentage + 100) / 100);
    }

    public int calculateNetDefensiveBonusForCity(City city, combative attacker) {
        Tile myLocation = city.getCentralTile();
        double percentage = 0;
        for (Feature feature : myLocation.getFeatures())
            percentage += feature.getCombatModifier();
        percentage += myLocation.getTerrainType().getCombatModifier();
        if (city.getOwner().getHappiness() < 0)
            percentage -= 25;
        percentage += city.getTerritories().size() * 2;
        if (city.getGarrisoningUnit() != null)
            percentage += 33;
        if (city.hasBuildingType(BuildingType.CASTLE))
            percentage += 30;
        if (city.hasBuildingType(BuildingType.WALLS))
            percentage += 20;
        percentage = Math.max(-50, percentage);
        percentage = Math.min(50, percentage);
        return (int) percentage;
    }

    public int calculateEffectiveMeleeCombatStrengthForUnit(Unit unit, combative target) {
        Tile myLocation = unit.getLocation();
        double percentage = 0;
        Tile targetTile;
        if (target instanceof City)
            targetTile = ((City) target).getCentralTile();
        else
            targetTile = ((Unit) target).getLocation();
        if (GameController.getGameController().hasCommonRiver(myLocation, targetTile) && unit.getType().getCombatType() == CombatType.MOUNTED)
            percentage -= 33;
        percentage = Math.max(-100, percentage);
        double effectOfHitPoints = (10 - ((10 - unit.getHitPointsLeft()) / 2)) * 10;
        return (int) (unit.getType().getCombatStrength() * (percentage + 100) / 100 * effectOfHitPoints);
    }

    public int calculateEffectiveRangedCombatStrengthForUnit(Unit unit, combative target) {
        Tile myLocation = unit.getLocation();
        double percentage = 0;
        for (Feature feature : myLocation.getFeatures()) {
            if (feature.getCombatModifier() < 0 || !(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE)) {
                percentage += feature.getCombatModifier();
            }
        }
        if (myLocation.getTerrainType().getCombatModifier() < 0 || !(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE))
            percentage += myLocation.getTerrainType().getCombatModifier();
        if (unit.getOwner().getHappiness() < 0)
            percentage -= 25;
        if (!(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE)) {
            if ((unit.getType() == UnitType.SPEARMAN || unit.getType() == UnitType.PIKEMAN) && target instanceof Unit && ((Unit) target).getType().getCombatType() == CombatType.MOUNTED)
                percentage += 100;
            if ((unit.getType() == UnitType.CATAPULT || unit.getType() == UnitType.TREBUCHET || unit.getType() == UnitType.CANNON || unit.getType() == UnitType.ARTILLERY || unit.getType() == UnitType.TANK) && target instanceof City)
                percentage += 10;
            if (unit.getType() == UnitType.ANTI_TANK_GUN && target instanceof Unit && ((Unit) target).getType() == UnitType.TANK)
                percentage += 10;
        }
        percentage = Math.max(-100, percentage);
        double effectOfHitPoints = (10 - ((10 - unit.getHitPointsLeft()) / 2)) * 10;
        return (int) (unit.getType().getRangedCombatStrength() * (percentage + 100) / 100 * effectOfHitPoints);
    }

    public int calculateNetDefensiveBonusForUnit(Unit unit, combative attacker) {
        Tile myLocation = unit.getLocation();
        double percentage = 0;
        for (Feature feature : myLocation.getFeatures()) {
            if (feature.getCombatModifier() < 0 || !(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE)) {
                percentage += feature.getCombatModifier();
            }
        }
        if (myLocation.getTerrainType().getCombatModifier() < 0 || !(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE))
            percentage += myLocation.getTerrainType().getCombatModifier();
        if (unit.getOwner().getHappiness() < 0)
            percentage -= 25;
        if ((unit.getState() == UnitState.FORTIFY || unit.getState() == UnitState.FORTIFYUNTILHEALED || unit.getState() == UnitState.ALERT) && !(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE)) {
            if (unit.getStateDuration() == 1)
                percentage += 25;
            else if (unit.getStateDuration() > 1)
                percentage += 50;
        }
        if (!(unit.getType().getCombatType() == CombatType.ARMORED || unit.getType().getCombatType() == CombatType.MOUNTED || unit.getType().getCombatType() == CombatType.SIEGE)) {
            if ((unit.getType() == UnitType.SPEARMAN || unit.getType() == UnitType.PIKEMAN) && attacker instanceof Unit && ((Unit) attacker).getType().getCombatType() == CombatType.MOUNTED)
                percentage += 100;
            if ((unit.getType() == UnitType.CATAPULT || unit.getType() == UnitType.TREBUCHET || unit.getType() == UnitType.CANNON || unit.getType() == UnitType.ARTILLERY || unit.getType() == UnitType.TANK) && attacker instanceof City)
                percentage += 10;
            if (unit.getType() == UnitType.ANTI_TANK_GUN && attacker instanceof Unit && ((Unit) attacker).getType() == UnitType.TANK)
                percentage += 10;
        }
        percentage = Math.max(-50, percentage);
        percentage = Math.min(50, percentage);
        return (int) percentage;
    }

    private int calculateEffectiveMeleeCombatStrength(combative attacker, combative defender) {
        if (attacker instanceof Unit) {
            return calculateEffectiveMeleeCombatStrengthForUnit((Unit) attacker, defender);
        } else {
            return calculateEffectiveMeleeCombatStrengthForCity((City) attacker, defender);
        }
    }

    public int calculateEffectiveRangedCombatStrength(combative attacker, combative defender) {
        if (attacker instanceof City) {
            return calculateEffectiveRangedCombatStrengthForCity((City) attacker, defender);
        } else {
            return calculateEffectiveRangedCombatStrengthForUnit((Unit) attacker, defender);
        }
    }

    private int calculateEffectiveNetDefensiveBonus(combative defender, combative attacker) {
        if (defender instanceof City) {
            return calculateNetDefensiveBonusForCity((City) defender, attacker);
        } else {
            return calculateNetDefensiveBonusForUnit((Unit) defender, attacker);
        }
    }

    public void kill(City city) {
        city.getOwner().addNotification("Your city at " + city.getCentralTile().findTileYCoordinateInMap() + ", " +
                city.getCentralTile().findTileXCoordinateInMap() + " was destroyed!");
        Civilization owner = null;
        if (city.isCapital()) {
            owner = city.getOwner();
            owner.setCapital(null);
        }
        gameController.destroyCity(city);
        if (owner != null) {
            changeCivsCapital(owner);
        }
    }

    public void kill(Unit unit) {
        unit.getOwner().addNotification("Your " + unit.getType().getName() + " at " +
                unit.getLocation().findTileYCoordinateInMap() + ", " + unit.getLocation().findTileXCoordinateInMap() +
                " was destroyed!");

        gameController.removeUnit(unit);
    }

    public void kill(combative entityToKill) {
        if (entityToKill instanceof Unit) {
            kill((Unit) entityToKill);
        } else {
            kill((City) entityToKill);
        }
    }

    public void annexCity(City city, Civilization newOwner) {
        Civilization prevOwner = null;
        if (city.isCapital()) {
            prevOwner = city.getOwner();
            prevOwner.setCapital(null);
        }
        city.setOwner(newOwner);
        if (prevOwner != null) {
            changeCivsCapital(prevOwner);
        }

        if (newOwner.getOriginCapital() == city) {
            newOwner.changeCapital(city);
        }
        gameController.setMapImageOfCivilization(newOwner);
    }

    private void changeCivsCapital(Civilization civilization) {
        if (civilization.getCities().size() == 0) {
            // TODO: civ loses
            GameController.getGameController().defeatCivilization(civilization);
            System.out.println("civ " + civilization.getName() + " has lost!");
            GameController.getGameController().checkVictoryByDominion();
            return;
        }
        civilization.changeCapital(civilization.getCities().get(0));
        GameController.getGameController().checkVictoryByDominion();
    }

    private void captureUnit(Unit attacker, Unit defender) {
        defender.getOwner().addNotification("Your " + defender.getType().getName() + " at " + defender.getLocation().findTileYCoordinateInMap() +
                ", " + defender.getLocation().findTileXCoordinateInMap() + " was captured by " + attacker.getOwner().getName());
        gameController.removeUnit(defender);
        gameController.createUnit(UnitType.WORKER, attacker.getOwner(), defender.getLocation());
        gameController.moveUnit(attacker, defender.getLocation());
    }

    private void applyAttackChangesOnUnit(Unit attacker) {
        if (attacker.getType().getCombatType() != CombatType.MOUNTED) {
            attacker.setMovePointsLeft(0);
        }
        attacker.setHasAttackedThisTurns(true);
        attacker.resetInactivityDuration();
    }

    private void applyAttackChangesOnCity(City attacker) {
        attacker.setHasAttackedThisTurn(true);
    }

    private void applyMeleeCombatEndEffects(combative winner, combative loser) {
        Unit capturedUnit = null;
        if (loser instanceof Unit && winner instanceof Unit) {
            Unit loserUnit = (Unit) loser;
            capturedUnit = gameController.getCivilianUnitInTile(loserUnit.getLocation());
        }

        if (loser instanceof City) {
            ArrayList<Unit> units = gameController.getUnitsInTile(((City) loser).getCentralTile());
            for (Unit unit : units) {
                gameController.removeUnit(unit);
            }
            ((City) loser).setDefeated(true);
        } else if (loser instanceof Unit) {
            kill((Unit) loser);
            if (capturedUnit != null) {
                captureUnit((Unit) winner, capturedUnit);
            }
        }

        if (winner instanceof Unit) {
            gameController.moveUnit((Unit) winner, loser.getLocation());
        }
    }

    public void executeMeleeAttack(Unit attacker, combative defender) {
        applyAttackChangesOnUnit(attacker);

        if (defender instanceof Unit && ((Unit) defender).isCivilian()) {
            captureUnit(attacker, (Unit) defender);
        }

        int attackerStrength = calculateEffectiveMeleeCombatStrengthForUnit(attacker, defender);
        int defenderStrength = calculateEffectiveMeleeCombatStrength(defender, attacker);
        int defenderDefensiveBonus = calculateEffectiveNetDefensiveBonus(defender, attacker);
        int attackerDefensiveBonus = calculateNetDefensiveBonusForUnit(attacker, defender);
        int damageDoneToDefender = attackerStrength * (100 - defenderDefensiveBonus) / 100;
        int damageDoneToAttacker = defenderStrength * (100 - attackerDefensiveBonus) / 100;

        if (defender.getHitPointsLeft() <= damageDoneToDefender) {
            attacker.reduceHitPoints(damageDoneToAttacker);
            if (attacker.getHitPointsLeft() <= 0) {
                attacker.setHitPointsLeft(1);
            }

            applyMeleeCombatEndEffects(attacker, defender);
        } else if (attacker.getHitPointsLeft() <= damageDoneToAttacker) {
            defender.reduceHitPoints(damageDoneToDefender);

            applyMeleeCombatEndEffects(defender, attacker);
        } else {
            attacker.reduceHitPoints(damageDoneToAttacker);
            defender.reduceHitPoints(damageDoneToDefender);
        }
    }

    public void executeRangedAttack(combative attacker, combative defender) {
        if (attacker instanceof Unit) {
            applyAttackChangesOnUnit((Unit) attacker);
        } else {
            applyAttackChangesOnCity((City) attacker);
        }

        int attackerStrength = calculateEffectiveRangedCombatStrength(attacker, defender);
        int defenderDefenseBonus = calculateEffectiveNetDefensiveBonus(defender, attacker);

        int totalDamageDone = attackerStrength * (100 - defenderDefenseBonus) / 100;
        if (totalDamageDone >= defender.getHitPointsLeft()) {
            if (defender instanceof City) {
                ((City) defender).setHitPoints(1);
            } else {
                kill(defender);
            }
        } else {
            defender.reduceHitPoints(totalDamageDone);
        }
    }

    public boolean isCityDestructible(City city, Unit unit) {
        Civilization civilization = unit.getOwner();
        if (civilization == city.getFounder())
            return false;
        for (Civilization civilization1 : GameDataBase.getGameDataBase().getCivilizations()) {
            if (civilization1.getCapital() == city || civilization1.getOriginCapital() == city)
                return false;
        }
        return true;
    }
}
