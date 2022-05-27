package views.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import views.Main;

import java.net.MalformedURLException;
import java.net.URL;

public class ChangeProfilePhotoPageController {

    @FXML
    private Pane pane;

    @FXML
    public void initialize() throws MalformedURLException {
        showDefaultProfilePhotos();
        makePageButtons();
    }

    public void showDefaultProfilePhotos() throws MalformedURLException {
        Text text = new Text("Select the profile photo you want by clicking on it, or you can upload your own profile photo!");
        text.setStyle("-fx-font-family: 'Academy Engraved LET'; -fx-fill: #ad5a5a");
        text.setX((pane.getPrefWidth() - text.getLayoutBounds().getWidth()) / 2);
        text.setY(50);
        pane.getChildren().add(text);
        ImageView firstImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/0.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        firstImage.setX(415);
        firstImage.setY(100);
        ImageView secondImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/1.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        secondImage.setX(715);
        secondImage.setY(100);
        ImageView thirdImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/2.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        thirdImage.setX(415);
        thirdImage.setY(300);
        ImageView fourthImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/3.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        fourthImage.setX(715);
        fourthImage.setY(300);
        pane.getChildren().add(firstImage);
        pane.getChildren().add(secondImage);
        pane.getChildren().add(thirdImage);
        pane.getChildren().add(fourthImage);
    }

    public void makePageButtons(){
        //<Button text="Back" styleClass="menu-button" prefHeight="50" prefWidth="200" onMouseClicked="#back"/>
        Button upload = new Button("Upload Photo");
        upload.setPrefHeight(50);
        upload.setPrefWidth(200);
        upload.getStyleClass().add("menu-button");
        upload.setLayoutX(540);
        upload.setLayoutY(520);
        Button back = new Button("Back");
        back.setPrefHeight(50);
        back.setPrefWidth(200);
        back.getStyleClass().add("menu-button");
        back.setLayoutX(540);
        back.setLayoutY(620);
        pane.getChildren().add(upload);
        pane.getChildren().add(back);
    }

    public void setReturnToProfileMenuForObject(Node node){
        //node.setOnMouseClicked();
    }
}
