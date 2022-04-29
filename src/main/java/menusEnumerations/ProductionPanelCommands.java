package menusEnumerations;

import java.util.ArrayList;

public class ProductionPanelCommands extends Commands {
    private static ArrayList<ProductionPanelCommands> allCommands = new ArrayList<>();

    public static ProductionPanelCommands BACK = new ProductionPanelCommands("back", "back");
    public static ProductionPanelCommands SHOW_INFO = new ProductionPanelCommands("(show )?info", "show info");

    private final String name;
    private ProductionPanelCommands(String regex, String name) {
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

    public static ArrayList<ProductionPanelCommands> getAllCommands() {
        return new ArrayList<>(allCommands);
    }
}
