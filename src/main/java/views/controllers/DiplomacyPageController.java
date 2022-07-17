package views.controllers;

import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Civilization;
import models.GameDataBase;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class DiplomacyPageController {
    @FXML
    private ListView civilizationsList;
    @FXML
    private ListView messageList;

    private ObservableList<Civilization> civsListData;

    private GameDataBase database;
    private GameController controller;

    @FXML
    public void initialize() {
        database = GameDataBase.getGameDataBase();
        controller = GameController.getGameController();
//        for (CivilizationPair civilizationPair : GameDataBase.getGameDataBase().getCivilizationPairs()) {
//            System.out.println(civilizationPair.getCivilizationsArray().get(0).getName() + " " +
//                    civilizationPair.getCivilizationsArray().get(1).getName());
//        }
        ArrayList<Civilization> discoveredCivs = database.getDiscoveredCivilizations(controller.getCurrentPlayer());
        civsListData = FXCollections.observableArrayList();
        civsListData.setAll(discoveredCivs);
        civilizationsList.setItems(civsListData);
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
