package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.Notification;
import models.ProgramDatabase;
import models.User;
import views.Main;

import java.io.IOException;

public class InvitationPageController {

    private ProgramDatabase programDatabase = ProgramDatabase.getProgramDatabase();

    @FXML
    private VBox box;
    @FXML
    private TextField username;

    public void submit(MouseEvent mouseEvent) {
        if(programDatabase.getUserByUsername(username.getText()) == null){
            RegisterPageGraphicalController.showPopup("This username doesn't exist!");
            return;
        }
        if(username.getText().equals(programDatabase.getLoggedInUser().getUsername())){
            RegisterPageGraphicalController.showPopup("You cannot invite yourself to play:)");
            return;
        }
        Notification notification = new Notification(programDatabase.getLoggedInUser().getUsername() + " has invited you to play Civilization V!", false, 0);
        User user = programDatabase.getUserByUsername(username.getText());
        user.getInvitations().add(notification);
        RegisterPageGraphicalController.showPopup("Invitation to " + username.getText() + " sent!");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("GamePage");
    }
}
