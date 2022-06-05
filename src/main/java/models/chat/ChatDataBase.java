package models.chat;

import models.User;

import java.util.ArrayList;

public class ChatDataBase {
    private static ChatDataBase chatDatabase;
    public static ChatDataBase getChatDatabase() {
        if (chatDatabase == null) {
            chatDatabase = new ChatDataBase();
        }
        return chatDatabase;
    }
    private ChatDataBase() {}

    private ArrayList<PrivateChat> privateChats;
    private ArrayList<Room> rooms;
    private int currentPrivateContactId;    // id of the user whose private chat page should be opened.
    private int currentRoomIndex;

    public int getCurrentPrivateContactId() {
        return currentPrivateContactId;
    }

    public void setCurrentPrivateContactId(int currentPrivateContactId) {
        this.currentPrivateContactId = currentPrivateContactId;
    }

    public int getCurrentRoomIndex() {
        return currentRoomIndex;
    }

    public void setCurrentRoomIndex(int currentRoomIndex) {
        this.currentRoomIndex = currentRoomIndex;
    }
}