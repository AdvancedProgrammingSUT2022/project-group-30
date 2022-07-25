package models.chat;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatDataBase {
    public ChatDataBase() {
    }

    private ArrayList<PrivateChat> privateChats = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Message> globalChat = new ArrayList<>();

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Message> getGlobalChat() {
        return globalChat;
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

    public PrivateChat findPrivateChatById(int id) {
        for (PrivateChat privateChat : privateChats) {
            if (privateChat.getId() == id) {
                return privateChat;
            }
        }
        return null;
    }

    public Room findRoomById(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }
}