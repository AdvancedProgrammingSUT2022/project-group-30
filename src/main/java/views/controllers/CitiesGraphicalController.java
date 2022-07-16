package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import menusEnumerations.CitizenManagementPanelCommands;
import menusEnumerations.CityCommands;
import models.Citizen;
import models.City;
import models.GameMap;
import models.Tile;
import models.interfaces.TileImage;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class CitiesGraphicalController {

    private static GameController controller = GameController.getGameController();

    private static ScrollPane cityActionTabPane;
    private static VBox cityCommandsBox;

    public static void initializeCityActionTab(Pane pane){
        cityActionTabPane = new ScrollPane();
        pane.getChildren().add(cityActionTabPane);
        cityCommandsBox = new VBox();
        cityActionTabPane.setLayoutX(960);
        cityActionTabPane.setLayoutY(300);
        cityActionTabPane.setPrefWidth(300);
        cityActionTabPane.setPrefHeight(400);
        cityActionTabPane.setContent(cityCommandsBox);
        cityCommandsBox.setStyle("-fx-background-color: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        cityActionTabPane.setStyle("-fx-background: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        cityActionTabPane.setFitToHeight(true);
        cityActionTabPane.setFitToWidth(true);
        cityCommandsBox.setAlignment(Pos.CENTER);
        cityActionTabPane.setVisible(false);
        cityCommandsBox.setDisable(true);
        cityCommandsBox.setSpacing(10);
    }

    public static void makeTheCityActionTab(City city, Pane pane){
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        ArrayList<CityCommands> allCommands = CityCommands.getAllCommands();
        for(int i = 0; i < allCommands.size(); i++){
            if(!allCommands.get(i).getName().equals(CityCommands.SHOW_COMMANDS.getName())){
                addButtonForCityCommand(city, pane, allCommands.get(i));
            }
        }
    }

    private static void addButtonForCityCommand(City city, Pane pane, CityCommands command){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        cityCommandsBox.getChildren().add(button);
        if(command.getName().equals(CityCommands.DESELECT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectCity(city);
                }
            });
        }
        else if(command.getName().equals(CityCommands.PURCHASE_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToBuy(city, pane);
                }
            });
        }
        else if(command.getName().equals(CityCommands.SHOW_CITIZEN_MANAGEMENT_PANEL.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeCitizenManagementPanel(city, pane);
                }
            });
        }
    }

    public static void makeCitizenManagementPanel(City city, Pane pane){
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        ArrayList<CitizenManagementPanelCommands> allCommands = CitizenManagementPanelCommands.getAllCommands();
        for(int i = 0; i < allCommands.size(); i++){
            addCitizenManagementButtonForCommand(city, pane, allCommands.get(i));
        }

    }

    public static void makeCitizenManagementShowInfoPanel(City city, Pane pane){
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        Button showWorkedTiles = new Button("Tiles being worked");
        showWorkedTiles.getStyleClass().add("menu-button");
        showWorkedTiles.setPrefWidth(150);
        showWorkedTiles.setPrefHeight(80);
        showWorkedTiles.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showWorkingTiles(city, pane);
            }
        });
        showWorkedTiles.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i = 0; i < pane.getChildren().size(); i++){
                    if(pane.getChildren().get(i) instanceof Polygon){
                        Polygon hexagon = (Polygon) pane.getChildren().get(i);
                        hexagon.setStroke(null);
                    }
                }
            }
        });
        cityCommandsBox.getChildren().add(showWorkedTiles);
        Button showNonWorkedTiles = new Button("Tiles not being worked");
        showNonWorkedTiles.getStyleClass().add("menu-button");
        showNonWorkedTiles.setPrefWidth(150);
        showNonWorkedTiles.setPrefHeight(80);
        showNonWorkedTiles.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showNonWorkingTiles(city, pane);
            }
        });
        showNonWorkedTiles.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i = 0; i < pane.getChildren().size(); i++){
                    if(pane.getChildren().get(i) instanceof Polygon){
                        Polygon hexagon = (Polygon) pane.getChildren().get(i);
                        hexagon.setStroke(null);
                    }
                }
            }
        });
        cityCommandsBox.getChildren().add(showNonWorkedTiles);


    }

    private static void showNonWorkingTiles(City city, Pane pane){
        ArrayList<Citizen> citizens = city.getCitizens();
        ArrayList<Tile> nonWorkingTiles = city.getUnworkedTiles();
        for(int i = 0; i < citizens.size(); i++){
            if(citizens.get(i).getWorkPlace() instanceof Tile){
                nonWorkingTiles.add((Tile) citizens.get(i).getWorkPlace());
            }
        }
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile && nonWorkingTiles.contains((Tile) tileImage)){
                    hexagon.setStroke(Color.RED);
                }
            }
        }
    }

    private static void showWorkingTiles(City city, Pane pane){
        ArrayList<Citizen> citizens = city.getCitizens();
        ArrayList<Tile> workingTiles = new ArrayList<>();
        for(int i = 0; i < citizens.size(); i++){
            if(citizens.get(i).getWorkPlace() instanceof Tile){
                workingTiles.add((Tile) citizens.get(i).getWorkPlace());
            }
        }
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile && workingTiles.contains((Tile) tileImage)){
                    hexagon.setStroke(Color.RED);
                }
            }
        }
    }

    private static void addCitizenManagementButtonForCommand(City city, Pane pane, CitizenManagementPanelCommands command){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        cityCommandsBox.getChildren().add(button);
        if(command.getName().equals(CitizenManagementPanelCommands.BACK.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeTheCityActionTab(city, pane);
                }
            });
        }
        else if(command.getName().equals(CitizenManagementPanelCommands.WORK_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToWork(city, pane);
                }
            });
        }
        else if(command.getName().equals(CitizenManagementPanelCommands.FREE_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToFree(city, pane);
                }
            });

        }
        else if(command.getName().equals(CitizenManagementPanelCommands.SHOW_INFO.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeCitizenManagementShowInfoPanel(city, pane);
                }
            });

        }
    }

    private static void waitForChoosingTileToWork(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        workTile(city, getTileImageFromHexagon(hexagon), pane);
                    }
                });
            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void waitForChoosingTileToFree(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        freeTile(city, getTileImageFromHexagon(hexagon), pane);
                    }
                });
            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void freeTile(City city, TileImage tileImage, Pane pane) {
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only choose visible tiles.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Tile tile = (Tile) tileImage;
        if (!city.getTerritories().contains(tile)) {
            RegisterPageGraphicalController.showPopup("The tile you have entered is not in this city's territory!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (!city.isTileBeingWorked(tile)) {
            RegisterPageGraphicalController.showPopup("This tile is not being worked!");
            makeTheCityActionTab(city, pane);
            return;
        }
        Citizen citizen = city.getCitizenAssignedToTile(tile);
        citizen.setWorkPlace(null);
        RegisterPageGraphicalController.showPopup("Tile at Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap() + " freed! A citizen is out of work!");
        makeTheCityActionTab(city, pane);
    }

    private static void workTile(City city, TileImage tileImage, Pane pane) {
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only choose visible tiles.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Tile tile = (Tile) tileImage;
        if (!city.getTerritories().contains(tile)) {
            RegisterPageGraphicalController.showPopup("The tile you have entered is not in this city's territory!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (tile.calculateDistance(city.getCentralTile()) > 2) {
            RegisterPageGraphicalController.showPopup("This tile is too far away from city center!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (city.isTileBeingWorked(tile)) {
            RegisterPageGraphicalController.showPopup("This tile is already being worked!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (city.calculateWorklessCitizenCount() == 0) {
            RegisterPageGraphicalController.showPopup("There are no workless citizens to assign to this tile! Free a citizen and try again.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Citizen citizen = city.getWorklessCitizen();
        city.assignCitizenToWorkplace(tile, citizen);
        RegisterPageGraphicalController.showPopup("Citizen assigned to tile at Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap());
        makeTheCityActionTab(city, pane);
    }

    public static void waitForChoosingTileToBuy(City city, Pane pane){
        ArrayList<Tile> purchasableTiles = city.findPurchasableTiles();
        //city.getOwner().setGoldCount(1000);
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setDisable(true);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile && purchasableTiles.contains((Tile) tileImage)){
                    hexagon.setStroke(Color.RED);
                    hexagon.setDisable(false);
                    hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            purchaseTile(city, (Tile) tileImage);
                        }
                    });
                }

            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void purchaseTile(City city, Tile tile){
        int cost = city.calculateNextTilePrice();
        if (cost > city.getOwner().getGoldCount()) {
            RegisterPageGraphicalController.showPopup("You don't have enough gold to buy this tile!");
            return;
        }
        controller.getCurrentPlayer().decreaseGold(cost);
        controller.addTileToCityTerritory(city, tile);
        RegisterPageGraphicalController.showPopup("Tile successfully purchased!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deselectCity(City city) {
        city.getOwner().setSelectedEntity(null);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(false);
        cityCommandsBox.setDisable(true);
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TileImage getTileImageFromHexagon(Polygon hexagon){
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getCurrentPlayer());
        double startXCoordinate = hexagon.getPoints().get(0);
        double startYCoordinate = hexagon.getPoints().get(1);
        for(int i = 0; i < tilesToShow.length; i++){
            for(int j = 0; j < tilesToShow[i].length; j++){
                double xCoordinate = 160 + (double) 32 / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if(controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap() % 2 == 1){
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * 32 * (i +isOdd * (double) (j % 2) / (double) 2);
                if(startXCoordinate == xCoordinate &&  startYCoordinate == yCoordinate){
                    return tilesToShow[i][j];
                }
            }
        }
        return null;
    }

}
