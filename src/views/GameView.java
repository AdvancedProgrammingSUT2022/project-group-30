package views;

import java.util.Scanner;

import controllers.GameController;
import utilities.MyScanner;

public class GameView implements View {

    private static GameView gameView;

    private GameView() {
        scanner = MyScanner.getScanner();
        controller = GameController.getGameController();
    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    private GameController controller;
    private Scanner scanner;

    public void run() {
        controller.startGame();
        showMap();
        
        while (true) {
            
        }
    }

    private void showMap() {
        // TODO : mahyar 
    }

    @Override
    public void showMenu() {
        // leave empty
    }
    @Override
    public void setController() {
        // nakhoondam
    }
}
