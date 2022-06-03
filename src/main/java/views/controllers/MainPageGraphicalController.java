package views.controllers;

import controllers.MainPageController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class MainPageGraphicalController {

    private MainPageController controller = MainPageController.getMainPageController();

    @FXML
    private VBox box;

    @FXML
    public void initialize(){
        controller.setProgramDatabase();
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        this.controller.logoutUser();
        Main.loadFxmlFile("SecondPage");
    }

    public void profileMenu(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ProfilePage");
    }

    public void scoreboardPage(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ScoreboardPage");
    }

    public void chatRoomPage(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ChatFirstPage");
    }
}
