package menusEnumerations;

import java.util.ArrayList;

public class WorkerCommands extends Commands {
    public static WorkerCommands BUILD_ROAD = new WorkerCommands("build road", "build road");
    public static WorkerCommands BUILD_RAILROAD = new WorkerCommands("build railroad", "build railroad");
    public static WorkerCommands BUILD_FARM = new WorkerCommands("build farm", "build farm");
    public static WorkerCommands BUILD_MINE = new WorkerCommands("build mine", "build mine");
    public static WorkerCommands BUILD_TRADING_POST = new WorkerCommands("build trading post", "build trading post");
    public static WorkerCommands BUILD_LUMBER_MILL = new WorkerCommands("build lumber mill", "build lumber mill");
    public static WorkerCommands BUILD_PASTURE = new WorkerCommands("build pasture", "build pasture");
    public static WorkerCommands BUILD_PLANTATION = new WorkerCommands("build plantation", "build plantation");
    public static WorkerCommands BUILD_QUARRY = new WorkerCommands("build quarry", "build quarry");
    public static WorkerCommands BUILD_CAMP = new WorkerCommands("build camp", "build camp");
    public static WorkerCommands CLEAR_JUNGLE = new WorkerCommands("clear jungle", "clear jungle");
    public static WorkerCommands CLEAR_FOREST = new WorkerCommands("clear forrest", "clear forrest");
    public static WorkerCommands CLEAR_MARSH = new WorkerCommands("clear marsh", "clear marsh");
    public static WorkerCommands CLEAR_ROUTES = new WorkerCommands("clear routes", "clear routes");
    public static WorkerCommands FIX_IMPROVEMENT = new WorkerCommands("fix improvement (?<name>.*)", "fix improvement <improvement name>");

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
