package views.controllers;

import controllers.NetworkController;
import controllers.ProfilePageController;
import controllers.ProgramController;
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

    private ProgramController controller = ProgramController.getProgramController();

    @FXML
    private VBox box;
    @FXML
    private ImageView profilePhoto;

    @FXML
    public void initialize() {
        controller.setProgramDatabase();
        String imageName = controller.getLoggedInUserImageName(NetworkController.getNetworkController().getToken());
        box.getChildren().remove(profilePhoto);
        profilePhoto = new ImageView(new Image("file:src/main/resources/images/avatars/" + imageName, 150, 150, false, false));
        box.getChildren().add(0, profilePhoto);
    }

    public void changeProfilePhoto(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangeProfilePhotoPage");
    }

    public void changePassword(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangePasswordPage");
    }

    public void changeNickname(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChangeNicknamePage");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }
}
