package models;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class User  implements java.io.Serializable{

    private int rank; // this field will only change when user click on the scoreboard page button
    private final int id;
    private String username;
    private String password;
    private String nickname;
    private int score;
    private String imageName;
    private String lastLoginTime;
    private String lastScoreChangeTime;  // note : be careful of updating this field after each game and score change using updateUserLastScoreChangeTime() function in ProgramDatabase.java
    private ArrayList<Notification> invitations;

    public User(String username, String password, String nickname, int score) {
        this.rank = -1; // initialize ranking into -1
        this.id = findNextId();
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = score;
        this.lastLoginTime = null;
        this.invitations = new ArrayList<>();
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

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastScoreChangeTime(String lastScoreChangeTime) {
        this.lastScoreChangeTime = lastScoreChangeTime;
    }

    public int getId() {
        return id;
    }

    public String getLastScoreChangeTime() {
        return this.lastScoreChangeTime;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }

    public void setInvitations(ArrayList<Notification> invitations){
        this.invitations = invitations;
    }

    public ArrayList<Notification> getInvitations(){
        return this.invitations;
    }

    private static int findNextId() {
        int nextId = 0;

        try {
            String input = new String(Files.readAllBytes(Paths.get("src", "main", "resources", "json", "userid.txt")));
            if (!input.isEmpty()) {
                nextId = Integer.parseInt(input);
            }
        } catch (IOException e) {}

        File main = new File("src", "main");
        File resources = new File(main, "resources");
        File json = new File(resources, "json");
        File useridFile = new File(json, "userid.txt");
        try {
            FileWriter userWriter = new FileWriter(useridFile);
            userWriter.write(String.valueOf(nextId + 1));
            userWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextId;
    }
}
