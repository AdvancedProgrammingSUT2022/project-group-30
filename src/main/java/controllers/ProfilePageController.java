package controllers;

import models.ProgramDatabase;
import models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilePageController {
    private static ProfilePageController profilePageController;

    private ProgramDatabase programDatabase;

    private ProfilePageController() {
        programDatabase = null;
    }

    public static ProfilePageController getProfilePageController() {
        return profilePageController == null ? profilePageController = new ProfilePageController()
                : profilePageController;
    }

    public void changeLoggedInUserNickname(String nickname) {
        this.programDatabase.getLoggedInUser().setNickname(nickname);
        LoginPageController.writeUsersListToFile();
    }

    public void changeLoggedInUserPassword(String newPassword) {
        this.programDatabase.getLoggedInUser().setPassword(newPassword);
        LoginPageController.writeUsersListToFile();
    }

    public boolean checkNewPasswordAndCurrentPasswordEquality(String currentPassword, String newPassword) {
        return currentPassword.equals(newPassword) ? true : false;
    }

    // returns true if the password is correct, false otherwise
    public boolean checkLoggedInUserPassword(String password) {
        if (this.programDatabase.getLoggedInUser().getPassword().equals(password)) {
            return true;
        }
        return false;
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

    public boolean checkNicknameUniqueness(String nickname) {
        User user = this.programDatabase.getUserByNickname(nickname);
        return user == null ? true : false;
    }

    public String getLoggedInUserImageName(){
        return this.programDatabase.getLoggedInUser().getImageName();
    }

    public void setProgramDatabase() {
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase() {
        return this.programDatabase;
    }

}
