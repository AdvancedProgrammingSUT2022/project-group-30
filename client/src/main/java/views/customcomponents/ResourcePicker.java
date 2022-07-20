package views.customcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.resources.Resource;

import java.util.HashMap;

public abstract class ResourcePicker extends Stage {
    private Scene scene;

    public ResourcePicker(HashMap<? extends Resource, Integer> resources) {
        Label topLabel = new Label("Pick a Resource:");
        topLabel.setMinWidth(200);
        VBox buttonsBox = new VBox();
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(5);
        for (Resource resource : resources.keySet()) {
            if (resources.get(resource) > 0) {
                Button newButton = new Button(resource.getName() + ": " + resources.get(resource));
                newButton.setStyle("-fx-pref-height: 50; -fx-pref-width: 150; -fx-font-size: 20; -fx-background-color: white; -fx-text-fill: black;");
                newButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        onResourceButtonClick(resource);
                    }
                });
                buttonsBox.getChildren().add(newButton);
            }
        }
        VBox parent = new VBox(topLabel, buttonsBox);
        parent.setAlignment(Pos.CENTER);
        parent.setSpacing(10);
        parent.setPadding(new Insets(10));
        scene = new Scene(parent);
        setScene(scene);
    }

    protected abstract void onResourceButtonClick(Resource resource);
}
