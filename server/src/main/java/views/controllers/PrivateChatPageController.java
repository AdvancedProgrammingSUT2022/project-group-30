package views.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.Message;
import views.Main;
import views.customcomponents.MessageBox;

import java.io.IOException;
import java.util.ArrayList;

public class PrivateChatPageController {
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea textArea;
    @FXML
    private Circle contactImage;
    @FXML
    private Label contactUsernameField;

    private ObservableList<Message> data;

    @FXML
    public void initialize() {
        User currentUser = ProgramDatabase.getProgramDatabase().getLoggedInUser();
        User contact = ProgramDatabase.getProgramDatabase().getUserById(ChatDataBase.getChatDatabase().getCurrentPrivateContactId());
        contactUsernameField.setText(contact.getUsername());
        contactImage.setFill(new ImagePattern(new Image("file:src/main/resources/images/avatars/" + contact.getImageName())));
        contactImage.setStroke(Color.BLACK);
        contactImage.setStyle("-fx-background-size: cover;");
        data = FXCollections.observableArrayList();
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("meow"));
        messages.add(new Message("Hi"));
        messages.add(new Message("weeee"));
        messages.add(new Message("1", 1));
        messages.add(new Message("2"));
        messages.add(new Message("3"));
        messages.add(new Message("4", 1));
        messages.add(new Message("I hanshf hlsfhel hosdfh sfn efhfndn jldhfdls hflsdh foe"));
        messages.add(new Message("I, Tonya:\nmeow meow\nfhdskfs;", 2));
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

    @FXML
    protected void onSendButtonClick() {
        data.add(new Message(textArea.getText()));
        textArea.setText("");
        scrollToBottom();
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            Main.loadFxmlFile("PrivateChatsList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        messageList.scrollTo(data.size() - 1);
    }
}
