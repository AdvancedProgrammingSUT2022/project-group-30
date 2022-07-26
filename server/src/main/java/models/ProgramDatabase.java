package models;

import models.chat.TokenData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProgramDatabase {
    private static ProgramDatabase programDatabase;

    private ArrayList<User> users;
    private ArrayList<TokenData> tokenData = new ArrayList<>();

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

    public void updateLoggedInUserLastLoginTime(int token){
        getLoggedInUser(token).setLastLoginTime(this.getCurrentDate());
        // THIS METHOD SHOULD BE CHANGED TO SUPPORT TOKENS
    }

    public TokenData fetchTokenData(int token) {
        for (TokenData data : tokenData) {
            if (data.getToken() == token) {
                return data;
            }
        }
        TokenData newTokenData = new TokenData(token);
        tokenData.add(newTokenData);
        return newTokenData;
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
        fetchTokenData(token).setLoggedInUser(user.getId());
    }

    public User getLoggedInUser(int token) {
        return getUserById(fetchTokenData(token).getLoggedInUser());
    }

    public User getLoggedInUser() {
        // THIS METHOD NEEDS TO BE TERMINATED
        return null;
    }

    public void setLoggedInUser(User user) {
        // THIS METHOD SHOULD BE TERMINATED
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<TokenData> getTokenData() {
        return tokenData;
    }

}
