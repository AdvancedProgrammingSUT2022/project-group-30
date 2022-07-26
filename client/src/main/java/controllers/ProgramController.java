package controllers;

import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.TokenData;
import netPackets.Request;
import utilities.MyGson;

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
        Request request = new Request("ProgramController", "hackIntoChat", MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public void hackIntoChat1(int token) {
        Request request = new Request("ProgramController", "hackIntoChat1", MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public TokenData fetchTokenData(int token) {
        Request request = new Request("ProgramController", "fetchTokenData", MyGson.toJson(token));
        return (TokenData) NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<TokenData> getTokenData() {
        Request request = new Request("ProgramController", "getTokenData");
        return (ArrayList<TokenData>) NetworkController.getNetworkController().transferData(request, TokenData[].class);
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
