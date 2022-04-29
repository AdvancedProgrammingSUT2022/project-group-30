package views;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

import controllers.GameController;
import menusEnumerations.*;
import models.*;
import models.buildings.BuildingType;
import models.interfaces.Producible;
import models.units.UnitState;
import models.*;
import models.buildings.Building;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.units.UnitType;
import utilities.Debugger;
import utilities.PrintableCharacters;
import utilities.Printer;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
            } else if (controller.getCurrentPlayer().getSelectedEntity() != null && controller.getCurrentPlayer().getSelectedEntity() instanceof City) {
                runCityActionsTab();
                continue;
            }

            command = scanner.nextLine().trim();

            if ((matcher = GameMainPageCommands.SHOW_MAP.getCommandMatcher(command)) != null) {
                showMap();
            } else if ((matcher = GameMainPageCommands.SHOW_INFO.getCommandMatcher(command)) != null) {
                showCivInfo();
            } else if ((matcher = GameMainPageCommands.GET_TILE_INFO.getCommandMatcher(command)) != null) {
                printTileInfo(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_UNIT.getCommandMatcher(command)) != null) {
                selectUnit(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_CIVILIAN_UNIT.getCommandMatcher(command)) != null) {
                selectCivilianUnit(matcher);
            } else if ((matcher = GameMainPageCommands.SELECT_CITY.getCommandMatcher(command)) != null) {
                selectCity(matcher);
            } else if ((matcher = GameMainPageCommands.RIGHT.getCommandMatcher(command)) != null) {
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

    private void showCivInfo() {
        Civilization civilization = controller.getCurrentPlayer();
        printer.printlnBlue(civilization.getName());
        printer.println("Science: " + civilization.getBeakerCount());
        printer.println("Gold: " + civilization.getGoldCount());
        int happiness = (int) civilization.calculateHappiness();
        printer.print("Happiness: ");
        if (happiness >= 0) {
            printer.printlnBlue(happiness);
        } else {
            printer.printlnRed(happiness);
        }
        printer.printlnPurple("Strategic Resources:");
        for (StrategicResource strategicResource : civilization.getStrategicResources().keySet()) {
            int amount = civilization.getStrategicResources().get(strategicResource);
            if (amount > 0) {
                printer.println(strategicResource.getName() + ": " + amount);
            }
        }
        printer.printlnPurple("Luxury Resources:");
        for (LuxuryResource luxuryResource : civilization.getLuxuryResources().keySet()) {
            int amount = civilization.getLuxuryResources().get(luxuryResource);
            if (amount > 0) {
                printer.println(luxuryResource.getName() + ": " + amount);
            }
        }
        waitForClick();
        showMap();
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
                waitForClick();
            } else if ((matcher = CityCommands.SHOW_INFO.getCommandMatcher(command)) != null) {
                showCityInfo();
                waitForClick();
            } else if ((matcher = CityCommands.DESELECT.getCommandMatcher(command)) != null) {
                deselectCity(city);
                break;
            } else if ((matcher = CityCommands.SHOW_CITIZEN_MANAGEMENT_PANEL.getCommandMatcher(command)) != null) {
                runCitizenManagementPanel(city);
            } else if ((matcher = CityCommands.SHOW_PRODUCTION_PANEL.getCommandMatcher(command)) != null) {
                runProductionPanel(city);
            } else {
                printer.printlnError("Invalid command for city!");
            }
        }
    }

    private void runProductionPanel(City city) {
        String command;
        Matcher matcher;

        while (true) {
            printer.printlnPurple("********************* Production Panel *********************");
            printer.println("For a list of commands, enter \"show commands\"");
            command = scanner.nextLine();
            if (command.equals("show commands")) {
                showProductionPanelCommands();
                waitForClick();
            } else if ((matcher = ProductionPanelCommands.BACK.getCommandMatcher(command)) != null) {
                break;
            } else if ((matcher = ProductionPanelCommands.SHOW_INFO.getCommandMatcher(command)) != null) {
                showProductionInfo(city);
                waitForClick();
            } else if ((matcher = ProductionPanelCommands.CHOOSE_PRODUCTION.getCommandMatcher(command)) != null) {
                chooseProduction(city);
            } else if ((matcher = ProductionPanelCommands.STOP_PRODUCTION.getCommandMatcher(command)) != null) {
                  stopProduction(city);
            } else {
                printer.printlnError("Invalid command for Production Panel");
            }
        }
    }

    private void stopProduction(City city) {
        if (city.getEntityInProduction() == null) {
            printer.printlnError("There is no ongoing production in this city");
            return;
        }
        printer.println("Are you sure you want to stop the production of " + city.getEntityInProduction().getName() + "? enter \"y\" if so.");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            city.stopProduction();
            printer.println("Stopped Production.");
        } else {
            printer.printlnError("Operation Canceled.");
            return;
        }
    }

    private void chooseProduction(City city) {
        if (city.getEntityInProduction() != null) {
            printer.printlnError("This city is already producing a " + city.getEntityInProduction().getName() + ". Its production will be halted if you choose another production");
            waitForClick();
        }
        printer.printlnPurple("Choose This City's Next Production From The Below Lists: (enter \"cancel\" to exit)");
        ArrayList<UnitType> producibleUnits = city.calculateProductionReadyUnitTypes();
        ArrayList<BuildingType> producibleBuildings = city.calculateProductionReadyBuildingTypes();

        printer.println("Units:");
        for (UnitType producibleUnit : producibleUnits) {
            int hammerCost = producibleUnit.calculateHammerCost();
            int turnsRequired = Math.max(1, hammerCost / city.calculateOutput().getProduction());
            printer.println(producibleUnit.getName() + ",\t\t\t" + hammerCost + " Hammers, " + turnsRequired + " turns");
            HashMap<StrategicResource, Integer> resources = producibleUnit.getPrerequisiteResources();
            for (StrategicResource resource : resources.keySet()) {
                printer.println("\t" + resource.getName() + ": " + resources.get(resource));
            }
        }

        printer.println("Buildings:");
        for (BuildingType producibleBuilding : producibleBuildings) {
            int hammerCost = producibleBuilding.calculateHammerCost();
            int turnsReuquired = Math.max(1, hammerCost / city.calculateOutput().getProduction());
            printer.println(producibleBuilding.getName() + ",\t\t\t" + hammerCost + " Hammers, " + turnsReuquired + " turns");
        }

        while (true) {
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("cancel") || choice.equalsIgnoreCase("back")) {
                printer.println("Production change canceled.");
                break;
            }
            ArrayList<Producible> allProducibles = new ArrayList<Producible>(producibleBuildings);
            allProducibles.addAll(producibleUnits);

            boolean isCommandValid = false;
            for (Producible producible : allProducibles) {
                if (choice.equalsIgnoreCase(producible.getName())) {
                    city.changeProduction(producible);
                    printer.printlnBlue("Set city's production to " + producible.getName());
                    isCommandValid = true;
                    break;
                }
            }
            if (isCommandValid == false) {
                printer.printlnRed("Item was not found on the list, try again.");
            } else {
                break;
            }
        }
    }

    private void showProductionInfo(City city) {
        printer.printlnPurple("########### City Production Info ###########");

        Producible currentProduction = city.getEntityInProduction();
        printer.print("Currently producing: ");
        if (currentProduction == null) {
            printer.println("Nothing!");
        } else {
            printer.println(currentProduction.getName());
            int hammerCost = currentProduction.calculateHammerCost();
            int productionOutput = city.calculateOutput().getProduction();
            int hammerCount = (int) city.getHammerCount();
            int turnsRemaining = Math.max(1, (hammerCost - hammerCount) / productionOutput);
            printer.println(hammerCount + " out of " + hammerCost);
            printer.println(turnsRemaining + " turns remaining");
        }

        printer.println("Halted Productions:");
        for (Producible producible : city.getProductionReserve().keySet()) {
            printer.println(producible.getName() + ": " + city.getProductionReserve().get(producible) + " out of " + producible.calculateHammerCost());
        }

        printer.printlnPurple("Production-Ready Units:");
        for (UnitType type : city.calculateProductionReadyUnitTypes()) {
            printer.println(type.getName());
        }

        printer.printlnPurple("Production-Ready Buildings:");
        for (BuildingType type : city.calculateProductionReadyBuildingTypes()) {
            printer.println(type.getName());
        }
    }

    private void showProductionPanelCommands() {
        printer.printlnPurple("Production Panel Commands:");
        for (ProductionPanelCommands command : ProductionPanelCommands.getAllCommands()) {
            printer.println(command.getName());
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
        if (city.isCapital()) {
            printer.printlnPurple(controller.getCurrentPlayer().getName() + "'s Capital City");
        } else {
            printer.printPurple(controller.getCurrentPlayer().getName() + "'s City");
        }
        printer.println("Y: " + city.getCentralTile().findTileYCoordinateInMap() + ", X: " + city.getCentralTile().findTileXCoordinateInMap());
        printer.println("The following tiles comprise this city's territory:");
        for (Tile tile : city.getTerritories()) {
            if (tile != city.getCentralTile()) {
                printer.print("Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap() + " ");
                if (city.isTileBeingWorked(tile)) {
                    printer.printlnBlue("(worked)");
                } else {
                    printer.printlnRed("(not worked)");
                }
            }
        }
        printer.printlnBlue("This city has " + city.getCitizens().size() + " citizens. " + city.calculateWorklessCitizenCount()
                + " of them are workless.");
        printer.println("City's food balance:");
        if (city.getFoodCount() >= 0) {
            printer.printlnBlue(city.getFoodCount());
        } else {
            printer.printlnRed(city.getFoodCount());
        }
    }

    private void deselectCity(City city) {
        city.getOwner().setSelectedEntity(null);
        printer.println("City deselected");
        showMap();
    }

    private void runCitizenManagementPanel(City city) {
        showCitizenInfo(city);
        String command;
        Matcher matcher;
        while (true) {
            printer.printlnPurple("********************* Citizen Management Panel *********************");
            printer.println("For a list of commands, enter \"show commands\"");
            command = scanner.nextLine();
            if ((matcher = CitizenManagementPanelCommands.SHOW_INFO.getCommandMatcher(command)) != null) {
                showCitizenInfo(city);
                waitForClick();
            } else if ((matcher = CitizenManagementPanelCommands.BACK.getCommandMatcher(command)) != null) {
                break;
            } else if ((matcher = CitizenManagementPanelCommands.WORK_TILE.getCommandMatcher(command)) != null) {
                workTile(matcher, city);
            } else if ((matcher = CitizenManagementPanelCommands.FREE_TILE.getCommandMatcher(command)) != null) {
                freeTile(matcher, city);
            } else if (command.matches("(show )?commands")) {
                showCitizenManagementCommands();
                waitForClick();
            } else {
                printer.printlnError("Invalid command for Citizen Management Panel!");
            }
        }
    }

    private void freeTile(Matcher matcher, City city) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnRed("Invalid coordinates!");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        if (!city.getTerritories().contains(tile)) {
            printer.printlnRed("The tile you have entered is not in this city's territory!");
            return;
        }
        if (!city.isTileBeingWorked(tile)) {
            printer.printlnRed("This tile is not being worked!");
            return;
        }
        Citizen citizen = city.getCitizenAssignedToTile(tile);
        citizen.setWorkPlace(null);
        printer.printlnBlue("Tile at Y: " + y + ", X: " + x + " freed! A citizen is out of work!");
    }

    private void workTile(Matcher matcher, City city) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnRed("Invalid coordinates!");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        if (!city.getTerritories().contains(tile)) {
            printer.printlnRed("The tile you have entered is not in this city's territory!");
            return;
        }
        if (city.isTileBeingWorked(tile)) {
            printer.printlnRed("This tile is already being worked!");
            return;
        }
        if (city.calculateWorklessCitizenCount() == 0) {
            printer.printlnRed("There are no workless citizens to assign to this tile! Free a citizen and try again.");
            return;
        }
        Citizen citizen = city.getWorklessCitizen();
        city.assignCitizenToWorkplace(tile, citizen);
        printer.printlnBlue("Citizen assigned to tile at Y: " + y + ", X: " + x);
    }

    private void showCitizenInfo(City city) {
        ArrayList<Citizen> citizens = city.getCitizens();
        printer.printlnBlue("Citizen Info:");
        printer.println(citizens.size() + " citizens. " + city.calculateWorklessCitizenCount() + " of them are workless.");
        printer.printlnPurple("Tiles being worked:");
        for (Citizen citizen : citizens) {
            if (citizen.getWorkPlace() instanceof Tile) {
                printer.println("Y: " + ((Tile) citizen.getWorkPlace()).findTileYCoordinateInMap() + " X: " +
                        ((Tile) citizen.getWorkPlace()).findTileXCoordinateInMap());
            }
        }
        printer.printlnRed("Tile not being worked:");
        for (Tile unworkedTile : city.getUnworkedTiles()) {
            printer.println("Y: " + unworkedTile.findTileYCoordinateInMap() + " X: " + unworkedTile.findTileXCoordinateInMap());
        }
    }

    private void showCitizenManagementCommands() {
        printer.printlnPurple("Citizen Management Panel Commands:");
        for (CitizenManagementPanelCommands command : CitizenManagementPanelCommands.getAllCommands()) {
            printer.println(command.getName());
        }
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
            } else if ((matcher = UnitCommands.SLEEP.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.SLEEP)) {
                sleepUnit(unit);
                break;
            } else if ((matcher = UnitCommands.ALERT.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.ALERT)) {
                alertAUnit(unit);
                break;
            } else if ((matcher = UnitCommands.FORTIFY.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.FORTIFY)) {
                fortifyAUnit(unit);
                break;
            } else if ((matcher = UnitCommands.FORTIFY_UNTIL_HEALED.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.FORTIFY_UNTIL_HEALED)) {
                fortifyAUnitUntilHealed(unit);
                break;
            } else if ((matcher = UnitCommands.GARRISON.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.GARRISON)) {
                garrisonAUnit(unit);
                break;
            } else if ((matcher = UnitCommands.AWAKE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.AWAKE)) {
                awakeAUnit(unit);
            } else if ((matcher = UnitCommands.DELETE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.DELETE)) {
                deleteAUnit(unit);
                break;
            } else {
                printer.printlnError("Invalid Unit Command!");
            }
        }
    }

    private void waitForClick() {
        printer.printlnPurple("enter anything to continue...");
        scanner.nextLine();
    }

    private HashMap<UnitCommands, Boolean> calculateAllowedCommands(Unit unit) {
        HashMap<UnitCommands, Boolean> result = new HashMap<>();
        for (UnitCommands command : UnitCommands.getAllCommands()) {
            result.put(command, false);
        }

        result.put(UnitCommands.DESELECT, true);
        result.put(UnitCommands.SHOW_INFO, true);
        result.put(UnitCommands.DELETE, true);
        if (unit.getState().waitsForCommand && controller.canUnitMove(unit)) {
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
            result.put(UnitCommands.SLEEP, true);
            result.put(UnitCommands.ALERT, true);
        } else {
            result.put(UnitCommands.AWAKE, true);
        }

        if (unit.getState().waitsForCommand && unit.getType().getCombatType().isStateAllowed(UnitState.GARRISON) &&
                controller.getCityCenteredInTile(unit.getLocation()) != null &&
                controller.getCityCenteredInTile(unit.getLocation()).getOwner() == unit.getOwner()) {
            result.put(UnitCommands.GARRISON, true);
        }

        // TODO : consider all commands

        return result;
    }

    private void deleteAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.getOwner().setGoldCount(unit.getOwner().getGoldCount() + (double) unit.getType().getCost() / (double) 10);
        GameDataBase.getGameDataBase().getUnits().remove(unit);
        printer.println("this unit is now deleted");
        showMap();
    }

    private void awakeAUnit(Unit unit) {
        unit.setState(UnitState.AWAKE);
        printer.println("this unit is now awake and ready to order");
        showMap();
    }

    private void garrisonAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.GARRISON);
        printer.println("unit state changed to garrison");
        showMap();
    }

    private void fortifyAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFY);
        printer.println("unit state changed to fortify");
        showMap();
    }

    private void fortifyAUnitUntilHealed(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.FORTIFYUNTILHEALED);
        printer.println("unit state changed to fortify until healed");
        showMap();
    }

    private void alertAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        unit.setState(UnitState.ALERT);
        printer.println("unit state changed to alert");
        showMap();
    }

    private void sleepUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
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
        controller.getCurrentPlayer().setSelectedEntity(null);
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
        printer.println("Y: " + y + ", X: " + x);

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
            if (improvement.getIsPillaged()) {
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
                if (tilesImage[i][j] != null) {
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
        int tileStartingVerticalIndex = ((XIndex + tiles[0][0].findTileXCoordinateInMap() % 2) % 2) * 3 + 6 * YIndex;
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
                int tileStartingVerticalIndex = ((j + tiles[0][0].findTileXCoordinateInMap() % 2) % 2) * 3 + 6 * i;
                int tileStartingHorizontalIndex = 2 + j * 8;
                Tile tile = null;
                if (tilesImage[i][j] != null) {
                    ArrayList<Unit> units = new ArrayList<>();
                    if (tilesImage[i][j] instanceof Tile) {
                        tile = (Tile) tilesImage[i][j];
                        units = this.controller.getUnitsInTile(tile);
                    } else if (tilesImage[i][j] instanceof TileHistory) {
                        tile = ((TileHistory) tilesImage[i][j]).getTile();
                        units = ((TileHistory) tilesImage[i][j]).getUnits();
                        printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 1].setCharacter('R');
                    }
                    if (units.size() > 2) {
                        Debugger.debug("there are more than one units in this tile!");
                    } else {
                        for (int k = 0; k < units.size(); k++) {
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
                    this.printCitiesAndTheirTerritoriesInMap(tile, tileStartingVerticalIndex, tileStartingHorizontalIndex, printableCharacters);
                }
            }
        }
    }

    private void printCitiesAndTheirTerritoriesInMap(Tile tile, int tileStartingVerticalIndex, int tileStartingHorizontalIndex, PrintableCharacters[][] printableCharacters) {
        if (tile.getCityOfTile() != null) {
            if (tile.getCityOfTile().getCentralTile().equals(tile)) {
                printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 3].setCharacter('C');
                printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + 3].setCharacter(tile.getCivilization().getName().charAt(0));
                printableCharacters[tileStartingVerticalIndex + 2][tileStartingHorizontalIndex + 4].setCharacter(tile.getCivilization().getName().charAt(1));
            } else {
                printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex + 3].setCharacter('T');
            }
        }

    }

    private char findUnitPrintableCharacter(Unit unit) {
        if (unit.getType() == UnitType.WORKER) {
            return 'W';
        }
        if (unit.getType() == UnitType.SETTLER) {
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
                if (i == 2 && j < 48) {
                    if ((tiles[0][0].findTileXCoordinateInMap() % 2 == 0 && j % 16 >= 11) || (tiles[0][0].findTileXCoordinateInMap() % 2 == 1 && j % 16 >= 3 && j % 16 <= 7)) {
                        printableCharacters[i][j].setCharacter('_');
                    }
                }
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int tileStartingVerticalIndex = ((j + tiles[0][0].findTileXCoordinateInMap() % 2) % 2) * 3 + 6 * i;
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
