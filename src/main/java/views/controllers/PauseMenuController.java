package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import views.Main;

import java.io.IOException;

public class PauseMenuController {
    @FXML
    private void onResumeGameButtonClick() throws IOException {
        Main.loadFxmlFile("CivilizationGamePage");
    }

    @FXML
    private void onSaveAndExitButtonClick() throws IOException {
        GameController.getGameController().saveGame();
        Main.loadFxmlFile("MainPage");
    }

    @FXML
    private void onExitButtonClick() throws IOException {
        Main.loadFxmlFile("MainPage");
    }
}
