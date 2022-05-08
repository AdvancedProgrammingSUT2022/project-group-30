package menusEnumerations;

import java.util.ArrayList;

public class UnitCommands extends Commands {
    public static UnitCommands DESELECT = new UnitCommands("deselect", "deselect");
    public static UnitCommands SET_UP_FOR_RANGED_ATTACK = new UnitCommands("set up( for ranged attack)?", "set up for ranged attack");
    public static UnitCommands MELEE_ATTACK = new UnitCommands("(melee )?attack", "melee attack");
    public static UnitCommands RANGED_ATTACK = new UnitCommands("ranged attack", "ranged attack");
    public static UnitCommands MOVE_TO = new UnitCommands("move (?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)", "move y, x");
    public static UnitCommands SHOW_INFO = new UnitCommands("(show )?info", "show info");
    public static UnitCommands FOUND_CITY = new UnitCommands("found city", "found city");
    public static UnitCommands SLEEP = new UnitCommands("sleep", "sleep");
    public static UnitCommands ALERT = new UnitCommands("alert", "alert");
    public static UnitCommands FORTIFY = new UnitCommands("fortify", "fortify");
    public static UnitCommands FORTIFY_UNTIL_HEALED = new UnitCommands("fortify until healed", "fortify until healed");
    public static UnitCommands GARRISON = new UnitCommands("garrison", "garrison");
    public static UnitCommands AWAKE = new UnitCommands("wake", "wake");
    public static UnitCommands DELETE = new UnitCommands("delete", "delete");
    public static UnitCommands CANCEL_MOVE = new UnitCommands("cancel move", "cancel move");
    // if you add a command, make the appropriate changes to the calculateAllwedCommands method of GameView
    
    private static ArrayList<UnitCommands> allCommands;

    private String name;
    private UnitCommands(String regex, String name) {
        super(regex);
        this.name = name;

        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public String getName() {
        return name;
    }

    public static ArrayList<UnitCommands> getAllCommands() {
        return new ArrayList<>(allCommands);
    }
}
