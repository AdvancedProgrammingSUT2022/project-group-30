package menusEnumerations;

import java.util.ArrayList;

public class CityCommands extends Commands {
    public static final CityCommands SHOW_INFO = new CityCommands("(show )?info", "show info");


    private String name;
    private static ArrayList<CityCommands> allCommands;

    private CityCommands(String regex, String name) {
        super(regex);
        this.name = name;

        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public ArrayList<CityCommands> getAllCommands() {
        return new ArrayList<>(allCommands);
    }
}
