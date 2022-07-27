package views;

import views.customcomponents.CheatBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CheatCodes {
    UNLOCK_TECHNOLOGY("alohomora (?<name>.*)"),

    // MAIN_PAGE
    MAKE_EVERYTHING_VISIBLE("let there be light"),
    ADD_GOLD("cash ziad eine hatami"),
    DISABLE_TURN_BREAK("jenab sarvan welam kon"),
    ADD_STRATEGIC_RESOURCE("need me some (?<name>.*)"),
    ADD_LUXURY_RESOURCE("asan luxury shodan monshiam (?<name>.*)"),
    ADD_UNIT("send dudes (?<name>.*)[-: ](?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)"),
    KILL_UNIT("avada kedavra (?<y>\\d+)\\s*[,: ]\\s*(?<x>\\d+)"),
    MAKE_IMPROVEMENT("do you wanna build a snowman (?<name>.*) (?<y>\\d+)\\s*[:, ](?<x>\\d+)"),
    DEPLOY_FEATURE("deploy feature (?<name>.*) (?<y>\\d+)\\s*[:, ](?<x>\\d+)"),
    CLEAR_ALL_FEATURES("smite (?<y>\\d+)\\s*[:, ](?<x>\\d+)"),
    WRITE_TO_FILE("write");


    private String regex;

    CheatCodes(String regex) {
        this.regex = regex;
    }

    public Matcher getMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
