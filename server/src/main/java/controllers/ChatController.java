package controllers;

public class ChatController {
    private static ChatController chatController;
    private ChatController() {}

    public static ChatController getChatController() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }


}
