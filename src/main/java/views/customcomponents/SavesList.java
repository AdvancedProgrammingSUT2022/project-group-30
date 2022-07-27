package views.customcomponents;

import controllers.GameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.GameDataBase;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class SavesList extends Stage {
    private Scene scene;
    private VBox parent;

    public SavesList() {
        parent = new VBox();
        parent.setSpacing(5);
        parent.setAlignment(Pos.CENTER);
        parent.setPadding(new Insets(15));
        Label headLabel = new Label("Choose Save:");
        headLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 24;");
        parent.getChildren().addAll(headLabel);
        VBox.setMargin(headLabel, new Insets(0,0,30,0));
        ArrayList<String> allSaves = GameController.getGameController().getUsersSaves();
        ArrayList<String> savesToShow = new ArrayList<>();
        int savesCount = GameDataBase.getGameDataBase().getNumberOfAutoSavedFiles();
        for (int i = allSaves.size() - 1; i >= 0 && i >= allSaves.size() - savesCount; i--) {
            savesToShow.add(allSaves.get(i));
        }
        for (String saveName : savesToShow) {
            Button button = new Button(saveName);
            button.setStyle("-fx-font-size: 15; -fx-font-family: 'Times New Roman'; -fx-background-color: white; -fx-text-fill: black; -fx-pref-width: 300;");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    GameController.getGameController().initializeGameFromFile(saveName);
                    try {
                        Main.loadFxmlFile("CivilizationGamePage");
                        SavesList.this.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            parent.getChildren().addAll(button);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 20; -fx-font-family: 'Times New Roman'; -fx-background-color: red; -fx-text-fill: white; -fx-pref-width: 200");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SavesList.this.close();
            }
        });
        VBox.setMargin(backButton, new Insets(15, 0, 0, 0));

        parent.getChildren().addAll(backButton);

        scene = new Scene(parent);
        setScene(scene);
    }
}
