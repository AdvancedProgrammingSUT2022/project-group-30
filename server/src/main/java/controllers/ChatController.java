package controllers;

import models.User;
import models.chat.*;

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
    }

    public synchronized void addMessagetoPrivateChat(int id, Message message) {
        PrivateChat privateChat = database.findPrivateChatById(id);
        privateChat.getMessages().add(message);
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
}
