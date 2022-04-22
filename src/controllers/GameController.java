package controllers;

import models.Tile;
import models.works.Work;

public class GameController {
    private static GameController gameController;
    private GameController() {

    }
    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public void startGame() {

    }

    public Tile findWorksLocation(Work work) {  // gets a work and finds the tile that owns it
        // TODO
        return null;
    }
}
