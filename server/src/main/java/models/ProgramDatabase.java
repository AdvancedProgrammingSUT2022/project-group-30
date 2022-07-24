package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramDatabase {
    private static ProgramDatabase programDatabase;

    private ArrayList<User> users;
    private HashMap<Integer, User> loggedInUsers = new HashMap<>();

    private ProgramDatabase() {
        this.users = new ArrayList<>();
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

    public void updateLoggedInUserLastLoginTime(){
//        this.loggedInUsers.setLastLoginTime(this.getCurrentDate());
        // THIS METHOD SHOULD BE CHANGED TO SUPPORT TOKENS
    }

    public void updateUserLastScoreChangeTime(User user){
        user.setLastScoreChangeTime(this.getCurrentDate());
    }

    private String getCurrentDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setLoggedInUser(User user, int token) {
        loggedInUsers.put(token, user);
    }

    public void setLoggedInUser(User user) {
        // THIS METHOD SHOULD BE TERMINATED
    }

    public User getLoggedInUser(int token) {
        return loggedInUsers.get(token);
    }

    public User getLoggedInUser() {
        // NEEDS TO BE TERMINATED
        return loggedInUsers.get(0);
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

}
