package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import models.*;
import models.interfaces.TileImage;
import models.resources.Resource;
import views.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CivilizationGamePageController {

    private GameController controller = GameController.getGameController();

    private double hexagonsSideLength = 32;

    @FXML
    private Pane pane;


    @FXML
    public void initialize() throws MalformedURLException {
        //controller.makeEverythingVisible();
        //printAllTilesInfo();
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
        drawFeatures();
        drawRiverSegments();
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

    public void drawFeatures() throws MalformedURLException {
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getCurrentPlayer());
        for(int i = 0; i < tilesToShow.length; i++){
            for(int j = 0; j < tilesToShow[i].length; j++){
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if(controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap() % 2 == 1){
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i +isOdd * (double) (j % 2) / (double) 2);
                if(tilesToShow[i][j] instanceof Tile){
                    ArrayList<Feature> features = ((Tile) tilesToShow[i][j]).getFeatures();
                    for(int k = 0; k < features.size(); k++){
                        Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
                        hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Features/" + features.get(k).getName() + ".png").toExternalForm()).toExternalForm())));
                        pane.getChildren().add(hexagon);
                    }

                }
            }
        }
    }

    public void drawRiverSegments() throws MalformedURLException {
        ArrayList<RiverSegment> rivers = GameMap.getGameMap().getRivers();
        for(int i = 0; i < rivers.size(); i++){
            Tile firstTile = rivers.get(i).getFirstTile();
            Tile secondTile = rivers.get(i).getSecondTile();
            if(rivers.get(i).findRiverSegmentDirectionForTile(firstTile).equals("RD")){
                drawRiverForTile(firstTile, "RD");
            }
            else if(rivers.get(i).findRiverSegmentDirectionForTile(firstTile).equals("LD")){
                drawRiverForTile(firstTile, "LD");
            }
            else if(rivers.get(i).findRiverSegmentDirectionForTile(secondTile).equals("RD")){
                drawRiverForTile(secondTile, "RD");
            }
            else if(rivers.get(i).findRiverSegmentDirectionForTile(secondTile).equals("LD")){
                drawRiverForTile(secondTile, "LD");
            }
        }
    }

    private void drawRiverForTile(Tile tile, String direction) throws MalformedURLException {
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getCurrentPlayer());
        if(findTileCoordinatesInScene(tile, tilesToShow).isEmpty()){
            return;
        }
        int xPosition = findTileCoordinatesInScene(tile, tilesToShow).get(0);
        int yPosition = findTileCoordinatesInScene(tile, tilesToShow).get(1);
        double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * xPosition);
        int isOdd = 1;
        if(controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap() % 2 == 1){
            isOdd = -1;
        }
        double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (yPosition +isOdd * (double) (xPosition % 2) / (double) 2);
        Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
        if(direction.equals("RD")) {
            hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/River-BottomRight.png").toExternalForm()).toExternalForm())));
        }
        else{
            hexagon.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/TerrainTypes/River-BottomLeft.png").toExternalForm()).toExternalForm())));
        }
        pane.getChildren().add(hexagon);
    }

    //this functions returns x and y coordinates in an ArrayList if the tile is in the scene and visible, returns empty arrayList otherwise
    private ArrayList<Integer> findTileCoordinatesInScene(Tile tile, TileImage[][] tilesToShow){
        ArrayList<Integer> coordinates = new ArrayList<>();
        for(int i = 0; i < tilesToShow.length; i++){
            for(int j = 0; j < tilesToShow[i].length; j++){
                if(tilesToShow[i][j] instanceof Tile){
                    if(((Tile) tilesToShow[i][j]).equals(tile)){
                        coordinates.add(j);
                        coordinates.add(i);
                    }
                }
            }
        }
        return coordinates;
    }


    // this function is only for debugging purposes
    private void printAllTilesInfo(){
        Tile[][] map = GameMap.getGameMap().getMap();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                System.out.println("********************");
                System.out.println("Tile located in i: " + i + " and j: " + j);
                System.out.println();
                System.out.println("FEATURES:");
                ArrayList<Feature> features = map[i][j].getFeatures();
                for(int k = 0; k < features.size(); k++){
                    System.out.println(features.get(k).getName());
                }
                System.out.println();
                System.out.println("RESOURCES:");
                HashMap<Resource, Integer> resources = map[i][j].getResources();
                for (Resource resource : resources.keySet()) {
                    for (int k = 0; k < resources.get(resource); k++)
                        System.out.println(resource.getName());
                }
                System.out.println();
                System.out.println("********************");
            }
        }

    }


}
