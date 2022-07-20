package controllers;

import models.ProgramDatabase;
import models.User;
import terminalViews.GameView;
import terminalViews.ProfilePageView;
import terminalViews.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainPageController {
    private static MainPageController mainPageController;

    private ProgramDatabase programDatabase;

    private MainPageController() {
        programDatabase = null;
    }

    public static MainPageController getMainPageController() {
        return mainPageController == null ? mainPageController = new MainPageController() : mainPageController;
    }

    public void logoutUser() {
        ProgramDatabase.getProgramDatabase().updateLoggedInUserLastLoginTime();
        LoginPageController.writeUsersListToFile();
        this.programDatabase.setLoggedInUser(null);
    }

    public boolean checkNextMenuValidity(String menuName) {
        return menuName.equals("Game Menu") || menuName.equals("Profile Menu") ? true : false;
    }

    public View findTheNextMenu(String menuName) {
        if (menuName.equals("Profile Menu")) {
            return ProfilePageView.getProfilePageView();
        }
        if (menuName.equals("Game Menu")) {
            return GameView.getGameView();
        }
        return null;
    }

    public void sortUsersArrayList(){
        ArrayList<User> users = ProgramDatabase.getProgramDatabase().getUsers();
        Comparator<User> comparator = Comparator.comparing(User::getScore).reversed().thenComparing(User::getLastScoreChangeTime).thenComparing(User::getUsername);
        Collections.sort(users, comparator);
        for(int i = 0; i < users.size(); i++){
            users.get(i).setRank(i + 1);
        }
    }

    public void setProgramDatabase() {
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase() {
        return this.programDatabase;
    }

}
