package menusEnumerations;

import java.util.ArrayList;

public class DemographicPanelCommands extends Commands{

    public static DemographicPanelCommands TERRITORY_SIZE = new DemographicPanelCommands("(show )?(total )?territory size", "territory size");
    public static DemographicPanelCommands GOLD_COUNT = new DemographicPanelCommands("(show )?(total )?gold count", "gold count");
    public static DemographicPanelCommands RESOURCES = new DemographicPanelCommands("(show )?(all )?resources", "resources");
    public static DemographicPanelCommands IMPROVEMENTS = new DemographicPanelCommands("(show )?(all )?improvements", "improvements");
    public static DemographicPanelCommands LEARNED_TECHNOLOGIES = new DemographicPanelCommands("(show )?(all )?(learned )?technologies", "learned technologies");
    public static DemographicPanelCommands MILITARY_UNITS = new DemographicPanelCommands("(show )?(all )?military units", "military units");
    public static DemographicPanelCommands OUTPUT = new DemographicPanelCommands("(show )?(total )?output","output");
    public static DemographicPanelCommands SCORE = new DemographicPanelCommands("(show )?(total )?score", "score");
    public static DemographicPanelCommands SCOREBOARD = new DemographicPanelCommands("(show )?scoreboard", "scoreboard");
    public static DemographicPanelCommands BACK = new DemographicPanelCommands("back", "back");

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

    public static ArrayList<DemographicPanelCommands> getAllCommands(){
        return allCommands;
    }
}
