package views;

import controllers.MainPageController;
import controllers.SceneController;
import menusEnumerations.MainPageCommands;
import utilities.MyScanner;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainPageView implements View {
    private static MainPageView mainPageView;

    private MainPageController controller;

    private MainPageView() {
        controller = null;
    }

    public static MainPageView getMainPageView() {
        return mainPageView == null ? mainPageView = new MainPageView() : mainPageView;
    }

    public void run() {
        Scanner scanner = MyScanner.getScanner();
        boolean quit = false;
        View nextView = null;
        while (!quit) {
            Matcher matcher;
            String input = scanner.nextLine();
            if ((matcher = MainPageCommands.getCommandMatcher(input, MainPageCommands.ENTER_MENU)) != null) {
                nextView = this.enterMenu(matcher);
                if (nextView != null) {
                    quit = true;
                }
            } else if ((matcher = MainPageCommands.getCommandMatcher(input, MainPageCommands.EXIT_MENU)) != null) {
                nextView = LoginPageView.getLoginPageView();
                quit = true;
            } else if ((matcher = MainPageCommands.getCommandMatcher(input, MainPageCommands.LOGOUT_USER)) != null) {
                this.logoutUser();
                nextView = LoginPageView.getLoginPageView();
                quit = true;
            } else if ((matcher = MainPageCommands.getCommandMatcher(input, MainPageCommands.SHOW_MENU)) != null) {
                this.showMenu();
            } else {
                System.out.println("Invalid command!");
            }
        }
        this.goToNextView(nextView);
    }

    public void goToNextView(View view) {
        SceneController sceneController = SceneController.getSceneController();
        view.setController();
        sceneController.setNextView(view);
    }

    // returns the next menu if entering new menu is possible, null otherwise
    public View enterMenu(Matcher matcher) {
        String menuName = matcher.group("menuName");
        if (!this.controller.checkNextMenuValidity(menuName)) {
            System.out.println("menu navigation is not possible");
            return null;
        }
        return this.controller.findTheNextMenu(menuName);
    }

    public void logoutUser() {
        this.controller.logoutUser();
        System.out.println("user logged out successfully!");
    }

    public void showMenu() {
        System.out.println("Main Menu");
    }

    public void setController() {
        this.controller = MainPageController.getMainPageController();
        this.controller.setProgramDatabase();
    }

    public MainPageController getController() {
        return this.controller;
    }

}
