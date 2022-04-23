package views;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

import controllers.GameController;
import menusEnumerations.GameMainPageCommands;
import models.Tile;
import models.TileVisibility;
import models.units.Unit;
import utilities.MyScanner;

public class GameView implements View {

    private static GameView gameView;

    private GameView() {
        scanner = MyScanner.getScanner();
        controller = GameController.getGameController();

        tabs = new HashMap<>();
        tabs.put(UnitActionsTab.getUnitActionsTab(), false);
    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    private GameController controller;
    private Scanner scanner;
    private HashMap<Tab, Boolean> tabs;

    public void run() {
        controller.startGame();
        showMap();
        
        String command;
        Matcher matcher;
        while (true) {
            command = scanner.nextLine().trim();
            if ((matcher = GameMainPageCommands.GET_TILE_INFO.getCommandMatcher(command)) != null) {
                printTileInfo(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_UNIT.getCommandMatcher(command)) != null) {
                selectMilitaryUnit(matcher);
            } else if ((matcher = GameMainPageCommands.DESELECT.getCommandMatcher(command)) != null) {
                deselect();
            } else if (runTabs(command)) {
                
            } else {
                System.out.println("Invalid Command!");
            }
        }
    }

    private boolean runTabs(String command) {   // runs command through all open tabs and returns true and closes the tab if it matches with one of their allowed commands
        if (tabs.get(UnitActionsTab.getUnitActionsTab()) && UnitActionsTab.getUnitActionsTab().checkCommand(command)) {
            
        }
        return false;
    }

    private void deselect() {
        
    }

    private void openTab(Tab tab) {
        tabs.put(tab, true);
        tab.initialize();
        tab.printTab();
    }

    private void closeTab(Tab tab) {
        tabs.put(tab, false);
    }

    private void showMap() {
        // TODO : mahyar 
    }

    private void selectMilitaryUnit(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (controller.areCoordinatesValid(x, y) == false) {
            System.out.println("Invalid coordinates");
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        TileVisibility visibility = controller.getTileVisibilityForPlayer(tile);
        if (visibility != TileVisibility.VISIBLE) {
            System.out.println("You can only select units in tiles you can see!");
        }
        Unit unit = controller.getMilitaryUnitInTile(tile, controller.getCurrentPlayer());
        if (unit == null) {
            System.out.println("None of your military units are in this tile!");
        }
        controller.setSelectedEntity(unit);
        openTab(UnitActionsTab.getUnitActionsTab());
        // TODO
    }

    private void printTileInfo(Matcher matcher) {
        // TODO : needs to handle fog of war
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        System.out.println("printing info for tile : x, y");
        if (controller.areCoordinatesValid(x, y) == false) {
            System.out.println("Invalid coordinates");
        } else {
            Tile tile = controller.getTileByCoordinates(x, y);
            TileVisibility visibility = controller.getTileVisibilityForPlayer(tile);
            System.out.println("Visibility : " + visibility.getName());

            if (visibility == TileVisibility.FOG_OF_WAR) {
                System.out.println("No further information is accessible for this tile");
                return;
            } else if (visibility == TileVisibility.REVEALED) {
                System.out.println("This information may be out of date:");
                // TODO
            } else if (visibility == TileVisibility.VISIBLE) {
                // TODO
            }
            // TODO
        }
    }
    

    @Override
    public void showMenu() {
        // leave empty
    }
    @Override
    public void setController() {
        // nakhoondam
    }
}
