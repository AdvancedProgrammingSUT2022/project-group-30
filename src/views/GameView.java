package views;

import controllers.GameController;

public class GameView implements View{

    private static GameView gameView;
    private GameController controller = GameController.getGameController();

    private GameView(){

    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    public void run(){

    }

    public void showMap()  {
        PrintableCharacters printableCharacters[][] = this.controller.makeMapReadyToPrint();
        for(int i = 0; i < printableCharacters.length; i++){
            for(int j = 0; j < printableCharacters[i].length; j++){
                System.out.print(printableCharacters[i][j].getANSI_COLOR()+printableCharacters[i][j].getCharacter()+PrintableCharacters.ANSI_RESET);
            }
            System.out.println();
        }
    }

    public void showMenu(){

    }
    
    public void setController()  {
        this.controller = GameController.getGameController();
        this.controller.setGameDataBase();
        this.controller.setProgramDatabase();
    }
}
