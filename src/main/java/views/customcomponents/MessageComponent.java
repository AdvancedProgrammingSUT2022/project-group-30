package views.customcomponents;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.ProgramDatabase;
import models.User;
import models.chat.Message;

public class MessageComponent extends VBox {
    Message message;
    Label seenLabel;

    public MessageComponent(Message message) {
        this.message = message;
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
        senderInfo.getChildren().addAll(senderAvatar, senderUsername);

        Label text = new Label();
        text.setAlignment(Pos.TOP_LEFT);
        text.setWrapText(true);
        text.setText(message.getText());
        text.setMinSize(200, 60);
        text.setMaxWidth(350);

        HBox bottomPane = new HBox();
        bottomPane.setSpacing(5.0);
        bottomPane.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 13;");
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        Label timeSent = new Label();
        timeSent.setText(message.getTimeSentAsHHMM());
        seenLabel = new Label();
        seenLabel.setText((message.isSeen()) ? "seen" : "unseen");
        bottomPane.getChildren().addAll(timeSent, seenLabel);

        setStyle("-fx-border-radius: 10px; -fx-background-color: #7272ff; -fx-padding: 10px");

        getChildren().addAll(senderInfo, text, bottomPane);
        setSpacing(5.0);
    }

    public void updateSeen() {
        seenLabel.setText((message.isSeen()) ? "seen" : "unseen");
    }
}
