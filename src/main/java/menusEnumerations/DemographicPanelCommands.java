package menusEnumerations;

import java.util.ArrayList;

public class DemographicPanelCommands extends Commands{

    private static ArrayList<DemographicPanelCommands> allCommands;

    private String name;

    public DemographicPanelCommands(String regex, String name){
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

    public ArrayList<DemographicPanelCommands> getAllCommands(){
        return allCommands;
    }
}
