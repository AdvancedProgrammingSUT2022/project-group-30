package views;

import java.util.ArrayList;

import controllers.GameController;
import models.Feature;
import models.GameMap;
import models.ProgramDatabase;
import models.RiverSegment;
import models.Tile;
import models.TileHistory;
import utilities.PrintableCharacters;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import menusEnumerations.GameMainPageCommands;
import models.TileVisibility;
import models.User;
import models.interfaces.TileImage;
import models.units.Unit;
import utilities.MyScanner;

public class GameView implements View {

    private static GameView gameView;
    private GameController controller = GameController.getGameController();

    private GameView() {
        scanner = MyScanner.getScanner();
        controller = GameController.getGameController();
    }

    public static GameView getGameView() {
        return gameView == null ? gameView = new GameView() : gameView;
    }

    private Scanner scanner;

    public void run() {
        askUserForPlayers();
        controller.initializeGame();

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

    public void showMap() {
        PrintableCharacters printableCharacters[][] = this.makeMapReadyToPrint();
        for (int i = 0; i < printableCharacters.length; i++) {
            for (int j = 0; j < printableCharacters[i].length; j++) {
                System.out.print(printableCharacters[i][j].getANSI_COLOR() + printableCharacters[i][j].getCharacter()
                        + PrintableCharacters.ANSI_RESET);
            }
            System.out.println();
        }
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

            // TOF:
            ArrayList<Unit> units = controller.getUnitsInTile(tile);
            for (Unit unit : units) {
                System.out.println("Unit : " + unit.getType().getName() + ", owner : " + unit.getOwner().getName());
            }
            // System.out.println("Terrain Type : " + tile.getTerrainType());

            // TODO
        }
    }

    private void colorRiverSegments(Tile tiles[][], PrintableCharacters printableCharacters[][]) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                ArrayList<RiverSegment> riverSegments = GameMap.getGameMap().findTilesRiverSegments(tiles[i][j]);
                for (int k = 0; k < riverSegments.size(); k++) {
                    String riverDirection = riverSegments.get(k).findRiverSegmentDirectionForTile(tiles[i][j]);
                    this.colorATileRiverSegment(riverDirection, printableCharacters, j, i);
                }
            }
        }
    }

    private void colorATileRiverSegment(String riverDirection, PrintableCharacters printableCharacters[][], int XIndex,
            int YIndex) {
        int tileStartingVerticalIndex = (XIndex % 2) * 3 + 6 * YIndex;
        int tileStartingHorizontalIndex = 2 + XIndex * 8;
        if (riverDirection.equals("RU")) {
            printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + 8]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 7]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 6]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("RD")) {
            printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 6]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 7]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + 8]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("LU")) {
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex - 1]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex - 2]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("LD")) {
            printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex - 2]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex - 1]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex]
                    .setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
    }

    private void colorTiles(TileImage tiles[][], PrintableCharacters printableCharacters[][]) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int tileStartingVerticalIndex = (j % 2) * 3 + 6 * i;
                int tileStartingHorizontalIndex = 2 + j * 8;
                Tile tile = null;
                if (tiles[i][j] != null) {
                    if (tiles[i][j] instanceof Tile) {
                        tile = (Tile) tiles[i][j];
                    } else if (tiles[i][j] instanceof TileHistory) {
                        tile = ((TileHistory) tiles[i][j]).getTile();
                    }
                    String color = PrintableCharacters.findTilesColor(tile);
                    for (int k = 0; k < 5; k++) {
                        printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + k + 1]
                                .setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + k + 1]
                                .setANSI_COLOR(color);
                    }
                    for (int k = 0; k < 7; k++) {
                        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + k]
                                .setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + k]
                                .setANSI_COLOR(color);
                    }
                    for (int k = 0; k < 9; k++) {
                        printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + k - 1]
                                .setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + k - 1]
                                .setANSI_COLOR(color);
                    }
                    for(int k = 0 ; k < tile.getFeatures().size(); k++){
                        String name = this.findFeatureCharacters(tile.getFeatures().get(k));
                        if(k == 0){
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex].setCharacter(name.charAt(0));
                            if(name.length() == 2){
                                printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 1].setCharacter(name.charAt(1));
                            }
                        }
                        if(k == 1){
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 3].setCharacter(name.charAt(0));
                            if(name.length() == 2){
                                printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 4].setCharacter(name.charAt(1));
                            }
                        }
                        if(k == 2){
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 6].setCharacter(name.charAt(0));
                        }
                    }
                }
            }
        }
    }

    private void drawATile(PrintableCharacters printableCharacters[][], int tileStartingVerticalIndex,
            int tileStartingHorizontalIndex, int i, int j) {
        Tile frameBase = controller.getCurrentPlayer().getFrameBase();
        int frameBaseXCoordinate = frameBase.findTileXCoordinateInMap();
        int frameBaseYCoordinate = frameBase.findTileYCoordinateInMap();

        int tileXCoordinate = frameBaseXCoordinate + j;
        int tileYCoordinate = frameBaseYCoordinate + i;
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 1]
                .setCharacter((char) (tileYCoordinate / 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 2]
                .setCharacter((char) (tileYCoordinate % 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 3].setCharacter(',');
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 4]
                .setCharacter((char) (tileXCoordinate / 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 5]
                .setCharacter((char) (tileXCoordinate % 10 + 48));

        printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex - 1].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex - 2].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex - 2].setCharacter('\\');
        printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex - 1].setCharacter('\\');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex].setCharacter('\\');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 1].setCharacter('_');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 2].setCharacter('_');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 3].setCharacter('_');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 4].setCharacter('_');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 5].setCharacter('_');
        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 6].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 7].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + 8].setCharacter('/');
        printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + 8].setCharacter('\\');
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 7].setCharacter('\\');
        printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 6].setCharacter('\\');
    }

    private PrintableCharacters[][] makeMapReadyToPrint() {
        Tile tiles[][] = this.controller.getGameDataBase().getMap().findTilesToPrint(controller.getCurrentPlayer());
        TileImage tilesImage[][] = this.controller.getGameDataBase().getMap()
                .getCivilizationsImage(controller.getCurrentPlayer());
        PrintableCharacters printableCharacters[][] = new PrintableCharacters[21][52];
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 52; j++) {
                printableCharacters[i][j] = new PrintableCharacters();
                if (i == 2 && j % 16 >= 11 && j < 48) {
                    printableCharacters[i][j].setCharacter('_');
                }
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int tileStartingVerticalIndex = (j % 2) * 3 + 6 * i;
                int tileStartingHorizontalIndex = 2 + j * 8;
                this.drawATile(printableCharacters, tileStartingVerticalIndex, tileStartingHorizontalIndex, i, j);
            }
        }
        this.colorTiles(tilesImage, printableCharacters);
        this.colorRiverSegments(tiles, printableCharacters);
        return printableCharacters;
    }

    private String findFeatureCharacters(Feature feature){
        if(feature == Feature.FLOOD_PLAINS){
            return "FL";
        }
        else if(feature == Feature.FOREST){
            return "FO";
        }
        else if(feature == Feature.ICE){
            return "I";
        }
        else if(feature == Feature.JUNGLE){
            return "J";
        }
        else if(feature == Feature.MARSH){
            return "M";
        }
        else if(feature == Feature.OASIS){
            return "O";
        }
        return null;
    }

    private void askUserForPlayers() {
        String command;
        String[] commandSegments;
        User[] players;

        while (true) {
            players = new User[5];

            command = scanner.nextLine();
            commandSegments = command.split(" ");

            if ((commandSegments[0] + commandSegments[1]).compareTo("playgame") != 0) {
                System.out.println("invalid command!");
                continue;
            }

            boolean wasCommandValid = true;
            for (int i = 2; i < commandSegments.length; i += 2) {
                Matcher matcher = Pattern.compile("((-p)|(--player))(?<number>\\d+)").matcher(commandSegments[i]);
                if (matcher.matches()) {
                    int number = Integer.parseInt(matcher.group("number")) - 1;
                    if (number < 0 || number >= players.length) {
                        System.out.println("Invalid Command!");
                        wasCommandValid = false;
                        break;
                    }
                    String username = commandSegments[i + 1];
                    User player = ProgramDatabase.getProgramDatabase().getUserByUsername(username);
                    if (player == null) {
                        System.out.println("user not found for player " + (number + 1));
                        wasCommandValid = false;
                        break;
                    }
                    players[number] = player;
                } else {
                    System.out.println("invalid command!");
                    wasCommandValid = false;
                    break;
                }
            }
            if (!wasCommandValid) {
                continue;
            }

            if (players[0] == null || players[1] == null) {
                System.out.println("Invalid players!");
                continue;
            }
            boolean shouldBeNull = false;
            wasCommandValid = true;
            for (int i = 2; i < players.length; i++) {
                if (players[i] != null) {
                    if (shouldBeNull == true) {
                        System.out.println("Invalid players!");
                        wasCommandValid = false;
                        break;
                    }
                } else {
                    shouldBeNull = true;
                }
            }

            if (wasCommandValid == false) {
                continue;
            }

            break;
        }

        controller.addPlayers(players);
    }

    @Override
    public void setController() {
        this.controller = GameController.getGameController();
        this.controller.setGameDataBase();
        this.controller.setProgramDatabase();
    }

    @Override
    public void showMenu() {
        // leave empty
    }
}
