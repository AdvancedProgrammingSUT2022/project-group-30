package controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.ProgramDatabase;
import Models.User;

public class LoginPageController {
    private static LoginPageController loginPageController;

    private ProgramDatabase programDatabase;

    private LoginPageController(){
        programDatabase = null;
    }

    public static LoginPageController getLoginPageController(){
        return loginPageController == null ? new LoginPageController() : loginPageController;
    }

    public boolean checkUsernameValidity(String username){
        Matcher matcher = Pattern.compile("[a-zA-Z]{1}\\s*[a-zA-Z]*").matcher(username);
        return matcher.matches() ? true : false;
    }

    public boolean checkPasswordSize(String password){
        return password.length() < 8 ? false : true;
    }
    
    public boolean checkPasswordValidity(String password){
        Matcher uppercaseMatcher = Pattern.compile("[A-Z]+").matcher(password);
        Matcher lowercaseMatcher = Pattern.compile("[a-z]+").matcher(password);
        Matcher numberMatcher = Pattern.compile("[0-9]+").matcher(password);
        Matcher whiteSpaceMatcher = Pattern.compile("\\s+").matcher(password);
        if(uppercaseMatcher.matches() && lowercaseMatcher.matches() && numberMatcher.matches() && !whiteSpaceMatcher.matches()){
            return true;
        }
        return false;
    }

    public boolean checkNicknameValidity(String nickname){
        Matcher matcher = Pattern.compile("[a-zA-Z0-9_-@]{1}\\s*[a-zA-Z0-9_-@]*").matcher(nickname);
        return matcher.matches() ? true : false;
    }

    public boolean checkUsernameUniqueness(String username){
        User user = this.programDatabase.getUserByUsername(username);
        return user == null ? true : false;
    }

    public boolean checkNicknameUniqueness(String nickname){
        User user = this.programDatabase.getUserByNickname(nickname);
        return user == null ? true : false;
    }

    public void addUser(User user){
        this.programDatabase.getUsers().add(user);
    }

    // returns true if there was an error and false otherwise
    public boolean checkLoginErrors(String username, String password){
        User user = this.programDatabase.getUserByUsername(username);
        if(user == null){
            return true;
        }
        if(!user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public void loginUser(String username){
        User user = this.programDatabase.getUserByUsername(username);
        this.programDatabase.setLoggedInUser(user);
    }

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }
}
