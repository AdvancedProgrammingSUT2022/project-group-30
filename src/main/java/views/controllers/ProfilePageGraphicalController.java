package views.controllers;

import controllers.ProfilePageController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class ProfilePageGraphicalController {

    private ProfilePageController controller = ProfilePageController.getProfilePageController();

    @FXML
    private VBox box;
    @FXML
    private ImageView profilePhoto;

    @FXML
    public void initialize(){

    }

    public void changeProfilePhoto(MouseEvent mouseEvent) {
    }

    public void changePassword(MouseEvent mouseEvent) {
    }

    public void changeNickname(MouseEvent mouseEvent) {
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }
}
