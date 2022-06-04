package views.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.chat.Message;
import views.customcomponents.MessageComponent;

import java.util.ArrayList;

public class PrivateChatPageController {
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea textArea;

    private ObservableList<Message> data;

    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList();
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
        messageList.setFocusTraversable(false);
        messageList.setStyle("-fx-background-color: transparent");
        scrollToBottom();

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
            if (item != null && empty != true) {
                AnchorPane pane = new AnchorPane();
                MessageComponent box = new MessageComponent(item);
                pane.getChildren().add(box);
                AnchorPane.setRightAnchor(box, 10.0);
                setGraphic(pane);
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteForMeItem = new MenuItem();
                deleteForMeItem.setText("Delete for me");
                deleteForMeItem.setOnAction(event -> {
                    RegisterPageGraphicalController.showPopup(item.getText());
                });
                MenuItem deleteForEveryoneItem = new MenuItem();
                deleteForEveryoneItem.setText("Delete for everybody");
                MenuItem editItem = new MenuItem();
                editItem.setText("Edit");
                contextMenu.getItems().addAll(deleteForMeItem, deleteForEveryoneItem, editItem);
                contextMenu.getStyleClass().add("messageContextMenu");
                this.setContextMenu(contextMenu);
            }
        }
    }

    @FXML
    protected void onSendButtonClick() {
        data.add(new Message(textArea.getText()));
        textArea.setText("");
        scrollToBottom();
    }

    private void scrollToBottom() {
        messageList.scrollTo(data.size() - 1);
    }
}
