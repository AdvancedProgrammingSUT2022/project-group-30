package views.customcomponents;

import controllers.ChatController;
import controllers.ProgramController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import views.controllers.RegisterPageGraphicalController;

public class AddMemberDialog extends Stage {
    private Scene scene;
    private VBox parent;
    private int roomId;

    public AddMemberDialog(int roomId) {
        this.roomId = roomId;
        initialize();
    }

    private void initialize() {
        parent = new VBox();
        parent.setPadding(new Insets(10));
        parent.setSpacing(10);
        parent.setAlignment(Pos.CENTER);
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        Label label = new Label("Username:");
        TextField textField = new TextField();
        HBox.setHgrow(textField, Priority.ALWAYS);
        hbox.setStyle("-fx-font-size: 18; -fx-font-family: 'Times New Roman';");
        hbox.getChildren().addAll(label, textField);
        Button submitButton = new Button("Add");
        submitButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 20; -fx-cursor: hand;");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = textField.getText();
                User user = ProgramController.getProgramController().getUserByUsername(username);
                if (user == null) {
                    RegisterPageGraphicalController.showPopup("User Doesn't Exist!");
                    AddMemberDialog.this.close();
                } else if (ChatController.getChatController().isUserMemberOfRoom(user.getId(), roomId)) {
                    RegisterPageGraphicalController.showPopup("User is already a member!");
                    AddMemberDialog.this.close();
                } else {
                    ChatController.getChatController().addUserToRoom(user.getId(), roomId);
                    RegisterPageGraphicalController.showPopup("User added!");
                    AddMemberDialog.this.close();
                }
            }
        });
        parent.getChildren().addAll(hbox, submitButton);
        scene = new Scene(parent);
        this.setScene(scene);
    }
}
