package views;

import java.util.Scanner;
import java.util.regex.Matcher;

import controllers.GameController;
import menusEnumerations.GameMainPageCommands;
import models.Tile;
import models.TileVisibility;
import utilities.MyScanner;

public class GameView implements View {

    private static GameView gameView;

    private GameView() {
        scanner = MyScanner.getScanner();
        controller = GameController.getGameController();
    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    private GameController controller;
    private Scanner scanner;

    public void run() {
        controller.startGame();
        showMap();
        
        String command;
        Matcher matcher;
        while (true) {
            command = scanner.nextLine().trim();
            if ((matcher = GameMainPageCommands.GET_TILE_INFO.getCommandMatcher(command)) != null) {
                printTileInfo(matcher);
            } else {
                System.out.println("Invalid Command!");
            }
        }
    }

    private void showMap() {
        // TODO : mahyar 
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
            System.out.println("x : " + x + ", y : " + y);
            TileVisibility visibility = controller.getTileVisibilityForPlayer(tile);
            System.out.println("Visibility : " + visibility.getName());

            if (visibility == TileVisibility.FOG_OF_WAR) {
                return;
            }

            System.out.println("Terrain Type : " + tile.getTerrainType());
            
            
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
