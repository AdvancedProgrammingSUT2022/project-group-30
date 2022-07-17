package views.customcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class GoldAmountDialog extends Stage {
    protected VBox parent;
    protected Scene scene;
    protected TextField inputField;
    protected Label messageLabel;

    public GoldAmountDialog() {
        parent = new VBox();
        parent.setSpacing(10);
        parent.setPadding(new Insets(10));
        parent.setAlignment(Pos.CENTER);

        messageLabel = new Label("Enter Amount");
        messageLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Times New Roman';");
        Label amountLabel = new Label("Amount:");
        inputField = new TextField();
        HBox hbox = new HBox(amountLabel, inputField);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);

        Button submitButton = new Button("SUBMIT");
        submitButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white;");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onSubmitButtonClicked();
            }
        });
        parent.getChildren().addAll(messageLabel, hbox, submitButton);
        scene = new Scene(parent);
        setScene(scene);
    }

    protected abstract void onSubmitButtonClicked();
}
