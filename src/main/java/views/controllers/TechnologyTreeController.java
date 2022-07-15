package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import models.technology.Technology;
import views.customcomponents.TechnologyBlock;

import java.util.HashMap;

public class TechnologyTreeController {
    @FXML
    private HBox parent;
    @FXML
    private Pane techPane;

    private final int padding = 15;
    private final int hspace = 60;
    private final int vspace = 30;

    private HashMap<Technology, TechnologyBlock> techBlocks;

    @FXML
    public void initialize() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(techPane);
        scrollPane.setPannable(true);
//        scrollPane.fitToHeightProperty().set(true);
//        scrollPane.setStyle("-fx-border-color: blue;");

        techBlocks = new HashMap<>();
        int[] nextFreeRowIndex = new int[Technology.findMaxGrade() + 1];
        for (int i = 0; i < Technology.values().length; i++) {
            Technology technology = Technology.values()[i];
            TechnologyBlock block = new TechnologyBlock();
            block.setTechnology(GameController.getGameController().getCurrentPlayer().getTechnologies(), technology);
            block.setLayoutX(padding + technology.getGrade() * (hspace + block.getPrefWidth()));
            block.setLayoutY(padding + nextFreeRowIndex[technology.getGrade()] * (vspace + block.getPrefHeight()));
            nextFreeRowIndex[technology.getGrade()]++;
            techPane.getChildren().add(block);
            techBlocks.put(technology, block);
        }
        techPane.setMinWidth(padding * 2 + (Technology.findMaxGrade() + 1) * (hspace + TechnologyBlock.WIDTH) - hspace);

//        Line linel = new Line();
//        linel.setStartX(0);
//        linel.setStartY(0);
//        linel.setEndX(400);
//        linel.setEndY(400);
//        linel.setFill(Color.GREEN);
//        linel.setStroke(Color.GREEN);
//        parent.getChildren().add(0, linel);
//        linel.toFront();
//
//        for (Technology technology : Technology.values()) {
//            for (Technology dependentTechnology : technology.getDependentTechnologies()) {
//                Line line = new Line();
//                line.setStartX(techBlocks.get(technology).getLayoutX());
//                line.setStartY(techBlocks.get(technology).getLayoutY());
//                line.setEndX(techBlocks.get(dependentTechnology).getLayoutX());
//                line.setEndY(techBlocks.get(dependentTechnology).getLayoutY());
//                line.setFill(Color.RED);
//                parent.getChildren().add(0, line);
//                line.toFront();
//            }
//        }

        parent.setStyle("-fx-border-color: yellow");
        parent.getChildren().add(scrollPane);

//        parent.setMaxWidth(Double.MAX_VALUE);
    }
}