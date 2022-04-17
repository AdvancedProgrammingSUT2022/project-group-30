package views;

import java.util.Scanner;
import java.util.regex.Matcher;

import Models.User;
import Models.menusEnumerations.LoginPageCommands;
import Models.utilities.MyScanner;
import controllers.LoginPageController;
import controllers.SceneController;

public class LoginPageView implements View{

    private static LoginPageView loginPageView;

    private LoginPageController controller;

    private LoginPageView(){
        controller = null;
    }

    public static LoginPageView getLoginPageView(){
        return loginPageView == null ? new LoginPageView() : loginPageView; 
    }

    public void run(){
        Scanner scanner = MyScanner.getScanner();
        boolean quit = false;
        while(!quit){
            String input = scanner.nextLine();
            Matcher matcher;
            if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER)) != null){
                this.registerUser(matcher);
            }
            else if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.CREATE_USER_ABBREVIATED_FORM)) != null){
                this.registerUser(matcher);
            }
            else if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.LOGIN_USER)) != null){
                this.loginUser(matcher);
                quit = true;
            }
            else if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.LOGIN_USER_ABBREVIATED_FORM)) != null){
                this.loginUser(matcher);
                quit = true;
            }
            else if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.EXIT_MENU)) != null){
                quit = true;
            }
            else if((matcher = LoginPageCommands.getCommandMatcher(input, LoginPageCommands.SHOW_MENU)) != null){
                this.showMenu();
            }
        }
        scanner.close();
    }

    public void goToNextView(){
        SceneController sceneController = SceneController.getSceneController();
        MainPageView mainPageView = MainPageView.getMainPageView();
        mainPageView.setController();
        sceneController.setNextView(mainPageView);
    }

    public void loginUser(Matcher matcher){
        String username = matcher.group("username");
        String password = matcher.group("password");
        if(this.controller.checkLoginErrors(username, password)){
            System.out.println("Username and password didn't match!");
            return;
        }
        this.controller.loginUser(username);
        System.out.println("user logged in successfully!");
    }

    public void registerUser(Matcher matcher){
        String username = matcher.group("username");
        String nickname = matcher.group("nickname");
        String password = matcher.group("password");
        if(!this.controller.checkUsernameValidity(username)){
            System.out.println("Username can only contain uppercase and lowercase English letters, numbers, and whiteSpace (if there is at least one other character).");
            return;
        }
        if(!this.controller.checkPasswordSize(password)){
            System.out.println("The Password must be at least 8 characters");
            return;
        }
        if(!this.controller.checkPasswordValidity(password)){
            System.out.println("The Password must contain at least one uppercase letter, one lowercase letter, and one number character, and must not contain any whiteSpaces.");
            return;
        }
        if(!this.controller.checkNicknameValidity(nickname)){
            System.out.println("Nickname can only contain uppercase and lowercase English letters, numbers, and characters from this character set: {@,-,_}");
            return;
        }
        if(!this.controller.checkUsernameUniqueness(username)){
            System.err.println("user with username " + username + " already exists.");
            return;
        }
        if(!this.controller.checkNicknameUniqueness(nickname)){
            System.err.println("user with nickname " + nickname + " already exists.");
            return;
        }
        User user = new User(username, password, nickname, 0);
        this.controller.addUser(user);
        System.out.println("user created successfully!");
    }

    public void showMenu(){
        System.err.println("Login Menu");
    }

    public void setController(){
        this.controller = LoginPageController.getLoginPageController();
        this.controller.setProgramDatabase();
    }

    public LoginPageController getController(){
        return this.controller;
    }
    
}
