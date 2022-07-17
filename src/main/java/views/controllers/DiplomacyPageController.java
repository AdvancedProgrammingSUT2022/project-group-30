package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DiplomacyPageController {
    @FXML
    private ListView civilizationsList;
    @FXML
    private ListView messageList;

    @FXML
    public void initialize() {

    }

    @FXML
    private void onBackButtonClick() {
        System.out.println("back button clicked");
    }
}
