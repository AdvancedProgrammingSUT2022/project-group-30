package controllers;

import models.User;
import models.chat.*;
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

    public void setCurrentRoom(int roomId, int token) {
        Request request = new Request("ChatController", "setCurrentRoom", MyGson.toJson(roomId), MyGson.toJson(token));
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

    public void addMessagetoPrivateChat(int id, Message message) {
        Request request = new Request("ChatController", "addMessagetoPrivateChat", MyGson.toJson(id), MyGson.toJson(message));
        NetworkController.getNetworkController().transferData(request);
    }

    public void addMessageToGlobalChat(Message message) {
        Request request = new Request("ChatController", "addMessageToGlobalChat", MyGson.toJson(message));
        NetworkController.getNetworkController().transferData(request);
    }

    public void editMessageText(int id, String newText) {
        Request request = new Request("ChatController", "editMessageText", MyGson.toJson(id), MyGson.toJson(newText));
        NetworkController.getNetworkController().transferData(request);
    }

    public void markMessageAsSeen(int id) {
        Request request = new Request("ChatController", "markMessageAsSeen", MyGson.toJson(id));
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<Message> getGlobalChat() {
        Request request = new Request("ChatController", "getGlobalChat");
        return (ArrayList<Message>) NetworkController.getNetworkController().transferData(request, Message[].class);
    }

    public void deleteMessage(int id) {
        Request request = new Request("ChatController", "deleteMessage", MyGson.toJson(id));
        NetworkController.getNetworkController().transferData(request);
    }

    public int getNextPrivateChatId() {
        Request request = new Request("ChatController", "getNextPrivateChatId");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int getNextRoomId() {
        Request request = new Request("ChatController", "getNextRoomId");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public int getNextMessageId() {
        Request request = new Request("ChatController", "getNextMessageId");
        return (int) NetworkController.getNetworkController().transferData(request);
    }

    public Message findMessageById(int id) {
        Request request = new Request("ChatController", "findMessageById", MyGson.toJson(id));
        return (Message) NetworkController.getNetworkController().transferData(request);
    }
}
