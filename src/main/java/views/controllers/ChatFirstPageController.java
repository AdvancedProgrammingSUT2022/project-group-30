package views.controllers;

import javafx.fxml.FXML;
import views.Main;

import java.io.IOException;

public class ChatFirstPageController {
    @FXML
    public void initialize() {
        Main.getStage().setWidth(800);
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Main.getStage().setWidth(1294);
        Main.loadFxmlFile("MainPage");
    }
}
