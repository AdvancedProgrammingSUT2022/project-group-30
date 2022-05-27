package views.controllers;

import controllers.ProfilePageController;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangeProfilePhotoPageController {

    private ProfilePageController controller = ProfilePageController.getProfilePageController();

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
        chooseDefaultPicture(firstImage);
        ImageView secondImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/1.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        secondImage.setX(715);
        secondImage.setY(100);
        chooseDefaultPicture(secondImage);
        ImageView thirdImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/2.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        thirdImage.setX(415);
        thirdImage.setY(300);
        chooseDefaultPicture(thirdImage);
        ImageView fourthImage = new ImageView(new Image(new URL(Main.class.getResource("/images/avatars/3.jpeg").toExternalForm()).toExternalForm(), 150, 150, false, false));
        fourthImage.setX(715);
        fourthImage.setY(300);
        chooseDefaultPicture(fourthImage);
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
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("ProfilePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pane.getChildren().add(upload);
        pane.getChildren().add(back);
    }

    public void chooseDefaultPicture(ImageView imageView){
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String[] tokens = imageView.getImage().getUrl().toString().split("/");
                String imageName = tokens[tokens.length - 1];
                ChangeProfilePhotoPageController.this.controller.changeLoggedInUsersProfileImage(imageName);
                try {
                    Main.loadFxmlFile("ProfilePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
