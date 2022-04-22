package views;

import controllers.GameController;

public class GameView implements View{

    private static GameView gameView;
    private GameController controller;

    private GameView(){

    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    public void run(){

    }

    public void showMenu(){

    }
    
    public void setController(){
        this.controller = GameController.getGameController();
        this.controller.setGameDataBase();
        this.controller.setProgramDatabase();
    }
}
