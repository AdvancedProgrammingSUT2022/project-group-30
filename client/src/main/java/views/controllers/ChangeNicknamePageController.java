package views.controllers;

import controllers.NetworkController;
import controllers.ProfilePageController;
import controllers.ProgramController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class ChangeNicknamePageController {

    private ProgramController controller = ProgramController.getProgramController();

    @FXML
    private VBox box;
    @FXML
    private TextField nickname;

    public void submit(MouseEvent mouseEvent) throws IOException {
        controller.setProgramDatabase();
        if (!this.controller.checkNicknameValidity(nickname.getText())) {
            RegisterPageGraphicalController.showPopup("Nickname can only contain uppercase and lowercase English letters, numbers, and characters from this character set: {-,_}");
            return;
        }
        if (!this.controller.checkNicknameUniqueness(nickname.getText())) {
            RegisterPageGraphicalController.showPopup("user with nickname " + nickname.getText() + " already exists.");
            return;
        }
        this.controller.changeLoggedInUserNickname(nickname.getText(), NetworkController.getNetworkController().getToken());
        RegisterPageGraphicalController.showPopup("nickname changed successfully!");
        Main.loadFxmlFile("ProfilePage");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ProfilePage");
    }
}
