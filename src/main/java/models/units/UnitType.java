package models.units;

import java.util.ArrayList;
import java.util.HashMap;

import models.interfaces.Producible;
import models.resources.StrategicResource;
import models.technology.Technology;

public enum UnitType implements Producible {
    ARCHER("Archer", 70, CombatType.ARCHERY,
            4, 6, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.ARCHERY),
    CHARIOT_ARCHER("Chariot Archer", 60,
            CombatType.MOUNTED, 3, 6, 2, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.THE_WHEEL),
    SCOUT("Scout", 25, CombatType.RECON,
            4, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    SETTLER("Settler", 89, CombatType.CIVILIAN,
            0, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    SPEARMAN("Spearman", 50, CombatType.MELEE,
            7, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.BRONZE_WORKING),
    WARRIOR("Warrior", 40, CombatType.MELEE,
            6, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    WORKER("Worker", 70, CombatType.CIVILIAN,
            0, 0, 0, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    CATAPULT("Catapult", 100, CombatType.SIEGE,
            4, 14, 2, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.MATHEMATICS),
    HORSEMAN("Horseman", 80, CombatType.MOUNTED,
            12, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE),
            Technology.HORSEBACK_RIDING),
    SWORDSMAN("Swordsman", 80, CombatType.MELEE,
            11, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.IRON_WORKING),
    CROSSBOWMAN("Crossbowman", 120, CombatType.ARCHERY,
            6, 12, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.MACHINERY),
    KNIGHT("Knight", 150, CombatType.MOUNTED,
            18, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.CHIVALRY),
    LONGSWORDSMAN("Longswordsman", 150, CombatType.MELEE,
            18, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.STEEL),
    PIKEMAN("Pikeman", 100, CombatType.MELEE,
            10, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.CIVIL_SERVICE),
    TREBUCHET("Trebuchet", 170, CombatType.SIEGE,
            6, 20, 2, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.PHYSICS),
    CANNON("Cannon", 250, CombatType.SIEGE,
            10, 26, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.CHEMISTRY),
    CAVALRY("Cavalry", 260, CombatType.MOUNTED,
            25, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE),
            Technology.MILITARY_SCIENCE),
    LANCER("Lancer", 220, CombatType.MOUNTED,
            22, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.METALLURGY),
    MUSKETMAN("Musketman", 120, CombatType.GUNPOWDER,
            16, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.GUNPOWDER),
    RIFLEMAN("Rifleman", 200, CombatType.GUNPOWDER,
            25, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.RIFLING),
    ANTI_TANK_GUN("Anti-Tank Gun", 300, CombatType.GUNPOWDER,
            32, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.REPLACEABLE_PARTS),
    ARTILLERY("Artillery", 420, CombatType.SIEGE,
            16, 32, 3, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.DYNAMITE),
    INFANTRY("Infantry", 300, CombatType.GUNPOWDER,
            36, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.REPLACEABLE_PARTS),
    PANZER("Panzer", 450, CombatType.ARMORED,
            60, 0, 1, 5,
            StrategicResource.getRequiredResourceHashMap(), Technology.COMBUSTION),
    TANK("Tank", 450, CombatType.ARMORED,
            50, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(), Technology.COMBUSTION);

    protected final String name;
    protected final CombatType combatType;
    protected final int combatStrength;
    protected final int rangedCombatStrength;
    protected final int range;
    protected final int hitPoints;
    protected final int movementSpeed;
    protected final HashMap<StrategicResource, Integer> prerequisiteResources;
    protected final Technology prerequisiteTechnology;
    protected final int cost;

    private UnitType(String name, int cost, CombatType combatType, int combatStrength, int rangedCombatStrength,
                     int range, int movementSpeed,
                     HashMap<StrategicResource, Integer> prerequisiteResources, Technology prerequisitTechnology) {
        this.name = name;
        this.combatType = combatType;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.hitPoints = 10;
        this.movementSpeed = movementSpeed;
        this.prerequisiteTechnology = prerequisitTechnology;
        this.prerequisiteResources = prerequisiteResources;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public Technology getPrerequisitTechnology() {
        return prerequisiteTechnology;
    }

    public HashMap<StrategicResource, Integer> getPrerequisiteResources() {
        return prerequisiteResources;
    }

    public int getCost() {
        return cost;
    }

    public boolean isRanged() {
        if (range > 1) {
            return true;
        }
        return false;
    }

    public boolean needsAssmbly() {
        return combatType.needsAssembly();
    }

    public boolean isStateAllowed(UnitState state) {
        return combatType.isStateAllowed(state);
    }

    public CombatType getCombatType() {
        return combatType;
    }

    public int calculateHammerCost() {
        return (int) cost / 10;
    }

    public boolean isCivilian() {
        return (combatType == CombatType.CIVILIAN);
    }
}