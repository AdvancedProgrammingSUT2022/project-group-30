package views.customcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AttackYesNoDialog extends Stage {
    public AttackYesNoDialog() {
        Label textLabel = new Label("You are at peace with this civilization, are you sure you want to attack it?");
        textLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Times New Roman';");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onYesButtonClick();
            }
        });
        yesButton.setStyle("-fx-pref-width: 150; -fx-font-size: 20; -fx-background-color: white; -fx-text-fill: black;");
        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onNoButtonClick();
            }
        });
        noButton.setStyle("-fx-pref-width: 150; -fx-font-size: 20; -fx-background-color: white; -fx-text-fill: black;");
        HBox buttonsBox = new HBox(noButton, yesButton);
        buttonsBox.setSpacing(5);
        VBox parent = new VBox(textLabel, buttonsBox);
        parent.setSpacing(10);
        parent.setPadding(new Insets(15));
        parent.setAlignment(Pos.CENTER);
        Scene scene = new Scene(parent);
        setScene(scene);
    }

    public abstract void onYesButtonClick();

    public void onNoButtonClick() {
        this.close();
    }
}
