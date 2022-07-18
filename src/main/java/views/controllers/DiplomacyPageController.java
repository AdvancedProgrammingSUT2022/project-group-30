package views.controllers;

import controllers.GameController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import models.Civilization;
import models.GameDataBase;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import views.Main;
import views.customcomponents.GoldAmountDialog;
import views.customcomponents.ResourcePicker;

import java.io.IOException;
import java.util.ArrayList;

public class DiplomacyPageController {
    @FXML
    private ListView<Civilization> civilizationsList;
    @FXML
    private ListView messageList;
    @FXML
    private Label relationStatusField;

    private ObservableList<Civilization> civsListData;

    private GameDataBase database;
    private GameController controller;
    private Civilization selectedCiv = null;

    @FXML
    public void initialize() {
        database = GameDataBase.getGameDataBase();
        controller = GameController.getGameController();
//        for (CivilizationPair civilizationPair : GameDataBase.getGameDataBase().getCivilizationPairs()) {
//            System.out.println(civilizationPair.getCivilizationsArray().get(0).getName() + " " +
//                    civilizationPair.getCivilizationsArray().get(1).getName());
//        }
        initializeCivilizationsList();
    }

    private void initializeCivilizationsList() {
        ArrayList<Civilization> discoveredCivs = database.getDiscoveredCivilizations(controller.getCurrentPlayer());
        civsListData = FXCollections.observableArrayList();
        civsListData.setAll(discoveredCivs);
        civilizationsList.setItems(civsListData);
        civilizationsList.setCellFactory(new Callback<ListView<Civilization>, ListCell<Civilization>>() {
            @Override
            public ListCell<Civilization> call(ListView<Civilization> civilizationListView) {
                return new CivilizationItem();
            }
        });
        civilizationsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Civilization>() {
            @Override
            public void changed(ObservableValue<? extends Civilization> observableValue, Civilization civilization, Civilization t1) {
                selectedCiv = t1;
                updateInfo();
            }
        });
    }

    private void updateInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(selectedCiv.getName()).append("\n");
        if (database.getDiplomaticRelation(selectedCiv, controller.getCurrentPlayer()).areMutuallyVisible())
        {
            sb.append("Discovery Status: Discovered\n");
        } else {
            sb.append("Discovery Status: Not Discovered\n");
        }
        if (database.getDiplomaticRelation(selectedCiv, controller.getCurrentPlayer()).areAtWar())
        {
            sb.append("War Status: At War\n");
        } else {
            sb.append("War Status: At Peace\n");
        }
        relationStatusField.setText(sb.toString());
    }

    @FXML
    private void onDeclareWarButtonClick() {
        if (selectedCiv != null) {
            controller.declareWar(controller.getCurrentPlayer(), selectedCiv);
            updateInfo();
        }
    }

    @FXML
    private void onDeclarePeaceButtonClick() {
        if (selectedCiv != null) {
            controller.declarePeace(controller.getCurrentPlayer(), selectedCiv);
            updateInfo();
        }
    }

    @FXML
    private void onSendGoldButtonClick() {
        if (selectedCiv == null) {
            return;
        }

        GoldAmountDialog amountDialog = new GoldAmountDialog() {
            @Override
            protected void onSubmitButtonClicked() {
                String text = inputField.getText();
                if (!text.matches("\\d+")) {
                    messageLabel.setText("Invalid Number Format");
                    return;
                }
                int amount = Integer.parseInt(text);
                if (amount > controller.getCurrentPlayer().getGoldCount()) {
                    messageLabel.setText("You don't have enough gold!");
                    return;
                }
                controller.getCurrentPlayer().reduceGold(amount);
                selectedCiv.addGold(amount);
                // NOTIF?
                updateInfo();
                this.close();
            }
        };
        amountDialog.show();
    }

    @FXML
    private void onSendLuxuryResourceClick() {
        if (selectedCiv == null) {
            return;
        }
        ResourcePicker resourcePicker = new ResourcePicker(controller.getCurrentPlayer().getLuxuryResources()) {
            @Override
            protected void onResourceButtonClick(Resource resource) {
                controller.getCurrentPlayer().addLuxuryResource((LuxuryResource) resource, -1);
                selectedCiv.addLuxuryResource((LuxuryResource) resource, 1);
//                System.out.println(resource.getName() + " " + controller.getCurrentPlayer().getLuxuryResources().get(resource));
//                System.out.println(resource.getName() + " " + selectedCiv.getLuxuryResources().get(resource));
                this.close();
                updateInfo();
            }
        };
        resourcePicker.showAndWait();
    }

    @FXML
    private void onSendStrategicResourceClick() {
        if (selectedCiv == null) {
            return;
        }
        ResourcePicker resourcePicker = new ResourcePicker(controller.getCurrentPlayer().getStrategicResources()) {
            @Override
            protected void onResourceButtonClick(Resource resource) {
                controller.getCurrentPlayer().addStrategicResource((StrategicResource) resource, -1);
                selectedCiv.addStrategicResource((StrategicResource) resource, 1);
//                System.out.println(resource.getName() + " " + controller.getCurrentPlayer().getStrategicResources().get(resource));
//                System.out.println(resource.getName() + " " + selectedCiv.getStrategicResources().get(resource));
                this.close();
                updateInfo();
            }
        };
        resourcePicker.showAndWait();
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class CivilizationItem extends ListCell<Civilization> {
        @Override
        public void updateItem(Civilization civilization, boolean empty) {
            if (civilization == null && empty) {
                return;
            }
            Label label = new Label();
            BorderPane parent = new BorderPane(label);
            parent.setCenter(label);
            label.setText(civilization.getName());
            label.getStyleClass().add("civilizationItem");
            setGraphic(parent);
        }
    }
}
