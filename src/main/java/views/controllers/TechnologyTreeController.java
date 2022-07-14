package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class TechnologyTreeController {
    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.GREEN);
        GridPane.setConstraints(rectangle, 0, 0);
        rectangle.setWidth(200);
        rectangle.setHeight(200);
        gridPane.getChildren().add(rectangle);
    }
}