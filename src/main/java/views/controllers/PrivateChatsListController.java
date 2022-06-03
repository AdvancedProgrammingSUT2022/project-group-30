package views.controllers;

import javafx.fxml.FXML;

public class PrivateChatsListController {
    @FXML
    protected void onChatButtonClick() {
        RegisterPageGraphicalController.showPopup("meow");
    }
}
