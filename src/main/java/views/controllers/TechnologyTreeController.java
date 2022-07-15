package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import models.technology.Technology;
import views.customcomponents.TechnologyBlock;

import java.util.HashMap;

public class TechnologyTreeController {
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox parent;
    @FXML
    private Pane pane;

    private HashMap<Technology, TechnologyBlock> techBlocks;

    @FXML
    public void initialize() {
        gridPane.setHgap(60);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(15));
//        gridPane.setStyle("-fx-border-color: red;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setPannable(true);
        scrollPane.fitToHeightProperty().set(true);
//        scrollPane.setStyle("-fx-border-color: blue;");

        techBlocks = new HashMap<>();
        int[] nextFreeRowIndex = new int[Technology.findMaxGrade() + 1];
        for (int i = 0; i < Technology.values().length; i++) {
            Technology technology = Technology.values()[i];
            TechnologyBlock block = new TechnologyBlock();
            block.setTechnology(GameController.getGameController().getCurrentPlayer().getTechnologies(), technology);
            GridPane.setConstraints(block, technology.getGrade(), nextFreeRowIndex[technology.getGrade()]);
            nextFreeRowIndex[technology.getGrade()]++;
            gridPane.getChildren().add(block);
            techBlocks.put(technology, block);
        }

        Line linel = new Line();
        linel.setStartX(0);
        linel.setStartY(0);
        linel.setEndX(400);
        linel.setEndY(400);
        linel.setFill(Color.GREEN);
        linel.setStroke(Color.GREEN);
        pane.getChildren().add(0, linel);
        linel.toFront();

        for (Technology technology : Technology.values()) {
            for (Technology dependentTechnology : technology.getDependentTechnologies()) {
                Line line = new Line();
                line.setStartX(techBlocks.get(technology).getLayoutX());
                line.setStartY(techBlocks.get(technology).getLayoutY());
                line.setEndX(techBlocks.get(dependentTechnology).getLayoutX());
                line.setEndY(techBlocks.get(dependentTechnology).getLayoutY());
                line.setFill(Color.RED);
                pane.getChildren().add(0, line);
                line.toFront();
            }
        }

        parent.getChildren().add(scrollPane);
        pane.setStyle("-fx-border-color: yellow");
    }
}