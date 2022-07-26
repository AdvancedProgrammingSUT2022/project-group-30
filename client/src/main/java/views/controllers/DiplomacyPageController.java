package views.controllers;

import controllers.GameController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import models.Civilization;
import models.diplomacy.DiplomaticMessage;
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
    private ListView<DiplomaticMessage> messageList;
    @FXML
    private Label relationStatusField;
    @FXML
    private TextField newMessageField;

    private ObservableList<Civilization> civsListData;
    private ObservableList<DiplomaticMessage> messagesData;

    private GameController controller;
    private Civilization selectedCiv = null;

    @FXML
    public void initialize() {
        controller = GameController.getGameController();
//        for (CivilizationPair civilizationPair : GameDataBase.getGameDataBase().getCivilizationPairs()) {
//            System.out.println(civilizationPair.getCivilizationsArray().get(0).getName() + " " +
//                    civilizationPair.getCivilizationsArray().get(1).getName());
//        }
        initializeCivilizationsList();
    }

    private void initializeMessageList() {
        ArrayList<DiplomaticMessage> diplomaticMessages = controller.getDiplomaticRelation(controller.getCurrentPlayer(), selectedCiv).getMessages();
        System.out.println("messages:");
        for (DiplomaticMessage diplomaticMessage : diplomaticMessages) {
            System.out.println(diplomaticMessage.getMessage());
        }
//        messages.add(new Message("meow", controller.getCurrentPlayer()));
//        messages.add(new Message("helloooo:)", selectedCiv));
//        messages.add(new Message("noooooo", controller.getCurrentPlayer()));
        messagesData = FXCollections.observableArrayList();
        messagesData.setAll(diplomaticMessages);
        messageList.setItems(messagesData);
        System.out.println(messagesData.size());
        messageList.setCellFactory(new Callback<ListView<DiplomaticMessage>, ListCell<DiplomaticMessage>>() {
            @Override
            public ListCell<DiplomaticMessage> call(ListView listView) {
                return new MessageBox();
            }
        });
    }

    private void initializeCivilizationsList() {
        ArrayList<Civilization> discoveredCivs = controller.getDiscoveredCivilizations(controller.getCurrentPlayer());
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
        if (controller.getDiplomaticRelation(selectedCiv, controller.getCurrentPlayer()).areMutuallyVisible()) {
            sb.append("Discovery Status: Discovered\n");
        } else {
            sb.append("Discovery Status: Not Discovered\n");
        }
        if (controller.getDiplomaticRelation(selectedCiv, controller.getCurrentPlayer()).areAtWar()) {
            sb.append("War Status: At War\n");
        } else {
            sb.append("War Status: At Peace\n");
        }
        relationStatusField.setText(sb.toString());
        initializeMessageList();
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
                controller.reduceGoldFromCiv(controller.getCurrentPlayer(), amount);
                controller.addGoldToCiv(selectedCiv, amount);
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
                controller.addLuxuryResourceToCiv(controller.getCurrentPlayer(), (LuxuryResource) resource, -1);
                controller.addLuxuryResourceToCiv(selectedCiv, (LuxuryResource) resource, 1);
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
                controller.addStrategicResourceToCiv(controller.getCurrentPlayer(), (StrategicResource) resource, -1);
                controller.addStrategicResourceToCiv(selectedCiv, (StrategicResource) resource, 1);
//                System.out.println(resource.getName() + " " + controller.getCurrentPlayer().getStrategicResources().get(resource));
//                System.out.println(resource.getName() + " " + selectedCiv.getStrategicResources().get(resource));
                this.close();
                updateInfo();
            }
        };
        resourcePicker.showAndWait();
    }

    @FXML
    private void onSendButtonClick() {
        if (selectedCiv == null) {
            return;
        }
        String messageText = newMessageField.getText();
        DiplomaticMessage newDiplomaticMessage = new DiplomaticMessage(messageText, GameController.getGameController().getCurrentPlayer());
        controller.sendDiplomaticMessage(newDiplomaticMessage, newDiplomaticMessage.getSender(), selectedCiv);
        newMessageField.setText("");
        initializeMessageList();
    }

    @FXML
    private void onReloadButtonClick() {
        initializeMessageList();
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

    private static class MessageBox extends ListCell<DiplomaticMessage> {
        @Override
        public void updateItem(DiplomaticMessage diplomaticMessage, boolean empty) {
            if (diplomaticMessage == null || empty) {
                return;
            }
            Label textLabel = new Label(diplomaticMessage.getMessage());
            if (diplomaticMessage.getSender().equals(GameController.getGameController().getCurrentPlayer())) {
                AnchorPane.setRightAnchor(textLabel, 10.0);
            } else {
                AnchorPane.setLeftAnchor(textLabel, 10.0);
            }
            textLabel.setStyle("-fx-font-size: 16; -fx-font-family: 'Times New Roman'; -fx-background-color: white; -fx-text-fill: black;");
            AnchorPane parent = new AnchorPane(textLabel);
            setGraphic(parent);
        }
    }
}
