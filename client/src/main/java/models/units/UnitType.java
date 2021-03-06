package models.units;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;
import models.interfaces.Producible;
import models.resources.StrategicResource;
import models.technology.Technology;

import java.util.HashMap;

public enum UnitType implements Producible, EnumInterface {
    @SerializedName("Enum models.units.UnitType Archer")
    ARCHER("Archer", 70, CombatType.ARCHERY,
            4, 6, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.ARCHERY),
    @SerializedName("Enum models.units.UnitType Chariot Archer")
    CHARIOT_ARCHER("Chariot Archer", 60,
            CombatType.MOUNTED, 3, 6, 2, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.THE_WHEEL),
    @SerializedName("Enum models.units.UnitType Scout")
    SCOUT("Scout", 25, CombatType.RECON,
            4, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    @SerializedName("Enum models.units.UnitType Settler")
    SETTLER("Settler", 89, CombatType.CIVILIAN,
            0, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    @SerializedName("Enum models.units.UnitType Spearman")
    SPEARMAN("Spearman", 50, CombatType.MELEE,
            7, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.BRONZE_WORKING),
    @SerializedName("Enum models.units.UnitType Warrior")
    WARRIOR("Warrior", 40, CombatType.MELEE,
            6, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    @SerializedName("Enum models.units.UnitType Worker")
    WORKER("Worker", 70, CombatType.CIVILIAN,
            0, 0, 0, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.AGRICULTURE),
    @SerializedName("Enum models.units.UnitType Catapult")
    CATAPULT("Catapult", 100, CombatType.SIEGE,
            4, 14, 2, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.MATHEMATICS),
    @SerializedName("Enum models.units.UnitType Horseman")
    HORSEMAN("Horseman", 80, CombatType.MOUNTED,
            12, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE),
            Technology.HORSEBACK_RIDING),
    @SerializedName("Enum models.units.UnitType Swordsman")
    SWORDSMAN("Swordsman", 80, CombatType.MELEE,
            11, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.IRON_WORKING),
    @SerializedName("Enum models.units.UnitType Crossbowman")
    CROSSBOWMAN("Crossbowman", 120, CombatType.ARCHERY,
            6, 12, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.MACHINERY),
    @SerializedName("Enum models.units.UnitType Knight")
    KNIGHT("Knight", 150, CombatType.MOUNTED,
            18, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.CHIVALRY),
    @SerializedName("Enum models.units.UnitType Longswordsman")
    LONGSWORDSMAN("Longswordsman", 150, CombatType.MELEE,
            18, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.STEEL),
    @SerializedName("Enum models.units.UnitType Pikeman")
    PIKEMAN("Pikeman", 100, CombatType.MELEE,
            10, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.CIVIL_SERVICE),
    @SerializedName("Enum models.units.UnitType Trebuchet")
    TREBUCHET("Trebuchet", 170, CombatType.SIEGE,
            6, 20, 2, 2,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.IRON), Technology.PHYSICS),
    @SerializedName("Enum models.units.UnitType Cannon")
    CANNON("Cannon", 250, CombatType.SIEGE,
            10, 26, 2, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.CHEMISTRY),
    @SerializedName("Enum models.units.UnitType Cavalry")
    CAVALRY("Cavalry", 260, CombatType.MOUNTED,
            25, 0, 1, 3,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE),
            Technology.MILITARY_SCIENCE),
    @SerializedName("Enum models.units.UnitType Lancer")
    LANCER("Lancer", 220, CombatType.MOUNTED,
            22, 0, 1, 4,
            StrategicResource.getRequiredResourceHashMap(StrategicResource.HORSE), Technology.METALLURGY),
    @SerializedName("Enum models.units.UnitType Musketman")
    MUSKETMAN("Musketman", 120, CombatType.GUNPOWDER,
            16, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.GUNPOWDER),
    @SerializedName("Enum models.units.UnitType Rifleman")
    RIFLEMAN("Rifleman", 200, CombatType.GUNPOWDER,
            25, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.RIFLING),
    @SerializedName("Enum models.units.UnitType Anti-Tank Gun")
    ANTI_TANK_GUN("Anti-Tank Gun", 300, CombatType.GUNPOWDER,
            32, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.REPLACEABLE_PARTS),
    @SerializedName("Enum models.units.UnitType Artillery")
    ARTILLERY("Artillery", 420, CombatType.SIEGE,
            16, 32, 3, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.DYNAMITE),
    @SerializedName("Enum models.units.UnitType Infantry")
    INFANTRY("Infantry", 300, CombatType.GUNPOWDER,
            36, 0, 1, 2,
            StrategicResource.getRequiredResourceHashMap(), Technology.REPLACEABLE_PARTS),
    @SerializedName("Enum models.units.UnitType Panzer")
    PANZER("Panzer", 450, CombatType.ARMORED,
            60, 0, 1, 5,
            StrategicResource.getRequiredResourceHashMap(), Technology.COMBUSTION),
    @SerializedName("Enum models.units.UnitType Tank")
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

    @SerializedName("type")
    private String typeName;

    private UnitType(String name, int cost, CombatType combatType, int combatStrength, int rangedCombatStrength,
                     int range, int movementSpeed,
                     HashMap<StrategicResource, Integer> prerequisiteResources, Technology prerequisitTechnology) {
        this.typeName = getClass().getName();
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

    public int getRange() {
        return range;
    }

    public static UnitType getUnitTypeByName(String name) {
        for (UnitType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}