package models.units;

import java.util.Arrays;
import java.util.ArrayList;

public enum UnitState {
    AWAKE(true),
    ASLEEP(false),
    FORTIFY(false),
    FORTIFYUNTILHEALED(false),
    GARRISON(false);

    public boolean waitsForCommand;

    private UnitState(boolean waitsForCommand) {
        this.waitsForCommand = waitsForCommand;
    }

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
}
