package menusEnumerations;

import java.util.ArrayList;

public class UnitActionsTabCommands extends Commands {
    public static ArrayList<Commands> allCommands = new ArrayList<>();

    public static UnitActionsTabCommands MOVE_TO = new UnitActionsTabCommands("move( to)? (?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)", "move to y,x");
    public static UnitActionsTabCommands MOVE = new UnitActionsTabCommands("move", "move");
    public static UnitActionsTabCommands SHOW_INFO = new UnitActionsTabCommands("(show )?unit info", "show unit info");

    UnitActionsTabCommands(String regex) {
        super(regex);
        allCommands.add(this);
    }
    
    UnitActionsTabCommands(String regex, String name) {
        super(regex, name);
        allCommands.add(this);
    }

    public static ArrayList<Commands> getAllCommands() {
        return allCommands;
    }
}
