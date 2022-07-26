package views.controllers;

import controllers.GameController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TileImage;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.units.Unit;
import models.units.UnitType;
import views.CheatCodes;
import views.Main;
import views.customcomponents.CheatBox;
import views.customcomponents.TechnologyPopup;
import views.images.Images;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class CivilizationGamePageController {
    private final boolean debugMode = false; // for debugging purposes only

    private GameController controller = GameController.getGameController();

    private static double hexagonsSideLength = 32;

    @FXML
    private Pane pane;

    private TechnologyPopup techPopup;
    private Button goToDiplomacyPageButton;
    private Button goToNextTurnButton;
    private Civilization currentPlayer;
    private TileImage[][] tilesToShow;

    @FXML
    public void initialize() throws MalformedURLException {


        //controller.makeEverythingVisible();
        //printAllTilesInfo();
        //showTileValues(currentPlayer.getFrameBase());
        pane.getChildren().clear();
        drawMap();
        setSceneOnKeyPressed();
        createStatusBar();
        if (debugMode || (currentPlayer.getResearchProject() == null && !currentPlayer.getCities().isEmpty())) {
            createTechnologyPopup();
        }
        UnitsGraphicalController.initializeUnitActionTab(this.pane);
        CitiesGraphicalController.initializeCityActionTab(this.pane);
        initializeNextTurnButton();
        initializeDiplomacyButton();
    }

    private void initializeDiplomacyButton() {
        goToDiplomacyPageButton = new Button("Diplomacy");
        goToDiplomacyPageButton.getStyleClass().add("menu-button");
        goToDiplomacyPageButton.setPrefHeight(30);
        goToDiplomacyPageButton.setPrefWidth(150);
        goToDiplomacyPageButton.setLayoutX(1100);
        goToDiplomacyPageButton.setLayoutY(90);
        goToDiplomacyPageButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("DiplomacyPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pane.getChildren().add(goToDiplomacyPageButton);
    }

    private void initializeNextTurnButton() {
        goToNextTurnButton = new Button("Next Turn");
        goToNextTurnButton.getStyleClass().add("menu-button");
        goToNextTurnButton.setPrefHeight(30);
        goToNextTurnButton.setPrefWidth(150);
        goToNextTurnButton.setLayoutX(1100);
        goToNextTurnButton.setLayoutY(50);
        goToNextTurnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                passTurn();
            }
        });
        pane.getChildren().add(goToNextTurnButton);
    }

    private void passTurn() {
//        ArrayList<Unit> idleUnits = controller.getCurrentPlayersUnitsWaitingForCommand();
//        if (idleUnits.isEmpty() == false && !currentPlayer.isTurnBreakDisabled()) {
//            RegisterPageGraphicalController.showPopup("Some units are waiting for a command!");
//            return;
//        }
//        if (!controller.getCivilizationCities(currentPlayer).isEmpty() && currentPlayer.getResearchProject() == null &&
//                !currentPlayer.isTurnBreakDisabled()) {
//            RegisterPageGraphicalController.showPopup("You should start a research project!");
//            return;
//        }
//
//        ArrayList<City> citiesWaitingForProduction = controller.getCivilizationCitiesWaitingForProduction(currentPlayer);
//        if (citiesWaitingForProduction.isEmpty() == false && !currentPlayer.isTurnBreakDisabled()) {
//            RegisterPageGraphicalController.showPopup("Some cities are waiting for their next production!");
//            return;
//        }

        controller.goToNextPlayer();
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Polygon createHexagon(double xCoordinate, double yCoordinate) {
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(xCoordinate, yCoordinate,
                xCoordinate + hexagonsSideLength, yCoordinate,
                xCoordinate + hexagonsSideLength * 1.5, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength,
                xCoordinate + hexagonsSideLength, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate, yCoordinate + Math.sqrt(3) * hexagonsSideLength,
                xCoordinate - hexagonsSideLength / 2, yCoordinate + Math.sqrt(3) / 2 * hexagonsSideLength);
        return hexagon;
    }

    public void removeAllPolygonsFromPane() {
        ArrayList<Polygon> toRemove = new ArrayList<>();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Polygon) {
                toRemove.add((Polygon) pane.getChildren().get(i));
            }
        }
        pane.getChildren().removeAll(toRemove);
    }

    public void drawMap() throws MalformedURLException {
        currentPlayer = controller.getCurrentPlayer();
        tilesToShow = controller.getCivilizationImageToShowOnScene(currentPlayer);
        removeAllPolygonsFromPane();
        removeAllCirclesFromPane();
        putCitiesOnMap();
        putUnitsOnMap();
        drawRiverSegments();
        drawFeatures();
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i + isOdd * (double) (j % 2) / (double) 2);
                Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
                if (tilesToShow[i][j] == null) {
                    hexagon.setFill(Images.getImage("FogOfWar"));
                } else if (tilesToShow[i][j] instanceof Tile) {
                    String terrainTypeName = ((Tile) tilesToShow[i][j]).getTerrainType().getName();
                    hexagon.setFill(Images.getImage(terrainTypeName));
                    setVisibleTilesHexagonsOnMouseClicked(hexagon);
                } else if (tilesToShow[i][j] instanceof TileHistory) {
                    String terrainTypeName = ((TileHistory) tilesToShow[i][j]).getTile().getTerrainType().getName();
                    hexagon.setFill(Images.getImage(terrainTypeName));
                    hexagon.setOpacity(0.5);
                }
                pane.getChildren().add(0, hexagon);
            }
        }


    }

    private void createTechnologyPopup() {
        techPopup = new TechnologyPopup();
        pane.getChildren().add(techPopup);
        techPopup.setLayoutX(20);
        techPopup.setLayoutY(20);
        techPopup.updateInfo(GameController.getGameController().getCurrentPlayer().getTechnologies());
    }


    private void removeTechnologyPopup() {
        pane.getChildren().remove(techPopup);
    }

    public void setSceneOnKeyPressed() {
//        addCheatBox();

//        KeyCombination diplomacyCombo = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
//        Main.getScene().getAccelerators().put(diplomacyCombo, new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Main.loadFxmlFile("DiplomacyPage");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        KeyCombination cheatCombo = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
        Main.getScene().getAccelerators().put(cheatCombo, new Runnable() {
            @Override
            public void run() {
                createTechnologyPopup();
            }
        });

        Main.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
//                int yPosition = controller.getGameDataBase().getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
                int yPosition = controller.findTileYCoordinateInMap(currentPlayer.getFrameBase());
//                int xPosition = controller.getGameDataBase().getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
                int xPosition = controller.findTileXCoordinateInMap(currentPlayer.getFrameBase());
                switch (keyName) {
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
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void goUp(int yPosition, int xPosition) {
        if (yPosition > 0) {
            controller.setCurrentPlayerFrameBase(controller.getTileFromMap(xPosition, yPosition - 1));
        }
    }

    public void goDown(int yPosition, int xPosition) {
        if (yPosition + 9 < controller.getMapHeight() - 1) {
            controller.setCurrentPlayerFrameBase(controller.getTileFromMap(xPosition, yPosition + 1));
        }
    }

    public void goLeft(int yPosition, int xPosition) {
        if (xPosition > 0) {
            controller.setCurrentPlayerFrameBase(controller.getTileFromMap(xPosition - 1, yPosition));
        }
    }

    public void goRight(int yPosition, int xPosition) {
        if (xPosition + 19 < controller.getMapWidth() - 1) {
            controller.setCurrentPlayerFrameBase(controller.getTileFromMap(xPosition + 1, yPosition));
        }
    }

    public void putUnitsOnMap() throws MalformedURLException {
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i + isOdd * (double) (j % 2) / (double) 2);
                if (tilesToShow[i][j] instanceof Tile) {
                    ArrayList<Unit> units = controller.getUnitsInTile((Tile) tilesToShow[i][j]);
//                    System.out.println("Units: " + units);
                    for (int k = 0; k < units.size(); k++) {
                        Circle circle = new Circle();
                        circle.setCenterY(yCoordinate + 10 + 20 * k);
                        circle.setCenterX(xCoordinate + 16);
                        circle.setRadius(10);
                        circle.setFill(Images.getImage(units.get(k).getType().getName()));
                        Unit unit = units.get(k);
                        if (controller.isUnitOwnerEqualToCurrentPlayer(unit)) {
                            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    CivilizationGamePageController.this.controller.setPlayersSelectedEntity(unit);
                                    try {
                                        UnitsGraphicalController.makeTheUnitActionTab(unit, CivilizationGamePageController.this.pane);
                                    } catch (MalformedURLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                        pane.getChildren().add(0, circle);
                    }
                }
            }
        }
    }

    public void putCitiesOnMap() throws MalformedURLException {
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i + isOdd * (double) (j % 2) / (double) 2);
                if (tilesToShow[i][j] instanceof Tile) {
                    City city = controller.getCityCenteredInTile((Tile) tilesToShow[i][j]);
                    if (city != null) {
                        Circle circle = new Circle();
                        circle.setCenterY(yCoordinate + hexagonsSideLength * Math.sqrt(3) / 2);
                        circle.setCenterX(xCoordinate - 5);
                        circle.setRadius(15);
                        circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/City/cityBanner.png").toExternalForm()).toExternalForm())));
                        if (controller.isCityOwnerEqualToCurrentPlayer(city)) {
                            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    CivilizationGamePageController.this.controller.setPlayersSelectedEntity(city);
                                    CitiesGraphicalController.makeTheCityActionTab(city, pane);
                                }
                            });
                        }
                        Text text = new Text(String.valueOf(city.getCitizens().size()));
                        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
                        text.setX(circle.getCenterX());
                        text.setY(circle.getCenterY() - 15);
                        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                pane.getChildren().add(text);
                            }
                        });
                        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                pane.getChildren().remove(text);
                            }
                        });


                        pane.getChildren().add(0, circle);

                    }

                }
            }
        }

    }

    public void removeAllCirclesFromPane() {
        ArrayList<Circle> toRemove = new ArrayList<>();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Circle) {
                toRemove.add((Circle) pane.getChildren().get(i));
            }
        }
        pane.getChildren().removeAll(toRemove);
    }

    public void drawFeatures() {
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i + isOdd * (double) (j % 2) / (double) 2);
                if (tilesToShow[i][j] instanceof Tile) {
                    ArrayList<Feature> features = ((Tile) tilesToShow[i][j]).getFeatures();
                    for (int k = 0; k < features.size(); k++) {
                        Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
                        hexagon.setFill(Images.getImage(features.get(k).getName()));
                        setVisibleTilesHexagonsOnMouseClicked(hexagon);
                        pane.getChildren().add(0, hexagon);
                    }
                }
            }
        }
    }

    public void drawRiverSegments() throws MalformedURLException {
        ArrayList<RiverSegment> rivers = controller.getMapRivers();
        for (int i = 0; i < rivers.size(); i++) {
            Tile firstTile = rivers.get(i).getFirstTile();
            Tile secondTile = rivers.get(i).getSecondTile();
            if (controller.findRiverSegmentDirectionForTile(rivers.get(i), firstTile).equals("RD")) {
                drawRiverForTile(firstTile, "RD");
            } else if (controller.findRiverSegmentDirectionForTile(rivers.get(i), firstTile).equals("LD")) {
                drawRiverForTile(firstTile, "LD");
            } else if (controller.findRiverSegmentDirectionForTile(rivers.get(i), secondTile).equals("RD")) {
                drawRiverForTile(secondTile, "RD");
            } else if (controller.findRiverSegmentDirectionForTile(rivers.get(i), secondTile).equals("LD")) {
                drawRiverForTile(secondTile, "LD");
            }
        }
    }

    private void drawRiverForTile(Tile tile, String direction) {
        if (findTileCoordinatesInScene(tile, tilesToShow).isEmpty()) {
            return;
        }
        int xPosition = findTileCoordinatesInScene(tile, tilesToShow).get(0);
        int yPosition = findTileCoordinatesInScene(tile, tilesToShow).get(1);
        double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * xPosition);
        int isOdd = 1;
        if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
            isOdd = -1;
        }
        double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (yPosition + isOdd * (double) (xPosition % 2) / (double) 2);
        Polygon hexagon = createHexagon(xCoordinate, yCoordinate);
        if (direction.equals("RD")) {
            hexagon.setFill(Images.getImage("River-BottomRight"));
        } else {
            hexagon.setFill(Images.getImage("River-BottomLeft"));
        }
        setVisibleTilesHexagonsOnMouseClicked(hexagon);
        pane.getChildren().add(0, hexagon);
    }

    //this functions returns x and y coordinates in an ArrayList if the tile is in the scene and visible, returns empty arrayList otherwise
    private ArrayList<Integer> findTileCoordinatesInScene(Tile tile, TileImage[][] tilesToShow) {
        ArrayList<Integer> coordinates = new ArrayList<>();
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                if (tilesToShow[i][j] instanceof Tile) {
                    if (controller.isTileImageEqualToTile(tile, tilesToShow[i][j])) {
                        coordinates.add(j);
                        coordinates.add(i);
                    }
                }
            }
        }
        return coordinates;
    }


    // this function is only for debugging purposes
    private void printAllTilesInfo() {
        Tile[][] map = GameMap.getGameMap().getMap();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.println("********************");
                System.out.println("Tile located in i: " + i + " and j: " + j);
                System.out.println();
                System.out.println("FEATURES:");
                ArrayList<Feature> features = map[i][j].getFeatures();
                for (int k = 0; k < features.size(); k++) {
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

    public void showTileValues(Tile tile) throws MalformedURLException {
        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        BorderPane pane = new BorderPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        pane.getStylesheets().addAll(this.pane.getStylesheets());
        pane.getStyleClass().add("shadow-pane");
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        pane.setCenter(vbox);

        Text text = new Text();
        text.setText("Tile coordinates: i = " + controller.findTileYCoordinateInMap(tile) + " , j = " + controller.findTileXCoordinateInMap(tile));
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(text);

        Circle terrainTypeCircle = new Circle();
        terrainTypeCircle.setRadius(20);
        terrainTypeCircle.setFill(Images.getImage(tile.getTerrainType().getName()));
        Text terrainType = new Text();
        terrainType.setText("Terrain Type : " + tile.getTerrainType().getName());
        terrainType.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(terrainTypeCircle);
        vbox.getChildren().add(terrainType);

        TableView<Output> outputTableView = getOutputTableForTile(tile);
        vbox.getChildren().add(outputTableView);

        TableView<Feature> featuresTable = getFeaturesTable(tile.getFeatures());
        vbox.getChildren().add(featuresTable);

        TableView<Resource> resourcesTable = getResourcesTable(tile, tile.getResourcesAsArrayList());
        vbox.getChildren().add(resourcesTable);

        TableView<Improvement> improvementTableView = getImprovementTable(tile, tile.getImprovements());
        vbox.getChildren().add(improvementTableView);


        Button button = new Button();
        button.setText("Ok");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.hide();
            }
        });
        vbox.getChildren().add(button);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();
    }

    private TableView<Feature> getFeaturesTable(ArrayList<Feature> features) {
        TableView<Feature> tableView = new TableView<>();
        TableColumn<Feature, String> imageColumn = new TableColumn<>();
        imageColumn.setText("Feature");
        TableColumn<Feature, String> nameColumn = new TableColumn<>();
        nameColumn.setText("Name");
        TableColumn<Feature, Output> goldColumn = new TableColumn<>();
        goldColumn.setText("Gold");
        TableColumn<Feature, Output> foodColumn = new TableColumn<>();
        foodColumn.setText("Food");
        TableColumn<Feature, Output> productionColumn = new TableColumn<>();
        productionColumn.setText("Production");
        tableView.getColumns().add(imageColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(goldColumn);
        tableView.getColumns().add(foodColumn);
        tableView.getColumns().add(productionColumn);
        tableView.setFixedCellSize(40);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        imageColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        productionColumn.setCellValueFactory(new PropertyValueFactory<>("output"));

        imageColumn.setCellFactory(col -> {
            TableCell<Feature, String> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    circle.setFill(Images.getImage(newValue));
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(circle));
                }
            });
            return cell;
        });

        goldColumn.setCellFactory(col -> {
            TableCell<Feature, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getGold());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        foodColumn.setCellFactory(col -> {
            TableCell<Feature, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getFood());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        productionColumn.setCellFactory(col -> {
            TableCell<Feature, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getProduction());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });


        ObservableList<Feature> list = FXCollections.observableArrayList(features);
        tableView.setItems(list);

        return tableView;
    }

    private TableView<Output> getOutputTableForTile(Tile tile) {
        ArrayList<Output> outputs = new ArrayList<>();
        outputs.add(controller.calculateTheoreticalOutputForTile(tile));
        TableView<Output> tableView = new TableView<>();
        TableColumn<Output, Integer> goldColumn = new TableColumn<>();
        goldColumn.setText("Gold");
        TableColumn<Output, Integer> foodColumn = new TableColumn<>();
        foodColumn.setText("Food");
        TableColumn<Output, Integer> productionColumn = new TableColumn<>();
        productionColumn.setText("Production");
        tableView.getColumns().add(goldColumn);
        tableView.getColumns().add(foodColumn);
        tableView.getColumns().add(productionColumn);
        tableView.setFixedCellSize(40);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("gold"));
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        productionColumn.setCellValueFactory(new PropertyValueFactory<>("production"));
        setTableColumnData(goldColumn, "Gold");
        setTableColumnData(foodColumn, "Food");
        setTableColumnData(productionColumn, "Production");
        ObservableList<Output> list = FXCollections.observableArrayList(outputs);
        tableView.setItems(list);
        return tableView;
    }


    private void setTableColumnData(TableColumn<Output, Integer> tableColumn, String name) {
        tableColumn.setCellFactory(col -> {
            TableCell<Output, Integer> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    try {
                        circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/" + name + ".png").toExternalForm()).toExternalForm())));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    Text text = new Text("" + newValue);
                    HBox hBox = new HBox();
                    hBox.setSpacing(10);
                    hBox.setAlignment(Pos.CENTER);
                    hBox.getChildren().add(circle);
                    hBox.getChildren().add(text);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(hBox));
                }
            });
            return cell;
        });
    }

    private TableView<Resource> getResourcesTable(Tile tile, ArrayList<Resource> resources) {
        TableView<Resource> tableView = new TableView<>();
        TableColumn<Resource, String> imageColumn = new TableColumn<>();
        imageColumn.setText("Resource");
        TableColumn<Resource, String> nameColumn = new TableColumn<>();
        nameColumn.setText("Name");
        TableColumn<Resource, Output> goldColumn = new TableColumn<>();
        goldColumn.setText("Gold");
        TableColumn<Resource, Output> foodColumn = new TableColumn<>();
        foodColumn.setText("Food");
        TableColumn<Resource, Output> productionColumn = new TableColumn<>();
        productionColumn.setText("Production");
        tableView.getColumns().add(imageColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(goldColumn);
        tableView.getColumns().add(foodColumn);
        tableView.getColumns().add(productionColumn);
        tableView.setFixedCellSize(40);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        imageColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        productionColumn.setCellValueFactory(new PropertyValueFactory<>("output"));

        imageColumn.setCellFactory(col -> {
            TableCell<Resource, String> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    circle.setFill(Images.getImage(newValue));
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(circle));
                }
            });
            return cell;
        });

        goldColumn.setCellFactory(col -> {
            TableCell<Resource, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getGold());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        foodColumn.setCellFactory(col -> {
            TableCell<Resource, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getFood());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        productionColumn.setCellFactory(col -> {
            TableCell<Resource, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getProduction());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });


        ObservableList<Resource> list = FXCollections.observableArrayList(resources);
        tableView.setItems(list);

        PseudoClass higlighted = PseudoClass.getPseudoClass("highlighted");

        tableView.setRowFactory(view -> {
            TableRow<Resource> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldOrder, newOrder) ->
                    row.pseudoClassStateChanged(higlighted, newOrder != null && controller.canResourceBeExploitedForTile(newOrder, tile)));
            return row;
        });

        return tableView;
    }

    private TableView<Improvement> getImprovementTable(Tile tile, ArrayList<Improvement> improvements) {
        TableView<Improvement> tableView = new TableView<>();
        TableColumn<Improvement, String> imageColumn = new TableColumn<>();
        imageColumn.setText("Improvement");
        TableColumn<Improvement, String> nameColumn = new TableColumn<>();
        nameColumn.setText("Name");
        TableColumn<Improvement, Output> goldColumn = new TableColumn<>();
        goldColumn.setText("Gold");
        TableColumn<Improvement, Output> foodColumn = new TableColumn<>();
        foodColumn.setText("Food");
        TableColumn<Improvement, Output> productionColumn = new TableColumn<>();
        productionColumn.setText("Production");
        tableView.getColumns().add(imageColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(goldColumn);
        tableView.getColumns().add(foodColumn);
        tableView.getColumns().add(productionColumn);
        tableView.setFixedCellSize(40);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        imageColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        productionColumn.setCellValueFactory(new PropertyValueFactory<>("output"));

        imageColumn.setCellFactory(col -> {
            TableCell<Improvement, String> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    circle.setFill(Images.getImage(newValue));
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(circle));
                }
            });
            return cell;
        });

        goldColumn.setCellFactory(col -> {
            TableCell<Improvement, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getGold());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        foodColumn.setCellFactory(col -> {
            TableCell<Improvement, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getFood());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });

        productionColumn.setCellFactory(col -> {
            TableCell<Improvement, Output> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Text text = new Text("" + newValue.getProduction());
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(text));
                }
            });
            return cell;
        });


        ObservableList<Improvement> list = FXCollections.observableArrayList(improvements);
        tableView.setItems(list);

        return tableView;
    }

    private TileImage getTileImageFromHexagon(Polygon hexagon) {
        double startXCoordinate = hexagon.getPoints().get(0);
        double startYCoordinate = hexagon.getPoints().get(1);
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) hexagonsSideLength / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * hexagonsSideLength * (i + isOdd * (double) (j % 2) / (double) 2);
                if (startXCoordinate == xCoordinate && startYCoordinate == yCoordinate) {
                    return tilesToShow[i][j];
                }
            }
        }
        return null;
    }

    private void setVisibleTilesHexagonsOnMouseClicked(Polygon hexagon) {
        hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    showTileValues((Tile) getTileImageFromHexagon(hexagon));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hexagon.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hexagon.setOpacity(0.5);
            }
        });
        hexagon.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hexagon.setOpacity(1);
            }
        });
    }

    public void createStatusBar() throws MalformedURLException {
        HBox hBox = new HBox();
        hBox.setLayoutX(0);
        hBox.setLayoutY(0);
        hBox.setPrefWidth(1280);
        hBox.setPrefHeight(20);
        hBox.setStyle("-fx-background-color: #02cefc");

        HBox data = new HBox();
        HBox buttons = new HBox();

        Circle science = new Circle();
        science.setRadius(10);
        science.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/Science.png").toExternalForm()).toExternalForm())));
        Text scienceText = new Text("" + currentPlayer.getBeakerCount());

        Circle gold = new Circle();
        gold.setRadius(10);
        gold.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/Gold.png").toExternalForm()).toExternalForm())));
        Text goldText = new Text("" + currentPlayer.getGoldCount());

        Circle happiness = new Circle();
        happiness.setRadius(10);
        happiness.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/Happiness.png").toExternalForm()).toExternalForm())));
        Text happinessText = new Text("" + currentPlayer.getHappiness());

        data.setSpacing(20);
        data.getChildren().add(science);
        data.getChildren().add(scienceText);
        data.getChildren().add(gold);
        data.getChildren().add(goldText);
        data.getChildren().add(happiness);
        data.getChildren().add(happinessText);

        Rectangle pause = new Rectangle();
        pause.setHeight(20);
        pause.setWidth(20);
        pause.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Icons/Pause.png").toExternalForm()).toExternalForm())));

        Rectangle settings = new Rectangle();
        settings.setHeight(20);
        settings.setWidth(20);
        settings.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Icons/Settings.png").toExternalForm()).toExternalForm())));

        buttons.getChildren().add(pause);
        buttons.getChildren().add(settings);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setSpacing(20);

        HBox.setHgrow(buttons, Priority.ALWAYS);

        hBox.getChildren().add(data);
        hBox.getChildren().add(buttons);
        hBox.setPadding(new Insets(2));

        pane.getChildren().add(hBox);
    }

    private void addCheatBox() {
        KeyCombination cheatCombo = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        Main.getScene().getAccelerators().put(cheatCombo, new Runnable() {
            @Override
            public void run() {
                CheatBox cheatBox = new CheatBox() {
                    @Override
                    public void onSubmitButtonClick() {
                        String text = textField.getText();
                        Matcher matcher;
                        if ((matcher = CheatCodes.MAKE_EVERYTHING_VISIBLE.getMatcher(text)) != null) {
                            controller.makeEverythingVisible();
                        } else if ((matcher = CheatCodes.ADD_GOLD.getMatcher(text)) != null) {
                            currentPlayer.addGold(200);
                        } else if ((matcher = CheatCodes.DISABLE_TURN_BREAK.getMatcher(text)) != null) {
                            controller.disableTurnBreak();
                        } else if ((matcher = CheatCodes.ADD_STRATEGIC_RESOURCE.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            StrategicResource chosenResource = StrategicResource.getStrategicResourceByName(name);
                            if (chosenResource == null) {
                                return;
                            }
                            currentPlayer.addStrategicResource(chosenResource, 5);
                        } else if ((matcher = CheatCodes.ADD_LUXURY_RESOURCE.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            LuxuryResource chosenResource = LuxuryResource.getLuxuryResourceByName(name);
                            if (chosenResource == null) {
                                return;
                            }
                            currentPlayer.addLuxuryResource(chosenResource, 5);
                        } else if ((matcher = CheatCodes.ADD_UNIT.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            int y = Integer.parseInt(matcher.group("y"));
                            int x = Integer.parseInt(matcher.group("x"));
                            UnitType selectedType = UnitType.getUnitTypeByName(name);
                            if (selectedType == null) {
                                return;
                            }
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            if (!controller.canUnitTeleportToTile(selectedType, currentPlayer, tile)) {
                                return;
                            }
                            controller.createUnit(selectedType, currentPlayer, tile);
                        } else if ((matcher = CheatCodes.KILL_UNIT.getMatcher(text)) != null) {
                            int y = Integer.parseInt(matcher.group("y"));
                            int x = Integer.parseInt(matcher.group("x"));
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            ArrayList<Unit> units = controller.getUnitsInTile(tile);
                            if (units.size() == 0) {
                                return;
                            }
                            for (Unit unit : units) {
                                controller.removeUnit(unit);
                            }
                        } else if ((matcher = CheatCodes.MAKE_IMPROVEMENT.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            int x = Integer.parseInt(matcher.group("x"));
                            int y = Integer.parseInt(matcher.group("y"));
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            ImprovementType chosenType = ImprovementType.getImprovementTypeByName(name);
                            if (chosenType == null) {
                                return;
                            }
                            if (tile.containsImprovment(chosenType)) {
                                return;
                            }
                            if (chosenType != ImprovementType.ROAD && chosenType != ImprovementType.RAILROAD &&
                                    (tile.getCityOfTile() == null || tile.getCityOfTile().getOwner() != currentPlayer)) {
                                return;
                            }
                            controller.addImprovementToTile(tile, chosenType);
                        } else if ((matcher = CheatCodes.DEPLOY_FEATURE.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            int x = Integer.parseInt(matcher.group("x"));
                            int y = Integer.parseInt(matcher.group("y"));
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            Feature chosenFeature = Feature.getFeatureByName(name);
                            if (chosenFeature == null) {
                                return;
                            }
                            tile.addFeatureAndApplyChanges(chosenFeature);
                        } else if ((matcher = CheatCodes.CLEAR_ALL_FEATURES.getMatcher(text)) != null) {
                            int x = Integer.parseInt(matcher.group("x"));
                            int y = Integer.parseInt(matcher.group("y"));
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            tile.removeAllFeaturesAndApplyChanges();
                        }
                        try {
                            Main.loadFxmlFile("CivilizationGamePage");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.close();
                    }
                };
                cheatBox.show();
            }
        });
    }
}
