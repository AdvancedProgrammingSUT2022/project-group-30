package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import models.GameMap;
import models.Tile;
import models.TileHistory;
import models.interfaces.TileImage;
import views.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CivilizationGamePageController {

    private GameController controller = GameController.getGameController();

    private double hexagonsSideLength = 32;

    @FXML
    private Pane pane;


    @FXML
    public void initialize() throws MalformedURLException {
        drawMap();
        setSceneOnKeyPressed();
    }

    private Polygon createHexagon(double xCoordinate, double yCoordinate){
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(xCoordinate, yCoordinate,
                xCoordinate + hexagonsSideLength, yCoordinate,
                xCoordinate + hexagonsSideLength * 1.5, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength,
                xCoordinate + hexagonsSideLength, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate - hexagonsSideLength / 2, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength);
        return hexagon;
    }

    public void removeAllPolygonsFromPane(){
        ArrayList<Polygon> toRemove = new ArrayList<>();
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                toRemove.add((Polygon) pane.getChildren().get(i));
            }
        }
        pane.getChildren().removeAll(toRemove);
    }

    public void drawMap() throws MalformedURLException {
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getCurrentPlayer());
        removeAllPolygonsFromPane();
        for(int i = 0; i < tilesToShow.length; i++){
            for(int j = 0; j < tilesToShow[i].length; j++){
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if(controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap() % 2 == 1){
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i +isOdd * (double) (j % 2) / (double) 2);
                Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
                if(tilesToShow[i][j] == null){
                    hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/FogOfWar.png").toExternalForm()).toExternalForm())));
                }
                else if(tilesToShow[i][j] instanceof Tile){
                    String terrainTypeName = ((Tile) tilesToShow[i][j]).getTerrainType().getName();
                    hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/" + terrainTypeName + ".png").toExternalForm()).toExternalForm())));
                }
                else if(tilesToShow[i][j] instanceof TileHistory){
                    String terrainTypeName = ((TileHistory) tilesToShow[i][j]).getTile().getTerrainType().getName();
                    hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/" + terrainTypeName + ".png").toExternalForm()).toExternalForm())));
                    hexagon.setOpacity(0.5);
                }
                pane.getChildren().add(hexagon);
            }
        }
    }

    public void setSceneOnKeyPressed(){
        Main.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
                int yPosition = controller.getGameDataBase().getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
                int xPosition = controller.getGameDataBase().getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
                switch (keyName){
                    case "Up":
                        goUp(yPosition, xPosition);
                        break;
                    case "Down":
                        goDown(yPosition, xPosition);
                        break;
                    case "Right":
                        goRight(yPosition, xPosition);
                        break;
                    case "Left":
                        goLeft(yPosition, xPosition);
                        break;
                    default:
                        break;
                }
                try {
                    drawMap();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void goUp(int yPosition, int xPosition){
        if(yPosition > 0){
            controller.getCurrentPlayer().setFrameBase(GameMap.getGameMap().getTile(xPosition, yPosition - 1));
        }
    }

    public void goDown(int yPosition, int xPosition){
        if(yPosition + 9 < GameMap.getGameMap().getMap().length - 1){
            controller.getCurrentPlayer().setFrameBase(GameMap.getGameMap().getTile(xPosition, yPosition + 1));
        }
    }

    public void goLeft(int yPosition, int xPosition){
        if(xPosition > 0){
            controller.getCurrentPlayer().setFrameBase(GameMap.getGameMap().getTile(xPosition - 1, yPosition));
        }
    }

    public void goRight(int yPosition, int xPosition){
        if(xPosition + 19 < GameMap.getGameMap().getMap()[0].length - 1){
            controller.getCurrentPlayer().setFrameBase(GameMap.getGameMap().getTile(xPosition + 1, yPosition));
        }
    }


}
