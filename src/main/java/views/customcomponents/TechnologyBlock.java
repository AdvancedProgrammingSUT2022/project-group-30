package views.customcomponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.buildings.BuildingType;
import models.technology.Technology;
import models.technology.TechnologyMap;
import models.units.UnitType;

public class TechnologyBlock extends HBox {
    public static Color LEARNED_COLOR = Color.GREEN;
    public static Color UNLOCKED_COLOR = Color.BLUE;
    public static Color LOCKED_COLOR = Color.BLACK;

    public static int WIDTH = 200;
    public static int HEIGHT = 50;

    private Tooltip tooltip;
    private Label techName;

    public TechnologyBlock() {
        setPadding(new Insets(15));
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setAlignment(Pos.CENTER);

        techName = new Label();
        techName.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20; -fx-text-fill: white");

        tooltip = new Tooltip();
        Tooltip.install(this, tooltip);

        getChildren().addAll(techName);
    }

    public void changeColor(Color nextColor) {
        setBackground(new Background(new BackgroundFill(nextColor, new CornerRadii(0), new Insets(0))));
    }

    public void setTechnology(Technology technology) {
        techName.setText(technology.getName());
    }

    public void setTechnology(TechnologyMap techMap, Technology technology) {
        techName.setText(technology.getName());
        if (techMap.isTechnologyLearned(technology)) {
            changeColor(LEARNED_COLOR);
        } else if (techMap.isTechnologyUnlockedAndNotLearned(technology)) {
            changeColor(UNLOCKED_COLOR);
        } else {
            changeColor(LOCKED_COLOR);
        }

        StringBuilder tipTextBuilder = new StringBuilder();
        tipTextBuilder.append("Unlocks Technologies:\n");
        for (Technology dependentTechnology : technology.getDependentTechnologies()) {
            tipTextBuilder.append(dependentTechnology.getName() + "\n");
        }
        tipTextBuilder.append("Unlocks Units:\n");
        for (UnitType value : UnitType.values()) {
            if (value.getPrerequisitTechnology() == technology) {
                tipTextBuilder.append(value.getName() + "\n");
            }
        }
        tipTextBuilder.append("Unlocks Buildings:\n");
        for (BuildingType value : BuildingType.values()) {
            if (value.getPrerequisiteTechnology() == technology) {
                tipTextBuilder.append(value.getName() + "\n");
            }
        }
        tooltip.setText(tipTextBuilder.toString());
    }

    public int findCenterRightX() {
        return ((int) getLayoutX() + WIDTH);
    }

    public int findCenterRightY() {
        return ((int) getLayoutY() + HEIGHT / 2);
    }

    public int findCenterLeftX() {
        return ((int) getLayoutX());
    }

    public int findCenterLeftY() {
        return ((int) getLayoutY() + HEIGHT / 2);
    }
}
