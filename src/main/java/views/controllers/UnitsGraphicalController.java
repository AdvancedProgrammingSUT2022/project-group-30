package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import menusEnumerations.UnitCommands;
import models.GameMap;
import models.Tile;
import models.interfaces.TileImage;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import views.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UnitsGraphicalController {

    private static GameController controller = GameController.getGameController();

    private static ScrollPane unitActionTabPane;
    private static VBox unitCommandsBox;

    public static void initializeUnitActionTab(Pane pane){
        unitActionTabPane = new ScrollPane();
        pane.getChildren().add(unitActionTabPane);
        unitCommandsBox = new VBox();
        unitActionTabPane.setLayoutX(20);
        unitActionTabPane.setLayoutY(300);
        unitActionTabPane.setPrefWidth(300);
        unitActionTabPane.setPrefHeight(400);
        unitActionTabPane.setContent(unitCommandsBox);
        unitCommandsBox.setStyle("-fx-background-color: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        unitActionTabPane.setStyle("-fx-background: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        unitActionTabPane.setFitToHeight(true);
        unitActionTabPane.setFitToWidth(true);
        unitCommandsBox.setAlignment(Pos.CENTER);
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
        unitCommandsBox.setSpacing(10);
    }

    public static void makeTheUnitActionTab(Unit unit, Pane pane) throws MalformedURLException {
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(true);
        unitCommandsBox.setDisable(false);
        HashMap<UnitCommands, Boolean> allowedCommands = calculateAllowedCommands(unit);
        for (UnitCommands commandType : allowedCommands.keySet()) {
            if (allowedCommands.get(commandType)) {
                addButtonForUnitCommand(commandType, unit, pane);
            }
        }
        Circle circle = new Circle();
        circle.setRadius(30);
        circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Units3/" + unit.getType().getName() + ".png").toExternalForm()).toExternalForm())));
        unitCommandsBox.getChildren().add(circle);
        Text name = new Text(controller.getCurrentPlayer().getName() + "'s " + unit.getType().getName());
        name.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        unitCommandsBox.getChildren().add(name);
        Text mp = new Text("MP: " + unit.getMovePointsLeft());
        mp.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        unitCommandsBox.getChildren().add(mp);
        Text cs = new Text("CS: " + unit.getType().getCombatStrength());
        cs.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        unitCommandsBox.getChildren().add(cs);

    }

    private static void addButtonForUnitCommand(UnitCommands command, Unit unit, Pane pane){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        unitCommandsBox.getChildren().add(button);
        if(command.getName().equals(UnitCommands.DESELECT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectUnit(unit);
                }
            });
        }
        else if(command.getName().equals(UnitCommands.SLEEP.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    sleepUnit(unit);
                }
            });
        }
        else if(command.getName().equals(UnitCommands.ALERT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    alertAUnit(unit);
                }
            });

        }
        else if(command.getName().equals(UnitCommands.FORTIFY.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fortifyAUnit(unit);
                }
            });

        }
        else if(command.getName().equals(UnitCommands.FORTIFY_UNTIL_HEALED.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fortifyAUnitUntilHealed(unit);
                }
            });

        }
        else if(command.getName().equals(UnitCommands.GARRISON.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    garrisonAUnit(unit);
                }
            });

        }
        else if(command.getName().equals(UnitCommands.AWAKE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    awakeAUnit(unit, pane);
                }
            });

        }
        else if(command.getName().equals(UnitCommands.CANCEL_MOVE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cancelUnitMove(unit, pane);
                }
            });
        }
        else if(command.getName().equals(UnitCommands.DELETE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deleteAUnit(unit);
                }
            });
        }
        else if(command.getName().equals(UnitCommands.MOVE_TO.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileAsDestination(pane);
                }
            });
        }
        else if(command.getName().equals(UnitCommands.FOUND_CITY.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    foundCity(unit);
                }
            });
        }
    }

    private static void foundCity(Unit unit) {
        if (unit.getMovePointsLeft() == 0) {
            RegisterPageGraphicalController.showPopup("Settler has no movepoints!");
            return;
        }
        if (controller.isTileTooNearCity(unit.getLocation())) {
            RegisterPageGraphicalController.showPopup("You can't found a city within a 3-tile distance of another city");
            return;
        }
        controller.foundCityWithSettler(unit);
        controller.getCurrentPlayer().setSelectedEntity(null);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void cancelUnitMove(Unit unit, Pane pane) {
        unit.setPath(null);
        try {
            makeTheUnitActionTab(unit, pane);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        controller.deleteUnit(unit);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void awakeAUnit(Unit unit, Pane pane) {
        unit.setState(UnitState.AWAKE);
        try {
            makeTheUnitActionTab(unit, pane);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void garrisonAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.GARRISON);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void fortifyAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFY);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void fortifyAUnitUntilHealed(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFYUNTILHEALED);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void alertAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.ALERT);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void sleepUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.ASLEEP);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void deselectUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
    }

    private static void moveTo(TileImage tileImage){
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only choose visible tiles as destination!");
            return;
        }
        Unit unit = (Unit) controller.getCurrentPlayer().getSelectedEntity();
        Tile destination = (Tile) tileImage;
        if (controller.isTileImpassable(destination)) {
            RegisterPageGraphicalController.showPopup("The destination you have entered is impassable!");
            return;
        }
        ArrayList<Tile> path = controller.findPath(unit, unit.getLocation(), destination);
        if (path == null) {
            RegisterPageGraphicalController.showPopup("No path was found to the destination you have entered");
            return;
        }

        // SET UNITS PATH
        unit.setPath(path);
        // MOVE AND UPDATE FOG OF WAR
        controller.moveUnitAlongItsPath(unit);
        if (unit.getMovePointsLeft() == 0) {
            deselectUnit(unit);
        }
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(false);
        unitCommandsBox.setDisable(true);
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void waitForChoosingTileAsDestination(Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        moveTo(getTileImageFromHexagon(hexagon));
                    }
                });
            }
        }

    }

    private static HashMap<UnitCommands, Boolean> calculateAllowedCommands(Unit unit) {
        HashMap<UnitCommands, Boolean> result = new HashMap<>();
        for (UnitCommands command : UnitCommands.getAllCommands()) {
            result.put(command, false);
        }

        boolean isUnitAWorkingWorker = unit.getType() == UnitType.WORKER && controller.isWorkerWorking(unit);
        if (controller.canUnitSetUpForRangedAttack(unit)) {
            result.put(UnitCommands.SET_UP_FOR_RANGED_ATTACK, true);
        }
        if (controller.canUnitMeleeAttack(unit)) {
            result.put(UnitCommands.MELEE_ATTACK, true);
        }
        if (controller.canUnitRangedAttack(unit)) {
            result.put(UnitCommands.RANGED_ATTACK, true);
        }
        if (controller.canUnitPillage(unit)) {
            result.put(UnitCommands.PILLAGE, true);
        }
        result.put(UnitCommands.DESELECT, true);
        result.put(UnitCommands.SHOW_INFO, true);
        if (!isUnitAWorkingWorker) {
            result.put(UnitCommands.DELETE, true);
        }
        if (unit.getState().waitsForCommand && controller.canUnitMove(unit) && !isUnitAWorkingWorker) {
            result.put(UnitCommands.MOVE_TO, true);
        }

        if (unit.getState().waitsForCommand && unit.getType() == UnitType.SETTLER && unit.getMovePointsLeft() > 0) {
            result.put(UnitCommands.FOUND_CITY, true);
        }

        if (unit.getState() == UnitState.AWAKE) {
            if (unit.getType().getCombatType().isStateAllowed(UnitState.FORTIFY)) {
                result.put(UnitCommands.FORTIFY, true);
                if (unit.getHitPointsLeft() < unit.getType().getHitPoints()) {
                    result.put(UnitCommands.FORTIFY_UNTIL_HEALED, true);
                }
            }
            if (!isUnitAWorkingWorker) {
                result.put(UnitCommands.ALERT, true);
                result.put(UnitCommands.SLEEP, true);
            }
        } else {
            result.put(UnitCommands.AWAKE, true);
        }

        if (unit.getState().waitsForCommand && unit.getType() == UnitType.WORKER) {
            result.put(UnitCommands.WORK_ACTIONS, true);
        }

        if (unit.getState().waitsForCommand && unit.getType().getCombatType().isStateAllowed(UnitState.GARRISON) &&
                controller.getCityCenteredInTile(unit.getLocation()) != null &&
                controller.getCityCenteredInTile(unit.getLocation()).getOwner() == unit.getOwner()) {
            result.put(UnitCommands.GARRISON, true);
        }

        if (unit.getPath() != null) {
            result.put(UnitCommands.CANCEL_MOVE, true);
        }
        return result;
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
