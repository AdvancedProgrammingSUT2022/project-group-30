package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.technology.Technology;
import models.technology.TechnologyMap;
import views.Main;
import views.customcomponents.CheatBox;
import views.customcomponents.TechnologyBlock;

import java.io.IOException;
import java.util.HashMap;

public class TechnologyTreeController {
    @FXML
    private VBox parent;
    @FXML
    private BorderPane topBar;
    @FXML
    private TextField searchField;
    @FXML
    private Label learnedColorGuide;
    @FXML
    private Label unlockedColorGuide;
    @FXML
    private Label lockedColorGuide;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label currentResearchLabel;

    private Pane techPane;


    private final int padding = 15;
    private final int hspace = 60;
    private final int vspace = 30;

    private HashMap<Technology, TechnologyBlock> techBlocks;

    @FXML
    public void initialize() {
        techPane = new Pane();
        techPane.setStyle("-fx-background-color: #4c4c4c;");
        scrollPane.setContent(techPane);
        scrollPane.setPannable(true);
        scrollPane.setFitToHeight(true);

        addTechnologyBlocks();
        addDependecyLines();
        createBottomBar();
        setCheatTab();
    }

    private void setCheatTab() {
        KeyCombination combination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        Main.getScene().getAccelerators().put(combination, new Runnable() {
            @Override
            public void run() {
                System.out.println("cheat box should be up");
                CheatBox cheatBox = new CheatBox() {
                    @Override
                    public void onSubmitButtonClick() {
                        if (textField.getText().equals("hello")) {
                            textField.setText("meow");
                        }
                    }
                };
                cheatBox.show();
            }
        });
    }

    private void addTechnologyBlocks() {
        techBlocks = new HashMap<>();
        int[] nextFreeRowIndex = new int[Technology.findMaxGrade() + 1];
        for (int i = 0; i < Technology.values().length; i++) {
            Technology technology = Technology.values()[i];
            TechnologyBlock block = new TechnologyBlock();
            TechnologyMap techMap = GameController.getGameController().getCurrentPlayer().getTechnologies();
            block.setTechnology(techMap, technology);
            block.setLayoutX(padding + technology.getGrade() * (hspace + block.getPrefWidth()));
            block.setLayoutY(padding + nextFreeRowIndex[technology.getGrade()] * (vspace + block.getPrefHeight()));
            nextFreeRowIndex[technology.getGrade()]++;
            if (techMap.isTechnologyUnlockedAndNotLearned(technology)) {
                block.setStyle("-fx-cursor: hand");
                block.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getClickCount() > 1) {
                            GameController.getGameController().stopPreviousResearchAndStartNext(GameController.getGameController().getCurrentPlayer(), technology);
                            try {
                                Main.loadFxmlFile("TechnologyTree");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            techPane.getChildren().add(block);
            techBlocks.put(technology, block);
        }
        techPane.setMinWidth(padding * 2 + (Technology.findMaxGrade() + 1) * (hspace + TechnologyBlock.WIDTH) - hspace);

        Rectangle bottomPadding = new Rectangle();
        bottomPadding.setHeight(padding);
        bottomPadding.setLayoutY(padding + Technology.findMostPopulousGradesPopulation() * (vspace + TechnologyBlock.HEIGHT));
        techPane.getChildren().add(bottomPadding);
    }

    private void addDependecyLines() {
        for (Technology technology : Technology.values()) {
            for (Technology dependentTechnology : technology.getDependentTechnologies()) {
                Line line = new Line();
                line.setStartX(techBlocks.get(technology).findCenterRightX());
                line.setStartY(techBlocks.get(technology).findCenterRightY());
                line.setEndX(techBlocks.get(dependentTechnology).findCenterLeftX());
                line.setEndY(techBlocks.get(dependentTechnology).findCenterLeftY());
                techPane.getChildren().add(line);
                if (dependentTechnology.getGrade() - technology.getGrade() == 1) {
                    line.setStroke(Color.BLUEVIOLET);
                } else {
                    line.getStrokeDashArray().addAll(5.0, 5.0);
                    line.setStroke(Color.rgb(0, 0, 255, 0.5));
                }
            }
        }
    }

    private void createBottomBar() {
        learnedColorGuide.setBackground(new Background(new BackgroundFill(TechnologyBlock.LEARNED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        learnedColorGuide.setPrefSize(300, TechnologyBlock.HEIGHT);
        unlockedColorGuide.setBackground(new Background(new BackgroundFill(TechnologyBlock.UNLOCKED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        unlockedColorGuide.setPrefSize(300, TechnologyBlock.HEIGHT);
        lockedColorGuide.setBackground(new Background(new BackgroundFill(TechnologyBlock.LOCKED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        lockedColorGuide.setPrefSize(300, TechnologyBlock.HEIGHT);


        StringBuilder sb = new StringBuilder();
        Technology currentResearch = GameController.getGameController().getCurrentPlayer().getResearchProject();
        sb.append("Currently being researched: ");
        if (currentResearch == null) {
            sb.append("nothing!\n");
        } else {
            sb.append(currentResearch.getName());
            sb.append("\n");
            sb.append("Beakers: ")
                    .append((int) GameController.getGameController().getCurrentPlayer().getBeakerCount())
                    .append(" out of the ").append(currentResearch.getCost())
                    .append(" required have been collected\n");

            int turnsLeft = (int) ((((double) currentResearch.getCost() - GameController.getGameController().getCurrentPlayer().getBeakerCount()))
                    / GameController.getGameController().getCurrentPlayer().calculateTotalBeakers());
            if (GameController.getGameController().getCurrentPlayer().calculateTotalBeakers() > 0) {
                sb.append("Turns left at current pace: ").append(turnsLeft);
            }
        }
        currentResearchLabel.setText(sb.toString());
    }

    @FXML
    private void onSearchButtonPressed() {
        String searchText = searchField.getText();
        Technology technology = Technology.getTechnologyByName(searchText);
        if (technology != null) {
            TechnologyBlock block = techBlocks.get(technology);
            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double x = block.getBoundsInParent().getMaxX();
            scrollPane.setHvalue(x / width);
            block.requestFocus();
        }
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}