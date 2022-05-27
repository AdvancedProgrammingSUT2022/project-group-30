package views.controllers;

import controllers.ProfilePageController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfilePageGraphicalController {

    private ProfilePageController controller = ProfilePageController.getProfilePageController();

    @FXML
    private VBox box;
    @FXML
    private ImageView profilePhoto;

    @FXML
    public void initialize() throws MalformedURLException {
        controller.setProgramDatabase();
        String imageName = controller.getLoggedInUserImageName();
        box.getChildren().remove(profilePhoto);
        profilePhoto = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/" + imageName).toExternalForm()).toExternalForm(), 150, 150, false, false));
        box.getChildren().add(0, profilePhoto);
    }

    public void changeProfilePhoto(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangeProfilePhotoPage");
    }

    public void changePassword(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangePasswordPage");
    }

    public void changeNickname(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangeNickname");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }
}
