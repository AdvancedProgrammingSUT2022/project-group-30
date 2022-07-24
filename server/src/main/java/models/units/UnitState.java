package models.units;

import com.google.gson.annotations.SerializedName;
import models.interfaces.EnumInterface;

import java.util.ArrayList;
import java.util.Arrays;

public enum UnitState implements EnumInterface {
    @SerializedName("Enum models.units.UnitState Awake")
    AWAKE(true, "Awake"),
    @SerializedName("Enum models.units.UnitState Asleep")
    ASLEEP(false, "Asleep"),
    @SerializedName("Enum models.units.UnitState Alert")
    ALERT(false, "Alert"),
    @SerializedName("Enum models.units.UnitState Fortify")
    FORTIFY(false, "Fortify"),
    @SerializedName("Enum models.units.UnitState FortifyUntilHealed")
    FORTIFYUNTILHEALED(false, "FortifyUntilHealed"),
    @SerializedName("Enum models.units.UnitState Garrison")
    GARRISON(false, "Garrison");

    public final boolean waitsForCommand;

    private UnitState(boolean waitsForCommand, String name) {
        this.waitsForCommand = waitsForCommand;
        this.name = name;
    }
    private String name;

    public static ArrayList<UnitState> getStateList(UnitState... exceptions) {
        ArrayList<UnitState> result = new ArrayList<UnitState>();
        for (UnitState state : UnitState.values()) {
            if (Arrays.asList(exceptions).contains(state)) {
                continue;
            }
            result.add(state);
        }
        return result;
    }

    public boolean waitsForCommand() {
        return waitsForCommand;
    }

    public String getName(){
        return this.name;
    }
}
