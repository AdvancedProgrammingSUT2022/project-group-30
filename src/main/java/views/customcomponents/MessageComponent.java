package views.customcomponents;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.chat.Message;

public class MessageComponent extends VBox {
    public MessageComponent(Message message) {
        Label text = new Label();
        text.setText(message.getText());
        getChildren().add(text);
    }
}
