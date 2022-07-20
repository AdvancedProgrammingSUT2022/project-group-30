package views.controllers;

import controllers.CombatController;
import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import menusEnumerations.ProductionPanelCommands;
import menusEnumerations.UnitCommands;
import menusEnumerations.WorkerCommands;
import models.*;
import models.improvements.Improvement;
import models.improvements.ImprovementType;
import models.interfaces.TileImage;
import models.interfaces.combative;
import models.units.CombatType;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;
import models.works.*;
import views.Main;
import views.customcomponents.AttackYesNoDialog;
import views.customcomponents.CityDefeatDialog;
import views.images.Images;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class UnitsGraphicalController {

    private static GameController controller = GameController.getGameController();

    private static ScrollPane unitActionTabPane;
    private static VBox unitCommandsBox;
    private static boolean isAnswerYes;

    public static void initializeUnitActionTab(Pane pane) {
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
        circle.setFill(Images.getImage(unit.getType().getName()));
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

    private static void addButtonForUnitCommand(UnitCommands command, Unit unit, Pane pane) {
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        unitCommandsBox.getChildren().add(button);
        if (command.getName().equals(UnitCommands.DESELECT.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectUnit(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.SLEEP.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    sleepUnit(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.ALERT.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    alertAUnit(unit);
                }
            });

        } else if (command.getName().equals(UnitCommands.FORTIFY.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fortifyAUnit(unit);
                }
            });

        } else if (command.getName().equals(UnitCommands.FORTIFY_UNTIL_HEALED.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fortifyAUnitUntilHealed(unit);
                }
            });

        } else if (command.getName().equals(UnitCommands.GARRISON.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    garrisonAUnit(unit);
                }
            });

        } else if (command.getName().equals(UnitCommands.AWAKE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    awakeAUnit(unit, pane);
                }
            });

        } else if (command.getName().equals(UnitCommands.CANCEL_MOVE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cancelUnitMove(unit, pane);
                }
            });
        } else if (command.getName().equals(UnitCommands.DELETE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deleteAUnit(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.MOVE_TO.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileAsDestination(pane);
                }
            });
        } else if (command.getName().equals(UnitCommands.FOUND_CITY.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    foundCity(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.MELEE_ATTACK.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToMeleeAttack(unit, pane);
                }
            });
        } else if (command.getName().equals(UnitCommands.RANGED_ATTACK.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileForRangedAttack(unit, pane);
                }
            });
        } else if (command.getName().equals(UnitCommands.SHOW_INFO.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showUnitInfo(unit, pane);
                }
            });
        } else if (command.getName().equals(UnitCommands.SET_UP_FOR_RANGED_ATTACK.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    setUpForRangedAttack(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.PILLAGE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    pillage(unit);
                }
            });
        } else if (command.getName().equals(UnitCommands.WORK_ACTIONS.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeWorkPanel(unit, pane);
                }
            });
        }
    }

    private static void waitForChoosingTileForRangedAttack(Unit unit, Pane pane) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Polygon) {
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        rangedAttack(getTileImageFromHexagon(hexagon), unit);
                    }
                });
            }
        }

    }

    private static void rangedAttack(TileImage tileImage, Unit unit) {
        if (!(tileImage instanceof Tile)) {
            RegisterPageGraphicalController.showPopup("You can only attack visible tiles!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Tile targetTile = (Tile) tileImage;
        if (targetTile.calculateDistance(unit.getLocation()) > unit.getType().getRange() ||
                !controller.getVisibleTilesByUnit(unit).contains(targetTile)) {
            RegisterPageGraphicalController.showPopup("You can only attack target that are seen and within range!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, unit.getOwner())) {
            RegisterPageGraphicalController.showPopup("You can't attack this tile because there are no hostile units in it!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, unit.getOwner());
        if (!GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).areAtWar()) {
            AttackYesNoDialog dialog = new AttackYesNoDialog() {
                @Override
                public void onYesButtonClick() {
                    GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).setAreAtWar(true);
                    CombatController.getCombatController().executeRangedAttack(unit, target);
                    RegisterPageGraphicalController.showPopup("Ranged Attacked " + targetTile.findTileYCoordinateInMap() + ", " + targetTile.findTileXCoordinateInMap());
                    try {
                        Main.loadFxmlFile("CivilizationGamePage");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    this.close();
                }
            };
            dialog.show();
        } else {
            CombatController.getCombatController().executeRangedAttack(unit, target);
            RegisterPageGraphicalController.showPopup("Ranged Attacked " + targetTile.findTileYCoordinateInMap() + ", " + targetTile.findTileXCoordinateInMap());
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void waitForChoosingTileToMeleeAttack(Unit unit, Pane pane) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Polygon) {
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        meleeAttack(getTileImageFromHexagon(hexagon), unit);
                    }
                });
            }
        }

    }

    private static void meleeAttack(TileImage tileImage, Unit unit) {
        if (!(tileImage instanceof Tile)) {
            RegisterPageGraphicalController.showPopup("You can only melee attack visible tiles!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Tile targetTile = (Tile) tileImage;
        if (!controller.areTwoTilesAdjacent(targetTile, unit.getLocation())) {
            RegisterPageGraphicalController.showPopup("You can only melee attack adjacent tiles!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, unit.getOwner())) {
            RegisterPageGraphicalController.showPopup("You can't attack this tile because there are no hostile units in it!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, unit.getOwner());
        if (!GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).areAtWar()) {
            AttackYesNoDialog dialog = new AttackYesNoDialog() {
                @Override
                public void onYesButtonClick() {
                    GameDataBase.getGameDataBase().getDiplomaticRelation(target.getOwner(), unit.getOwner()).setAreAtWar(true);
                    applyMeleeAttackEffects(unit, target, targetTile);
                    this.close();
                }
            };
            dialog.show();
        } else {
            applyMeleeAttackEffects(unit, target, targetTile);
        }
    }

    public static void applyMeleeAttackEffects(Unit unit, combative target, Tile targetTile) {
        CombatController.getCombatController().executeMeleeAttack(unit, target);
        if (target instanceof City) {
            City targetCity = (City) target;
            if (targetCity.isDefeated()) {
                if (targetCity.isOriginalCapital() && targetCity.getFounder() == unit.getOwner()) {
                    CombatController.getCombatController().annexCity(targetCity, controller.getCurrentPlayer());
                    try {
                        Main.loadFxmlFile("CivilizationGamePage");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    CityDefeatDialog defeatDialog = new CityDefeatDialog() {
                        @Override
                        public void onDestroyButtonClick() {
                            CombatController.getCombatController().kill(targetCity);
                            try {
                                Main.loadFxmlFile("CivilizationGamePage");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onAnnexButtonClick() {
                            CombatController.getCombatController().annexCity(targetCity, controller.getCurrentPlayer());
                            try {
                                Main.loadFxmlFile("CivilizationGamePage");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    defeatDialog.show();
                }
            }
        }
        RegisterPageGraphicalController.showPopup("Melee Attacked " + targetTile.findTileYCoordinateInMap() + ", " + targetTile.findTileXCoordinateInMap());
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setUpForRangedAttack(Unit unit) {
        unit.assemble();
        unit.setMovePointsLeft(0);
        unit.setPath(null);
        RegisterPageGraphicalController.showPopup("Unit successfully assembled!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void pillage(Unit unit) {
        controller.pillageUnitsTile(unit);
        RegisterPageGraphicalController.showPopup("You just successfully tore apart hard-earned value!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showUnitInfo(Unit unit, Pane gamePagePane) {
        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        BorderPane pane = new BorderPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        pane.getStylesheets().addAll(gamePagePane.getStylesheets());
        pane.getStyleClass().add("shadow-pane");
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        pane.setCenter(vbox);

        addTextToVBox(vbox, unit.getOwner().getName() + "'s " + unit.getType().getName());
        addTextToVBox(vbox, "State: " + unit.getState());
        if (unit.getType().getCombatType() == CombatType.SIEGE) {
            addTextToVBox(vbox, (unit.isAssembled()) ? "Assembled" : "Disassembled");
        }
        if (unit.getType() == UnitType.WORKER) {
            if (controller.getWorkersWork(unit) == null) {
                addTextToVBox(vbox, "Not Currently Working...");
            } else {
                addTextToVBox(vbox, "Work: " + controller.getWorkersWork(unit).getTitle());
                Tile location = controller.getWorkersWork(unit).findLocation();
                addTextToVBox(vbox, "Work Place: Y: " + location.findTileYCoordinateInMap() + ", X: " + location.findTileXCoordinateInMap());

            }
        }
        addTextToVBox(vbox, "Y: " + unit.getLocation().findTileYCoordinateInMap() + ", X: " + unit.getLocation().findTileXCoordinateInMap());
        addTextToVBox(vbox, "Move Points: " + unit.getMovePointsLeft() + " out of " + unit.getType().getMovementSpeed());
        addTextToVBox(vbox, "Hit Points: " + unit.getHitPointsLeft() + " out of " + unit.getType().getHitPoints());
        if (unit.getPath() != null) {
            addTextToVBox(vbox, "Path:");
            for (Tile tile : unit.getPath()) {
                addTextToVBox(vbox, "Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap());
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

    private static void moveTo(TileImage tileImage) {
        if (!(tileImage instanceof Tile)) {
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

    public static void waitForChoosingTileAsDestination(Pane pane) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Polygon) {
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

    private static TileImage getTileImageFromHexagon(Polygon hexagon) {
        TileImage[][] tilesToShow = GameMap.getGameMap().getCivilizationImageToShowOnScene(controller.getCurrentPlayer());
        double startXCoordinate = hexagon.getPoints().get(0);
        double startYCoordinate = hexagon.getPoints().get(1);
        for (int i = 0; i < tilesToShow.length; i++) {
            for (int j = 0; j < tilesToShow[i].length; j++) {
                double xCoordinate = 160 + (double) 32 / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if (controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap() % 2 == 1) {
                    isOdd = -1;
                }
                double yCoordinate = 69 + Math.sqrt(3) * 32 * (i + isOdd * (double) (j % 2) / (double) 2);
                if (startXCoordinate == xCoordinate && startYCoordinate == yCoordinate) {
                    return tilesToShow[i][j];
                }
            }
        }
        return null;
    }

    private static void addTextToVBox(VBox box, String text) {
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }

    private static void addTextToHBox(HBox box, String text) {
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }

    public static void makeWorkPanel(Unit unit, Pane pane) {
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(true);
        unitCommandsBox.setDisable(false);
        ArrayList<WorkerCommands> allCommands = calculateWorkerAllowedActions(unit);
        for (int i = 0; i < allCommands.size(); i++) {
            addWokPanelButtonForCommand(unit, pane, allCommands.get(i));
        }
        Button back = new Button("back");
        back.getStyleClass().add("menu-button");
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    makeTheUnitActionTab(unit, pane);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        unitCommandsBox.getChildren().add(back);

    }

    private static void addWokPanelButtonForCommand(Unit unit, Pane pane, WorkerCommands command) {
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        unitCommandsBox.getChildren().add(button);
        if (command.getName().equals(WorkerCommands.BUILD_ROAD.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.ROAD);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.ROAD, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_RAILROAD.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.RAILROAD);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.RAILROAD, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_FARM.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildFarmOrMine(unit, ImprovementType.FARM);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovementAndRemoveFeature(unit, ImprovementType.FARM));
        } else if (command.getName().equals(WorkerCommands.BUILD_MINE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildFarmOrMine(unit, ImprovementType.MINE);

                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovementAndRemoveFeature(unit, ImprovementType.FARM));
        } else if (command.getName().equals(WorkerCommands.BUILD_TRADING_POST.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.TRADING_POST);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.TRADING_POST, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_LUMBER_MILL.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.LUMBER_MILL);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.LUMBER_MILL, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_PASTURE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.PASTURE);

                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.PASTURE, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_PLANTATION.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.PLANTATION);

                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.PLANTATION, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_QUARRY.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.QUARRY);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.QUARRY, unit));
        } else if (command.getName().equals(WorkerCommands.BUILD_CAMP.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildImprovement(unit, ImprovementType.CAMP);
                }
            });
            addTooltipForWorkButtons(button, unit, new BuildImprovement(ImprovementType.CAMP, unit));
        } else if (command.getName().equals(WorkerCommands.CLEAR_FOREST.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    clearFeature(unit, Feature.FOREST);
                }
            });
            addTooltipForWorkButtons(button, unit, new ClearFeature(Feature.FOREST, unit));
        } else if (command.getName().equals(WorkerCommands.CLEAR_JUNGLE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    clearFeature(unit, Feature.JUNGLE);
                }
            });
            addTooltipForWorkButtons(button, unit, new ClearFeature(Feature.JUNGLE, unit));
        } else if (command.getName().equals(WorkerCommands.CLEAR_MARSH.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    clearFeature(unit, Feature.MARSH);

                }
            });
            addTooltipForWorkButtons(button, unit, new ClearFeature(Feature.MARSH, unit));
        } else if (command.getName().equals(WorkerCommands.CLEAR_ROUTES.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    clearRoutes(unit);
                }
            });
            addTooltipForWorkButtons(button, unit, new ClearRoutes(unit));
        } else if (command.getName().equals(WorkerCommands.FIX_IMPROVEMENT.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fixImprovement(unit, false);
                }
            });
            addTooltipForWorkButtons(button, unit, new FixPillage(controller.getTypeOfPillagedImprovement(unit), unit));
        } else if (command.getName().equals(WorkerCommands.FIX_ROUTE.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    fixImprovement(unit, true);
                }
            });
            ImprovementType improvementType;
            if (unit.getLocation().containsImprovment(ImprovementType.ROAD))
                improvementType = ImprovementType.ROAD;
            else
                improvementType = ImprovementType.RAILROAD;
            addTooltipForWorkButtons(button, unit, new FixPillage(improvementType, unit));
        } else if (command.getName().equals(WorkerCommands.STOP_WORK.getName())) {
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stopWork(unit);
                }
            });
        }
    }

    private static ArrayList<WorkerCommands> calculateWorkerAllowedActions(Unit worker) {
        ArrayList<WorkerCommands> result = new ArrayList<WorkerCommands>();
        if (controller.canWorkerBuildRoute(worker, ImprovementType.ROAD)) {
            result.add(WorkerCommands.BUILD_ROAD);
        }
        if (controller.canWorkerBuildRoute(worker, ImprovementType.RAILROAD)) {
            result.add(WorkerCommands.BUILD_RAILROAD);
        }
        if (controller.canWorkerBuildFarmOrMine(worker, ImprovementType.FARM)) {
            result.add(WorkerCommands.BUILD_FARM);
        }
        if (controller.canWorkerBuildFarmOrMine(worker, ImprovementType.MINE)) {
            result.add(WorkerCommands.BUILD_MINE);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.TRADING_POST)) {
            result.add(WorkerCommands.BUILD_TRADING_POST);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.LUMBER_MILL)) {
            result.add(WorkerCommands.BUILD_LUMBER_MILL);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.PASTURE)) {
            result.add(WorkerCommands.BUILD_PASTURE);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.PLANTATION)) {
            result.add(WorkerCommands.BUILD_PLANTATION);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.QUARRY)) {
            result.add(WorkerCommands.BUILD_QUARRY);
        }
        if (controller.canWorkerBuildImprovement(worker, ImprovementType.CAMP)) {
            result.add(WorkerCommands.BUILD_CAMP);
        }
        if (controller.canWorkerClearFeature(worker, Feature.JUNGLE)) {
            result.add(WorkerCommands.CLEAR_JUNGLE);
        }
        if (controller.canWorkerClearFeature(worker, Feature.FOREST)) {
            result.add(WorkerCommands.CLEAR_FOREST);
        }
        if (controller.canWorkerClearFeature(worker, Feature.MARSH)) {
            result.add(WorkerCommands.CLEAR_MARSH);
        }
        if (controller.canWorkerClearRoutes(worker)) {
            result.add(WorkerCommands.CLEAR_ROUTES);
        }
        if (controller.canWorkerFixImprovement(worker)) {
            result.add(WorkerCommands.FIX_IMPROVEMENT);
        }
        if (controller.canWorkerFixRoute(worker)) {
            result.add(WorkerCommands.FIX_ROUTE);
        }
        if (controller.isWorkerWorking(worker)) {
            result.add(WorkerCommands.STOP_WORK);
        }
        return result;
    }

    private static void buildImprovement(Unit worker, ImprovementType improvementType) {
        Tile location = worker.getLocation();
        if (!askToReplace(location)) {
            RegisterPageGraphicalController.showPopup("Building " + improvementType.getName() + "canceled");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (location.getWork() != null) {
            if (location.getWork() instanceof BuildImprovement &&
                    ((BuildImprovement) location.getWork()).getImprovement() == improvementType) {
                ((BuildImprovement) location.getWork()).startWork(worker);
                RegisterPageGraphicalController.showPopup("Resumed " + improvementType.getName().toLowerCase() + " construction here");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            askYesOrNoQuestionPopup("Last project will be terminated. Are you sure you want to continue? y/n");
            if (!isAnswerYes) {
                RegisterPageGraphicalController.showPopup("Building " + improvementType.getName().toLowerCase() + " canceled");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        BuildImprovement newWork = new BuildImprovement(improvementType, worker);
        location.setWork(newWork);
        worker.setPath(null);
        RegisterPageGraphicalController.showPopup("Started the construction of a " + improvementType.getName().toLowerCase() + " here!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean askToReplace(Tile location) {
        Improvement nonRouteImprovement;
        if ((nonRouteImprovement = location.getNonRouteImprovement()) != null) {
            askYesOrNoQuestionPopup("There is already an improvement (" + nonRouteImprovement.getType().getName()
                    + ") on this tile, do you wish to replace it? y/n");
            if (isAnswerYes) {
                location.removeImprovement(nonRouteImprovement);
                RegisterPageGraphicalController.showPopup("Removed " + nonRouteImprovement.getType().getName() + " improvement!");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static void askYesOrNoQuestionPopup(String question) {
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        pane.setPrefHeight(200);
        pane.setPrefWidth(400);
        pane.setStyle("-fx-background-color: #74bfc7;");
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        Text text = new Text(question);
        text.setStyle("-fx-font-family: \"Academy Engraved LET\"; -fx-text-fill: #e50000; -fx-font-size: 20");
        box.getChildren().add(text);
        pane.setCenter(box);
        Button yes = new Button();
        yes.setText("Yes");
        yes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isAnswerYes = true;
                stage.hide();
            }
        });
        box.getChildren().add(yes);
        Button no = new Button();
        no.setText("Yes");
        no.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isAnswerYes = false;
                stage.hide();
            }
        });
        box.getChildren().add(no);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }

    private static void buildFarmOrMine(Unit worker, ImprovementType type) {
        Tile location = worker.getLocation();
        if (!askToReplace(location)) {
            RegisterPageGraphicalController.showPopup("Building " + type.getName() + " canceled");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (location.getWork() != null) {
            if (location.getWork() instanceof BuildImprovementAndRemoveFeature &&
                    (((BuildImprovementAndRemoveFeature) location.getWork()).getImprovement() == type)) {
                ((BuildImprovementAndRemoveFeature) location.getWork()).startWork(worker);
                RegisterPageGraphicalController.showPopup("Resumed " + type.getName().toLowerCase() + " construction here");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            askYesOrNoQuestionPopup("Last project will be terminated. Are you sure you want to continue? y/n");
            if (!isAnswerYes) {
                RegisterPageGraphicalController.showPopup("Building " + type.getName().toLowerCase() + " canceled");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        BuildImprovementAndRemoveFeature newWork = new BuildImprovementAndRemoveFeature(worker, type);
        location.setWork(newWork);
        worker.setPath(null);
        RegisterPageGraphicalController.showPopup("Started the construction of a " + type.getName().toLowerCase() + " here!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return;
    }

    private static void clearFeature(Unit worker, Feature feature) {
        Tile location = worker.getLocation();
        if (location.getWork() != null) {
            if (location.getWork() instanceof ClearFeature &&
                    ((ClearFeature) location.getWork()).getFeature() == feature) {
                ((ClearFeature) location.getWork()).startWork(worker);
                RegisterPageGraphicalController.showPopup("Resumed " + feature.getName().toLowerCase() + " clearance here");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            askYesOrNoQuestionPopup("Last project will be terminated. Are you sure you want to continue? y/n");
            if (!isAnswerYes) {
                RegisterPageGraphicalController.showPopup("Clearance " + feature.getName().toLowerCase() + " canceled");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        location.setWork(new ClearFeature(feature, worker));
        worker.setPath(null);
        RegisterPageGraphicalController.showPopup("Started the clearance of a " + feature.getName().toLowerCase() + " here!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void clearRoutes(Unit worker) {
        Tile location = worker.getLocation();
        if (location.getWork() != null) {
            if (location.getWork() instanceof ClearRoutes) {
                ((ClearRoutes) location.getWork()).startWork(worker);
                RegisterPageGraphicalController.showPopup("Resumed routes clearance here");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            askYesOrNoQuestionPopup("Last project will be terminated. Are you sure you want to continue? y/n");
            if (!isAnswerYes) {
                RegisterPageGraphicalController.showPopup("Clearance routes canceled");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        location.setWork(new ClearRoutes(worker));
        worker.setPath(null);
        RegisterPageGraphicalController.showPopup("Started the clearance of routes here!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fixImprovement(Unit worker, boolean isRoute) {
        Tile location = worker.getLocation();
        ImprovementType improvementType;
        if (!isRoute)
            improvementType = controller.getTypeOfPillagedImprovement(worker);
        else if (worker.getLocation().containsImprovment(ImprovementType.ROAD))
            improvementType = ImprovementType.ROAD;
        else
            improvementType = ImprovementType.RAILROAD;
        if (location.getWork() != null) {
            if (location.getWork() instanceof FixPillage &&
                    ((FixPillage) location.getWork()).getImprovementType() == improvementType) {
                ((FixPillage) location.getWork()).startWork(worker);
                RegisterPageGraphicalController.showPopup("Resumed " + improvementType.getName().toLowerCase() + " fixation here");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            askYesOrNoQuestionPopup("Last project will be terminated. Are you sure you want to continue? y/n");
            if (!isAnswerYes) {
                RegisterPageGraphicalController.showPopup("Fixation " + improvementType.getName().toLowerCase() + " canceled");
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        location.setWork(new FixPillage(improvementType, worker));
        worker.setPath(null);
        RegisterPageGraphicalController.showPopup("Started the fixation of a " + improvementType.getName().toLowerCase() + " here!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void stopWork(Unit worker) {
        Work work = controller.getWorkersWork(worker);
        work.stopWork();
        RegisterPageGraphicalController.showPopup("Stopped this units work!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addTooltipForWorkButtons(Button button, Unit unit, Work work) {
        int totalTurns = work.getTurnsRemaining();
        int turnsLeft = totalTurns;
        if (work instanceof BuildImprovement && unit.getLocation().getWork() instanceof BuildImprovement && ((BuildImprovement) work).getImprovement() == ((BuildImprovement) unit.getLocation().getWork()).getImprovement()) {
            turnsLeft = unit.getLocation().getWork().getTurnsRemaining();
        }
        if (work instanceof BuildImprovementAndRemoveFeature && unit.getLocation().getWork() instanceof BuildImprovementAndRemoveFeature && ((BuildImprovementAndRemoveFeature) work).getImprovement() == ((BuildImprovementAndRemoveFeature) unit.getLocation().getWork()).getImprovement()) {
            turnsLeft = unit.getLocation().getWork().getTurnsRemaining();
        }
        if (work instanceof FixPillage && unit.getLocation().getWork() instanceof FixPillage && ((FixPillage) work).getImprovementType() == ((FixPillage) unit.getLocation().getWork()).getImprovementType()) {
            turnsLeft = unit.getLocation().getWork().getTurnsRemaining();
        }
        if (work instanceof ClearFeature && unit.getLocation().getWork() instanceof ClearFeature && ((ClearFeature) work).getFeature() == ((ClearFeature) unit.getLocation().getWork()).getFeature()) {
            turnsLeft = unit.getLocation().getWork().getTurnsRemaining();
        }
        if (work instanceof ClearRoutes && unit.getLocation().getWork() instanceof ClearRoutes) {
            turnsLeft = unit.getLocation().getWork().getTurnsRemaining();
        }
        String note = "turns remaining: " + String.valueOf(turnsLeft) + " out of " + String.valueOf(totalTurns);
        GamePageController.setToolTipForButton(button, note);
    }


}
