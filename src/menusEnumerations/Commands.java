package menusEnumerations;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    private String regex;
    private String name;

    protected Commands(String regex) {
        this.regex = regex;
        name = "no name";
    }

    protected Commands(String regex, String name) {
        this.regex = regex;
        this.name = name;
    }
    
    public Matcher getCommandMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        return matcher;
    }

    public String getName() {
        return name;
    }
}
