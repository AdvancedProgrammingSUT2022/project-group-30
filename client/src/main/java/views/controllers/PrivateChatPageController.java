package views.controllers;

import controllers.ChatController;
import controllers.NetworkController;
import controllers.ProgramController;
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
import models.chat.PrivateChat;
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

    private ChatController controller;
    private NetworkController netController;

    private int currentUserId;
    private int contactId;
    private int chatId;

    @FXML
    public void initialize() {
        controller = ChatController.getChatController();
        netController = NetworkController.getNetworkController();

        currentUserId = ProgramController.getProgramController().getLoggedInUser(netController.getToken()).getId();
        User contact = ProgramController.getProgramController().getUserById(controller.getCurrentPrivateContactId(netController.getToken()));
        contactId = contact.getId();
        PrivateChat chat = controller.fetchPrivateChatForUsers(currentUserId, contactId);

        contactUsernameField.setText(contact.getUsername());
        contactImage.setFill(new ImagePattern(new Image("file:src/main/resources/images/avatars/" + contact.getImageName())));
        contactImage.setStroke(Color.BLACK);
        contactImage.setStyle("-fx-background-size: cover;");
        chatId = chat.getId();
        data = FXCollections.observableArrayList();
        ArrayList<Message> messages = chat.getMessages();
        for (Message message : messages) {
            if (message.getSenderId() != currentUserId && !message.isSeen()) {
                controller.markMessageAsSeen(message.getId());
            }
        }
        data.setAll(messages);
        messageList.setItems(data);
        messageList.setFocusTraversable(false);
        messageList.setStyle("-fx-background-color: transparent");
        scrollToBottom();

        messageList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> messageListView) {
                return new MessageBox("PrivateChatPage");
            }
        });
    }

    @FXML
    protected void onSendButtonClick() {
        controller.addMessagetoPrivateChat(chatId, new Message(textArea.getText(), currentUserId));
        try {
            Main.loadFxmlFile("PrivateChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        try {
            Main.loadFxmlFile("PrivateChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
