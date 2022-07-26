package controllers;

import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.TokenData;
import terminalViews.GameView;
import terminalViews.ProfilePageView;
import terminalViews.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramController {
    private static ProgramController programController;
    private ProgramController() {
        database = ProgramDatabase.getProgramDatabase();
    }

    public static ProgramController getProgramController() {
        if (programController == null) {
            programController = new ProgramController();
        }
        return programController;
    }

    private ProgramDatabase database;

    public void hackIntoChat(int token) {
        LoginPageController.readUsersListFromFile();
        database.setLoggedInUser(getUserById(0), token);
    }

    public void hackIntoChat1(int token) {
        LoginPageController.readUsersListFromFile();
        database.setLoggedInUser(getUserById(1), token);
    }

    public TokenData fetchTokenData(int token) {
        return database.fetchTokenData(token);
    }

    public ArrayList<TokenData> getTokenData() {
        return database.getTokenData();
    }

    public User getUserByUsername(String username) {
        return database.getUserByUsername(username);
    }

    public User getLoggedInUser(int token) {
        return database.getLoggedInUser(token);
    }

    public User getUserById(int id) {
        return database.getUserById(id);
    }


    public boolean checkUsernameValidity(String username) {
        Matcher matcher = Pattern.compile("[a-zA-Z0-9]+").matcher(username);
        return matcher.matches() ? true : false;
    }

    public boolean checkPasswordSize(String password) {
        return password.length() < 8 ? false : true;
    }

    public boolean checkPasswordValidity(String password) {
        Matcher uppercaseMatcher = Pattern.compile(".*[A-Z]+.*").matcher(password);
        Matcher lowercaseMatcher = Pattern.compile(".*[a-z]+.*").matcher(password);
        Matcher numberMatcher = Pattern.compile(".*[0-9]+.*").matcher(password);
        if (uppercaseMatcher.matches() && lowercaseMatcher.matches() && numberMatcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean checkNicknameValidity(String nickname) {
        Matcher matcher = Pattern.compile("[a-zA-Z0-9_-]{1}\\s*[a-zA-Z0-9_-]*").matcher(nickname);
        return matcher.matches() ? true : false;
    }


    // ...

    public boolean checkUsernameUniqueness(String username) {
        User user = this.database.getUserByUsername(username);
        return user == null ? true : false;
    }

    public boolean checkNicknameUniqueness(String nickname) {
        User user = this.database.getUserByNickname(nickname);
        return user == null ? true : false;
    }

    public void addUser(User user) {
        this.database.getUsers().add(user);
        LoginPageController.writeUsersListToFile();
    }

    // returns true if there was an error and false otherwise
    public boolean checkLoginErrors(String username, String password) {
        User user = this.database.getUserByUsername(username);
        if (user == null) {
            return true;
        }
        if (!user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean isUserLoggedIn() {
        // NEEDS CHANGING
        return this.database.getLoggedInUser(0) == null ? false : true;
    }

    public boolean checkNextMenuValidity(String menuName) {
        return menuName.equals("Main Menu") ? true : false;
    }

    public void loginUser(String username, int token) {
        User user = this.database.getUserByUsername(username);
        this.database.setLoggedInUser(user, token);
        ProgramDatabase.getProgramDatabase().updateLoggedInUserLastLoginTime(token);
        LoginPageController.writeUsersListToFile();
        getUserById(fetchTokenData(token).getLoggedInUser()).setIsOnline(true);
    }


    public void setProgramDatabase() {
        this.database = ProgramDatabase.getProgramDatabase();
        LoginPageController.readUsersListFromFile();
    }

    public ProgramDatabase getProgramDatabase() {
        return this.database;
    }

    public void changeLoggedInUserNickname(String nickname) {
        this.database.getLoggedInUser().setNickname(nickname);
        LoginPageController.writeUsersListToFile();
    }

    public void changeLoggedInUserPassword(String newPassword) {
        this.database.getLoggedInUser().setPassword(newPassword);
        LoginPageController.writeUsersListToFile();
    }

    public boolean checkNewPasswordAndCurrentPasswordEquality(String currentPassword, String newPassword) {
        return currentPassword.equals(newPassword) ? true : false;
    }

    // returns true if the password is correct, false otherwise
    public boolean checkLoggedInUserPassword(String password) {
        if (this.database.getLoggedInUser().getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public String getLoggedInUserImageName(){
        return this.database.getLoggedInUser().getImageName();
    }

    public void changeLoggedInUsersProfileImage(String imageName){
        this.database.getLoggedInUser().setImageName(imageName);
        LoginPageController.writeUsersListToFile();
    }

    public void registerUser(String username, String password, String nickname, int token){
        User user = new User(username, password, nickname, 0);
        addUser(user);
        loginUser(username, token);
    }

    public void logoutUser(int token) {
        ProgramDatabase.getProgramDatabase().updateLoggedInUserLastLoginTime(token);
        LoginPageController.writeUsersListToFile();
        this.database.setLoggedInUser(null);
        getUserById(fetchTokenData(token).getLoggedInUser()).setIsOnline(false);
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

    public ArrayList<User> getUsers(){
        return database.getUsers();
    }

    public String getLoggedInUserUsername(int token){
        return ProgramDatabase.getProgramDatabase().getLoggedInUser(token).getUsername();
    }

}
