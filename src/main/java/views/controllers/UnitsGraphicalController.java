package views.controllers;

import controllers.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import menusEnumerations.UnitCommands;
import models.units.Unit;
import models.units.UnitState;
import models.units.UnitType;

import java.net.URL;
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

    public static void makeTheUnitActionTab(Unit unit){
        unitCommandsBox.getChildren().clear();
        unitActionTabPane.setVisible(true);
        unitCommandsBox.setDisable(false);
        HashMap<UnitCommands, Boolean> allowedCommands = calculateAllowedCommands(unit);
        for (UnitCommands commandType : allowedCommands.keySet()) {
            if (allowedCommands.get(commandType)) {
                Button button = new Button(commandType.getName());
                button.getStyleClass().add("menu-button");
                button.setPrefWidth(60);
                button.setPrefHeight(30);
                unitCommandsBox.getChildren().add(button);
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


}
