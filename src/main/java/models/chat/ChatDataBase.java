package models.chat;

import java.util.ArrayList;

public class ChatDataBase {
    private static ChatDataBase chatDatabase;
    public static ChatDataBase getChatDatabase() {
        if (chatDatabase == null) {
            chatDatabase = new ChatDataBase();
        }
        return chatDatabase;
    }
    private ChatDataBase() {}

    private ArrayList<PrivateChat> privateChats;
    private ArrayList<Room> rooms;


}
