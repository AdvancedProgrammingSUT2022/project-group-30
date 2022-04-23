package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.Commands;

public class GameMainPageCommands extends Commands {
    public static GameMainPageCommands GET_TILE_INFO = new GameMainPageCommands("(info)?(?<x>\\d+)\\s*[:,]\\s*(?<y>\\d+)");
    public static GameMainPageCommands SELECT_UNIT = new GameMainPageCommands("select unit (?<x>\\d+)\\s*[:,](?<x>\\d+)");
    public static GameMainPageCommands SELECT_CIVILIAN_UNIT = new GameMainPageCommands(
            "select civ(ilian unit)?\\s*(?<x>\\d+)\\s*[:,](?<y>\\d+)");

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
