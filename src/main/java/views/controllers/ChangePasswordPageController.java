package views.controllers;

import controllers.ProfilePageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class ChangePasswordPageController {

    private ProfilePageController controller = ProfilePageController.getProfilePageController();

    @FXML
    private TextField currentPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private VBox box;

    public void submit(MouseEvent mouseEvent) throws IOException {
        controller.setProgramDatabase();
        if (!this.controller.checkLoggedInUserPassword(currentPassword.getText())) {
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
        this.controller.changeLoggedInUserPassword(newPassword.getText());
        RegisterPageGraphicalController.showPopup("password changed successfully!");
        Main.loadFxmlFile("ProfilePage");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ProfilePage");
    }
}
