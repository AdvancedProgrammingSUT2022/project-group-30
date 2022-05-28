package views.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import views.Main;

public class ChatMainPageController {
    @FXML
    private Button backButton;
    @FXML
    private Button privateChatButton;
    @FXML
    private Button globalChatButton;
    @FXML
    private Button groupChatButton;

    private final int sideButtonSize = 60;

    @FXML
    public void initialize() {
        Main.getStage().setWidth(800);

        setImageAndTextToButton(backButton, "Back", "buttons/backbutton.png", sideButtonSize);
        setImageAndTextToButton(privateChatButton, "Private Chats", "buttons/privateChatButton.png", sideButtonSize);
        setImageAndTextToButton(globalChatButton, "Global Chat", "buttons/globalchat.png", sideButtonSize);
        setImageAndTextToButton(groupChatButton, "Group Chats", "buttons/groupchat.png", sideButtonSize);
    }

    private void setImageAndTextToButton(Button button, String text, String imageAddress, int width) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        ImageView imageView = new ImageView(new Image(Main.class.getResource("/images/" + imageAddress).toString()));
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
        Label textLabel = new Label();
        textLabel.setText(text);
        vbox.getChildren().addAll(imageView, textLabel);
        button.setGraphic(vbox);
        button.setBackground(Background.fill(Color.TRANSPARENT));
    }
}
