package views.customcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class CheatBox extends Stage {
    protected TextField textField;
    protected Button submitButton;
    protected VBox parent;
    protected Scene scene;

    public CheatBox() {
        parent = new VBox();
        parent.setAlignment(Pos.CENTER);
        parent.setPadding(new Insets(10));
        parent.setSpacing(5);
        parent.setStyle("-fx-background-color: #5f5f5f");
        textField = new TextField();
        textField.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-family: 'Old English Text MT'; -fx-font-size: 20;");
        submitButton = new Button();
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onSubmitButtonClick();
            }
        });
        submitButton.setText("SUBMIT");
        submitButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 24;");
        parent.getChildren().addAll(textField, submitButton);
        scene = new Scene(parent);
        setScene(scene);
    }

    public abstract void onSubmitButtonClick();
}
