
import java.util.Scanner;

import controllers.SceneController;
import utilities.MyScanner;
import views.LoginPageView;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = MyScanner.getScanner();
        SceneController sceneController = SceneController.getSceneController();
        LoginPageView loginPageView = LoginPageView.getLoginPageView();
        loginPageView.setController();
        sceneController.setNextView(loginPageView);
        sceneController.run();
        scanner.close();
    }
}