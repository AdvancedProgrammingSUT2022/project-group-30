package views.controllers;

import controllers.ChatController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.ProgramDatabase;
import models.User;
import views.Main;

import java.io.IOException;

public class PrivateChatsListController {
    @FXML
    private TextField contactUsernameField;

    @FXML
    protected void onChatButtonClick() {
        String contactUsername = contactUsernameField.getText();
        User contact = ProgramController.getProgramController().getUserByUsername(contactUsername);
        if (contact == null) {
            RegisterPageGraphicalController.showPopup("Username not found!");
            return;
        }
        if (contact == ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken())) {
            RegisterPageGraphicalController.showPopup("Invalid username!");
            return;
        }
        ChatController.getChatController().setCurrentPrivateContactId(contact.getId(), NetworkController.getNetworkController().getToken());

        try {
            Main.loadFxmlFile("PrivateChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Main.loadFxmlFile("ChatFirstPage");
    }
}
