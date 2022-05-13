package menusEnumerations;

import java.util.ArrayList;

public class ResearchCommands extends Commands {
    public static ResearchCommands LEARNED_TECHNOLOGIES = new ResearchCommands("(show )?(my )?learned technologies", "learned technologies");
    public static ResearchCommands UNLOCKED_TECHNOLOGIES = new ResearchCommands("(show )?(my )?unlocked technologies", "unlocked technologies");
    public static ResearchCommands START_RESEARCH = new ResearchCommands("start a research( project)?", "start research");
    public static ResearchCommands STOP_RESEARCH = new ResearchCommands("stop researching", "stop research");
    public static ResearchCommands CHANGE_RESEARCH = new ResearchCommands("change research( project)?", "change research");
    public static ResearchCommands RESERVED_RESEARCHES = new ResearchCommands("(show )?reserved researches", "reserved researches");
    public static ResearchCommands BACK = new ResearchCommands("back", "back");
    public static ResearchCommands SHOW_CURRENT_INFO = new ResearchCommands("(show )?(current )?research info", "show current research info");
    public static ResearchCommands SHOW_COMMANDS = new ResearchCommands("show commands", "show commands");


    // CHEAT CODES:
    public static ResearchCommands LEARN_TECHNOLOGY = new ResearchCommands("alohomora (?<name>.*)");


    private static ArrayList<ResearchCommands> allCommands;
    private String name;

    public ResearchCommands(String regex, String name) {
        super(regex);
        this.name = name;
        if (allCommands == null) {
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public ResearchCommands(String regex) {
        super(regex);
        this.name = "Cheat Code";
    }

    public String getName() {
        return this.name;
    }

    public static ArrayList<ResearchCommands> getAllCommands() {
        return allCommands;
    }
}
