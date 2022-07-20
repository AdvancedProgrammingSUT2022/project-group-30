package menusEnumerations;

import java.util.ArrayList;

public class CitizenManagementPanelCommands extends Commands {
    public static final CitizenManagementPanelCommands SHOW_INFO = new CitizenManagementPanelCommands("(show )?info", "show info");
    public static final CitizenManagementPanelCommands BACK = new CitizenManagementPanelCommands("b(ack)?", "back");
    public static final CitizenManagementPanelCommands WORK_TILE = new CitizenManagementPanelCommands("work tile (?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)",
            "work tile y, x");
    public static final CitizenManagementPanelCommands FREE_TILE = new CitizenManagementPanelCommands("free tile (?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)",
            "free tile y, x");

    private final String name;
    private static ArrayList<CitizenManagementPanelCommands> allCommands;

    private CitizenManagementPanelCommands(String regex, String name) {
        super(regex);
        this.name = name;

        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public static ArrayList<CitizenManagementPanelCommands> getAllCommands() {
        return new ArrayList<>(allCommands);
    }

    public String getName() {
        return name;
    }
}
