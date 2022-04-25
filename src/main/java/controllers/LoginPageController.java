package controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.ProgramDatabase;
import models.User;

public class LoginPageController {
    private static LoginPageController loginPageController;

    private ProgramDatabase programDatabase;

    private LoginPageController() {
        programDatabase = null;
    }

    public static LoginPageController getLoginPageController() {
        return loginPageController == null ? loginPageController = new LoginPageController() : loginPageController;
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

    public boolean checkUsernameUniqueness(String username) {
        User user = this.programDatabase.getUserByUsername(username);
        return user == null ? true : false;
    }

    public boolean checkNicknameUniqueness(String nickname) {
        User user = this.programDatabase.getUserByNickname(nickname);
        return user == null ? true : false;
    }

    public void addUser(User user) {
        this.programDatabase.getUsers().add(user);
        LoginPageController.writeUsersListToFile();
    }

    // returns true if there was an error and false otherwise
    public boolean checkLoginErrors(String username, String password) {
        User user = this.programDatabase.getUserByUsername(username);
        if (user == null) {
            return true;
        }
        if (!user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean isUserLoggedIn() {
        return this.programDatabase.getLoggedInUser() == null ? false : true;
    }

    public boolean checkNextMenuValidity(String menuName) {
        return menuName.equals("Main Menu") ? true : false;
    }

    public void loginUser(String username) {
        User user = this.programDatabase.getUserByUsername(username);
        this.programDatabase.setLoggedInUser(user);
    }

    public static void readUsersListFromFile(){
        try {
            String input = new String(Files.readAllBytes(Paths.get("src","main","java","resources","Users.json")));
            ArrayList<User> users = new Gson().fromJson(input, new TypeToken<List<User>>(){}.getType());
            if(users == null){
                users = new ArrayList<>();
            }
            ProgramDatabase.getProgramDatabase().setUsers(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeUsersListToFile(){
        File main = new File("src", "main");
        File java = new File(main, "java");
        File resources = new File(java, "resources");
        File usersFile = new File(resources, "Users.json");
        try {
            FileWriter userWriter = new FileWriter(usersFile);
            userWriter.write(new Gson().toJson(ProgramDatabase.getProgramDatabase().getUsers()));
            userWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void setProgramDatabase() {
        this.programDatabase = ProgramDatabase.getProgramDatabase();
        LoginPageController.readUsersListFromFile();
    }

    public ProgramDatabase getProgramDatabase() {
        return this.programDatabase;
    }
}
