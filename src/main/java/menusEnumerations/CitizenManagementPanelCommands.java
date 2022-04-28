package menusEnumerations;

import java.util.ArrayList;

public class CitizenManagementPanelCommands extends Commands {
    public static final CitizenManagementPanelCommands SHOW_INFO = new CitizenManagementPanelCommands("(show )?info", "show info");
    public static final CitizenManagementPanelCommands BACK = new CitizenManagementPanelCommands("b(ack)?", "back");

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
