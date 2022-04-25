package models;

import java.util.ArrayList;

public class ProgramDatabase {
    private static ProgramDatabase programDatabase;

    private ArrayList<User> users;
    private User loggedInUser;

    private ProgramDatabase() {
        this.users = new ArrayList<>();
        this.loggedInUser = null;
    }

    public static ProgramDatabase getProgramDatabase() {
        return programDatabase == null ? programDatabase = new ProgramDatabase() : programDatabase;
    }

    public User getUserByUsername(String username) {
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getUsername().equals(username)) {
                return this.users.get(i);
            }
        }
        return null;
    }

    public User getUserByNickname(String nickname) {
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getNickname().equals(nickname)) {
                return this.users.get(i);
            }
        }
        return null;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

}
