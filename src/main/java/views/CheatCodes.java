package views;

import views.customcomponents.CheatBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CheatCodes {
    UNLOCK_TECHNOLOGY("alohomora (?<name>.*)");

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
