package views.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import views.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SecondPageController {

    @FXML
    public Pane pane;

    @FXML
    public void initialize() throws MalformedURLException {
        addButtonToPain(this.pane, "register", 100, 100, "bg");
        pane.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("RegisterPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pane.getChildren().get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("RegisterPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        addButtonToPain(this.pane, "login", 100, 170, "bg_dio");
        pane.getChildren().get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("LoginPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pane.getChildren().get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("LoginPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void addButtonToPain(Pane pane, String buttonText, double xPosition, double yPosition, String buttonTemplateName) throws MalformedURLException {
        ImageView button = new ImageView(new Image(new URL(Main.class.getResource("/images/buttons/" + buttonTemplateName + ".png").toExternalForm()).toExternalForm()));
        button.setX(xPosition);
        button.setY(yPosition);
        Text text = new Text(buttonText);
        text.getStyleClass().add("font");
        text.setX(button.getX() + button.getImage().getWidth() / 2 - text.getLayoutBounds().getWidth());
        text.setY(button.getY() + (button.getImage().getHeight() + text.getLayoutBounds().getHeight()) / 2);
        pane.getChildren().add(button);
        pane.getChildren().add(text);
    }

}
