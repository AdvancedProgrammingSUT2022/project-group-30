package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;

import java.util.ArrayList;

public class DiplomaticRelation extends Diplomacy {
    private boolean areMutuallyVisible = false;
    private boolean areAtWar = false;
    private int friendliness = 0;
    private ArrayList<Message> messages = new ArrayList<>();

    public DiplomaticRelation(Civilization civ1, Civilization civ2) {
        super();
        this.pair = new CivilizationPair(civ1, civ2);
    }

    public DiplomaticRelation(DiplomaticRelation diplomaticRelation) {
        super(diplomaticRelation);
        this.areMutuallyVisible = diplomaticRelation.areMutuallyVisible;
        this.areAtWar = diplomaticRelation.areAtWar;
        this.friendliness = diplomaticRelation.friendliness;
        this.messages = diplomaticRelation.messages;
        this.pair = new CivilizationPair(new Civilization(diplomaticRelation.pair.getCivilizationsArray().get(0)),
                new Civilization(diplomaticRelation.pair.getCivilizationsArray().get(1)));
    }

    public void addMessage(String text, Civilization sender) {
        Message newMessage = new Message(text, sender);
        messages.add(newMessage);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public boolean areMutuallyVisible() {
        return areMutuallyVisible;
    }

    public void setAreMutuallyVisible(boolean areMutuallyVisible) {
        this.areMutuallyVisible = areMutuallyVisible;
    }

    public boolean areAtWar() {
        return areAtWar;
    }

    public void setAreAtWar(boolean areAtWar) {
        this.areAtWar = areAtWar;
    }

    public void setFriendliness(int friendliness) {
        this.friendliness = friendliness;
    }

    public int getFriendliness() {
        return friendliness;
    }

    public DiplomaticRelation(CivilizationPair pair) {
        this.pair = pair;
    }

}
