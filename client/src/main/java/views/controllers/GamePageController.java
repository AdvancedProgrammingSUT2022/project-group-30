package views.controllers;

import controllers.GameController;
import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.GameDataBase;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import views.Main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GamePageController {

    @FXML
    private Button newGameButton;
    @FXML
    private Button invitationButton;
    @FXML
    private Button receivedInvitationsButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox box;

    @FXML
    public void initialize(){
        setToolTipForButton(newGameButton, "starts a new game");
        setToolTipForButton(invitationButton, "allows you to invite other players to play Civilization V");
        setToolTipForButton(receivedInvitationsButton, "shows you your received invitations");
        setToolTipForButton(backButton, "opens main menu");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }

    public void startNewGame(MouseEvent mouseEvent) throws IOException {
        if (GameController.getGameController().isUserInGame(NetworkController.getNetworkController().getToken())) {
            Main.loadFxmlFile("CivilizationGamePage");
        } else {
            Main.loadFxmlFile("PlayersCountPage");
        }
    }

    public void invite(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("InvitationPage");
    }

    public void showReceivedInvitations(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ReceivedInvitationsPage");
    }

    public static void setToolTipForButton(Button button, String text){
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(10));
        button.setTooltip(tooltip);
    }
}
