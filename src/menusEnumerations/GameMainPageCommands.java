package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMainPageCommands {
    COMMAND("");

    private String regex;

    private GameMainPageCommands(String regex){
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, GameMainPageCommands gameMainPageCommands){
        Matcher matcher = Pattern.compile(input).matcher(gameMainPageCommands.regex);
        return matcher.matches() ? matcher : null;
    }
    
}
