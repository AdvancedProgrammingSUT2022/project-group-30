package models;

public class Notification {
    private final int id;
    public  int getId() {
        return id;
    }
    private static int nextAvailableId = 0;

    private String text;
    private boolean isSeen;
    private int turnNumber;

    public Notification(String text, boolean isSeen, int turnNumber) {
        this.id = nextAvailableId;
        nextAvailableId++;

        this.text = text;
        this.isSeen = isSeen;
        this.turnNumber = turnNumber;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public boolean getIsSeen() {
        return this.isSeen;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() {
        return this.turnNumber;
    }
}
