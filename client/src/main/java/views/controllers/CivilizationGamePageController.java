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
import menusEnumerations.DemographicPanelCommands;
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
import java.util.Map;
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

    private Button notificationHistoryButton;
    private Button economicOverviewButton;
    private Button militaryOverviewButton;
    private Button demographicPanelButton;
    private Civilization currentPlayer;
    private TileImage[][] tilesToShow;

    @FXML
    public void initialize() throws MalformedURLException {


        controller.makeEverythingVisible();
        //printAllTilesInfo();
        //showTileValues(currentPlayer.getFrameBase());
        pane.getChildren().clear();
        drawMap();
        setSceneOnKeyPressed();
        createStatusBar();
        System.out.println(currentPlayer.getCities());
        if (debugMode || (currentPlayer.getResearchProject() == null && !controller.getCivilizationCities(currentPlayer).isEmpty())) {
            createTechnologyPopup();
        }
        else{
            System.out.println("kire khar");
        }
        UnitsGraphicalController.initializeUnitActionTab(this.pane);
        CitiesGraphicalController.initializeCityActionTab(this.pane);
        initializeNextTurnButton();
        initializeDiplomacyButton();
        initializeInfoButtons();
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
//        if (idleUnits.isEmpty() == false && !controller.getCurrentPlayer().isTurnBreakDisabled()) {
//            RegisterPageGraphicalController.showPopup("Some units are waiting for a command!");
//            return;
//        }
//        if (!controller.getCivilizationCities(currentPlayer).isEmpty() && controller.getCurrentPlayer().getResearchProject() == null &&
//                !controller.getCurrentPlayer().isTurnBreakDisabled()) {
//            RegisterPageGraphicalController.showPopup("You should start a research project!");
//            return;
//        }
//        ArrayList<City> citiesWaitingForProduction = controller.getCivilizationCitiesWaitingForProduction(currentPlayer);
//        if (citiesWaitingForProduction.isEmpty() == false && !controller.getCurrentPlayer().isTurnBreakDisabled()) {
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
        addCheatBox();

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
        settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

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
                            controller.addGoldToCiv(controller.getCurrentPlayer(), 200);
                        } else if ((matcher = CheatCodes.DISABLE_TURN_BREAK.getMatcher(text)) != null) {
                            controller.disableTurnBreak();
                        } else if ((matcher = CheatCodes.ADD_STRATEGIC_RESOURCE.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            StrategicResource chosenResource = StrategicResource.getStrategicResourceByName(name);
                            if (chosenResource == null) {
                                return;
                            }
                            controller.addStrategicResourceToCiv(controller.getCurrentPlayer(), chosenResource, 5);
                        } else if ((matcher = CheatCodes.ADD_LUXURY_RESOURCE.getMatcher(text)) != null) {
                            String name = matcher.group("name");
                            LuxuryResource chosenResource = LuxuryResource.getLuxuryResourceByName(name);
                            if (chosenResource == null) {
                                return;
                            }
                            controller.addLuxuryResourceToCiv(controller.getCurrentPlayer(), chosenResource, 5);
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
                                    (controller.getCityOfTile(tile) == null || !controller.getTileCityOwner(tile).equals(controller.getCurrentPlayer()))) {
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
                            controller.addFeatureAndApplyChangesForTile(tile, chosenFeature);
                        } else if ((matcher = CheatCodes.CLEAR_ALL_FEATURES.getMatcher(text)) != null) {
                            int x = Integer.parseInt(matcher.group("x"));
                            int y = Integer.parseInt(matcher.group("y"));
                            if (!controller.areCoordinatesValid(x, y)) {
                                return;
                            }
                            Tile tile = controller.getTileByCoordinates(x, y);
                            controller.removeAllFeaturesAndApplyChangesForTile(tile);
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

    private void initializeInfoButtons(){
        notificationHistoryButton = new Button("Notification History");
        notificationHistoryButton.getStyleClass().add("menu-button");
        notificationHistoryButton.setPrefHeight(30);
        notificationHistoryButton.setPrefWidth(100);
        notificationHistoryButton.setLayoutX(350);
        notificationHistoryButton.setLayoutY(670);
        pane.getChildren().add(notificationHistoryButton);
        notificationHistoryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showAllNotification();
            }
        });

        economicOverviewButton = new Button("Economic Overview");
        economicOverviewButton.getStyleClass().add("menu-button");
        economicOverviewButton.setPrefHeight(30);
        economicOverviewButton.setPrefWidth(100);
        economicOverviewButton.setLayoutX(475);
        economicOverviewButton.setLayoutY(670);
        pane.getChildren().add(economicOverviewButton);
        economicOverviewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                economicOverviewPanel();
            }
        });

        militaryOverviewButton = new Button("Military Overview");
        militaryOverviewButton.getStyleClass().add("menu-button");
        militaryOverviewButton.setPrefHeight(30);
        militaryOverviewButton.setPrefWidth(100);
        militaryOverviewButton.setLayoutX(600);
        militaryOverviewButton.setLayoutY(670);
        pane.getChildren().add(militaryOverviewButton);
        militaryOverviewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                militaryOverviewPanel();
            }
        });

        demographicPanelButton = new Button("Demographic Panel");
        demographicPanelButton.getStyleClass().add("menu-button");
        demographicPanelButton.setPrefHeight(30);
        demographicPanelButton.setPrefWidth(100);
        demographicPanelButton.setLayoutX(725);
        demographicPanelButton.setLayoutY(670);
        pane.getChildren().add(demographicPanelButton);

        demographicPanelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                demographicPanel();
            }
        });



    }

    private void militaryOverviewPanel() {
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

        ArrayList<Unit> militaryUnits = controller.getCivilizationMilitaryUnits(currentPlayer);
        if (militaryUnits.isEmpty()) {
            addTextToVBox(vbox, "You don't have any military units to show!");
        } else {
            addTextToVBox(vbox, "Your military units : ");
            for (int i = 0; i < militaryUnits.size(); i++) {
                addTextToVBox(vbox, " " + (i + 1) + "- " + militaryUnits.get(i).getType().getName() + " in Y: " + controller.findTileYCoordinateInMap(militaryUnits.get(i).getLocation()) + " , X: " + controller.findTileYCoordinateInMap(militaryUnits.get(i).getLocation()));
            }
        }

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

    private void showAllNotification() {
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

        ArrayList<Notification> notifications = controller.getAllNotificationsForCivilization(currentPlayer);
        addTextToVBox(vbox, "Your all notifications :");
        for (int i = 0; i < notifications.size(); i++) {
            addTextToVBox(vbox, " " + (i + 1) + "- " + notifications.get(i).getText() + " , in turn: " + notifications.get(i).getTurnNumber());
        }
        this.controller.seenAllNotifications();

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


    private void demographicPanel(){
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

        ArrayList<DemographicPanelCommands> demographicPanelCommands = DemographicPanelCommands.getAllCommands();
        addTextToVBox(vbox, "Demographic panel commands:");
        for (DemographicPanelCommands demographicPanelCommand : demographicPanelCommands) {
            addButtonForDemographicPanelCommands(demographicPanelCommand, vbox);
        }

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

    private void addButtonForDemographicPanelCommands(DemographicPanelCommands demographicPanelCommand, VBox vbox){
        Button button = new Button();
        button.setText(demographicPanelCommand.getName());
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        button.getStyleClass().add("menu-button");
        if(demographicPanelCommand.getName().equals(DemographicPanelCommands.TERRITORY_SIZE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showTerritorySize();
                }
            });
            vbox.getChildren().add(button);
        }
        else if(demographicPanelCommand.getName().equals(DemographicPanelCommands.GOLD_COUNT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showGoldCount();
                }
            });
            vbox.getChildren().add(button);
        }
        else if(demographicPanelCommand.getName().equals(DemographicPanelCommands.RESOURCES.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showAllResources();
                }
            });
            vbox.getChildren().add(button);
        }
        else if(demographicPanelCommand.getName().equals(DemographicPanelCommands.IMPROVEMENTS.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showAllImprovements();
                }
            });
            vbox.getChildren().add(button);
        }
        else if(demographicPanelCommand.getName().equals(DemographicPanelCommands.OUTPUT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showOutput();
                }
            });
            vbox.getChildren().add(button);
        }
        else if(demographicPanelCommand.getName().equals(DemographicPanelCommands.SCORE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showScore();
                }
            });
            vbox.getChildren().add(button);
        }




    }

    private void showAllImprovements() {
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

        addTextToVBox(vbox, "You have these improvements on different tiles : (if there is two same improvements, it means that you have that on two tiles)");


        ArrayList<Improvement> improvements = controller.getCivilizationAllImprovements(currentPlayer);
        for (int i = 0; i < improvements.size(); i++) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(20);
            circle.setFill(Images.getImage(improvements.get(i).getType().getName()));
            hbox.getChildren().add(circle);
            addTextToHBox(hbox, " - " + improvements.get(i).getType().getName());
            vbox.getChildren().add(hbox);

        }

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

    private void showScore() {
        RegisterPageGraphicalController.showPopup("Your score is : " + this.controller.calculateScoreForCivilization(currentPlayer));
    }


    private void showOutput() {
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

        addTextToVBox(vbox, "Your collectable outputs in each turn:");
        addOutputLine("Gold", vbox);
        addOutputLine("Production", vbox);
        addOutputLine("Food", vbox);



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

    private void addOutputLine(String name, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(Images.getImage(name));
        hBox.getChildren().add(circle);
        if(name.equalsIgnoreCase("Gold")){
            addTextToHBox(hBox, "Gold : " + controller.calculateNetGoldProductionForCivilization(currentPlayer));
        }
        else if(name.equalsIgnoreCase("Food")){
            addTextToHBox(hBox, "Production : " + controller.calculateTotalBeakersForCivilization(currentPlayer));
        }
        else if(name.equalsIgnoreCase("Production")){
            addTextToHBox(hBox, "Food : " + controller.calculateTotalFoodFromCitizensForCivilization(currentPlayer));
        }
    }

    private void showAllResources() {
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
        addTextToVBox(vbox, "Your luxury resources:");
        ArrayList<LuxuryResource> luxuryResources = controller.getCivilizationLuxuryResource(currentPlayer);
        for(int i = 0; i < luxuryResources.size(); i++){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            circle.setFill(Images.getImage(luxuryResources.get(i).getName()));
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, luxuryResources.get(i).getName());
            vbox.getChildren().add(hBox);
        }
        addTextToVBox(vbox, "Your strategic resources:");
        ArrayList<StrategicResource> strategicResources = controller.getCivilizationStrategicResource(currentPlayer);
        for(int i = 0; i < strategicResources.size(); i++){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            circle.setFill(Images.getImage(strategicResources.get(i).getName()));
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, strategicResources.get(i).getName());
            vbox.getChildren().add(hBox);
        }

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

    private void showGoldCount() {
        RegisterPageGraphicalController.showPopup("You have " + (int) currentPlayer.getGoldCount() + " golds!");
    }

    private void showTerritorySize() {
        int territorySize = this.controller.calculateCivilizationTerritorySize(currentPlayer);
        RegisterPageGraphicalController.showPopup("You have " + territorySize + " tiles!");
    }

    private void economicOverviewPanel() {
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

        ArrayList<City> cities = this.controller.getCivilizationCities(currentPlayer);
        addTextToVBox(vbox, "You have " + cities.size() + " cities");
        for (int i = 0; i < cities.size(); i++) {
            addTextToVBox(vbox, "*******************************");
            addTextToVBox(vbox, "City central tile is in Y: " + controller.findTileYCoordinateInMap(cities.get(i).getCentralTile()) + " , X: " + controller.findTileYCoordinateInMap(cities.get(i).getCentralTile()));
            addTextToVBox(vbox, " population : " + cities.get(i).getCitizens().size());
            addTextToVBox(vbox, " effective combat strength : " + cities.get(i).getCombatStrength());
            addTextToVBox(vbox, " food : " + controller.calculateOutputForCity(cities.get(i)).getFood());
            addTextToVBox(vbox, " gold : " + controller.calculateOutputForCity(cities.get(i)).getGold());
            addTextToVBox(vbox, " production : " + controller.calculateOutputForCity(cities.get(i)).getProduction());
            addTextToVBox(vbox, " science : " + controller.calculateCityBeakerProduction(cities.get(i)));
            if (cities.get(i).getEntityInProduction() == null) {
                addTextToVBox(vbox, " This city doesn't have any entity in production!");
            } else {
                addTextToVBox(vbox, " entity in production : " + controller.getCityEntityInProduction(cities.get(i)).getName());
                addTextToVBox(vbox, " turns left : " + String.valueOf((int) ((controller.getCityEntityInProduction(cities.get(i)).getCost() - cities.get(i).getHammerCount()) / controller.calculateOutputForCity(cities.get(i)).getProduction())));
            }
        }

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

    private void addTextToVBox(VBox box, String text){
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }

    private void addTextToHBox(HBox box, String text){
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }
}
