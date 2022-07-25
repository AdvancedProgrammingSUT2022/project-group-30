package views.controllers;

import controllers.ChatController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import models.chat.Message;
import views.Main;
import views.customcomponents.MessageBox;

import java.io.IOException;
import java.util.ArrayList;

public class GlobalChatPageController {
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea textArea;

    private ObservableList<Message> data;

    private ChatController controller;
    private NetworkController netController;

    private int currentUserId;

    @FXML
    public void initialize() {
        controller = ChatController.getChatController();
        netController = NetworkController.getNetworkController();

        currentUserId = ProgramController.getProgramController().getLoggedInUser(netController.getToken()).getId();

        data = FXCollections.observableArrayList();
        ArrayList<Message> messages = controller.getGlobalChat();
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
                return new MessageBox("GlobalChatPage");
            }
        });
    }

    @FXML
    protected void onSendButtonClick() {
        controller.addMessageToGlobalChat(new Message(textArea.getText(), currentUserId));
        try {
            Main.loadFxmlFile("GlobalChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        try {
            Main.loadFxmlFile("GlobalChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            Main.loadFxmlFile("ChatFirstPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        messageList.scrollTo(data.size() - 1);
    }
}
