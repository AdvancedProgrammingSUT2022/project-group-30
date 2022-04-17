package views;

import controllers.ProfilePageController;

public class ProfilePageView implements View{

    private static ProfilePageView profilePageView;

    private ProfilePageController controller;

    private ProfilePageView(){
        controller = null;
    }

    public static ProfilePageView getProfilePageView(){
        return profilePageView == null ? new ProfilePageView() : profilePageView;
    }

    public void run(){

    }

    public void showMenu(){
        
    }

    public void setController(){
        this.controller = ProfilePageController.getProfilePageController();
        this.controller.setProgramDatabase();
    }

    public ProfilePageController getController(){
        return this.controller;
    }
    
}
