package views.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import models.chat.Message;
import views.customcomponents.MessageComponent;

import java.util.ArrayList;

public class PrivateChatPageController {
    @FXML
    private ListView<Message> messageList;

    @FXML
    public void initialize() {
        ObservableList<Message> data = FXCollections.observableArrayList();
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("meow"));
        messages.add(new Message("Hi"));
        messages.add(new Message("weeee"));
        messages.add(new Message("1"));
        messages.add(new Message("2"));
        messages.add(new Message("3"));
        messages.add(new Message("4"));
        messages.add(new Message("I hanshf hlsfhel hosdfh sfn efhfndn jldhfdls hflsdh foe"));
        messages.add(new Message("I, Tonya:\nmeow meow\nfhdskfs;"));
        data.setAll(messages);
        messageList.setItems(data);
        messageList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> messageListView) {
                return new MessageBox();
            }
        });
    }

    private static class MessageBox extends ListCell<Message> {
        @Override
        public void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            this.getStyleClass().add("messageBox");
            if (item != null) {
                AnchorPane pane = new AnchorPane();
                MessageComponent box = new MessageComponent(item);
                pane.getChildren().add(box);
                AnchorPane.setRightAnchor(box, 10.0);
                setGraphic(pane);
            }
        }
    }
}
