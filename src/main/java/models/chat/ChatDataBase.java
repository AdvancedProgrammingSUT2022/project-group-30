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

    private ChatDataBase() {
    }

    private ArrayList<PrivateChat> privateChats = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private int currentPrivateContactId;    // id of the user whose private chat page should be opened.
    private int currentRoomIndex;

    public Room getCurrentRoom() {
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.get(currentRoomIndex);
    }

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

    public void setCurrentRoom(Room room) {
        System.out.println(rooms.contains(room));
        this.currentRoomIndex = rooms.indexOf(room);
    }

    public Room getRoomByName(String name) {
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
        rooms.add(newRoom);
    }
}