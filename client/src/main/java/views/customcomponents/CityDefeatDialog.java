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

public abstract class CityDefeatDialog extends Stage {
    public CityDefeatDialog() {
        Label textLabel = new Label("You have captured this city, what do you want to do with it?");
        textLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Times New Roman';");
        Button destroyButton = new Button("Destroy");
        destroyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onDestroyButtonClick();
                CityDefeatDialog.this.close();
            }
        });
        destroyButton.setStyle("-fx-pref-width: 150; -fx-font-size: 20; -fx-background-color: white; -fx-text-fill: black;");
        Button annexButton = new Button("Annex");
        annexButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onAnnexButtonClick();
                CityDefeatDialog.this.close();
            }
        });
        annexButton.setStyle("-fx-pref-width: 150; -fx-font-size: 20; -fx-background-color: white; -fx-text-fill: black;");
        HBox buttonsBox = new HBox(annexButton, destroyButton);
        buttonsBox.setSpacing(5);
        VBox parent = new VBox(textLabel, buttonsBox);
        parent.setSpacing(10);
        parent.setPadding(new Insets(15));
        parent.setAlignment(Pos.CENTER);
        Scene scene = new Scene(parent);
        setScene(scene);
    }

    public abstract void onDestroyButtonClick();
    public abstract void onAnnexButtonClick();
}
