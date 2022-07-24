package controllers;

import models.User;
import models.chat.ChatDataBase;
import models.chat.PrivateChat;
import models.chat.Room;
import netPackets.Request;
import utilities.MyGson;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatController {
    private static ChatController chatController;

    private ChatController() {
        database = ChatDataBase.getChatDatabase();
    }

    public static ChatController getChatController() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    private ChatDataBase database;

    public Room getCurrentRoom(int token) {
        Request request = new Request("ChatController", "getCurrentRoom", MyGson.toJson(token));
        return (Room) NetworkController.getNetworkController().transferData(request);
    }

    public int getCurrentPrivateContactId(int token) {
        Request request = new Request("ChatController", "getCurrentPrivateContactId", MyGson.toJson(token));
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public PrivateChat fetchPrivateChatForUsers(int user1Id, int user2Id) {
        Request request = new Request("ChatController", "fetchPrivateChatForUsers", MyGson.toJson(user1Id), MyGson.toJson(user2Id));
        return (PrivateChat) NetworkController.getNetworkController().transferData(request);
    }

    public void setCurrentPrivateContactId(int currentPrivateContactId, int token) {
        Request request = new Request("ChatController", "setCurrentPrivateContactId", MyGson.toJson(currentPrivateContactId), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public void setCurrentRoom(Room room, int token) {
        Request request = new Request("ChatController", "setCurrentRoom", MyGson.toJson(room), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public Room getRoomByName(String name) {
        Request request = new Request("ChatController", "getRoomByName", MyGson.toJson(name));
        return (Room) NetworkController.getNetworkController().transferData(request);
    }

    public void createNewRoom(User owner, String name) {
        Request request = new Request("ChatController", "createNewRoom", MyGson.toJson(owner), MyGson.toJson(name));
        NetworkController.getNetworkController().transferData(request);
    }
}
