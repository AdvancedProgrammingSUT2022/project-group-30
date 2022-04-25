package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMainPageCommands extends Commands {
    public static GameMainPageCommands SHOW_MAP = new GameMainPageCommands("show map");
    public static GameMainPageCommands GET_TILE_INFO = new GameMainPageCommands("(info )?(?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands SELECT_UNIT = new GameMainPageCommands("select (unit )?(?<y>\\d+)\\s*[:, ](?<x>\\d+)");
    public static GameMainPageCommands SELECT_CIVILIAN_UNIT = new GameMainPageCommands(
            "select civ(ilian( unit)?)?\\s*(?<y>\\d+)\\s*[:, ](?<x>\\d+)");
    public static GameMainPageCommands RIGHT = new GameMainPageCommands("r(ight)?(?<count> \\d+)?");
    public static GameMainPageCommands LEFT = new GameMainPageCommands("l(eft)?(?<count> \\d+)?");
    public static GameMainPageCommands UP = new GameMainPageCommands("u(p)?(?<count> \\d+)?");
    public static GameMainPageCommands DOWN = new GameMainPageCommands("d(own)?(?<count> \\d+)?");
    public static GameMainPageCommands MOVE_FRAME_TO = new GameMainPageCommands("move frame to (?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");

    // move frame to x,y
    // pass the turn
    // show info about civ

    GameMainPageCommands(String regex) {
        super(regex);
    }

    @Override
    public Matcher getCommandMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}