package views.customcomponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.technology.Technology;
import models.technology.TechnologyMap;

public class TechnologyBlock extends HBox {
    public static Color LEARNED_COLOR = Color.GREEN;
    public static Color UNLOCKED_COLOR = Color.BLUE;
    public static Color LOCKED_COLOR = Color.BLACK;

    public static int WIDTH = 200;
    public static int HEIGHT = 50;

    Label techName;

    public TechnologyBlock() {
        setPadding(new Insets(15));
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setAlignment(Pos.CENTER);

        techName = new Label();
        techName.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20; -fx-text-fill: white");

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
        } else if (techMap.isTechnologyUnlocked(technology)) {
            changeColor(UNLOCKED_COLOR);
        } else {
            changeColor(LOCKED_COLOR);
        }
    }
}
