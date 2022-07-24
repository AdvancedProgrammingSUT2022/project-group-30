package models.chat;

import models.User;

import java.util.ArrayList;

public class PrivateChat {
    public PrivateChat() {

    }

    public PrivateChat(int user1Id, int user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public boolean matchUsers(int id1, int id2) {
        if ((id1 == user1Id && id2 == user2Id) || (id1 == user2Id && id2 == user1Id)) {
            return true;
        }
        return false;
    }

    private int user1Id;
    private int user2Id;
    private ArrayList<Message> messages = new ArrayList<>();
}
