package terminalViews;

import controllers.ProfilePageController;
import controllers.SceneController;
import menusEnumerations.ProfilePageCommands;
import utilities.MyScanner;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfilePageView implements View {

    private static ProfilePageView profilePageView;

    private ProfilePageController controller;

    private ProfilePageView() {
        controller = null;
    }

    public static ProfilePageView getProfilePageView() {
        return profilePageView == null ? profilePageView = new ProfilePageView() : profilePageView;
    }

    public void run() {
        Scanner scanner = MyScanner.getScanner();
        boolean quit = false;
        while (!quit) {
            Matcher matcher;
            String input = scanner.nextLine();
            if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.CHANGE_NICKNAME)) != null) {
                this.changeNickname(matcher);
            } else if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.CHANGE_PASSWORD_PATTERN_1)) != null) {
                this.changePassword(matcher);
            } else if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.CHANGE_PASSWORD_PATTERN_2)) != null) {
                this.changePassword(matcher);
            } else if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.ENTER_MENU)) != null) {
                this.enterMenu();
                ;
            } else if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.EXIT_MENU)) != null) {
                quit = true;
            } else if ((matcher = ProfilePageCommands.getCommandMatcher(input, ProfilePageCommands.SHOW_MENU)) != null) {
                this.showMenu();
            } else {
                System.out.println("Invalid command!");
            }
        }
        this.goToNextView();
    }

    public void changePassword(Matcher matcher) {
        String currentPassword = matcher.group("currentPassword");
        String newPassword = matcher.group("newPassword");
        if (!this.controller.checkLoggedInUserPassword(currentPassword)) {
            System.out.println("current password is invalid");
            return;
        }
        if (this.controller.checkNewPasswordAndCurrentPasswordEquality(currentPassword, newPassword)) {
            System.out.println("please enter a new password");
            return;
        }
        if (!this.controller.checkPasswordSize(newPassword)) {
            System.out.println("The Password must be at least 8 characters");
            return;
        }
        if (!this.controller.checkPasswordValidity(newPassword)) {
            System.out.println("The Password must contain at least one uppercase letter, one lowercase letter, and one number character.");
            return;
        }
        this.controller.changeLoggedInUserPassword(newPassword);
        System.out.println("password changed successfully!");

    }

    public void changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (!this.controller.checkNicknameValidity(nickname)) {
            System.out.println("Nickname can only contain uppercase and lowercase English letters, numbers, and characters from this character set: {-,_}");
            return;
        }
        if (!this.controller.checkNicknameUniqueness(nickname)) {
            System.err.println("user with nickname " + nickname + " already exists.");
            return;
        }
        this.controller.changeLoggedInUserNickname(nickname);
        System.out.println("nickname changed successfully!");
    }

    public void goToNextView() {
        SceneController sceneController = SceneController.getSceneController();
        MainPageView mainPageView = MainPageView.getMainPageView();
        mainPageView.setController();
        sceneController.setNextView(mainPageView);
    }

    public void enterMenu() {
        System.out.println("menu navigation is not possible");
    }

    public void showMenu() {
        System.out.println("Profile Menu");
    }

    public void setController() {
        this.controller = ProfilePageController.getProfilePageController();
        this.controller.setProgramDatabase();
    }

    public ProfilePageController getController() {
        return this.controller;
    }

}
