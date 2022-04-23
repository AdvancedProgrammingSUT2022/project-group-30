package menusEnumerations;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMainPageCommands extends Commands {
    public static GameMainPageCommands GET_TILE_INFO = new GameMainPageCommands("(info)?(?<y>\\d+)\\s*[:,]\\s*(?<x>\\d+)");
    public static GameMainPageCommands SELECT_UNIT = new GameMainPageCommands("select (unit)? (?<y>\\d+)\\s*[:,](?<x>\\d+)");
    public static GameMainPageCommands SELECT_CIVILIAN_UNIT = new GameMainPageCommands(
            "select civ(ilian unit)?\\s*(?<y>\\d+)\\s*[:,](?<x>\\d+)");
    public static GameMainPageCommands DESELECT = new GameMainPageCommands(
            "deselect");

    private String regex;

    GameMainPageCommands(String regex) {
        super(regex);
    }

    @Override
    public Matcher getCommandMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        return matcher;
    }
}
