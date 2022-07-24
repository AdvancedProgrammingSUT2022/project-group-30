package controllers;

import models.User;
import models.chat.ChatDataBase;
import models.chat.Message;
import models.chat.PrivateChat;
import models.chat.Room;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatController {
    private static ChatController chatController;
    private ChatController() {
        database = ChatDataBase.getChatDatabase();
    }

    public static ChatController getChatController() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    private ChatDataBase database;

    public Room getCurrentRoom(int token) {
        HashMap<Integer, Integer> currentRoomIndexes = database.getCurrentRoomIndexes();
        if (database.getRooms().isEmpty()) {
            return null;
        }
        return database.getRooms().get(currentRoomIndexes.get(token));
    }

    public int getCurrentPrivateContactId(int token) {
        HashMap<Integer, Integer> currentPrivateContactIds = database.getCurrentPrivateContactIds();
        return currentPrivateContactIds.get(token);
    }

    public PrivateChat fetchPrivateChatForUsers(int user1Id, int user2Id) {
        return database.fetchPrivateChatForUsers(user1Id, user2Id);
    }

    public void setCurrentPrivateContactId(int currentPrivateContactId, int token) {
        database.getCurrentPrivateContactIds().put(token, currentPrivateContactId);
    }

    public void setCurrentRoom(Room room, int token) {
        database.getCurrentRoomIndexes().put(token, database.getRooms().indexOf(room));
    }

    public Room getRoomByName(String name) {
        ArrayList<Room> rooms = database.getRooms();
        for (Room room : rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public void createNewRoom(User owner, String name) {
        Room newRoom = new Room();
        newRoom.setParticipants(new ArrayList<>());
        newRoom.getParticipants().add(owner.getId());
        newRoom.setOwner(owner.getId());
        newRoom.setName(name);
        database.getRooms().add(newRoom);
    }

    public void addMessagetoPrivateChat(int id, Message message) {
        PrivateChat privateChat = database.findPrivateChatById(id);
        privateChat.getMessages().add(message);
    }

    public int getNextPrivateChatId() {
        int largest = 0;
        for (PrivateChat privateChat : database.getPrivateChats()) {
            if (privateChat.getId() >= largest) {
                largest = privateChat.getId() + 1;
            }
        }
        return largest;
    }

    public int getNextMessageId() {
        int largest = 0;
        for (PrivateChat privateChat : database.getPrivateChats()) {
            for (Message message : privateChat.getMessages()) {
                if (message.getId() >= largest) {
                    largest = message.getId() + 1;
                }
            }
        }
        for (Room room : database.getRooms()) {
            for (Message message : room.getMessages()) {
                if (message.getId() >= largest) {
                    largest = message.getId() + 1;
                }
            }
        }

        for (Message message : database.getGlobalChat()) {
            if (message.getId() >= largest) {
                largest = message.getId() + 1;
            }
        }

        return largest;
    }
}
