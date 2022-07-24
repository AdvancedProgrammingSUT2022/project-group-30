package models.chat;

import models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatDataBase {
    private static ChatDataBase chatDatabase;

    public static ChatDataBase getChatDatabase() {
        if (chatDatabase == null) {
            chatDatabase = new ChatDataBase();
        }
        return chatDatabase;
    }

    private ChatDataBase() {
    }

    private ArrayList<PrivateChat> privateChats = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private HashMap<Integer, Integer> currentPrivateContactIds = new HashMap<>();
    private HashMap<Integer, Integer> currentRoomIndexes = new HashMap<>();

    public HashMap<Integer, Integer> getCurrentRoomIndexes() {
        return currentRoomIndexes;
    }

    public HashMap<Integer, Integer> getCurrentPrivateContactIds() {
        return currentPrivateContactIds;
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}