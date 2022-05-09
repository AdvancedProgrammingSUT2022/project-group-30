package menusEnumerations;

import java.util.ArrayList;

public class WorkerCommands extends Commands {


    private static ArrayList<WorkerCommands> allCommands;

    private String name;

    private WorkerCommands(String regex, String name) {
        super(regex);
        this.name = name;

        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public String getName() {
        return name;
    }

    public static ArrayList<WorkerCommands> getAllCommands() {
        return new ArrayList<WorkerCommands>(allCommands);
    }
}
