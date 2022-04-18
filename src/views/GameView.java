package views;

public class GameView implements View{

    private static GameView gameView;

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
        
    }
}
