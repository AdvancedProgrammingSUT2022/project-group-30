package views;

import java.util.HashMap;

import menusEnumerations.Commands;

public abstract class Tab {
    protected Tab() {
        commands = new HashMap<>();
    }
    protected HashMap<Commands,Boolean> commands;
    public abstract void initialize();   // populate the commands hashmap, allowed commands get true, and any other necessary inits
    public abstract boolean checkCommand(String command);   // check if the command matches any of the allowed commands, return true if so
    public abstract void printCommands();   // print your own commands : show the tab to the user
    public abstract void printTab();    // print everything on the tab : commands, info, etc.
}