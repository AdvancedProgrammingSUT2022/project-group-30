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

public class ChangePasswordPageController {

    private ProgramController controller = ProgramController.getProgramController();

    @FXML
    private TextField currentPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private VBox box;

    public void submit(MouseEvent mouseEvent) throws IOException {
        controller.setProgramDatabase();
        if (!this.controller.checkLoggedInUserPassword(currentPassword.getText(), NetworkController.getNetworkController().getToken())) {
            RegisterPageGraphicalController.showPopup("current password is invalid");
            return;
        }
        if (this.controller.checkNewPasswordAndCurrentPasswordEquality(currentPassword.getText(), newPassword.getText())) {
            RegisterPageGraphicalController.showPopup("please enter a new password");
            return;
        }
        if (!this.controller.checkPasswordSize(newPassword.getText())) {
            RegisterPageGraphicalController.showPopup("The Password must be at least 8 characters");
            return;
        }
        if (!this.controller.checkPasswordValidity(newPassword.getText())) {
            RegisterPageGraphicalController.showPopup("The Password must contain at least one uppercase letter, one lowercase letter, and one number character.");
            return;
        }
        this.controller.changeLoggedInUserPassword(newPassword.getText(), NetworkController.getNetworkController().getToken());
        RegisterPageGraphicalController.showPopup("password changed successfully!");
        this.controller.logoutUser(NetworkController.getNetworkController().getToken());
        Main.loadFxmlFile("SecondPage");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ProfilePage");
    }
}
