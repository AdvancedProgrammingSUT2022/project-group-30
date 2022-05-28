package models;

import java.time.format.DateTimeFormatter;
import java.util.Random;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private String imageName;
    private String lastLoginTime;
    private String lastScoreChangeTime;  // note : be careful of updating this field after each game and score change using updateUserLastScoreChangeTime() function in ProgramDatabase.java

    public User(String username, String password, String nickname, int score) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = score;
        this.lastLoginTime = null;
        ProgramDatabase.getProgramDatabase().updateUserLastScoreChangeTime(this);
        Random rand = new Random();
        int pictureNumber = rand.nextInt(4);
        this.imageName = Integer.toString(pictureNumber) + ".jpeg";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getImageName(){
        return this.imageName;
    }

    public void setLastLoginTime(String lastLoginTime){
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTime(){
        return this.lastLoginTime;
    }

    public void setLastScoreChangeTime(String lastScoreChangeTime){
        this.lastScoreChangeTime = lastScoreChangeTime;
    }

    public String getLastScoreChangeTime(){
        return this.lastScoreChangeTime;
    }
}
