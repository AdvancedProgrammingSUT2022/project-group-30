package controllers;

import models.User;
import models.chat.ChatDataBase;
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
}
