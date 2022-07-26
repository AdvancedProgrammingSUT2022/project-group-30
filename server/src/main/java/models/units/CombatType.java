package models.units;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;

import java.util.ArrayList;

public enum CombatType implements EnumInterface {
    @SerializedName("Enum models.units.CombatType Archery")
    ARCHERY(false, UnitState.getStateList(), "Archery"),
    @SerializedName("Enum models.units.CombatType Mounted")
    MOUNTED(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED), "Mounted"),
    @SerializedName("Enum models.units.CombatType Recon")
    RECON(false, UnitState.getStateList(), "Recon"),
    @SerializedName("Enum models.units.CombatType Civilian")
    CIVILIAN(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED, UnitState.GARRISON), "Civilian"),
    @SerializedName("Enum models.units.CombatType Melee")
    MELEE(false, UnitState.getStateList(), "Melee"),
    @SerializedName("Enum models.units.CombatType Siege")
    SIEGE(true, UnitState.getStateList(), "Siege"),
    @SerializedName("Enum models.units.CombatType Gunpowder")
    GUNPOWDER(false, UnitState.getStateList(), "Gunpowder"),
    @SerializedName("Enum models.units.CombatType Armored")
    ARMORED(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED), "Armored");

    private boolean needsAssembly;
    private ArrayList<UnitState> allowedStates;
    private String name;

    private CombatType(boolean needsAssembly, ArrayList<UnitState> allowedStates, String name) {
        this.needsAssembly = needsAssembly;
        this.allowedStates = allowedStates;
        this.name = name;
    }

    public boolean isStateAllowed(UnitState state) {
        return allowedStates.contains(state);
    }

    public boolean needsAssembly() {
        return needsAssembly;
    }

    public String getName(){
        return this.name;
    }
}