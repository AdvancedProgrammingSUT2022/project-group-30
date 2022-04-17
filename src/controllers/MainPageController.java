package controllers;

import Models.ProgramDatabase;

public class MainPageController {
    private static MainPageController mainPageController;

    private ProgramDatabase programDatabase;

    private MainPageController(){
        programDatabase = null;
    }

    public static MainPageController getMainPageController(){
        return mainPageController == null ? new MainPageController() : mainPageController;
    }

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }
    
}
