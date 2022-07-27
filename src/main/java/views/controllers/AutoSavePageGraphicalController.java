package views.controllers;

import controllers.AutoSavePageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import menusEnumerations.AutoSave;
import models.GameDataBase;
import views.Main;

import java.io.IOException;

public class AutoSavePageGraphicalController {
    private final AutoSavePageController autoSavePageController = AutoSavePageController.getAutoSavePageController();

    @FXML
    private TextField numberOfSavedFiles;
    @FXML
    private VBox box;

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("GamePage");
    }

    public void afterEachTurn(MouseEvent mouseEvent) throws IOException {
        GameDataBase.getGameDataBase().setAutoSaveMode(AutoSave.AFTER_EACH_TURN);
    }

    public void afterResearchCompletion(MouseEvent mouseEvent) {
        GameDataBase.getGameDataBase().setAutoSaveMode(AutoSave.AFTER_RESEARCH_COMPLETION);
    }

    public void afterCityCreation(MouseEvent mouseEvent) {
        GameDataBase.getGameDataBase().setAutoSaveMode(AutoSave.AFTER_CITY_CREATION);
    }

    public void Off(MouseEvent mouseEvent) {
        GameDataBase.getGameDataBase().setAutoSaveMode(AutoSave.OFF);
    }

    public void applyChanges(MouseEvent mouseEvent) {
        if (this.autoSavePageController.checkNumberOfSavedFilesErrors(numberOfSavedFiles.getText()))
            RegisterPageGraphicalController.showPopup("Please enter a number between 1 and 10!");
        else if (GameDataBase.getGameDataBase().getAutoSaveMode() == AutoSave.OFF)
            RegisterPageGraphicalController.showPopup("Auto save mode is off!");
        else {
            GameDataBase.getGameDataBase().setNumberOfAutoSavedFiles(Integer.parseInt(numberOfSavedFiles.getText()));
            RegisterPageGraphicalController.showPopup("Changes saved successfully!");
        }
    }
}
