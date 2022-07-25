package models;

import utilities.Debugger;

public class Player  implements java.io.Serializable{
    private final User user;
    private Civilization civilization;

    public Player(User user) {
        this.user = user;
        this.civilization = null;
    }

    public User getUser() {
        return user;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public void setCivilization(Civilization civilization) {
        if (this.civilization == null) {
            this.civilization = civilization;
        } else {
            Debugger.debug("Player's civ is already assigned! you are trying to reassign it!");
        }
    }
}
