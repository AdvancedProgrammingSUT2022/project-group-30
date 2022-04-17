package controllers;

import Models.ProgramDatabase;

public class ProfilePageController {
    private static ProfilePageController profilePageController;

    private ProgramDatabase programDatabase;

    private ProfilePageController(){
        programDatabase = null;
    }

    public static ProfilePageController getProfilePageController(){
        return profilePageController == null ? new ProfilePageController() : profilePageController;
    }

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();

    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }
    
}
