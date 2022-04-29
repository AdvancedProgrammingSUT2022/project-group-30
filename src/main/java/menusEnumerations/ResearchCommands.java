package menusEnumerations;

import java.util.ArrayList;

public class ResearchCommands extends Commands{

    public static ResearchCommands LEARNED_TECHNOLOGIES = new ResearchCommands("(show )?(my )?learned technologies", "learned technologies");
    public static ResearchCommands UNLOCKED_TECHNOLOGIES = new ResearchCommands("(show )?(my )?unlocked technologies", "unlocked technologies");
    public static ResearchCommands START_RESEARCH = new ResearchCommands("start a research( project)?", "start research");
    public static ResearchCommands STOP_RESEARCH = new ResearchCommands("stop researching", "stop research");
    public static ResearchCommands CHANGE_RESEARCH = new ResearchCommands("change research( project)?", "change research");
    public static ResearchCommands RESERVED_RESEARCHES = new ResearchCommands("(show )?reserved researches", "reserved researches");

    private static ArrayList<ResearchCommands> allCommands;
    private  String name;

    public ResearchCommands(String regex, String name){
        super(regex);
        this.name = name;
        if(allCommands == null){
            allCommands = new ArrayList<>();
        }
        allCommands.add(this);
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<ResearchCommands> getAllCommands(){
        return allCommands;
    }
}
