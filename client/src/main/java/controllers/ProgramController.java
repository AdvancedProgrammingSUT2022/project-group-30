package controllers;

import models.ProgramDatabase;
import models.User;
import netPackets.Request;
import utilities.MyGson;

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
        Request request = new Request("ProgramController", "hackIntoChat", MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public User getUserByUsername(String username) {
        Request request = new Request("ProgramController", "getUserByUsername", MyGson.toJson(username));
        return (User) NetworkController.getNetworkController().transferData(request);
    }

    public User getLoggedInUser(int token) {
        Request request = new Request("ProgramController", "getLoggedInUser", MyGson.toJson(token));
        return (User) NetworkController.getNetworkController().transferData(request);
    }

    public User getUserById(int id) {
        Request request = new Request("ProgramController", "getUserById", MyGson.toJson(id));
        return (User) NetworkController.getNetworkController().transferData(request);
    }
}
