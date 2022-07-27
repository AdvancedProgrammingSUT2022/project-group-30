package controllers;

import com.google.gson.reflect.TypeToken;
import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import models.chat.TokenData;
import netPackets.Request;
import terminalViews.GameView;
import terminalViews.ProfilePageView;
import terminalViews.View;
import utilities.MyGson;
import views.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean checkUsernameValidity(String username) {
        Request request = new Request("ProgramController", "checkUsernameValidity", MyGson.toJson(username));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkPasswordSize(String password) {
        Request request = new Request("ProgramController", "checkPasswordSize", MyGson.toJson(password));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkPasswordValidity(String password) {
        Request request = new Request("ProgramController", "checkPasswordValidity", MyGson.toJson(password));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkNicknameValidity(String nickname) {
        Request request = new Request("ProgramController", "checkNicknameValidity", MyGson.toJson(nickname));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }


// ...

    public boolean checkUsernameUniqueness(String username) {
        Request request = new Request("ProgramController", "checkUsernameUniqueness", MyGson.toJson(username));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkNicknameUniqueness(String nickname) {
        Request request = new Request("ProgramController", "checkNicknameUniqueness", MyGson.toJson(nickname));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void addUser(User user) {
        Request request = new Request("ProgramController", "addUser", MyGson.toJson(user));
        NetworkController.getNetworkController().transferData(request);
    }


    // returns true if there was an error and false otherwise
    public boolean checkLoginErrors(String username, String password) {
        Request request = new Request("ProgramController", "checkLoginErrors", MyGson.toJson(username), MyGson.toJson(password));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean isUserLoggedIn() {
        Request request = new Request("ProgramController", "isUserLoggedIn");
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkNextMenuValidity(String menuName) {
        Request request = new Request("ProgramController", "checkNextMenuValidity", MyGson.toJson(menuName));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public void loginUser(String username, int token) {
        Request request = new Request("ProgramController", "loginUser", MyGson.toJson(username), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }


    public void setProgramDatabase() {
        Request request = new Request("ProgramController", "setProgramDatabase");
        NetworkController.getNetworkController().transferData(request);
    }

    public ProgramDatabase getProgramDatabase() {
        Request request = new Request("ProgramController", "getProgramDatabase");
        return (ProgramDatabase) NetworkController.getNetworkController().transferData(request);
    }

    public void changeLoggedInUserNickname(String nickname, int token) {
        Request request = new Request("ProgramController", "changeLoggedInUserNickname", MyGson.toJson(nickname), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public void changeLoggedInUserPassword(String newPassword, int token) {
        Request request = new Request("ProgramController", "changeLoggedInUserPassword", MyGson.toJson(newPassword), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public boolean checkNewPasswordAndCurrentPasswordEquality(String currentPassword, String newPassword) {
        Request request = new Request("ProgramController", "checkNewPasswordAndCurrentPasswordEquality", MyGson.toJson(currentPassword), MyGson.toJson(newPassword));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }


    // returns true if the password is correct, false otherwise
    public boolean checkLoggedInUserPassword(String password, int token) {
        Request request = new Request("ProgramController", "checkLoggedInUserPassword", MyGson.toJson(password), MyGson.toJson(token));
        return (boolean) NetworkController.getNetworkController().transferData(request);
    }

    public String getLoggedInUserImageName(int token) {
        Request request = new Request("ProgramController", "getLoggedInUserImageName", MyGson.toJson(token));
        return (String) NetworkController.getNetworkController().transferData(request);
    }

    public void changeLoggedInUsersProfileImage(String imageName, int token) {
        Request request = new Request("ProgramController", "changeLoggedInUsersProfileImage", MyGson.toJson(imageName), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public void registerUser(String username, String password, String nickname, int token) {
        Request request = new Request("ProgramController", "registerUser", MyGson.toJson(username), MyGson.toJson(password), MyGson.toJson(nickname), MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }

    public void logoutUser(int token) {
        Request request = new Request("ProgramController", "logoutUser", MyGson.toJson(token));
        NetworkController.getNetworkController().transferData(request);
    }


    public View findTheNextMenu(String menuName) {
        Request request = new Request("ProgramController", "findTheNextMenu", MyGson.toJson(menuName));
        return (View) NetworkController.getNetworkController().transferData(request);
    }

    public void sortUsersArrayList() {
        Request request = new Request("ProgramController", "sortUsersArrayList");
        NetworkController.getNetworkController().transferData(request);
    }

    public ArrayList<User> getUsers() {
        Request request = new Request("ProgramController", "getUsers");
        return (ArrayList<User>) NetworkController.getNetworkController().transferData(request, User[].class);
    }


    public String getLoggedInUserUsername(int token){
        Request request=new Request("ProgramController","getLoggedInUserUsername",MyGson.toJson(token));
        return(String)NetworkController.getNetworkController().transferData(request);
    }


}
