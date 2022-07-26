package models.diplomacy;

import models.Civilization;

public class DiplomaticMessage {
    protected int id;
    public  int getId() {
        return id;
    }
    protected static int nextAvailableId = 0;

    private String message;
    private Civilization sender;

    public DiplomaticMessage() {
        id = 0;
    }

    public DiplomaticMessage(String message, Civilization sender) {
        this.id = nextAvailableId;
        nextAvailableId++;

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
