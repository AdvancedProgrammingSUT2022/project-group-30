package models.chat;

import controllers.ChatController;

import java.util.ArrayList;

public class Room {
    private int id;
    private String name;
    private ArrayList<Integer> participants = new ArrayList<>();
    private int ownerId;
    private ArrayList<Message> messages = new ArrayList<>();

    public Room() {
    }

    public Room(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Integer> participants) {
        this.participants = participants;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
