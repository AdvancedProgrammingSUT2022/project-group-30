package controllers;

import models.ProgramDatabase;
import models.User;

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
