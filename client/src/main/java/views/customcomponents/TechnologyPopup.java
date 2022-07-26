package views.customcomponents;

import controllers.GameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.GameDataBase;
import models.technology.Technology;
import models.technology.TechnologyMap;
import views.Main;

import java.io.IOException;

public class TechnologyPopup extends VBox {
    private Label lastResearchedBox;
    private Button openTechnologyTreeButton;
    private VBox researchOptions;

    public TechnologyPopup() {
        setAlignment(Pos.CENTER);
        setSpacing(3);
        setPadding(new Insets(5));
        setStyle("-fx-background-color: rgba(0,0,255,0.5)");

        lastResearchedBox = new Label();
        lastResearchedBox.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 24; -fx-text-fill: white");
        openTechnologyTreeButton = new Button();
        openTechnologyTreeButton.setStyle("-fx-background-color: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 20;");
        openTechnologyTreeButton.setText("Open Technology Tree");
        openTechnologyTreeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Main.loadFxmlFile("TechnologyTree");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        researchOptions = new VBox();

        getChildren().addAll(lastResearchedBox, openTechnologyTreeButton, researchOptions);
    }

    public void updateInfo(TechnologyMap map) {
        lastResearchedBox.setText(map.getLastUnlocked().getName());
        researchOptions.getChildren().clear();
        for (Technology unlockedTechnology : map.getUnlockedTechnologies()) {
            addUnlockedTechnologyLabel(unlockedTechnology);
        }
    }

    private void addUnlockedTechnologyLabel(Technology tech) {
        Label techLabel = new Label();
        techLabel.setText(tech.getName());
        techLabel.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 15; -fx-cursor: hand; -fx-alignment: CENTER");
        techLabel.setMaxWidth(Double.MAX_VALUE);
        Tooltip tooltip = new Tooltip();
        StringBuilder tipBuilder = new StringBuilder("Unlocks:\n");
        for (Technology dependentTechnology : tech.getDependentTechnologies()) {
            tipBuilder.append(dependentTechnology.getName()).append("\n");
        }
        tooltip.setText(tipBuilder.toString());
        techLabel.setTooltip(tooltip);
        techLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() > 1) {
                    GameController.getGameController().stopPreviousResearchAndStartNext(GameController.getGameController().getCurrentPlayer(), tech);
                    TechnologyPopup.this.setVisible(false);
                }
            }
        });
        researchOptions.getChildren().add(techLabel);
    }
}