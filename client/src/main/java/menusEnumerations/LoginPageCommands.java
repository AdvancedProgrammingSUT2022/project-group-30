package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginPageCommands {
    CREATE_USER_PATTERN_1(
            "user create (((--username)|(-u)) (?<username>\\S+) ((--nickname)|(-n)) (?<nickname>.+) ((--password)|(-p)) (?<password>\\S+))"),
    CREATE_USER_PATTERN_2(
            "user create (((--password)|(-p)) (?<password>\\S+) ((--username)|(-u)) (?<username>\\S+) ((--nickname)|(-n)) (?<nickname>.+))"),
    CREATE_USER_PATTERN_3(
            "user create (((--password)|(-p)) (?<password>\\S+) ((--nickname)|(-n)) (?<nickname>.+) ((--username)|(-u)) (?<username>\\S+))"),
    CREATE_USER_PATTERN_4(
            "user create (((--nickname)|(-n)) (?<nickname>.+) ((--password)|(-p)) (?<password>\\S+) ((--username)|(-u)) (?<username>\\S+))"),
    CREATE_USER_PATTERN_5(
            "user create (((--nickname)|(-n)) (?<nickname>.+) ((--username)|(-u)) (?<username>\\S+) ((--password)|(-p)) (?<password>\\S+))"),
    CREATE_USER_PATTERN_6(
            "user create (((--username)|(-u)) (?<username>\\S+) ((--password)|(-p)) (?<password>\\S+) ((--nickname)|(-n)) (?<nickname>.+))"),
    LOGIN_USER_PATTERN_1("user login (((--username)|(-u)) (?<username>\\S+) ((--password)|(-p)) (?<password>\\S+))"),
    LOGIN_USER_PATTERN_2("user login (((--password)|(-p)) (?<password>\\S+) ((--username)|(-u)) (?<username>\\S+))"),
    EXIT_MENU("menu exit"),
    SHOW_MENU("menu show-current"),
    ENTER_MENU("menu enter (?<menuName>.+)");

    private String regex;

    private LoginPageCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, LoginPageCommands loginPageCommand) {
        Matcher matcher = Pattern.compile(loginPageCommand.regex).matcher(input);
        return matcher.matches() ? matcher : null;
    }

}
