package views.controllers;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import models.GameMap;
import models.interfaces.TileImage;
import views.Main;

import java.net.MalformedURLException;
import java.net.URL;

public class CivilizationGamePageController {

    private GameController controller = GameController.getGameController();

    private int hexagonsSideLength = 32;

    @FXML
    private Pane pane;


    @FXML
    public void initialize() throws MalformedURLException {
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getGameDataBase().getCurrentPlayer());
        pane.getChildren().add(createHexagon(100,200));
    }

    private Polygon createHexagon(double xCoordinate, double yCoordinate) throws MalformedURLException {
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(xCoordinate, yCoordinate,
                xCoordinate + hexagonsSideLength, yCoordinate,
                xCoordinate + hexagonsSideLength * 1.5, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength,
                xCoordinate + hexagonsSideLength, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate - hexagonsSideLength / 2, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength);
        hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/FogOfWar.png").toExternalForm()).toExternalForm())));

        return hexagon;
    }
}
