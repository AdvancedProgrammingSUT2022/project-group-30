package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;

public class AutoSavePageController {

    @FXML
    private VBox box;

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("GamePage");
    }

    public void afterEachTurn(MouseEvent mouseEvent) throws IOException {
    }

    public void afterEachUnitAction(MouseEvent mouseEvent) {
    }

    public void afterEachWar(MouseEvent mouseEvent) {
    }
}
