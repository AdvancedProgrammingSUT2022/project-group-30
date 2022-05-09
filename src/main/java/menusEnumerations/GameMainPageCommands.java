package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMainPageCommands extends Commands {
    public static GameMainPageCommands SHOW_MAP = new GameMainPageCommands("show map");
    public static GameMainPageCommands GET_TILE_INFO = new GameMainPageCommands("(info )?(?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands SHOW_INFO = new GameMainPageCommands("(show )?info");
    public static GameMainPageCommands SELECT_CITY = new GameMainPageCommands("select city (?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands SELECT_UNIT = new GameMainPageCommands("select (unit )?(?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands SELECT_CIVILIAN_UNIT = new GameMainPageCommands(
            "select civ(ilian( unit)?)?\\s*(?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands RIGHT = new GameMainPageCommands("r(ight)?(?<count> \\d+)?");
    public static GameMainPageCommands LEFT = new GameMainPageCommands("l(eft)?(?<count> \\d+)?");
    public static GameMainPageCommands UP = new GameMainPageCommands("u(p)?(?<count> \\d+)?");
    public static GameMainPageCommands DOWN = new GameMainPageCommands("d(own)?(?<count> \\d+)?");
    public static GameMainPageCommands MOVE_FRAME_TO = new GameMainPageCommands("move frame to (?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
    public static GameMainPageCommands GO_TO_NEXT_TURN = new GameMainPageCommands("(go to )?next( turn)?");
    public static GameMainPageCommands SHOW_UNITS = new GameMainPageCommands("(show )?units");
    public static GameMainPageCommands MAKE_VISIBLE = new GameMainPageCommands("let there be light");
    public static GameMainPageCommands RESEARCH_TAB = new GameMainPageCommands("(show )?research tab");
    public static GameMainPageCommands UNITS_INFO = new GameMainPageCommands("units panel");
    public static GameMainPageCommands CITIES_INFO = new GameMainPageCommands("cities panel");
    public static GameMainPageCommands NOTIFICATION_HISTORY = new GameMainPageCommands("(show )notification history");
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