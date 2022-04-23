package controllers;

import models.ProgramDatabase;

import models.GameDataBase;
import models.Tile;
import models.works.Work;

public class GameController {
    private static GameController gameController;

    private GameDataBase gameDataBase = GameDataBase.getGameDataBase();
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

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }

    public void setGameDataBase()  {
        this.gameDataBase = GameDataBase.getGameDataBase();
    }

    public GameDataBase getGameDataBase(){
        return this.gameDataBase;
    }
}
