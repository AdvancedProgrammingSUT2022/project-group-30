import java.util.Scanner;

import controllers.SceneController;
import models.GameMap;
import utilities.MyScanner;
import views.GameView;
import views.LoginPageView;

public class Main {
    public static void main(String[] args)  {
        // Scanner scanner = MyScanner.getScanner();
        // SceneController sceneController = SceneController.getSceneController();
        // LoginPageView loginPageView = LoginPageView.getLoginPageView();
        // loginPageView.setController();
        // sceneController.setNextView(loginPageView);
        // sceneController.run();
        // scanner.close();
        GameMap map = GameMap.getGameMap();
        map.setFrameBase(map.getMap()[0][0]);
        GameView gameView = GameView.getGameView();
        gameView.showMap();
        
    }
}
