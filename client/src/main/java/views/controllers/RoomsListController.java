package views.controllers;

import controllers.ChatController;
import controllers.GameController;
import controllers.NetworkController;
import controllers.ProgramController;
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
        Room room = ChatController.getChatController().getRoomByName(roomName);
        if (room == null) {
            RegisterPageGraphicalController.showPopup("Room not Found!");
            return;
        }
        if (!room.getParticipants().contains(ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken()).getId())) {
            RegisterPageGraphicalController.showPopup("You are not a member of this room!");
            return;
        }
        ChatController.getChatController().setCurrentRoom(room, NetworkController.getNetworkController().getToken());
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
        Room room = ChatController.getChatController().getRoomByName(roomName);
        if (room != null) {
            RegisterPageGraphicalController.showPopup("Room name is already taken!");
            return;
        }
        ChatController.getChatController().createNewRoom(ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken()), roomName);
        RegisterPageGraphicalController.showPopup("Room " + roomName + " has been created!");
        ChatController.getChatController().setCurrentRoom(ChatController.getChatController().getRoomByName(roomName),
                NetworkController.getNetworkController().getToken());
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
