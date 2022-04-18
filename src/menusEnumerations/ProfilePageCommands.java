package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfilePageCommands {

    CHANGE_NICKNAME("profile change ((--nickname)|(-n)) (?<nickname>.+)"),
    CHANGE_PASSWORD_PATTERN_1("profile change ((--password)|(-p)) (((--current)|(-c)) (?<currentPassword>\\S+) ((--new)|(-n)) (?<newPassword>\\S+))"),
    CHANGE_PASSWORD_PATTERN_2("profile change ((--password)|(-p)) (((--new)|(-n)) (?<newPassword>\\S+) ((--current)|(-c)) (?<currentPassword>\\S+))"),
    SHOW_MENU("menu show-current"),
    EXIT_MENU("menu exit"),
    ENTER_MENU("menu enter (?<menuName>.+)");

    private String regex;

    private ProfilePageCommands(String regex){
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, ProfilePageCommands profilePageCommands){
        Matcher matcher = Pattern.compile(profilePageCommands.regex).matcher(input);
        return matcher.matches() ? matcher : null;
    }
    
}
