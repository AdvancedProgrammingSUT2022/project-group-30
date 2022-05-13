package views;

import controllers.LoginPageController;
import controllers.SceneController;
import menusEnumerations.LoginPageCommands;
import models.User;
import utilities.MyScanner;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginPageView implements View {

    private static LoginPageView loginPageView;

    private LoginPageController controller;

    private LoginPageView() {
        controller = null;
    }

    public static LoginPageView getLoginPageView() {
        return loginPageView == null ? loginPageView = new LoginPageView() : loginPageView;
    }

    public void run() {
        Scanner scanner = MyScanner.getScanner();
        boolean quitMenu = false;
        boolean quitProgram = false;
        while (!quitMenu) {
            String input = scanner.nextLine();
            Matcher matcher;
            if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_1)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_2)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_3)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_4)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_5)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_PATTERN_6)) != null) {
                this.registerUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.LOGIN_USER_PATTERN_1)) != null) {
                this.loginUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.LOGIN_USER_PATTERN_2)) != null) {
                this.loginUser(matcher);
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.ENTER_MENU)) != null) {
                if (this.enterMenu(matcher)) {
                    quitMenu = true;
                }
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.EXIT_MENU)) != null) {
                quitMenu = true;
                quitProgram = true;
            } else if ((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.SHOW_MENU)) != null) {
                this.showMenu();
            } else {
                System.out.println("Invalid command!");
            }
        }
        this.goToNextView(quitProgram);
    }

    // returns true if entering new menu is possible, false otherwise
    public boolean enterMenu(Matcher matcher) {
        String menuName = matcher.group("menuName");
        if (!this.controller.checkNextMenuValidity(menuName)) {
            System.out.println("menu navigation is not possible");
            return false;
        }
        if (!this.controller.isUserLoggedIn()) {
            System.out.println("please login first");
            return false;
        }
        return true;
    }

    public void goToNextView(boolean quit) {
        SceneController sceneController = SceneController.getSceneController();
        if (quit) {
            sceneController.setNextView(null);
            return;
        }
        MainPageView mainPageView = MainPageView.getMainPageView();
        mainPageView.setController();
        sceneController.setNextView(mainPageView);
    }

    public void loginUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        if (this.controller.checkLoginErrors(username, password)) {
            System.out.println("Username and password didn't match!");
            return;
        }
        this.controller.loginUser(username);
        System.out.println("user logged in successfully!");
    }

    public void registerUser(Matcher matcher) {
        String username = matcher.group("username");
        String nickname = matcher.group("nickname");
        String password = matcher.group("password");
        if (!this.controller.checkUsernameValidity(username)) {
            System.out.println("Username can only contain uppercase and lowercase English letters, numbers.");
            return;
        }
        if (!this.controller.checkPasswordSize(password)) {
            System.out.println("The Password must be at least 8 characters");
            return;
        }
        if (!this.controller.checkPasswordValidity(password)) {
            System.out.println("The Password must contain at least one uppercase letter, one lowercase letter, and one number character.");
            return;
        }
        if (!this.controller.checkNicknameValidity(nickname)) {
            System.out.println("Nickname can only contain uppercase and lowercase English letters, numbers, and characters from this character set: {-,_}");
            return;
        }
        if (!this.controller.checkUsernameUniqueness(username)) {
            System.err.println("user with username " + username + " already exists.");
            return;
        }
        if (!this.controller.checkNicknameUniqueness(nickname)) {
            System.err.println("user with nickname " + nickname + " already exists.");
            return;
        }
        User user = new User(username, password, nickname, 0);
        this.controller.addUser(user);
        System.out.println("user created successfully!");
    }

    public void showMenu() {
        System.err.println("Login Menu");
    }

    public void setController() {
        this.controller = LoginPageController.getLoginPageController();
        this.controller.setProgramDatabase();
    }

    public LoginPageController getController() {
        return this.controller;
    }

}
