package views.controllers;

import controllers.GameController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.GameDataBase;
import models.ProgramDatabase;
import models.User;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class PlayersCountPageController {

    private GameController controller = GameController.getGameController();


    @FXML
    private BorderPane pane;
    private int opponentsNumber;

    @FXML
    private Button firstButton;
    @FXML
    private Button secondButton;
    @FXML
    private Button thirdButton;
    @FXML
    private Button fourthButton;
    @FXML
    private VBox box;

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("GamePage");
    }

    public void setOpponents(MouseEvent mouseEvent) {
        if (firstButton.equals(mouseEvent.getSource())) {
            this.opponentsNumber = 1;
        } else if (secondButton.equals(mouseEvent.getSource())) {
            this.opponentsNumber = 2;
        } else if (thirdButton.equals(mouseEvent.getSource())) {
            this.opponentsNumber = 3;
        } else if (fourthButton.equals(mouseEvent.getSource())) {
            this.opponentsNumber = 4;
        }
        enterPlayersUsernames();
    }

    public void setPlayers(ArrayList<String> usernames) {
        User[] players = new User[usernames.size() + 1];
        players[0] = ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken());
        for (int i = 0; i < usernames.size(); i++) {
            players[i + 1] = ProgramController.getProgramController().getUserByUsername(usernames.get(i));
        }
        this.controller.addPlayers(players);
    }

    public void enteredStartingPlayersErrorHandling(ArrayList<String> usernames) throws IOException {
        for (int i = 0; i < usernames.size(); i++) {
            if (ProgramController.getProgramController().getUserByUsername(usernames.get(i)) == null) {
                RegisterPageGraphicalController.showPopup("Invalid players!");
                return;
            }
            if (usernames.get(i).equals(ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken()).getUsername())) {
                RegisterPageGraphicalController.showPopup("You cannot play with yourself");
                return;
            }
        }
        for (int i = 0; i < usernames.size(); i++) {
            for (int j = i + 1; j < usernames.size(); j++) {
                if (usernames.get(i).equals(usernames.get(j))) {
                    RegisterPageGraphicalController.showPopup("Two or more usernames are the same");
                    return;
                }
            }
        }
        setPlayers(usernames);
        Main.loadFxmlFile("ChooseMapPage");
    }

    public void enterPlayersUsernames() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(720);
        borderPane.setPrefWidth(1280);
        borderPane.getStylesheets().addAll(pane.getStylesheets());
        borderPane.getStyleClass().add("enteringMenus");
        VBox vbox = new VBox();
        borderPane.setCenter(vbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);

        for (int i = 0; i < opponentsNumber; i++) {
            TextField textField = new TextField();
            textField.getStyleClass().add("text-field");
            textField.setPromptText("Enter your opponent username:");
            textField.setMaxHeight(50);
            textField.setMaxWidth(300);
            vbox.getChildren().add(textField);
        }

        Button submit = new Button();
        submit.setText("Submit");
        submit.getStyleClass().add("menu-button");
        submit.setPrefHeight(50);
        submit.setPrefWidth(200);
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ArrayList<String> usernames = new ArrayList<>();
                for (int i = 0; i < vbox.getChildren().size(); i++) {
                    if (vbox.getChildren().get(i) instanceof TextField) {
                        TextField field = (TextField) vbox.getChildren().get(i);
                        usernames.add(field.getText());
                    }
                }
                try {
                    enteredStartingPlayersErrorHandling(usernames);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button back = new Button();
        back.setText("Back");
        back.getStyleClass().add("menu-button");
        back.setPrefHeight(50);
        back.setPrefWidth(200);
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("PlayersCountPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        vbox.getChildren().add(submit);
        vbox.getChildren().add(back);

        Main.getScene().setRoot(borderPane);
    }
}
