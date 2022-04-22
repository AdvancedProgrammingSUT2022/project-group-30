package controllers;

import models.ProgramDatabase;

import java.io.FileNotFoundException;

import models.GameDataBase;
import models.Tile;
import models.works.Work;
import views.PrintableCharacters;

public class GameController {
    private static GameController gameController;

    private GameDataBase gameDataBase;
    private ProgramDatabase programDatabase;

    private GameController() {

    }

    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public Tile findWorksLocation(Work work) {  // gets a work and finds the tile that owns it
        // TODO
        return null;
    }

    public PrintableCharacters[][] makeMapReadyToPrint() throws FileNotFoundException{
        Tile tiles[][] = this.gameDataBase.getMap().findTilesToPrint();

        return null;
    }

    private void drawATile(PrintableCharacters printableCharacters[][] , int i, int j){
        //printableCharacters[i][j].setCa
    }

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }

    public void setGameDataBase(){
        this.gameDataBase = GameDataBase.getGameDataBase();
    }

    public GameDataBase getGameDataBase(){
        return this.gameDataBase;
    }
}
