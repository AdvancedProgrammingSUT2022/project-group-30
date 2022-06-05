package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.ProgramDatabase;
import models.User;
import models.chat.ChatDataBase;
import views.Main;

import java.io.IOException;

public class PrivateChatsListController {
    @FXML
    private TextField contactUsernameField;

    @FXML
    protected void onChatButtonClick() {
        String contactUsername = contactUsernameField.getText();
        User contact = ProgramDatabase.getProgramDatabase().getUserByUsername(contactUsername);
        if (contact == null) {
            RegisterPageGraphicalController.showPopup("Username not found!");
            return;
        }
        if (contact == ProgramDatabase.getProgramDatabase().getLoggedInUser()) {
            RegisterPageGraphicalController.showPopup("Invalid username!");
            return;
        }
        ChatDataBase.getChatDatabase().setCurrentPrivateContactId(contact.getId());

        try {
            Main.loadFxmlFile("PrivateChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
