package Models.menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginPageCommands {
    CREATE_USER("user create ((--username (?<username>.+) --nickname (?<nickname>.+) --password (?<password>.+))|(--username (?<username>.+) --password (?<password>.+) --nickname (?<nickname>.+))|(--nickname (?<nickname>.+) --username (?<username>.+) --password (?<password>.+))|(--nickname (?<nickname>.+) --password (?<password>.+) --username (?<username>.+))|(--password (?<password>.+) --nickname (?<nickname>.+) --username (?<username>.+))|(--password (?<password>.+) --username (?<username>.+) --nickname (?<nickname>.+)))"),
    CREATE_USER_ABBREVIATED_FORM("user create ((-u (?<username>.+) -n (?<nickname>.+) -p (?<password>.+))|(-u (?<username>.+) -p (?<password>.+) -n (?<nickname>.+))|(-n (?<nickname>.+) -u (?<username>.+) -p (?<password>.+))|(-n (?<nickname>.+) -p (?<password>.+) -u (?<username>.+))|(-p (?<password>.+) -n (?<nickname>.+) -u (?<username>.+))|(-p (?<password>.+) -u (?<username>.+) -n (?<nickname>.+)))"),
    LOGIN_USER("user login ((--username (?<username>.+) --password (?<password>.+))|(--password (?<password>.+) --username (?<username>.+)))"),
    LOGIN_USER_ABBREVIATED_FORM("user login ((-u (?<username>.+) -p (?<password>.+))|(-p (?<password>.+) -u (?<username>.+)))"),
    EXIT_MENU("menu exit"),
    SHOW_MENU("menu show-current");

    String regex;

    private LoginPageCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, LoginPageCommands loginPageCommand) {
        Matcher matcher = Pattern.compile(loginPageCommand.regex).matcher(input);
        return matcher.matches() ? matcher : null;
    }

}
