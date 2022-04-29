package menusEnumerations;

import java.util.ArrayList;

public class ResearchCommands extends Commands{

    public static LEARNED_TECHNOLOGIES = new ResearchCommands("(show )?(my )?learned technologies","learned technologies");

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
