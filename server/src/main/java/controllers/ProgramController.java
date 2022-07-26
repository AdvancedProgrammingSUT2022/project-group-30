package controllers;

import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.TokenData;

import java.util.ArrayList;

public class ProgramController {
    private static ProgramController programController;
    private ProgramController() {
        database = ProgramDatabase.getProgramDatabase();
    }

    public static ProgramController getProgramController() {
        if (programController == null) {
            programController = new ProgramController();
        }
        return programController;
    }

    private ProgramDatabase database;

    public void hackIntoChat(int token) {
        LoginPageController.readUsersListFromFile();
        database.setLoggedInUser(getUserById(0), token);
    }

    public void hackIntoChat1(int token) {
        LoginPageController.readUsersListFromFile();
        database.setLoggedInUser(getUserById(1), token);
    }

    public TokenData fetchTokenData(int token) {
        return database.fetchTokenData(token);
    }

    public ArrayList<TokenData> getTokenData() {
        return database.getTokenData();
    }

    public User getUserByUsername(String username) {
        return database.getUserByUsername(username);
    }

    public User getLoggedInUser(int token) {
        return database.getLoggedInUser(token);
    }

    public User getUserById(int id) {
        return database.getUserById(id);
    }
}
