package views;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.GameController;
import menusEnumerations.CityCommands;
import models.City;
import models.Civilization;
import models.Feature;
import models.GameMap;
import models.ProgramDatabase;
import models.RiverSegment;
import models.Tile;
import models.TileHistory;
import models.TileVisibility;
import models.units.UnitState;
import models.units.UnitType;
import utilities.Debugger;
import utilities.PrintableCharacters;
import utilities.Printer;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import menusEnumerations.GameMainPageCommands;
import menusEnumerations.UnitCommands;
import models.User;
import models.improvements.Improvement;
import models.interfaces.TileImage;
import models.resources.Resource;
import models.units.Unit;
import utilities.MyScanner;

public class GameView implements View {

    private static GameView gameView;
    private GameController controller = GameController.getGameController();
    private Printer printer;

    private GameView() {
        scanner = MyScanner.getScanner();
        controller = GameController.getGameController();
        printer = Printer.getPrinter();
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
            if (controller.getCurrentPlayer().getSelectedEntity() != null && controller.getCurrentPlayer().getSelectedEntity() instanceof Unit) {
                runUnitActionsTab();
                continue;
            } else if (controller.getCurrentPlayer().getSelectedEntity() != null && controller.getCurrentPlayer().getSelectedEntity() instanceof  City) {
                runCityActionsTab();
                continue;
            }

            command = scanner.nextLine().trim();

            if ((matcher = GameMainPageCommands.SHOW_MAP.getCommandMatcher(command)) != null) {
                showMap();
            } else if ((matcher = GameMainPageCommands.GET_TILE_INFO.getCommandMatcher(command)) != null) {
                printTileInfo(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_UNIT.getCommandMatcher(command)) != null) {
                selectUnit(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_CIVILIAN_UNIT.getCommandMatcher(command)) != null) {
                selectCivilianUnit(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_CITY.getCommandMatcher(command)) != null) {
                selectCity(matcher);
            }else if ((matcher = GameMainPageCommands.RIGHT.getCommandMatcher(command)) != null) {
                goRight(matcher);
            } else if ((matcher = GameMainPageCommands.LEFT.getCommandMatcher(command)) != null) {
                goLeft(matcher);
            } else if ((matcher = GameMainPageCommands.UP.getCommandMatcher(command)) != null) {
                goUp(matcher);
            } else if ((matcher = GameMainPageCommands.DOWN.getCommandMatcher(command)) != null) {
                goDown(matcher);
            } else if ((matcher = GameMainPageCommands.MOVE_FRAME_TO.getCommandMatcher(command)) != null) {
                moveFrameTo(matcher);
            } else if ((matcher = GameMainPageCommands.GO_TO_NEXT_TURN.getCommandMatcher(command)) != null) {
                passTurn();
            } else if ((matcher = GameMainPageCommands.SHOW_UNITS.getCommandMatcher(command)) != null) {
                showUnits();
            } else if ((matcher = GameMainPageCommands.MAKE_VISIBLE.getCommandMatcher(command)) != null) {
                controller.makeEverythingVisible();
                showMap();
            } else {
                printer.printlnError("Invalid Command!");
            }
        }
    }

    private void runCityActionsTab() {
        City city = (City) controller.getCurrentPlayer().getSelectedEntity();
        String command;
        Matcher matcher;
        while (true) {
            printer.printlnRed("*****************************************");
            printer.println("City Actions Menu");
            printer.println("enter \"show commands\" to see all commands");

            command = scanner.nextLine();
            if ((matcher = CityCommands.SHOW_COMMANDS.getCommandMatcher(command)) != null) {
                showCityCommands();
            } else if ((matcher = CityCommands.SHOW_INFO.getCommandMatcher(command)) != null) {
                showCityInfo();
            } else if ((matcher = CityCommands.DESELECT.getCommandMatcher(command)) != null) {
                  deselectCity(city);
                  break;
            } else {
                printer.printlnError("Invalid command for city!");
            }
        }
    }

    private void showCityCommands() {
        printer.printlnPurple("City Actions:");
        for (CityCommands command : CityCommands.getAllCommands()) {
            printer.println(" -" + command.getName());
        }
    }

    private void showCityInfo() {
        // TODO
        City city = (City) controller.getCurrentPlayer().getSelectedEntity();
        printer.printlnPurple(controller.getCurrentPlayer().getName() + "'s City");
        printer.println("Y: " + city.getCentralTile().findTileYCoordinateInMap() + ", X: " + city.getCentralTile().findTileXCoordinateInMap());
        printer.println("The following tiles comprise this city's territory:");
        for (Tile tile : city.getTerritories()) {
            if (tile != city.getCentralTile()) {
                printer.print("Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap());
                if (city.isTileBeingWorked(tile)) {
                    printer.printlnBlue(" (worked)");
                } else {
                    printer.printlnRed(" (not worked)");
                }
            }
        }
    }

    private void deselectCity(City city) {
        city.getOwner().setSelectedEntity(null);
        printer.println("City deselected");
        showMap();
    }

    private void passTurn() {
        ArrayList<Unit> idleUnits = controller.getCurrentPlayersUnitsWaitingForCommand();
        if (idleUnits.isEmpty() == false) {
            printer.printlnError("Some units are waiting for a command!");
            controller.getCurrentPlayer().setSelectedEntity(idleUnits.get(0));
            return;
        }
        // TODO : if a city can start a new production, make the player choose it!

        controller.goToNextPlayer();
        showMap();
    }

    private void runUnitActionsTab() {
        Unit unit = (Unit) controller.getCurrentPlayer().getSelectedEntity();
        String command;
        Matcher matcher;

        while (true) {
            HashMap<UnitCommands, Boolean> allowedCommands = calculateAllowedCommands(unit);

            printer.printlnRed("***********************************");
            printer.println("Unit Actions for " + controller.getCurrentPlayer().getName() + "'s " + unit.getType().getName() + ":");
            printer.printlnBlue("Allowed commands:");
            for (UnitCommands commandType : allowedCommands.keySet()) {
                if (allowedCommands.get(commandType)) {
                    printer.println(" -" + commandType.getName());
                }
            }

            command = scanner.nextLine().trim();
            if ((matcher = UnitCommands.DESELECT.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.DESELECT)) {
                deselectUnit(unit);
                break;
            } else if ((matcher = UnitCommands.MOVE_TO.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.MOVE_TO)) {
                moveTo(matcher);
                if (unit.getOwner().getSelectedEntity() == null) {
                    break;
                }
            } else if ((matcher = UnitCommands.SHOW_INFO.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.SHOW_INFO)) {
                showUnitInfo(unit);
            } else if ((matcher = UnitCommands.FOUND_CITY.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.FOUND_CITY)) {
                if (foundCity(unit)) {
                    showMap();
                    break;
                }
            }
            else if((matcher = UnitCommands.SLEEP.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.SLEEP)){
                sleepUnit(unit);
                break;
            }
            else if((matcher = UnitCommands.ALERT.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.ALERT)){
                alertAUnit(unit);
                break;
            }
            else if((matcher = UnitCommands.FORTIFY.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.FORTIFY)){
                fortifyAUnit(unit);
                break;
            }
            else if((matcher = UnitCommands.FORTIFY_UNTIL_HEALED.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.FORTIFY_UNTIL_HEALED)){
                fortifyAUnitUntilHealed(unit);
                break;
            }
            else if((matcher = UnitCommands.GARRISON.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.GARRISON)){
                garrisonAUnit(unit);
                break;
            }
            else if((matcher = UnitCommands.AWAKE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.AWAKE)){
                awakeAUnit(unit);
            }
            else {
                printer.printlnError("Invalid Unit Command!");
            }
        }
    }

    private HashMap<UnitCommands, Boolean> calculateAllowedCommands(Unit unit) {
        HashMap<UnitCommands, Boolean> result = new HashMap<>();
        for (UnitCommands command : UnitCommands.getAllCommands()) {
            result.put(command, false);
        }

        result.put(UnitCommands.DESELECT, true);
        result.put(UnitCommands.SHOW_INFO, true);
        if (unit.getState().waitsForCommand  && controller.canUnitMove(unit)) {
            result.put(UnitCommands.MOVE_TO, true);
        }

        if (unit.getState().waitsForCommand  && unit.getType() == UnitType.SETTLER && unit.getMovePointsLeft() > 0) {
            result.put(UnitCommands.FOUND_CITY, true);
        }

        if(unit.getState() == UnitState.AWAKE){
            result.put(UnitCommands.SLEEP, true);
            result.put(UnitCommands.ALERT, true);
            result.put(UnitCommands.FORTIFY, true);
            result.put(UnitCommands.FORTIFY_UNTIL_HEALED, true);
        }
        else{
            result.put(UnitCommands.AWAKE, true);
        }

        if(unit.getState().waitsForCommand && unit.isUnitInItsCivilizationCities()){
            result.put(UnitCommands.GARRISON, true);
        }

        // TODO : consider all commands

        return result;
    }

    private void awakeAUnit(Unit unit){
        unit.setState(UnitState.AWAKE);
        printer.println("this unit is now awake and ready to order");
        showMap();
    }

    private void garrisonAUnit(Unit unit){
        unit.setState(UnitState.GARRISON);
        printer.println("unit state changed to garrison");
        showMap();
    }

    private void fortifyAUnit(Unit unit){
        unit.setState(UnitState.FORTIFY);
        printer.println("unit state changed to fortify");
        showMap();
    }

    private void fortifyAUnitUntilHealed(Unit unit){
        unit.setState(UnitState.FORTIFYUNTILHEALED);
        printer.println("unit state changed to fortify until healed");
        showMap();
    }

    private void alertAUnit(Unit unit){
        unit.setState(UnitState.ALERT);
        printer.println("unit state changed to alert");
        showMap();
    }

    private void sleepUnit(Unit unit){
        unit.setState(UnitState.ASLEEP);
        printer.println("unit state changed to asleep");
        showMap();
    }

    private boolean foundCity(Unit unit) {
        if (unit.getMovePointsLeft() == 0) {
            printer.printlnError("Settler has no movepoints!");
            return false;
        }
        if (controller.isTileTooNearCity(unit.getLocation())) {
            printer.printlnError("You can't found a city within a 3-tile distance of another city");
            return false;
        }
        controller.foundCityWithSettler(unit);
        return true;
    }

    private void deselectUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        printer.println("unit deselected");
        showMap();
    }

    private void showUnitInfo(Unit unit) {
        printer.printlnBlue(unit.getOwner().getName() + "'s " + unit.getType().getName());
        printer.println("State: " + unit.getState());
        printer.println("Y: " + unit.getLocation().findTileYCoordinateInMap() + ", X: " + unit.getLocation().findTileXCoordinateInMap());
        printer.println("Move Points: " + unit.getMovePointsLeft() + " out of " + unit.getType().getMovementSpeed());
        printer.println("Hit Points: " + unit.getHitPointsLeft() + " out of " + unit.getType().getHitPoints());
        if (unit.getPath() != null) {
            printer.printlnBlue("Path:");
            for (Tile tile : unit.getPath()) {
                printer.println("Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap());
            }
        }
        // TODO : show state and xp
    }

    private void moveTo(Matcher matcher) {  // you can only move to visible tiles!
        int destY = Integer.parseInt(matcher.group("y"));
        int destX = Integer.parseInt(matcher.group("x"));
        if (controller.areCoordinatesValid(destX, destY) == false) {
            printer.printlnError("Destination is invalid!");
            return;
        }

        Unit unit = (Unit) controller.getCurrentPlayer().getSelectedEntity();
        Tile destination = controller.getTileByCoordinates(destX, destY);

        if (controller.getTileVisibilityForPlayer(destination) != TileVisibility.VISIBLE) {
            printer.printlnError("You can only choose visible tiles as destination!");
            return;
        }

        if (controller.isTileImpassable(destination)) {
            printer.printlnError("The destination you have entered is impassable!");
            return;
        }

        ArrayList<Tile> path = controller.findPath(unit, unit.getLocation(), destination);
        if (path == null) {
            printer.printlnError("No path was found to the destination you have entered");
            return;
        }

        // SET UNITS PATH
        unit.setPath(path);
        // MOVE AND UPDATE FOG OF WAR
        controller.moveUnitAlongItsPath(unit);
        if (unit.getMovePointsLeft() == 0) {
            deselectUnit(unit);
        }
    }

    private void showUnits() {
        printer.printlnBlue(controller.getCurrentPlayer().getName() + "'s Units:");
        ArrayList<Unit> units = controller.getCurrentPlayer().getUnits();
        for (Unit unit : units) {
            printer.println(unit.getType().getName() + " at Y: " + unit.getLocation().findTileYCoordinateInMap() +
                    ", X: " + unit.getLocation().findTileXCoordinateInMap());
        }
    }

    private void selectCity(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (controller.areCoordinatesValid(x, y) == false) {
            printer.printlnError("Invalid coordinates");
            return;
        }

        Tile tile = controller.getTileByCoordinates(x, y);
        Civilization civilization = controller.getCurrentPlayer();
        City city = controller.getCivsCityInTile(tile, civilization);
        if (city == null) {
            printer.printlnError("You don't have a city in this tile!");
            return;
        }
        civilization.setSelectedEntity(city);
        printer.println(civilization.getName() + "'s city was selected");
    }

    private void selectUnit(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (controller.areCoordinatesValid(x, y) == false) {
            printer.printlnError("Invalid coordinates");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        Civilization civilization = controller.getCurrentPlayer();
        Unit unit = controller.getCivsUnitInTile(tile, civilization);
        if (unit == null) {
            printer.printlnError("You don't have a unit in this tile!");
            return;
        }
        civilization.setSelectedEntity(unit);
        printer.println(civilization.getName() + "'s " + unit.getType().getName() + " was selected");
    }

    private void selectCivilianUnit(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (controller.areCoordinatesValid(x, y) == false) {
            printer.printlnError("Invalid coordinates");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        Civilization civilization = controller.getCurrentPlayer();
        Unit unit = controller.getCivsCivilianUnitInTile(tile, civilization);
        if (unit == null) {
            printer.printlnError("You don't have a civilian unit in this tile!");
            return;
        }
        civilization.setSelectedEntity(unit);
        printer.println(civilization.getName() + "'s " + unit.getType().getName() + " was selected");
    }

    private void printTileInfo(Matcher matcher) {
        // TODO : show city territory for revealed tiles, show works
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        printer.println("printing info for tile : " + x + ", " + y);
        if (controller.areCoordinatesValid(x, y) == false) {
            printer.printlnError("Invalid coordinates");
            return;
        }

        TileImage image = controller.getCurrentPlayer().getTileImage(controller.getTileByCoordinates(x, y));
        printer.println("x : " + x + ", y : " + y);

        if (image == null) {
            printer.printlnRed("This tile is not visible!");
            return;
        }

        Tile tile = null;
        ArrayList<Unit> units = new ArrayList<>();
        City cityCentral = null;    // the city whose center is this tile
        City city = null;   // the city whose territory this tile is in but is not the center of
        if (image instanceof Tile) {
            tile = (Tile) image;
            units = controller.getUnitsInTile(tile);
            cityCentral = controller.getCityCenteredInTile(tile);
            city = controller.whoseTerritoryIsTileIn(tile);
        } else if (image instanceof TileHistory) {
            printer.printlnBlue("This tile is only revealed, the information contained may be out of date");
            TileHistory history = (TileHistory) image;

            tile = history.getTile();
            units = history.getUnits();
            cityCentral = history.getCity();
            city = cityCentral;
        }

        printer.printBlue("Terrain Type:");
        printer.println(" " + tile.getTerrainType().getName());

        printer.printlnBlue("Features:");
        if (tile.getFeatures().isEmpty()) {
            printer.println("no features!");
        }
        for (Feature feature : tile.getFeatures()) {
            printer.println(feature.getName());
        }

        if (cityCentral != null) {
            printer.printlnPurple(cityCentral.getOwner().getName() + "'s city is located here");
        } else if (city != null) {
            printer.println();
            printer.printlnGreen("This tile is located in the territory of a city owned by " + city.getOwner().getName());
        }

        printer.printlnBlue("Units:");
        for (Unit unit : units) {
            printer.println(unit.getOwner().getName() + "'s " + unit.getType().getName());
        }

        printer.printlnBlue("Resources:");
        HashMap<Resource, Integer> resources = tile.getResources();
        for (Resource resource : resources.keySet()) {
            if (resources.get(resource) > 0 && resource.isDiscoverable(controller.getCurrentPlayer())) {
                printer.println(resource.getName());
            }
        }

        printer.printlnBlue("Imrovements");
        for (Improvement improvement : tile.getImprovements()) {
            printer.print(improvement.getType().getName());
            if (improvement.isPillaged()) {
                printer.printRed(" (pillaged)");
            }
            printer.println();
        }
    }

    public void showMap() {
        printer.printlnPurple(controller.getCurrentPlayer().getName() + "'s Turn!");
        PrintableCharacters printableCharacters[][] = this.makeMapReadyToPrint();
        for (int i = 0; i < printableCharacters.length; i++) {
            for (int j = 0; j < printableCharacters[i].length; j++) {
                System.out.print(printableCharacters[i][j].getANSI_COLOR() + printableCharacters[i][j].getCharacter() + PrintableCharacters.ANSI_RESET);
            }
            printer.println();
        }
    }


    private void moveFrameTo(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (controller.areCoordinatesValid(x, y) == false || x > controller.getMapWidth() - 6 || y > controller.getMapHeight() - 3) {
            printer.printlnError("Invalid destination for frame!");
            return;
        }
        Tile dest = controller.getTileByCoordinates(x, y);
        controller.getCurrentPlayer().setFrameBase(dest);
        showMap();
    }

    private void goRight(Matcher matcher) {
        int count;
        if (matcher.group("count") == null) {
            count = 1;
        } else {
            count = Integer.parseInt(matcher.group("count").trim());
        }

        int currentX = controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
        int currentY = controller.getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
        System.out.println("width " + controller.getMapWidth());
        int destX = Math.min(currentX + count, (controller.getMapWidth() - 6));

        controller.getCurrentPlayer().setFrameBase(controller.getTileByCoordinates(destX, currentY));
        showMap();
    }

    private void goLeft(Matcher matcher) {
        int count;
        if (matcher.group("count") == null) {
            count = 1;
        } else {
            count = Integer.parseInt(matcher.group("count").trim());
        }

        int currentX = controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
        int currentY = controller.getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
        int destX = Math.max(currentX - count, 0);

        controller.getCurrentPlayer().setFrameBase(controller.getTileByCoordinates(destX, currentY));
        showMap();
    }

    private void goUp(Matcher matcher) {
        int count;
        if (matcher.group("count") == null) {
            count = 1;
        } else {
            count = Integer.parseInt(matcher.group("count").trim());
        }

        int currentX = controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
        int currentY = controller.getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
        int destY = Math.max(currentY - count, 0);

        controller.getCurrentPlayer().setFrameBase(controller.getTileByCoordinates(currentX, destY));
        showMap();
    }

    private void goDown(Matcher matcher) {
        int count;
        if (matcher.group("count") == null) {
            count = 1;
        } else {
            count = Integer.parseInt(matcher.group("count").trim());
        }

        int currentX = controller.getCurrentPlayer().getFrameBase().findTileXCoordinateInMap();
        int currentY = controller.getCurrentPlayer().getFrameBase().findTileYCoordinateInMap();
        int destY = Math.min(currentY + count, controller.getMapHeight() - 3);

        controller.getCurrentPlayer().setFrameBase(controller.getTileByCoordinates(currentX, destY));
        showMap();
    }

    private void colorRiverSegments(Tile tiles[][], PrintableCharacters printableCharacters[][]) {
        TileImage tilesImage[][] = this.controller.getGameDataBase().getMap()
                .getCivilizationsImage(controller.getCurrentPlayer());
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if(tilesImage[i][j] != null) {
                    ArrayList<RiverSegment> riverSegments = GameMap.getGameMap().findTilesRiverSegments(tiles[i][j]);
                    for (int k = 0; k < riverSegments.size(); k++) {
                        String riverDirection = riverSegments.get(k).findRiverSegmentDirectionForTile(tiles[i][j]);
                        this.colorATileRiverSegment(riverDirection, printableCharacters, j, i, tiles);
                    }
                }
            }
        }
    }

    private void colorATileRiverSegment(String riverDirection, PrintableCharacters printableCharacters[][], int XIndex,
                                        int YIndex, Tile[][] tiles) {
        int tileStartingVerticalIndex = ((XIndex+tiles[0][0].findTileXCoordinateInMap()%2) % 2) * 3 + 6 * YIndex;
        int tileStartingHorizontalIndex = 2 + XIndex * 8;
        if (riverDirection.equals("RU")) {
            printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + 8].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 7].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 6].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("RD")) {
            printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + 6].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 7].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + 8].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("LU")) {
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex - 1].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex - 2].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        } else if (riverDirection.equals("LD")) {
            printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex - 2].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex - 1].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
    }

    private void colorTiles(TileImage tilesImage[][], PrintableCharacters printableCharacters[][], Tile[][] tiles) {
        for (int i = 0; i < tilesImage.length; i++) {
            for (int j = 0; j < tilesImage[i].length; j++) {
                int tileStartingVerticalIndex = ((j +tiles[0][0].findTileXCoordinateInMap() % 2)  % 2) * 3 + 6 * i;
                int tileStartingHorizontalIndex = 2 + j * 8;
                Tile tile = null;
                if (tilesImage[i][j] != null) {
                    ArrayList<Unit> units = new ArrayList<>();
                    if (tilesImage[i][j] instanceof Tile) {
                        tile = (Tile) tilesImage[i][j];
                        units  = this.controller.getUnitsInTile(tile);
                    } else if (tilesImage[i][j] instanceof TileHistory) {
                        tile = ((TileHistory) tilesImage[i][j]).getTile();
                        units = ((TileHistory) tilesImage[i][j]).getUnits();
                    }
                    if(units.size() > 2){
                        Debugger.debug("there are more than one units in this tile!");
                    }
                    else{
                        for(int k = 0 ; k < units.size(); k++){
                            printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + 2 * k + 2].setCharacter(this.findUnitPrintableCharacter(units.get(k)));
                        }
                    }
                    String color = PrintableCharacters.findTilesColor(tile);
                    for (int k = 0; k < 5; k++) {
                        printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + k + 1].setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 5][tileStartingHorizontalIndex + k + 1].setANSI_COLOR(color);
                    }
                    for (int k = 0; k < 7; k++) {
                        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + k].setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + k].setANSI_COLOR(color);
                    }
                    for (int k = 0; k < 9; k++) {
                        printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + k - 1].setANSI_COLOR(color);
                        printableCharacters[tileStartingVerticalIndex + 3][tileStartingHorizontalIndex + k - 1].setANSI_COLOR(color);
                    }
                    for (int k = 0; k < tile.getFeatures().size(); k++) {
                        String name = this.findFeatureCharacters(tile.getFeatures().get(k));
                        if (k == 0) {
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex].setCharacter(name.charAt(0));
                            if (name.length() == 2) {
                                printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 1].setCharacter(name.charAt(1));
                            }
                        }
                        if (k == 1) {
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 3].setCharacter(name.charAt(0));
                            if (name.length() == 2) {
                                printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 4].setCharacter(name.charAt(1));
                            }
                        }
                        if (k == 2) {
                            printableCharacters[tileStartingVerticalIndex + 4][tileStartingHorizontalIndex + 6].setCharacter(name.charAt(0));
                        }
                    }
                }
            }
        }
    }

    private char findUnitPrintableCharacter(Unit unit){
        if(unit.getType() == UnitType.WORKER){
            return 'W';
        }
        if(unit.getType() == UnitType.SETTLER){
            return 'S';
        }
        return 'U';
    }


    private String findFeatureCharacters(Feature feature) {
        if (feature == Feature.FLOOD_PLAINS) {
            return "FL";
        } else if (feature == Feature.FOREST) {
            return "FO";
        } else if (feature == Feature.ICE) {
            return "I";
        } else if (feature == Feature.JUNGLE) {
            return "J";
        } else if (feature == Feature.MARSH) {
            return "M";
        } else if (feature == Feature.OASIS) {
            return "O";
        }
        return null;
    }


    private void drawATile(PrintableCharacters printableCharacters[][], int tileStartingVerticalIndex, int tileStartingHorizontalIndex, int i, int j) {
        Tile frameBase = controller.getCurrentPlayer().getFrameBase();
        int frameBaseXCoordinate = frameBase.findTileXCoordinateInMap();
        int frameBaseYCoordinate = frameBase.findTileYCoordinateInMap();

        int tileXCoordinate = frameBaseXCoordinate + j;
        int tileYCoordinate = frameBaseYCoordinate + i;
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 1].setCharacter((char) (tileYCoordinate / 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 2].setCharacter((char) (tileYCoordinate % 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 3].setCharacter(',');
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 4].setCharacter((char) (tileXCoordinate / 10 + 48));
        printableCharacters[tileStartingVerticalIndex + 1][tileStartingHorizontalIndex + 5].setCharacter((char) (tileXCoordinate % 10 + 48));

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
        TileImage tilesImage[][] = this.controller.getGameDataBase().getMap().getCivilizationsImage(controller.getCurrentPlayer());
        PrintableCharacters printableCharacters[][] = new PrintableCharacters[21][52];
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 52; j++) {
                printableCharacters[i][j] = new PrintableCharacters();
                if (i == 2 &&  j < 48) {
                    if((tiles[0][0].findTileXCoordinateInMap() % 2 == 0 && j%16 >= 11) || (tiles[0][0].findTileXCoordinateInMap() % 2 == 1 && j%16 >= 3 && j%16 <= 7)) {
                        printableCharacters[i][j].setCharacter('_');
                    }
                }
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int tileStartingVerticalIndex = ((j +tiles[0][0].findTileXCoordinateInMap() % 2)  % 2) * 3 + 6 * i;
                int tileStartingHorizontalIndex = 2 + j * 8;
                this.drawATile(printableCharacters, tileStartingVerticalIndex, tileStartingHorizontalIndex, i, j);
            }
        }
        this.colorTiles(tilesImage, printableCharacters, tiles);
        this.colorRiverSegments(tiles, printableCharacters);
        return printableCharacters;
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
