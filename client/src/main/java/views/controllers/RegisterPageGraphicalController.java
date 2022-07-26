package views.controllers;

import controllers.LoginPageController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import views.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegisterPageGraphicalController {

    private ProgramController controller = ProgramController.getProgramController();

    @FXML
    private TextField username;
    @FXML
    private TextField nickname;
    @FXML
    private TextField password;
    @FXML
    private VBox box;

    public static void showPopup(String note){
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        pane.setPrefHeight(200);
        pane.setPrefWidth(400);
        pane.setStyle("-fx-background-color: #74bfc7;");
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        Text text = new Text(note);
        text.setStyle("-fx-font-family: \"Academy Engraved LET\"; -fx-text-fill: #e50000; -fx-font-size: 20");
        box.getChildren().add(text);
        pane.setCenter(box);
        Button button = new Button();
        button.setText("Ok");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.hide();
            }
        });
        box.getChildren().add(button);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void submit(MouseEvent mouseEvent) throws IOException {
        this.controller.setProgramDatabase();
        if (!this.controller.checkUsernameValidity(username.getText())) {
            showPopup("Username can only contain uppercase and lowercase English letters, numbers.");
            return;
        }
        if (!this.controller.checkPasswordSize(password.getText())) {
            showPopup("The Password must be at least 8 characters");
            return;
        }
        if (!this.controller.checkPasswordValidity(password.getText())) {
            showPopup("The Password must contain at least one uppercase letter, one lowercase letter, and one number character.");
            return;
        }
        if (!this.controller.checkNicknameValidity(nickname.getText())) {
            showPopup("Nickname can only contain uppercase and lowercase English letters, numbers, and characters from this character set: {-,_}");
            return;
        }
        if (!this.controller.checkUsernameUniqueness(username.getText())) {
            showPopup("user with username " + username.getText() + " already exists.");
            return;
        }
        if (!this.controller.checkNicknameUniqueness(nickname.getText())) {
            showPopup("user with nickname " + nickname.getText() + " already exists.");
            return;
        }
        this.controller.registerUser(username.getText(), password.getText(), nickname.getText(), NetworkController.getNetworkController().getToken());
        showPopup("user created successfully!");
        Main.loadFxmlFile("MainPage");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("SecondPage");
    }
}
