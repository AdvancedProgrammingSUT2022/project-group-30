package views.controllers;

import controllers.ChatController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.Message;
import models.chat.Room;
import views.Main;
import views.customcomponents.AddMemberDialog;
import views.customcomponents.MessageBox;

import java.io.IOException;
import java.util.ArrayList;

public class RoomChatPageController {
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea textArea;
    @FXML
    private Label roomNameField;

    private ObservableList<Message> data;

    private ChatController controller;
    private NetworkController netController;
    private Room room;
    private User currentUser;

    @FXML
    public void initialize() {
        controller = ChatController.getChatController();
        netController = NetworkController.getNetworkController();

        currentUser = ProgramController.getProgramController().getLoggedInUser(netController.getToken());
        room =  controller.getCurrentRoom(netController.getToken());
        roomNameField.setText(room.getName());
        data = FXCollections.observableArrayList();
        ArrayList<Message> messages = room.getMessages();
        for (Message message : messages) {
            if (message.getSenderId() != currentUser.getId() && !message.isSeen()) {
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
                return new MessageBox("RoomChatPage");
            }
        });
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            Main.loadFxmlFile("RoomsList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSendButtonClick() {
        controller.addMessageToRoom(room.getId(), new Message(textArea.getText(), currentUser.getId()));
        try {
            Main.loadFxmlFile("RoomChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        try {
            Main.loadFxmlFile("RoomChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddMemberButtonClick() {
        if (currentUser.getId() != room.getOwnerId()) {
            RegisterPageGraphicalController.showPopup("You do not own this group!");
            return;
        }
        AddMemberDialog addMemberDialog = new AddMemberDialog(room.getId());
        addMemberDialog.show();
    }

    private void scrollToBottom() {
        messageList.scrollTo(data.size() - 1);
    }
}
