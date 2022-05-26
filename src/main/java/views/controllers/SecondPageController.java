package views.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import views.Main;

import java.net.MalformedURLException;
import java.net.URL;

public class SecondPageController {

    @FXML
    public Pane pane;

    @FXML
    public void initialize() throws MalformedURLException {
        addButtonToPain(this.pane, "register", 100, 100, "bg", null);
        addButtonToPain(this.pane, "login", 100, 170, "bg_dio", null);
    }

    public static void addButtonToPain(Pane pane, String buttonText, double xPosition, double yPosition, String buttonTemplateName, EventHandler onClick) throws MalformedURLException {
        ImageView button = new ImageView(new Image(new URL(Main.class.getResource("/images/buttons/" + buttonTemplateName + ".png").toExternalForm()).toExternalForm()));
        button.setX(xPosition);
        button.setY(yPosition);
        button.setOnMouseClicked(onClick);
        Text text = new Text(buttonText);
        text.getStyleClass().add("font");
        text.setX(button.getX() + button.getImage().getWidth() / 2 - text.getLayoutBounds().getWidth());
        text.setY(button.getY() + (button.getImage().getHeight() + text.getLayoutBounds().getHeight()) / 2);
        pane.getChildren().add(button);
        pane.getChildren().add(text);
    }

}
