package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.GameDataBase;
import views.Main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GamePageController {

    @FXML
    private VBox box;

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }

    public void startNewGame(MouseEvent mouseEvent) {
    }

    public void resumeGame(MouseEvent mouseEvent) {
    }

    public void saveGame(MouseEvent mouseEvent) throws IOException {
        GameController.getGameController().saveGameManually();
    }

    public void resumeSavedGames(MouseEvent mouseEvent) throws IOException {
       GameController.getGameController().loadGame("hello");
    }

    public void autoSave(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("AutoSavePage");
    }
}
