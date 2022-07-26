package models.chat;

public class TokenData {
    private int token;
    private Integer loggedInUser;
    private Integer currentPrivateChatId;
    private Integer currentRoomId;

    public TokenData(int token) {
        this.token = token;
        currentPrivateChatId = null;
        currentRoomId = null;
        loggedInUser = null;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public Integer getCurrentPrivateChatId() {
        return currentPrivateChatId;
    }

    public void setCurrentPrivateChatId(Integer currentPrivateChatId) {
        this.currentPrivateChatId = currentPrivateChatId;
    }

    public Integer getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(Integer currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public Integer getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Integer loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
