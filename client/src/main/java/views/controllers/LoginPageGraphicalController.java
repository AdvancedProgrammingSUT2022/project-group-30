package views.controllers;

import controllers.LoginPageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class LoginPageGraphicalController {

    private LoginPageController controller = LoginPageController.getLoginPageController();

    @FXML
    private VBox box;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    public void submit(MouseEvent mouseEvent) throws IOException {
        this.controller.setProgramDatabase();
        if (this.controller.checkLoginErrors(username.getText(), password.getText())) {
            RegisterPageGraphicalController.showPopup("Username and password didn't match!");
            return;
        }
        this.controller.loginUser(username.getText());
        RegisterPageGraphicalController.showPopup("user logged in successfully!");
        Main.loadFxmlFile("MainPage");
    }

    @FXML
    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("SecondPage");
    }
}
