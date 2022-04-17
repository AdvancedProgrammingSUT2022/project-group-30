package controllers;

import Models.ProgramDatabase;
import views.GameView;
import views.ProfilePageView;
import views.View;

public class MainPageController {
    private static MainPageController mainPageController;

    private ProgramDatabase programDatabase;

    private MainPageController(){
        programDatabase = null;
    }

    public static MainPageController getMainPageController(){
        return mainPageController == null ? mainPageController = new MainPageController() : mainPageController;
    }

    public void logoutUser(){
        this.programDatabase.setLoggedInUser(null);
    }

    public boolean checkNextMenuValidity(String menuName){
        return menuName.equals("Game Menu") || menuName.equals("Profile Menu") ? true : false;
    }

    public View findTheNextMenu(String menuName){
        if(menuName.equals("Profile Menu")){
            return ProfilePageView.getProfilePageView();
        }
        if(menuName.equals("Game Menu")){
            return GameView.getGameView();
        }
        return null;
    }

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }
    
}
