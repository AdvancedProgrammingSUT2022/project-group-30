package controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    private String regex;

    protected Commands(String regex) {
        this.regex = regex;
    }
    
    public Matcher getCommandMatcher(String command) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        return matcher;
    }
}
