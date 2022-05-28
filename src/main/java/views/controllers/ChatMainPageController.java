package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import views.Main;

public class ChatMainPageController {
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        Main.getStage().setWidth(800);

        setImageToButton(backButton, "buttons/backbutton.png", 50);
    }

    private void setImageToButton(Button button, String imageAddress, int width) {
        ImageView imageView = new ImageView(new Image(Main.class.getResource("/images/" + imageAddress).toString()));
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        button.setBackground(Background.fill(Color.TRANSPARENT));
    }
}
