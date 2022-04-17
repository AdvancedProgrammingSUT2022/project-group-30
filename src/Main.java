import java.util.Scanner;

import Models.utilities.MyScanner;
import controllers.SceneController;
import views.LoginPageView;

public class Main {
    public static void main(String[] args){
        Scanner scanner = MyScanner.getScanner();
        SceneController sceneController = SceneController.getSceneController();
        LoginPageView loginPageView = LoginPageView.getLoginPageView();
        loginPageView.setController();
        sceneController.setNextView(loginPageView);
        sceneController.run();
        scanner.close();
    }
}
