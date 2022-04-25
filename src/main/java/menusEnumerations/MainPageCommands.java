package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainPageCommands {
    LOGOUT_USER("user logout"),
    SHOW_MENU("menu show-current"),
    EXIT_MENU("menu exit"),
    ENTER_MENU("menu enter (?<menuName>.+)");

    private String regex;

    private MainPageCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, MainPageCommands mainPageCommands) {
        Matcher matcher = Pattern.compile(mainPageCommands.regex).matcher(input);
        return matcher.matches() ? matcher : null;
    }

}
