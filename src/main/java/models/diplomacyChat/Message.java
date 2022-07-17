package models.diplomacyChat;

import models.Civilization;

public class Message {
    private String message;
    private Civilization sender;

    public Message(String message, Civilization sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Civilization getSender() {
        return sender;
    }

    public void setSender(Civilization sender) {
        this.sender = sender;
    }
}
