package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class GamePageController {

    @FXML
    private VBox box;

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }

    public void startNewGame(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("PlayersCountPage");
    }

    public void resumeGame(MouseEvent mouseEvent) {
    }

    public void saveGame(MouseEvent mouseEvent) {
    }

    public void resumeSavedGames(MouseEvent mouseEvent) {
    }

    public void autoSave(MouseEvent mouseEvent) {
    }

    public void invite(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("InvitationPage");
    }

    public void showReceivedInvitations(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ReceivedInvitationsPage");
    }
}
