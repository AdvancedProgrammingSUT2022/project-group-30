package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.ProgramDatabase;
import models.chat.ChatDataBase;
import models.chat.Room;
import views.Main;

import java.io.IOException;

public class RoomsListController {
    @FXML
    private TextField roomNameField;

    @FXML
    protected void onEnterRoomButtonClick() {
        String roomName = roomNameField.getText();
        Room room = ChatDataBase.getChatDatabase().getRoomByName(roomName);
        if (room == null) {
            RegisterPageGraphicalController.showPopup("Room not Found!");
            return;
        }
        if (!room.getParticipants().contains(ProgramDatabase.getProgramDatabase().getLoggedInUser().getId())) {
            RegisterPageGraphicalController.showPopup("You are not a member of this room!");
            return;
        }
        ChatDataBase.getChatDatabase().setCurrentRoom(room);
        try {
            Main.loadFxmlFile("RoomChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCreateRoomButtonClick() {
        String roomName = roomNameField.getText();
        if (roomName.length() < 3) {
            RegisterPageGraphicalController.showPopup("Room name is invalid!");
            return;
        }
        Room room = ChatDataBase.getChatDatabase().getRoomByName(roomName);
        if (room != null) {
            RegisterPageGraphicalController.showPopup("Room name is already taken!");
            return;
        }
        ChatDataBase.getChatDatabase().createNewRoom(ProgramDatabase.getProgramDatabase().getLoggedInUser(), roomName);
        RegisterPageGraphicalController.showPopup("Room " + roomName + " has been created!");
        ChatDataBase.getChatDatabase().setCurrentRoom(ChatDataBase.getChatDatabase().getRoomByName(roomName));
        try {
            Main.loadFxmlFile("RoomChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Main.loadFxmlFile("ChatFirstPage");
    }
}
