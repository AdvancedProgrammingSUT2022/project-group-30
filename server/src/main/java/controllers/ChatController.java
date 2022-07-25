package controllers;

import models.ProgramDatabase;
import models.User;
import models.chat.*;
import utilities.MyGson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
        TokenData tokenData = ProgramController.getProgramController().fetchTokenData(token);

        if (database.getRooms().isEmpty()) {
            return null;
        }
        return database.findRoomById(tokenData.getCurrentRoomId());
    }

    public int getCurrentPrivateContactId(int token) {
        return ProgramController.getProgramController().fetchTokenData(token).getCurrentPrivateChatId();
    }

    public PrivateChat fetchPrivateChatForUsers(int user1Id, int user2Id) {
        return database.fetchPrivateChatForUsers(user1Id, user2Id);
    }

    public synchronized void setCurrentPrivateContactId(int currentPrivateContactId, int token) {
        ProgramController.getProgramController().fetchTokenData(token).setCurrentPrivateChatId(currentPrivateContactId);
    }

    public synchronized void setCurrentRoom(int roomId, int token) {
        TokenData tokenData = ProgramController.getProgramController().fetchTokenData(token);
        tokenData.setCurrentRoomId(roomId);
    }

    public Room getRoomByName(String name) {
        ArrayList<Room> rooms = database.getRooms();
        for (Room room : rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public synchronized void createNewRoom(User owner, String name) {
        Room newRoom = new Room();
        newRoom.setParticipants(new ArrayList<>());
        newRoom.getParticipants().add(owner.getId());
        newRoom.setOwnerId(owner.getId());
        newRoom.setName(name);
        database.getRooms().add(newRoom);
        saveDatabaseToFile();
    }

    public boolean isUserMemberOfRoom(int userId, int roomId) {
        Room room = findRoomById(roomId);
        if (room == null) {
            return false;
        }
        return room.getParticipants().contains(userId);
    }

    public Room findRoomById(int id) {
        for (Room room : database.getRooms()) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    public void addUserToRoom(int userId, int roomId) {
        findRoomById(roomId).getParticipants().add(userId);
    }

    public synchronized void addMessagetoPrivateChat(int id, Message message) {
        PrivateChat privateChat = database.findPrivateChatById(id);
        privateChat.getMessages().add(message);
    }

    public synchronized void addMessageToRoom(int id, Message message) {
        Room room = database.findRoomById(id);
        room.getMessages().add(message);
    }

    public synchronized void addMessageToGlobalChat(Message message) {
        database.getGlobalChat().add(message);
    }

    public synchronized void editMessageText(int id, String newText) {
        Message message = findMessageById(id);
        message.setText(newText);
    }

    public synchronized void markMessageAsSeen(int id) {
        Message message = findMessageById(id);
        message.setSeen(true);
    }

    public ArrayList<Message> getGlobalChat() {
        return database.getGlobalChat();
    }

    public synchronized void deleteMessage(int id) {
        for (PrivateChat privateChat : database.getPrivateChats()) {
            for (Message message : privateChat.getMessages()) {
                if (message.getId() == id) {
                    privateChat.getMessages().remove(message);
                    return;
                }
            }
        }

        for (Room room : database.getRooms()) {
            for (Message message : room.getMessages()) {
                if (message.getId() == id) {
                    room.getMessages().remove(message);
                    return;
                }
            }
        }

        for (Message message : database.getGlobalChat()) {
            if (message.getId() == id) {
                database.getGlobalChat().remove(message);
                return;
            }
        }
    }

    public int getNextPrivateChatId() {
        int largest = 0;
        for (PrivateChat privateChat : database.getPrivateChats()) {
            if (privateChat.getId() >= largest) {
                largest = privateChat.getId() + 1;
            }
        }
        return largest;
    }

    public int getNextRoomId() {
        int largest = 0;
        for (Room room : database.getRooms()) {
            if (room.getId() >= largest) {
                largest = room.getId() + 1;
            }
        }
        return largest;
    }

    public int getNextMessageId() {
        int largest = 0;
        for (PrivateChat privateChat : database.getPrivateChats()) {
            for (Message message : privateChat.getMessages()) {
                if (message.getId() >= largest) {
                    largest = message.getId() + 1;
                }
            }
        }
        for (Room room : database.getRooms()) {
            for (Message message : room.getMessages()) {
                if (message.getId() >= largest) {
                    largest = message.getId() + 1;
                }
            }
        }

        for (Message message : database.getGlobalChat()) {
            if (message.getId() >= largest) {
                largest = message.getId() + 1;
            }
        }

        return largest;
    }

    public Message findMessageById(int id) {
        for (PrivateChat privateChat : database.getPrivateChats()) {
            for (Message message : privateChat.getMessages()) {
                if (message.getId() == id) {
                    return message;
                }
            }
        }

        for (Room room : database.getRooms()) {
            for (Message message : room.getMessages()) {
                if (message.getId() == id) {
                    return message;
                }
            }
        }

        for (Message message : database.getGlobalChat()) {
            if (message.getId() == id) {
                return message;
            }
        }
        return null;
    }

    public void saveDatabaseToFile() {
        File main = new File("src", "main");
        File resources = new File(main, "resources");
        File json = new File(resources, "json");
        File usersFile = new File(json, "ChatDataBase.json");
        try {
            FileWriter userWriter = new FileWriter(usersFile);
            userWriter.write(MyGson.getGson().toJson(database));
            userWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
