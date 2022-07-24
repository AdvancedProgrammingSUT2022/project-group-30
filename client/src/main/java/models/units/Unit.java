package models.units;

import controllers.GameController;
import models.City;
import models.Civilization;
import models.Tile;
import models.interfaces.Selectable;
import models.interfaces.TurnHandler;
import models.interfaces.combative;

import java.util.ArrayList;

public class Unit implements Selectable, TurnHandler, combative {

    private final int id;
    public  int getId() {
        return id;
    }

    private static int newAvailableId = 0;
    private final Civilization owner;   //
    private final UnitType type;
    private Tile location;
    private int hitPointsLeft;
    private int movePointsLeft;
    private UnitState state;
    private boolean isAssembled;
    private boolean hasAttackedThisTurn;
    private int inactivityDuration; // measured in turns, starts at 0 when unit makes any move(attacks, moves, etc.)
    private int stateDuration;
    private ArrayList<Tile> path;   // should be NULL when unit has no destination
    public static final int MAINTENANCE_COST_OF_UNIT = 2;


    public Unit(Unit unit) {
        this.id = unit.getId();
        this.owner = null;
        this.type = unit.getType();
        this.location = unit.getLocation();
        this.hitPointsLeft = unit.hitPointsLeft;
        this.movePointsLeft = unit.movePointsLeft;
        this.state = unit.state;
        this.isAssembled = unit.isAssembled;
        this.hasAttackedThisTurn = unit.hasAttackedThisTurn;
        this.inactivityDuration = unit.inactivityDuration;
        this.stateDuration = unit.stateDuration;
        this.path = unit.path;

    }

    public Unit(Civilization owner, UnitType type, Tile location) {
        this.id = newAvailableId;
        newAvailableId++;
        this.owner = owner;
        this.type = type;
        this.location = location;
        hitPointsLeft = type.getHitPoints();
        movePointsLeft = type.getMovementSpeed();
        state = UnitState.AWAKE;
        hasAttackedThisTurn = false;
        inactivityDuration = 0;
        stateDuration = 0;
        path = null;
        if (this.type.needsAssmbly()) {
            isAssembled = false;
        } else {
            isAssembled = true;
        }
    }

    public Unit createImage() {
        Unit image = new Unit(owner, type, location);
        image.hitPointsLeft = hitPointsLeft;
        image.movePointsLeft = movePointsLeft;
        image.state = state;
        image.inactivityDuration = inactivityDuration;
        image.stateDuration = stateDuration;
        image.isAssembled = isAssembled;
        return image;
    }

    public boolean isUnitInItsCivilizationCities() {
        City city = this.location.getCityOfTile();
        if (city != null && city.getOwner().equals(this.getOwner()) && city.getCentralTile().equals(this.location)) {
            return true;
        }
        return false;
    }

    public void goToNextTurn() {
        if (!hasAttackedThisTurn && movePointsLeft == type.movementSpeed) {
            inactivityDuration++;
        }

        stateDuration++;
        hasAttackedThisTurn = false;
        movePointsLeft = type.getMovementSpeed();
        GameController.getGameController().moveUnitAlongItsPath(this);

        if (inactivityDuration >= 1) {
            City central = GameController.getGameController().getCityCenteredInTile(location);
            City territorial = GameController.getGameController().whoseTerritoryIsTileInButIsNotTheCenterOf(location);
            if (central != null && central.getOwner() == owner) {
                hitPointsLeft += 3;
            } else if (territorial != null && territorial.getOwner() != owner) {
                hitPointsLeft += 2;
            } else {
                hitPointsLeft += 1;
            }
            hitPointsLeft = Math.min(hitPointsLeft, type.getHitPoints());
            if (hitPointsLeft == type.getHitPoints() && state == UnitState.FORTIFYUNTILHEALED) {
                setState(UnitState.AWAKE);
            }
        }
    }

    public void assemble() {
        isAssembled = true;
    }

    public void disassemble() {
        isAssembled = false;
    }

    public boolean isAssembled() { // needs to be checked for all units, but only siege units may return false, the
        // rest all return true
        if (type.needsAssmbly()) {
            return isAssembled;
        } else {
            return true;
        }
    }

    public boolean isWaitingForCommand() {
        if (type == UnitType.WORKER && GameController.getGameController().isWorkerWorking(this)) {
            return false;
        }
        if (state.waitsForCommand == false || path != null) { // if it is in an inactive state like fortified or sleeping, return false
            return false;
        }
        if (movePointsLeft > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Civilization getOwner() {
        return this.owner;
    }

    @Override
    public Tile getLocation() {
        return this.location;
    }

    public void setLocation(Tile location) {
        this.location = location;
    }

    @Override
    public int getHitPointsLeft() {
        return this.hitPointsLeft;
    }

    @Override
    public void setHitPoints(int hitPointsLeft) {
        this.hitPointsLeft = hitPointsLeft;
    }

    @Override
    public void reduceHitPoints(int amount) {
        hitPointsLeft -= amount;
    }


    public void setHitPointsLeft(int hitPointsLeft) {
        this.hitPointsLeft = hitPointsLeft;
    }

    public int getMovePointsLeft() {
        return this.movePointsLeft;
    }

    public void setMovePointsLeft(int movePointsLeft) {
        this.movePointsLeft = movePointsLeft;
    }

    public void setState(UnitState state) {
        if (state != UnitState.AWAKE) {
            this.path = null;
        }
        if (this.state != state) {
            stateDuration = 0;
        }
        this.state = state;
    }

    public UnitState getState() {
        return this.state;
    }

    public int getInactivityDuration() {
        return this.inactivityDuration;
    }

    public void resetInactivityDuration() {
        this.inactivityDuration = 0;
    }

    public int getStateDuration() {
        return this.stateDuration;
    }

    public void setPath(ArrayList<Tile> path) {
        this.path = path;
    }

    public ArrayList<Tile> getPath() {
        return path;
    }

    public UnitType getType() {
        return this.type;
    }

    public boolean isCivilian() {
        return (type.getCombatType() == CombatType.CIVILIAN);
    }

    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurns(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }
}