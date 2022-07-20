package menusEnumerations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    protected String regex;

    protected Commands(String regex) {
        this.regex = regex;
    }

    public Matcher getCommandMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
