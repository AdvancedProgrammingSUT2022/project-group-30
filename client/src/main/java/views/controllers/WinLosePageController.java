package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Civilization;
import views.Main;

import java.io.IOException;

public class WinLosePageController {
    @FXML
    private VBox standingBox;

    @FXML
    public void initialize() {
        for (Civilization civilization : GameController.getGameController().getCivilizations()) {
            Label civLabel = new Label(civilization.getName() + ", Score: " + civilization.getScore());
            civLabel.getStyleClass().add("civLabel");
            if (GameController.getGameController().getWinner().getId() == civilization.getId()) {
                civLabel.getStyleClass().add("winnerLabel");
            }
            standingBox.getChildren().add(civLabel);
        }
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Main.loadFxmlFile("MainPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
