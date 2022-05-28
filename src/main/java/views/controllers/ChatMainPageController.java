package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import views.Main;

public class ChatMainPageController {
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        Main.getStage().setWidth(800);
        setImageToButton(backButton, "buttons/bg.png");
    }

    private void setImageToButton(Button button, String imageAddress) {
        button.setGraphic(new ImageView(new Image(Main.class.getResource("/images/" + imageAddress).toString())));
    }
}
