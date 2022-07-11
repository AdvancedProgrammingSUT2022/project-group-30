package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import views.Main;

import java.io.IOException;

public class GamePageController {

    @FXML
    private Button newGameButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadSavedGamesButton;
    @FXML
    private Button autoSaveButton;
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
        setToolTipForButton(resumeButton, "resumes the stopped game");
        setToolTipForButton(saveButton, "saves the stopped game");
        setToolTipForButton(loadSavedGamesButton, "shows you the previous saved games and you can choose them to resume");
        setToolTipForButton(autoSaveButton, "activates auto save");
        setToolTipForButton(invitationButton, "allows you to invite other players to play Civilization V");
        setToolTipForButton(receivedInvitationsButton, "shows you your received invitations");
        setToolTipForButton(backButton, "opens main menu");
    }

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

    public static void setToolTipForButton(Button button, String text){
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(10));
        button.setTooltip(tooltip);
    }
}
