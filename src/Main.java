
import models.GameMap;
import views.GameView;

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
        map.setFrameBase(map.getMap()[15][3]);
        GameView gameView = GameView.getGameView();
        gameView.showMap();
        
    }
}
