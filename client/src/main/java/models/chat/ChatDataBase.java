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

    public PrivateChat fetchPrivateChatForUsers(int user1Id, int user2Id) {
        for (PrivateChat privateChat : privateChats) {
            if (privateChat.matchUsers(user1Id, user2Id)) {
                return privateChat;
            }
        }
        PrivateChat privateChat = new PrivateChat(user1Id, user2Id);
        privateChats.add(privateChat);
        return privateChat;
    }
}