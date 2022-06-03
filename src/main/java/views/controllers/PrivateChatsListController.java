package views.controllers;

import javafx.fxml.FXML;
import views.Main;

import java.io.IOException;

public class PrivateChatsListController {
    @FXML
    protected void onChatButtonClick() {
//        RegisterPageGraphicalController.showPopup("meow");
        try {
            Main.loadFxmlFile("PrivateChatPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
