package views.controllers;

import javafx.fxml.FXML;
import views.Main;

public class ChatMainPageController {
    @FXML
    public void initialize() {
        Main.getStage().setWidth(800);
    }
}
