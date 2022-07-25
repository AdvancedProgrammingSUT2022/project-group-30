package models.units;

import java.util.ArrayList;

public enum CombatType  implements java.io.Serializable{
    ARCHERY(false, UnitState.getStateList()),
    MOUNTED(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED)),
    RECON(false, UnitState.getStateList()),
    CIVILIAN(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED, UnitState.GARRISON)),
    MELEE(false, UnitState.getStateList()),
    SIEGE(true, UnitState.getStateList()),
    GUNPOWDER(false, UnitState.getStateList()),
    ARMORED(false, UnitState.getStateList(UnitState.FORTIFY, UnitState.FORTIFYUNTILHEALED));

    private boolean needsAssembly;
    private ArrayList<UnitState> allowedStates;

    private CombatType(boolean needsAssembly, ArrayList<UnitState> allowedStates) {
        this.needsAssembly = needsAssembly;
        this.allowedStates = allowedStates;
    }

    public boolean isStateAllowed(UnitState state) {
        return allowedStates.contains(state);
    }

    public boolean needsAssembly() {
        return needsAssembly;
    }
}
