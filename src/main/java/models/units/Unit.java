package models.units;

import java.util.ArrayList;

import controllers.GameController;
import models.City;
import models.Civilization;
import models.Tile;
import models.interfaces.Producible;
import models.interfaces.Selectable;
import models.interfaces.TurnHandler;
import models.interfaces.combative;

public class Unit implements Selectable, TurnHandler, combative {
    private final Civilization owner;
    private final UnitType type;
    private Tile location;
    private int hitPointsLeft;
    private int movePointsLeft;
    private int experiencePoints; // note : won't be used if we don't implement unit upgrades
    private UnitState state;
    private boolean isAssembled;
    private boolean hasBeenInactive;
    private int inactivityDuration; // measured in turns, starts at 0 when unit makes any move(attacks, moves, etc.)
    private int stateDuration;
    private ArrayList<Tile> path;   // should be NULL when unit has no destination
    public static final int MAINTENANCE_COST_OF_UNIT = 2;


    public Unit(Civilization owner, UnitType type, Tile location) {
        this.owner = owner;
        this.type = type;
        this.location = location;
        hitPointsLeft = type.getHitPoints();
        movePointsLeft = type.getMovementSpeed();
        experiencePoints = 0;
        state = UnitState.AWAKE;
        hasBeenInactive = true;
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
        image.experiencePoints = experiencePoints;
        image.state = state;
        image.hasBeenInactive = hasBeenInactive;
        image.inactivityDuration = inactivityDuration;
        image.stateDuration = stateDuration;
        image.isAssembled = isAssembled;
        return image;
    }

    public boolean isUnitInItsCivilizationCities(){
        City city = this.location.getCityOfTile();
        if(city != null && city.getOwner().equals(this.getOwner()) && city.getCentralTile().equals(this.location)){
            return true;
        }
        return false;
    }

    public void goToNextTurn() {
        // TODO : very much incomplete
        movePointsLeft = type.getMovementSpeed();
        GameController.getGameController().moveUnitAlongItsPath(this);
        // TODO: heal:
        /*
            if it has been inactive for 1 turn: heal: modify healing pace based on doc, don't let HPs exceep max HPs
            make sure inactivityDuration is updated appropriately
        */
        // TODO : handle state duration, inactivity time, and healing(if the state is FORTIFYUNTILHEALED)
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

    public void attack(Unit target){
        //TODO
    }

    public void defend(Unit attacker){
        //TODO
    }

    public double calculateEffectiveCombatStrength() {
        // TODO : handle FORTIFY state on defensiveStrength and garrison effect
        return 0;
    }

    public double calculateEffectiveRangedCombatStrength() {
        // TODO
        return 0;
    }

    public void heal() { // replaces the setter for hitPointsLeft
        // TODO
    }

    public boolean isWaitingForCommand() {
        // TODO : might be incomplete
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

    public void move() { // like a setter for location : but it handles other things as well
        // TODO
        hasBeenInactive = false;
    }

    public Civilization getOwner() {
        return this.owner;
    }

    public Tile getLocation() {
        return this.location;
    }

    public void setLocation(Tile location) {
        this.location = location;
    }

    public int getHitPointsLeft() {
        return this.hitPointsLeft;
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

    public int getExperiencePoints() {
        return this.experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public void setState(UnitState state){
        if(state != UnitState.AWAKE){
            this.path = null;
        }
        this.state = state;
    }

    public UnitState getState() {
        return this.state;
    }

    public int getInactivityDuration() {
        return this.inactivityDuration;
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

    public UnitType getType(){
        return this.type;
    }

    public boolean isCivilian() {
        return (type.getCombatType() == CombatType.CIVILIAN);
    }
}