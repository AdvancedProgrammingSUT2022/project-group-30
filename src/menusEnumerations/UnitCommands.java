package menusEnumerations;

import java.util.ArrayList;

public class UnitCommands extends Commands {
    public static UnitCommands DESELECT = new UnitCommands("deselect", "deselect");
    public static UnitCommands MOVE_TO = new UnitCommands("move (?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)", "move y, x");
    public static UnitCommands SHOW_INFO = new UnitCommands("(show )?info", "show info");
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
