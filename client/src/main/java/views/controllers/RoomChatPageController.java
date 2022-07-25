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

    @FXML
    public void initialize() {
        controller = ChatController.getChatController();
        netController = NetworkController.getNetworkController();

        User currentUser = ProgramController.getProgramController().getLoggedInUser(netController.getToken());
        Room room =  controller.getCurrentRoom(netController.getToken());
        roomNameField.setText(room.getName());
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
        data.add(new Message(textArea.getText(), ProgramController.getProgramController().getLoggedInUser(netController.getToken()).getId()));
        textArea.setText("");
        scrollToBottom();
    }

    @FXML
    protected void onAddMemberButtonClick() {
        RegisterPageGraphicalController.showPopup("hello");
    }

    private void scrollToBottom() {
        messageList.scrollTo(data.size() - 1);
    }
}
