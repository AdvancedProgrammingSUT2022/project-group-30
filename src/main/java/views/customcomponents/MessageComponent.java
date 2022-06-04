package views.customcomponents;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.ProgramDatabase;
import models.User;
import models.chat.Message;

public class MessageComponent extends VBox {
    public MessageComponent(Message message) {
        User sender = ProgramDatabase.getProgramDatabase().getUserById(message.getSenderId());

        HBox senderInfo = new HBox();
        senderInfo.setAlignment(Pos.CENTER_LEFT);
        senderInfo.setSpacing(5.0);
        Circle senderAvatar = new Circle();
        senderAvatar.setRadius(20.0);
        senderAvatar.setFill(new ImagePattern(new Image("file:src/main/resources/images/avatars/" + sender.getImageName())));
        senderAvatar.setStroke(Color.BLACK);
        senderAvatar.setStyle("-fx-background-size: cover;");
        Label senderUsername = new Label();
        senderUsername.setText(sender.getUsername());
        senderUsername.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        Label text = new Label();
        text.setAlignment(Pos.TOP_LEFT);
        text.setWrapText(true);
//        text.setStyle("-fx-border-color: black; -fx-border-style: solid;");
        senderInfo.getChildren().addAll(senderAvatar, senderUsername);

        text.setText(message.getText());
        text.setMinSize(200, 60);
        text.setMaxWidth(350);

        setStyle("-fx-border-radius: 10px; -fx-background-color: #7272ff; -fx-padding: 10px");

        getChildren().addAll(senderInfo, text);
        setSpacing(5.0);
    }
}
