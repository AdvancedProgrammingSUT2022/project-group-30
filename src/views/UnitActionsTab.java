package views;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controllers.GameController;
import menusEnumerations.Commands;
import menusEnumerations.UnitActionsTabCommands;
import models.interfaces.Selectable;
import models.units.Unit;
import utilities.Debugger;

public class UnitActionsTab extends Tab {
    private static UnitActionsTab unitActionsTab;
    private UnitActionsTab() {
    }
    public static UnitActionsTab getUnitActionsTab() {
        if (unitActionsTab == null) {
            unitActionsTab = new UnitActionsTab();
        }
        return unitActionsTab;
    }


    private Unit selectedUnit;

    public void initialize() {
        Selectable selectedEntity = GameController.getGameController().getSelectedEntity();
        if (selectedEntity instanceof Unit == false || selectedEntity == null) {
            Debugger.debug("UnitActionsTab was told to calculate commands while the selected entity is a non unit or null");
            return;
        }
        selectedUnit = (Unit)selectedEntity;
        for (Commands command : UnitActionsTabCommands.getAllCommands()) {
            commands.put(command, false);
        }

        commands.put(UnitActionsTabCommands.SHOW_INFO, true);
        if (selectedUnit.hasMP()) {
            commands.put(UnitActionsTabCommands.MOVE, true);
            commands.put(UnitActionsTabCommands.MOVE_TO, true);
        }
    }

    public boolean checkCommand(String command) {   // the return value shows if the given command matched any of the allowed commands
        UnitActionsTabCommands matchedCommand = null;
        Matcher matcher = null;
        ArrayList<UnitActionsTabCommands> allowedCommands = getAllowedCommands();
        for (UnitActionsTabCommands allowedCommand : allowedCommands) {
            if ((matcher = allowedCommand.getCommandMatcher(command)) != null) {
                matchedCommand = allowedCommand;
                break;
            }
        }

        // TODO
        if (matchedCommand == UnitActionsTabCommands.SHOW_INFO) {
            showUnitInfo();
            return true;
        }

        return false;
    }

    private void deselect() {
        GameController.getGameController().setSelectedEntity(null);
    }

    public void printTab() {
        printCommands();
        showUnitInfo();
    }

    private void showUnitInfo() {
        // TODO
        System.out.println("Unit Info:");
        System.out.println(selectedUnit.getType().getName());
        System.out.println("MPs left : " + selectedUnit.getMovePointsLeft());
        System.out.println("Hit Points left : " + selectedUnit.getHitPointsLeft());
    }

    public void printCommands() {
        System.out.println("** Unit Actions Tab **");
        System.out.print("Allowed Commands: ");
        for (UnitActionsTabCommands command : getAllowedCommands()) {
            System.out.print(command.getName() + ", ");
        }
        System.out.println();
    }

    private ArrayList<UnitActionsTabCommands> getAllowedCommands() {
        ArrayList<UnitActionsTabCommands> result = new ArrayList<>();
        for (Commands command : commands.keySet()) {
            if (commands.get(command) == true) {
                result.add((UnitActionsTabCommands)command);
            }
        }
        return result;
    }
}
