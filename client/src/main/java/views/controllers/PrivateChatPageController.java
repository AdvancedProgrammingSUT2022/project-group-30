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

    @FXML
    public void initialize() {
        controller = ChatController.getChatController();
        netController = NetworkController.getNetworkController();

        User currentUser = ProgramController.getProgramController().getLoggedInUser(netController.getToken());
        User contact = ProgramController.getProgramController().getUserById(controller.getCurrentPrivateContactId(netController.getToken()));
        contactUsernameField.setText(contact.getUsername());
        contactImage.setFill(new ImagePattern(new Image("file:src/main/resources/images/avatars/" + contact.getImageName())));
        contactImage.setStroke(Color.BLACK);
        contactImage.setStyle("-fx-background-size: cover;");
        PrivateChat chat = controller.fetchPrivateChatForUsers(currentUser.getId(), contact.getId());
        controller.addMessagetoPrivateChat(chat.getId(), new Message("Meow", currentUser.getId()));
        controller.addMessagetoPrivateChat(chat.getId(), new Message("Hello", contact.getId()));
        controller.addMessagetoPrivateChat(chat.getId(), new Message("Ummm", contact.getId()));
        controller.addMessagetoPrivateChat(chat.getId(), new Message("Heeey :)", contact.getId()));
        data = FXCollections.observableArrayList();
        chat = controller.fetchPrivateChatForUsers(currentUser.getId(), contact.getId());
        ArrayList<Message> messages = chat.getMessages();
        controller.editMessageText(messages.get(0).getId(), "Fuck You man");
        controller.deleteMessage(messages.get(3).getId());
        chat = controller.fetchPrivateChatForUsers(currentUser.getId(), contact.getId());
        messages = chat.getMessages();
//        messages.add(new Message("meow"));
//        messages.add(new Message("Hi"));
//        messages.add(new Message("weeee"));
//        messages.add(new Message("1", 1));
//        messages.add(new Message("2"));
//        messages.add(new Message("3"));
//        messages.add(new Message("4", 1));
//        messages.add(new Message("I hanshf hlsfhel hosdfh sfn efhfndn jldhfdls hflsdh foe"));
//        messages.add(new Message("I, Tonya:\nmeow meow\nfhdskfs;", 2));
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
