package menusEnumerations;

import java.util.ArrayList;

public class CityCommands extends Commands {
    public static final CityCommands SHOW_INFO = new CityCommands("(show )?info", "show info");
    public static final CityCommands SHOW_COMMANDS = new CityCommands("(show )?commands", "show commands");
    public static final CityCommands SHOW_CITIZEN_MANAGEMENT_PANEL = new CityCommands("(open )?citizen management", "open citizen management");
    public static final CityCommands SHOW_PRODUCTION_PANEL = new CityCommands("(open )?production( panel)?", "open production panel");
    public static final CityCommands PURCHASE_TILE = new CityCommands("purchase tile", "purchase tile");
    public static final CityCommands ATTACK = new CityCommands("attack (?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)", "attack y, x");
    public static final CityCommands DESELECT = new CityCommands("deselect", "deselect");

    private final String name;
    private static ArrayList<CityCommands> allCommands;

    private CityCommands(String regex, String name) {
        super(regex);
        this.name = name;

        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public static ArrayList<CityCommands> getAllCommands() {
        return new ArrayList<>(allCommands);
    }

    public String getName() {
        return name;
    }
}
