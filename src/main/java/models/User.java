package models;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;

    public User(String username, String password, String nickname, int score) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = score;
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
}
