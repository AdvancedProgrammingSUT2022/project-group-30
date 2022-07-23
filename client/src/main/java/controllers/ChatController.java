package controllers;

import models.ProgramDatabase;
import models.User;

public class ChatController {
    private static ChatController chatController;
    private ChatController() {}
    public static ChatController getChatController() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    public User getUserByUsername(String username) {
        return ProgramDatabase.getProgramDatabase().getUserByUsername(username);
    }

    public void test() {
        System.out.println("test");
    }
}
