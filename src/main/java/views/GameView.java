package views;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.CombatController;
import controllers.GameController;
import menusEnumerations.*;
import models.*;
import models.buildings.Building;
import models.buildings.BuildingType;
import models.improvements.ImprovementType;
import models.interfaces.Producible;
import models.interfaces.combative;
import models.technology.Technology;
import models.units.CombatType;
import models.units.UnitState;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.units.UnitType;
import models.works.BuildImprovement;
import models.works.ClearFeature;
import models.works.ClearRoutes;
import models.works.FixPillage;
import models.works.BuildImprovementAndRemoveFeature;
import models.works.Work;
import utilities.Debugger;
import utilities.PrintableCharacters;
import utilities.Printer;

import java.util.Locale;
import java.util.Map;
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
            this.printNewNotifications();

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
                makeEverythingVisible();
                showMap();
            } else if ((matcher = GameMainPageCommands.RESEARCH_TAB.getCommandMatcher(command)) != null) {
                runResearchTab();
                showMap();
            } else if ((matcher = GameMainPageCommands.ADD_GOLD.getCommandMatcher(command)) != null) {
                addGold();
            } else if ((matcher = GameMainPageCommands.DISABLE_TURN_BREAK.getCommandMatcher(command)) != null) {
                disableTurnBreak();
            } else if ((matcher = GameMainPageCommands.ADD_STRATEGIC_RESOURCE.getCommandMatcher(command)) != null) {
                addStrategicResource(matcher);
            } else if ((matcher = GameMainPageCommands.ADD_LUXURY_RESOURCE.getCommandMatcher(command)) != null) {
                addLuxuryResource(matcher);
            } else if ((matcher = GameMainPageCommands.ADD_UNIT.getCommandMatcher(command)) != null) {
                addUnit(matcher);
            } else if ((matcher = GameMainPageCommands.KILL_UNIT.getCommandMatcher(command)) != null) {
                killUnit(matcher);
            } else if ((matcher = GameMainPageCommands.MAKE_IMPROVEMENT.getCommandMatcher(command)) != null) {
                makeImprovement(matcher);
            } else if ((matcher = GameMainPageCommands.DEPLOY_FEATURE.getCommandMatcher(command)) != null) {
                deployFeature(matcher);
            } else if ((matcher = GameMainPageCommands.CLEAR_ALL_FEATURES.getCommandMatcher(command)) != null) {
                clearAllFeatures(matcher);
            } else if ((matcher = GameMainPageCommands.UNITS_INFO.getCommandMatcher(command)) != null) {
                showUnitsInfo();
                showMap();
            } else if ((matcher = GameMainPageCommands.CITIES_INFO.getCommandMatcher(command)) != null) {
                showCitiesInfo();
                showMap();
            } else if ((matcher = GameMainPageCommands.NOTIFICATION_HISTORY.getCommandMatcher(command)) != null) {
                showAllNotification();
                showMap();
            } else if ((matcher = GameMainPageCommands.MILITARY_OVERVIEW.getCommandMatcher(command)) != null) {
                militaryOverviewPanel();
                showMap();
            } else if ((matcher = GameMainPageCommands.ECONOMIC_OVERVIEW.getCommandMatcher(command)) != null) {
                economicOverviewPanel();
                showMap();
            } else if ((matcher = GameMainPageCommands.DEMOGRAPHIC_PANEL.getCommandMatcher(command)) != null) {
                runDemographicPanel();
                showMap();
            } else {
                printer.printlnError("Invalid Command!");
            }
        }
    }

    private void makeEverythingVisible() {
        controller.makeEverythingVisible();
        printer.println("And then there was light!");
    }

    private void clearAllFeatures(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        tile.removeAllFeaturesAndApplyChanges();
        printer.printlnRed("All features were removed from this tile!");
    }

    private void deployFeature(Matcher matcher) {
        String name = matcher.group("name");
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        Feature chosenFeature = Feature.getFeatureByName(name);
        if (chosenFeature == null) {
            printer.printlnError("Not a valid feature!");
            return;
        }
        tile.addFeatureAndApplyChanges(chosenFeature);
        printer.println("added feature " + chosenFeature.getName() + " to this tile!");
    }

    private void makeImprovement(Matcher matcher) {
        String name = matcher.group("name");
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        ImprovementType chosenType = ImprovementType.getImprovementTypeByName(name);
        if (chosenType == null) {
            printer.printlnError("Not a valid improvement type!");
            return;
        }
        if (tile.containsImprovment(chosenType)) {
            printer.printlnError("This tile already contains an improvement of this type!");
            return;
        }
        if (chosenType != ImprovementType.ROAD && chosenType != ImprovementType.RAILROAD &&
                (tile.getCityOfTile() == null || tile.getCityOfTile().getOwner() != controller.getCurrentPlayer())) {
            printer.printlnError("You can only construct improvements in your own cities!");
            return;
        }
        controller.addImprovementToTile(tile, chosenType);
        printer.println("I would like a snowman");
    }

    private void killUnit(Matcher matcher) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("You missed! The coordinates are invalid");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        ArrayList<Unit> units = controller.getUnitsInTile(tile);
        if (units.size() == 0) {
            printer.printlnError("You missed! This is an empty tile!");
            return;
        }
        for (Unit unit : units) {
            controller.removeUnit(unit);
        }
        printer.println("Congratulations! You're a heartless murderer");
    }

    private void addUnit(Matcher matcher) {
        String name = matcher.group("name");
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        UnitType selectedType = UnitType.getUnitTypeByName(name);
        if (selectedType == null) {
            printer.printlnError("Unit not recognized");
            return;
        }
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates");
            return;
        }
        Tile tile = controller.getTileByCoordinates(x, y);
        if (!controller.canUnitTeleportToTile(selectedType, controller.getCurrentPlayer(), tile)) {
            printer.printlnError("Can't put a unit there!");
            return;
        }
        controller.createUnit(selectedType, controller.getCurrentPlayer(), tile);
        printer.println("Your units have been dispatched");
    }

    private void addStrategicResource(Matcher matcher) {
        String name = matcher.group("name");
        StrategicResource chosenResource = StrategicResource.getStrategicResourceByName(name);
        if (chosenResource == null) {
            printer.printlnError("Resource not recognized!");
            return;
        }
        controller.getCurrentPlayer().addStrategicResource(chosenResource, 5);
        printer.println("You just purchased some " + chosenResource.getName() + " on dark web. I'd look over my shoulder for a while...");
    }

    private void addLuxuryResource(Matcher matcher) {
        String name = matcher.group("name");
        LuxuryResource chosenResource = LuxuryResource.getLuxuryResourceByName(name);
        if (chosenResource == null) {
            printer.printlnError("Resource not recognized!");
            return;
        }
        controller.getCurrentPlayer().addLuxuryResource(chosenResource, 5);
        printer.println("You just purchased some " + chosenResource.getName() + " on dark web. I'd look over my shoulder for a while...");
    }

    private void disableTurnBreak() {
        controller.disableTurnBreak();
        printer.println("ee vaghte show gharibam");
    }

    private void addGold() {
        controller.getCurrentPlayer().addGold(200);
        printer.printlnYellow("Balam az zamane Khatami");
    }

    private void economicOverviewPanel() {
        ArrayList<City> cities = this.controller.getCurrentPlayer().getCities();
        printer.printlnGreen("You have " + cities.size() + " cities");
        for (int i = 0; i < cities.size(); i++) {
            printer.printlnBlue("City central tile is in Y: " + cities.get(i).getCentralTile().findTileYCoordinateInMap() + " , X: " + cities.get(i).getCentralTile().findTileXCoordinateInMap());
            printer.println(" population : " + cities.get(i).getCitizens().size());
            printer.println(" effective combat strength : " + cities.get(i).getCombatStrength());
            printer.println(" food : " + cities.get(i).calculateOutput().getFood());
            printer.println(" gold : " + cities.get(i).calculateOutput().getGold());
            printer.println(" production : " + cities.get(i).calculateOutput().getProduction());
            printer.println(" science : " + cities.get(i).calculateBeakerProduction());
            if (cities.get(i).getEntityInProduction() == null) {
                printer.println(" This city doesn't have any entity in production!");
            } else {
                printer.println(" entity in production : " + cities.get(i).getEntityInProduction().getName());
                printer.println(" turns left : " + (int) ((cities.get(i).getEntityInProduction().getCost() - cities.get(i).getHammerCount()) / cities.get(i).calculateOutput().getProduction()));
            }
        }
    }

    private void runDemographicPanel() {
        printer.printlnRed("This is your civilization demographic panel: " + this.controller.getCurrentPlayer().getName());
        String command;
        Matcher matcher;
        while (true) {
            printer.printlnRed("*****************************************");
            printer.println("Research Menu");
            printer.println("enter \"show commands\" to see all commands");

            command = scanner.nextLine();
            if (command.equals("show commands")) {
                showDemographicPanelCommands();
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.TERRITORY_SIZE.getCommandMatcher(command)) != null) {
                showTerritorySize();
            } else if ((matcher = DemographicPanelCommands.GOLD_COUNT.getCommandMatcher(command)) != null) {
                showGoldCount();
            } else if ((matcher = DemographicPanelCommands.RESOURCES.getCommandMatcher(command)) != null) {
                showAllResources();
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.IMPROVEMENTS.getCommandMatcher(command)) != null) {
                showAllImprovements();
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.LEARNED_TECHNOLOGIES.getCommandMatcher(command)) != null) {
                showLearnedTechnologies(this.controller.getCurrentPlayer());
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.MILITARY_UNITS.getCommandMatcher(command)) != null) {
                militaryOverviewPanel();
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.OUTPUT.getCommandMatcher(command)) != null) {
                showOutput();
            } else if ((matcher = DemographicPanelCommands.SCORE.getCommandMatcher(command)) != null) {
                showScore();
            } else if ((matcher = DemographicPanelCommands.SCOREBOARD.getCommandMatcher(command)) != null) {
                showScoreBoard();
                waitForClick();
            } else if ((matcher = DemographicPanelCommands.BACK.getCommandMatcher(command)) != null) {
                break;
            } else {
                printer.println("Invalid command for demographic panel!");
            }

        }
    }

    private void showAllImprovements() {
        printer.println("You have these improvements on different tiles : (if there is two same improvements, it means that you have that on two tiles)");
        Civilization civilization = this.controller.getCurrentPlayer();
        for (Improvement improvement : civilization.getAllImprovements()) {
            printer.println(" - " + improvement.getType().getName());
        }
    }

    private void showAllResources() {
        printer.printlnGreen("Your luxury resources:");
        HashMap<LuxuryResource, Integer> luxuryResources = this.controller.getCurrentPlayer().getLuxuryResources();
        for (Map.Entry<LuxuryResource, Integer> entry : luxuryResources.entrySet()) {
            if (entry.getValue() > 0) {
                printer.println(" " + "- " + entry.getKey() + ", " + entry.getValue());
            }
        }
        printer.printlnGreen("Your strategic resources:");
        HashMap<StrategicResource, Integer> strategicResources = this.controller.getCurrentPlayer().getStrategicResources();
        for (Map.Entry<StrategicResource, Integer> entry : strategicResources.entrySet()) {
            if (entry.getValue() > 0) {
                printer.println(" " + "- " + entry.getKey() + ", " + entry.getValue());
            }
        }
    }

    private void showOutput() {
        Civilization civilization = this.controller.getCurrentPlayer();
        printer.printlnGreen("Your collectable outputs in each turn:");
        printer.println("Gold : " + (int) civilization.calculateNetGoldProduction());
        printer.println("Production : " + (int) civilization.calculateTotalBeakers());
        printer.println("Food : " + (int) civilization.calculateTotalFoodFromCities());
    }

    private void showScoreBoard() {
        this.controller.sortPlayersInOrderOfScore();
        printer.printlnGreen("Game scoreboard:");
        showScore();
        printer.println("Highest score : " + this.controller.calculateHighestScore());
        printer.println("Lowest score : " + this.controller.calculateLowestScore());
        printer.println("Average score : " + this.controller.calculateAverageScore());
        printer.println("##########################");
        ArrayList<Player> players = GameDataBase.getGameDataBase().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            printer.println(" " + (i + 1) + "- " + players.get(i).getCivilization().getName() + " , score : " + this.controller.calculateScoreForCivilization(players.get(i).getCivilization()));
        }
        printer.println("##########################");
    }

    private void showScore() {
        Civilization civilization = this.controller.getCurrentPlayer();
        printer.println("Your score is : " + this.controller.calculateScoreForCivilization(civilization));
    }

    private void showGoldCount() {
        Civilization civilization = this.controller.getCurrentPlayer();
        printer.println("You have " + (int) civilization.getGoldCount() + " golds!");
    }

    private void showTerritorySize() {
        Civilization civilization = this.controller.getCurrentPlayer();
        int territorySize = this.controller.calculateCivilizationTerritorySize(civilization);
        printer.println("You have " + territorySize + " tiles!");
    }

    private void showDemographicPanelCommands() {
        ArrayList<DemographicPanelCommands> demographicPanelCommands = DemographicPanelCommands.getAllCommands();
        printer.printlnBlue("Demographic panel commands:");
        for (DemographicPanelCommands demographicPanelCommand : demographicPanelCommands) {
            printer.println(" -" + demographicPanelCommand.getName());
        }
    }

    private void militaryOverviewPanel() {
        ArrayList<Unit> militaryUnits = this.controller.getCurrentPlayer().getMilitaryUnits();
        if (militaryUnits.isEmpty()) {
            printer.println("You don't have any military units to show!");
        } else {
            printer.printlnBlue("Your military units : ");
            for (int i = 0; i < militaryUnits.size(); i++) {
                printer.println(" " + (i + 1) + "- " + militaryUnits.get(i).getType().getName() + " in Y: " + militaryUnits.get(i).getLocation().findTileYCoordinateInMap() + " , X: " + militaryUnits.get(i).getLocation().findTileXCoordinateInMap());
            }
        }
    }

    private void showAllNotification() {
        ArrayList<Notification> notifications = this.controller.getCurrentPlayer().getNotifications();
        printer.printlnGreen("Your all notifications :");
        for (int i = 0; i < notifications.size(); i++) {
            printer.println(" " + (i + 1) + "- " + notifications.get(i).getText() + " , in turn: " + notifications.get(i).getTurnNumber());
        }
        this.controller.seenAllNotifications();
    }

    private void printNewNotifications() {
        ArrayList<Notification> notifications = this.controller.getCivilizationNewNotification();
        if (!notifications.isEmpty()) {
            printer.printlnGreen("You have some new notifications:");
            for (Notification notification : notifications) {
                printer.println(notification.getText());
            }
            this.controller.seenAllNotifications();
        }
    }

    private void showCitiesInfo() {
        Civilization civilization = controller.getCurrentPlayer();
        ArrayList<City> cities = civilization.getCities();
        String command;
        boolean quit = false;
        while (!quit) {
            printer.printlnRed("*****************************************");
            printer.println("Cities panel");
            printer.println("Enter back to exit");
            printer.println("you can select a city from bellow list with this command : select <city number in below list>");

            command = scanner.nextLine();
            if (command.equals("back")) {
                break;
            } else if (command.startsWith("select ")) {
                Matcher matcher = Pattern.compile("select (?<cityNumber>\\d+)").matcher(command);
                if (!matcher.matches()) {
                    printer.println("Invalid command for Cities panel!");
                } else {
                    int cityNumber = Integer.parseInt(matcher.group("cityNumber"));
                    quit = selectCityFromCitiesPanel(civilization, cities, cityNumber);
                }
            } else {
                printer.println("Invalid command for Units panel!");
            }
        }
    }

    private boolean selectCityFromCitiesPanel(Civilization civilization, ArrayList<City> cities, int cityNumber) {
        if (cityNumber < 1 || cityNumber > cities.size()) {
            printer.println("Invalid city number!");
            return false;
        }
        City city = cities.get(cityNumber - 1);
        civilization.setSelectedEntity(city);
        return true;
    }

    private void printAllCities(Civilization civilization, ArrayList<City> cities) {
        printer.printlnBlue(civilization.getName() + " has cities with central tiles in this positions:");
        for (int i = 0; i < cities.size(); i++) {
            printer.println(" " + (i + 1) + "- " + " at Y: " + cities.get(i).getCentralTile().findTileYCoordinateInMap() +
                    ", X: " + cities.get(i).getCentralTile().findTileYCoordinateInMap());
        }
    }

    private void showUnitsInfo() {
        Civilization civilization = controller.getCurrentPlayer();
        ArrayList<Unit> units = civilization.getUnits();
        String command;
        boolean quit = false;
        while (!quit) {
            printer.printlnRed("*****************************************");
            printer.println("Units panel");
            printer.println("Enter back to exit");
            printer.println("you can select a unit from bellow list with this command : select <unit number in below list>");
            printAllUnits(civilization, units);

            command = scanner.nextLine();
            if (command.equals("back")) {
                break;
            } else if (command.startsWith("select ")) {
                Matcher matcher = Pattern.compile("select (?<unitNumber>\\d+)").matcher(command);
                if (!matcher.matches()) {
                    printer.println("Invalid command for Units panel!");
                } else {
                    int unitNumber = Integer.parseInt(matcher.group("unitNumber"));
                    quit = selectUnitFromUnitsPanel(civilization, units, unitNumber);
                }
            } else {
                printer.println("Invalid command for Units panel!");
            }
        }
    }

    private boolean selectUnitFromUnitsPanel(Civilization civilization, ArrayList<Unit> units, int unitNumber) {
        if (unitNumber < 1 || unitNumber > units.size()) {
            printer.println("Invalid unit number!");
            return false;
        }
        Unit unit = units.get(unitNumber - 1);
        civilization.setSelectedEntity(unit);
        printer.println(civilization.getName() + "'s " + unit.getType().getName() + " was selected");
        return true;
    }

    private void printAllUnits(Civilization civilization, ArrayList<Unit> units) {
        printer.printlnBlue(civilization.getName() + "'s Units:");
        for (int i = 0; i < units.size(); i++) {
            printer.println(" " + (i + 1) + "- " + units.get(i).getType().getName() + " at Y: " + units.get(i).getLocation().findTileYCoordinateInMap() +
                    ", X: " + units.get(i).getLocation().findTileXCoordinateInMap());
        }
    }


    private void runResearchTab() {
        Civilization civilization = controller.getCurrentPlayer();
        String command;
        Matcher matcher;
        while (true) {
            printer.printlnRed("*****************************************");
            printer.println("Research Menu");
            printer.println("enter \"show commands\" to see all commands");

            command = scanner.nextLine();
            if ((matcher = ResearchCommands.SHOW_COMMANDS.getCommandMatcher(command)) != null) {
                showResearchMenuCommands();
                waitForClick();
            } else if ((matcher = ResearchCommands.LEARNED_TECHNOLOGIES.getCommandMatcher(command)) != null) {
                showLearnedTechnologies(civilization);
                waitForClick();
            } else if ((matcher = ResearchCommands.UNLOCKED_TECHNOLOGIES.getCommandMatcher(command)) != null) {
                showUnlockedTechnologies(civilization);
                waitForClick();
            } else if ((matcher = ResearchCommands.RESERVED_RESEARCHES.getCommandMatcher(command)) != null) {
                showReservedResearches(civilization);
                waitForClick();
            } else if ((matcher = ResearchCommands.STOP_RESEARCH.getCommandMatcher(command)) != null) {
                stopResearch(civilization);
            } else if ((matcher = ResearchCommands.START_RESEARCH.getCommandMatcher(command)) != null) {
                startResearch(civilization);
            } else if ((matcher = ResearchCommands.BACK.getCommandMatcher(command)) != null) {
                printer.println("You exited research tab");
                break;
            } else if ((matcher = ResearchCommands.LEARN_TECHNOLOGY.getCommandMatcher(command)) != null) {
                learnTechnology(matcher);
            } else if ((matcher = ResearchCommands.CHANGE_RESEARCH.getCommandMatcher(command)) != null) {
                changeResearch(civilization);
            } else if ((matcher = ResearchCommands.SHOW_CURRENT_INFO.getCommandMatcher(command)) != null) {
                showCurrentResearchInfo(civilization);
                waitForClick();
            } else {
                printer.println("Invalid command for Research tab!");
            }
        }
    }

    private void learnTechnology(Matcher matcher) {
        String techName = matcher.group("name");
        if (techName.equalsIgnoreCase("all")) {
            controller.getCurrentPlayer().getTechnologies().learnAllTechnologies();
            printer.printlnBlue("You have learned all technologies!");
            return;
        }
        Technology chosenTech = Technology.getTechnologyByName(techName);
        if (chosenTech == null) {
            printer.printlnError("Technology not recognized");
            return;
        }
        if (controller.getCurrentPlayer().getTechnologies().isTechnologyLearned(chosenTech)) {
            printer.printlnError("You have already learned this technology!");
            return;
        }
        controller.getCurrentPlayer().getTechnologies().learnTechnologyAndPrerequisites(chosenTech);
        printer.println("You have successfully stolen this technology.");
    }

    private void showResearchMenuCommands() {
        printer.printlnPurple("Research Menu commands :");
        for (ResearchCommands command : ResearchCommands.getAllCommands()) {
            printer.println(" -" + command.getName());
        }
    }

    private void showCurrentResearchInfo(Civilization civilization) {
        if (civilization.getResearchProject() == null) {
            printer.printlnError("You don't have any active research projects!");
            return;
        }
        printer.printlnBlue(civilization.getName() + "'s research : " + civilization.getResearchProject().getName());
        int turnsLeft = (int) Math.ceil((civilization.getResearchProject().getCost() - civilization.getBeakerCount()) / civilization.calculateTotalBeakers());
        printer.println("Turns left to finish researching : " + turnsLeft);
        printer.println("Cost : " + civilization.getResearchProject().getCost());
        printer.println("Beaker Count : " + civilization.getBeakerCount());
    }

    private void changeResearch(Civilization civilization) {
        this.stopResearch(civilization);
        this.startResearch(civilization);
    }

    private void startResearch(Civilization civilization) {
        if (civilization.getCities().isEmpty()) {
            printer.printlnError("You should found a city first!");
            return;
        }
        if (civilization.getResearchProject() != null) {
            printer.printlnError("You have already started a research project!");
            return;
        }
        ArrayList<Technology> technologies = civilization.getTechnologies().getUnlockedTechnologies();
        if (technologies.isEmpty()) {
            printer.printlnError("There is no technology to research!");
            return;
        }
        printer.printlnBlue("Enter the number of the technology in order to start a research project:");
        for (int i = 0; i < technologies.size(); i++) {
            printer.println(" " + (i + 1) + "-" + technologies.get(i).getName());
        }
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if ((matcher = Pattern.compile("\\s*[0-9]+\\s*").matcher(input)) != null && Integer.parseInt(input) <= technologies.size() && Integer.parseInt(input) >= 1) {
                Technology researchProject = technologies.get(Integer.parseInt(input) - 1);
                if (civilization.getResearchReserve().containsKey(researchProject)) {
                    civilization.setBeakerCount(civilization.getResearchReserve().get(researchProject));
                    civilization.getResearchReserve().remove(researchProject);
                }
                civilization.setResearchProject(researchProject);
                break;
            } else {
                printer.printlnError("Please enter a number between 1 and " + technologies.size());
            }
        }
    }

    private void stopResearch(Civilization civilization) {
        if (civilization.getResearchProject() == null) {
            printer.printlnError("You don't have any active research projects!");
            return;
        }
        Technology researchProject = civilization.getResearchProject();
        civilization.getResearchReserve().put(researchProject, civilization.getBeakerCount());
        civilization.setResearchProject(null);
        civilization.setBeakerCount(0);
        printer.println("research " + researchProject.getName() + " has stopped!");
    }

    private void showReservedResearches(Civilization civilization) {
        if (civilization.getResearchReserve().isEmpty()) {
            printer.printlnError("There is no reserved research for " + civilization.getName() + " civilization!");
            return;
        }
        printer.printlnBlue(civilization.getName() + "'s reserved technologies:");
        for (Technology technology : civilization.getResearchReserve().keySet()) {
            printer.println(" -" + technology.getName() + ", beakers spent: " + civilization.getResearchReserve().get(technology));
        }
    }

    private void showUnlockedTechnologies(Civilization civilization) {
        printer.printlnBlue(civilization.getName() + "'s unlocked technologies:");
        ArrayList<Technology> technologies = civilization.getTechnologies().getUnlockedTechnologies();
        for (Technology technology : technologies) {
            printer.println(" -" + technology.getName());
        }
    }

    private void showLearnedTechnologies(Civilization civilization) {
        printer.printlnBlue(civilization.getName() + "'s learned technologies:");
        ArrayList<Technology> technologies = civilization.getTechnologies().getLearnedTechnologies();
        for (Technology technology : technologies) {
            printer.println(" -" + technology.getName());
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
            } else if ((matcher = CityCommands.PURCHASE_TILE.getCommandMatcher(command)) != null) {
                purchaseTile(city);
            } else if ((matcher = CityCommands.ATTACK.getCommandMatcher(command)) != null) {
                cityRangedAttack(matcher, city);
            } else {
                printer.printlnError("Invalid command for city!");
            }
        }
    }

    private void cityRangedAttack(Matcher matcher, City city) {
        if (city.hasAttackedThisTurn()) {
            printer.printlnError("This city has already attacked in this turn!");
            return;
        }
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile targetTile = controller.getTileByCoordinates(x, y);
        if (targetTile.calculateDistance(city.getCentralTile()) >= city.getRange()) {
            printer.printlnError("This target is not within city's range!");
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, city.getOwner())) {
            printer.printlnError("There are no hostile entities in this tile!");
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, city.getOwner());
        if (target instanceof City) {
            printer.printlnError("You can't attack another city!");
            return;
        }
        CombatController.getCombatController().executeRangedAttack(city, target);
        printer.println("Attacked target at " + y + ", " + x);
    }

    private void purchaseTile(City city) {
        printer.printlnPurple("Enter the coordinates (Y, X) of an adjacent tile you wish to buy for " + city.calculateNextTilePrice() + " Gold.");
        printer.println("Available Tiles:");
        ArrayList<Tile> purchasableTiles = city.findPurchasableTiles();
        for (Tile tile : purchasableTiles) {
            printer.println("Y: " + tile.findTileYCoordinateInMap() + ", X: " + tile.findTileXCoordinateInMap());
        }
        printer.printlnError("enter cancel to cancel the purchase");

        Pattern inputPattern = Pattern.compile("(?<y>\\d+)\\s*[:, ]\\s*(?<x>\\d+)");
        Matcher matcher;
        int x, y;

        Tile tile = null;
        int cost = 0;
        while (true) {
            String input = scanner.nextLine();
            if ((matcher = inputPattern.matcher(input)).matches()) {
                y = Integer.parseInt(matcher.group("y"));
                x = Integer.parseInt(matcher.group("x"));
                if (!controller.areCoordinatesValid(x, y)) {
                    printer.printlnError("Invalid coordinates, try again.");
                    continue;
                }
                tile = controller.getTileByCoordinates(x, y);
                if (!purchasableTiles.contains(tile)) {
                    printer.printlnError("You can only choose from the presented list. Try again.");
                    continue;
                }
                cost = city.calculateNextTilePrice();
                if (cost > city.getOwner().getGoldCount()) {
                    printer.printlnError("You don't have enough gold to buy this tile!");
                    return;
                }
                break;
            } else if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("back")) {
                printer.printlnRed("purchase canceled.");
                return;
            } else {
                printer.printlnError("Invalid input, try again.");
                continue;
            }
        }
        controller.getCurrentPlayer().decreaseGold(cost);
        controller.addTileToCityTerritory(city, tile);
        printer.println("Tile successfully purchased!");
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
            } else if ((matcher = ProductionPanelCommands.PURCHASE.getCommandMatcher(command)) != null) {
                purchase(city);
            } else {
                printer.printlnError("Invalid command for Production Panel");
            }
        }
    }

    private void purchase(City city) {
        printer.println("Total Gold: " + (int) city.getOwner().getGoldCount());
        printer.println("Choose what you wish to purchase with gold. enter cancel to abort.");

        printer.printlnPurple("Purchasable Units:");
        ArrayList<UnitType> purchasableUnits = city.calculatePurchasableUnitTypes();
        for (UnitType unit : purchasableUnits) {
            printer.println(unit.getName() + "\t\t\t" + unit.getCost() + " Gold");
        }

        printer.printlnPurple("Purchasable Buildings:");
        ArrayList<BuildingType> purchasableBuildings = city.calculatePurchasableBuildingTypes();
        for (BuildingType building : purchasableBuildings) {
            printer.println(building.getName() + "\t\t\t" + building.getCost() + " Gold");
        }

        ArrayList<Producible> purchasables = new ArrayList<Producible>(purchasableUnits);
        purchasables.addAll(purchasableBuildings);
        Producible chosenPurchasable = null;

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("back")) {
                printer.printlnError("Purchase canceled");
                return;
            }

            for (Producible purchasable : purchasables) {
                if (input.equalsIgnoreCase(purchasable.getName())) {
                    chosenPurchasable = purchasable;
                    break;
                }
            }

            if (chosenPurchasable == null) {
                printer.printlnError("Input did not match any of the presented choices. Try again.");
            } else {
                break;
            }
        }

        if (city.getOwner().getGoldCount() < chosenPurchasable.getCost()) {
            printer.printlnError("You don't have enough gold to purchase this item!");
            return;
        }

        if (chosenPurchasable instanceof BuildingType) {
            city.addBuilding((BuildingType) chosenPurchasable);
            printer.println("Successfully purchased " + chosenPurchasable.getName());
        }
        if (chosenPurchasable instanceof UnitType) {
            if (city.getCentralTile().doesPackingLetUnitEnter((UnitType) chosenPurchasable)) {
                controller.createUnit((UnitType) chosenPurchasable, city.getOwner(), city.getCentralTile());
                printer.println("Successfully purchased " + chosenPurchasable.getName());
            } else {
                printer.printlnError("Your city is full! A new unit can't enter. Move the existing units and try again");
                return;
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
        ArrayList<BuildingType> producibleBuildings = city.calculateProductionReadyBuildingTypes(false);

        printer.println("Units:");
        for (UnitType producibleUnit : producibleUnits) {
            int hammerCost = producibleUnit.calculateHammerCost();
            int turnsRequired = (int) Math.ceil((double) hammerCost / city.calculateOutput().getProduction());
            printer.println(producibleUnit.getName() + ",\t\t\t" + hammerCost + " Hammers, " + turnsRequired + " turns");
            HashMap<StrategicResource, Integer> resources = producibleUnit.getPrerequisiteResources();
            for (StrategicResource resource : resources.keySet()) {
                printer.println("\t" + resource.getName() + ": " + resources.get(resource));
            }
        }

        printer.println("Buildings:");
        for (BuildingType producibleBuilding : producibleBuildings) {
            int hammerCost = producibleBuilding.calculateHammerCost();
            int turnsReuquired = (int) Math.ceil((double) hammerCost / city.calculateOutput().getProduction());
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
                    if (producible instanceof UnitType && !city.getCentralTile().doesPackingLetUnitEnter((UnitType) producible)) {
                        printer.printlnError("There is already a unit in the city. You need to move it to make room!");
                        return;
                    }
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
            int turnsRemaining = (int) Math.ceil((double) (hammerCost - hammerCount) / productionOutput);
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

        printer.printlnBlue("Resources in this city:");
        for (Tile tile : city.getTerritories()) {
            for (Resource resource : tile.getResourcesAsArrayList()) {
                printer.print(resource.getName());
                if (resource.canBeExploited(tile)) {
                    printer.println(" (exploited by " + resource.getPrerequisiteImprovement().getName() + ")");
                } else {
                    printer.println(" (not exploited, requires " + resource.getPrerequisiteImprovement().getName() + ")");
                }
            }
        }

        printer.printlnBlue("This city has " + city.getCitizens().size() + " citizens. " + city.calculateWorklessCitizenCount()
                + " of them are workless.");
        printer.println("Hit Points Left: " + city.getHitPointsLeft());
        printer.println("City's food balance:");
        if (city.getFoodCount() >= 0) {
            printer.printlnBlue(city.getFoodCount());
        } else {
            printer.printlnRed(city.getFoodCount());
        }
        String productionName = (city.getEntityInProduction() == null) ? "nothing!" : city.getEntityInProduction().getName();
        printer.println("This city is producing " + productionName);
        printer.printlnGreen("Food Output: " + city.calculateOutput().getFood());
        printer.printlnRed("Production Output: " + city.calculateOutput().getProduction());
        printer.printlnBlue("Science Output: " + city.calculateBeakerProduction());
        printer.printlnYellow("Gold Output: " + city.calculateOutput().getGold());
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
        if (tile.calculateDistance(city.getCentralTile()) > 2) {
            printer.printlnError("This tile is too far away from city center!");
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
        if (idleUnits.isEmpty() == false && !controller.getCurrentPlayer().isTurnBreakDisabled()) {
            printer.printlnError("Some units are waiting for a command!");
            controller.getCurrentPlayer().setSelectedEntity(idleUnits.get(0));
            return;
        }
        if (!controller.getCurrentPlayer().getCities().isEmpty() && controller.getCurrentPlayer().getResearchProject() == null &&
                !controller.getCurrentPlayer().isTurnBreakDisabled()) {
            printer.printlnError("You should start a research project!");
            // TODO : FOR MY SELF
            return;
        }

        ArrayList<City> citiesWaitingForProduction = controller.getCurrentPlayer().getCitiesWaitingForProduction();
        if (citiesWaitingForProduction.isEmpty() == false && !controller.getCurrentPlayer().isTurnBreakDisabled()) {
            printer.printlnError("Some cities are waiting for their next production!");
            controller.getCurrentPlayer().setSelectedEntity(citiesWaitingForProduction.get(0));
            return;
        }

        controller.goToNextPlayer();
        showMap();
    }

    private void runWorkActionsTab() {
        Unit worker = (Unit) controller.getCurrentPlayer().getSelectedEntity();
        ArrayList<WorkerCommands> allowedCommands = calculateWorkerAllowedActions(worker);
        printer.printlnRed("**********************************");
        printer.printlnRed("Work Actions Tab");
        printer.println("Choose an action from the below list: enter back to go back :)");
        for (WorkerCommands allowedCommand : allowedCommands) {
            printer.println(allowedCommand.getName());
        }

        String command;
        Matcher matcher;
        while (true) {
            command = scanner.nextLine();
            if ((matcher = WorkerCommands.BUILD_ROAD.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_ROAD)) {
                buildImprovement(worker, ImprovementType.ROAD);
            } else if ((matcher = WorkerCommands.BUILD_RAILROAD.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_RAILROAD)) {
                buildImprovement(worker, ImprovementType.RAILROAD);
            } else if ((matcher = WorkerCommands.BUILD_FARM.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_FARM)) {
                buildFarmOrMine(worker, ImprovementType.FARM);
            } else if ((matcher = WorkerCommands.BUILD_MINE.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_MINE)) {
                buildFarmOrMine(worker, ImprovementType.MINE);
            } else if ((matcher = WorkerCommands.BUILD_TRADING_POST.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_TRADING_POST)) {
                buildImprovement(worker, ImprovementType.TRADING_POST);
            } else if ((matcher = WorkerCommands.BUILD_LUMBER_MILL.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_LUMBER_MILL)) {
                buildImprovement(worker, ImprovementType.LUMBER_MILL);
            } else if ((matcher = WorkerCommands.BUILD_PASTURE.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_PASTURE)) {
                buildImprovement(worker, ImprovementType.PASTURE);
            } else if ((matcher = WorkerCommands.BUILD_PLANTATION.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_PLANTATION)) {
                buildImprovement(worker, ImprovementType.PLANTATION);
            } else if ((matcher = WorkerCommands.BUILD_QUARRY.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_QUARRY)) {
                buildImprovement(worker, ImprovementType.QUARRY);
            } else if ((matcher = WorkerCommands.BUILD_CAMP.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.BUILD_CAMP)) {
                buildImprovement(worker, ImprovementType.CAMP);
            } else if ((matcher = WorkerCommands.CLEAR_FOREST.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.CLEAR_FOREST)) {
                clearFeature(worker, Feature.FOREST);
            } else if ((matcher = WorkerCommands.CLEAR_JUNGLE.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.CLEAR_JUNGLE)) {
                clearFeature(worker, Feature.JUNGLE);
            } else if ((matcher = WorkerCommands.CLEAR_MARSH.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.CLEAR_MARSH)) {
                clearFeature(worker, Feature.MARSH);
            } else if ((matcher = WorkerCommands.CLEAR_ROUTES.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.CLEAR_ROUTES)) {
                clearRoutes(worker);
            } else if ((matcher = WorkerCommands.FIX_IMPROVEMENT.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.FIX_IMPROVEMENT)) {
                fixImprovement(worker, false);
            } else if ((matcher = WorkerCommands.FIX_ROUTE.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.FIX_ROUTE)) {
                fixImprovement(worker, true);
            } else if ((matcher = WorkerCommands.STOP_WORK.getCommandMatcher(command)) != null && allowedCommands.contains(WorkerCommands.STOP_WORK)) {
                stopWork(worker);
                break;
            } else if (command.equals("cancel") || command.equals("back")) {
                printer.println("You have exited Work Actions Panel");
                break;
            } else {
                printer.printlnError("Invalid Command!");
            }
        }
    }


    private void stopWork(Unit worker) {
        Work work = controller.getWorkersWork(worker);
        work.stopWork();
        printer.println("Stopped this units work!");
    }

    private boolean askToReplace(Tile location) {
        Improvement nonRouteImprovement;
        if ((nonRouteImprovement = location.getNonRouteImprovement()) != null) {
            printer.println("There is already an improvement (" + nonRouteImprovement.getType().getName()
                    + ") on this tile, do you wish to replace it? y/n");
            String input = scanner.nextLine();
            if (input.startsWith("y") || input.startsWith("Y")) {
                location.removeImprovement(nonRouteImprovement);
                printer.printlnRed("Removed " + nonRouteImprovement.getType().getName() + " improvement!");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private void buildFarmOrMine(Unit worker, ImprovementType type) {
        Tile location = worker.getLocation();
        if (!askToReplace(location)) {
            printer.println("Building " + type.getName() + "canceled");
            return;
        }
        if (location.getWork() != null) {
            if (location.getWork() instanceof BuildImprovementAndRemoveFeature &&
                    (((BuildImprovementAndRemoveFeature) location.getWork()).getImprovement() == type)) {
                ((BuildImprovementAndRemoveFeature) location.getWork()).startWork(worker);
                printer.println("Resumed " + type.getName().toLowerCase() + " construction here");
                return;
            }
            printer.printlnPurple("Last project will be terminated. Are you sure you want to continue? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                printer.println("Building " + type.getName().toLowerCase() + " canceled");
                return;
            }
        }
        BuildImprovementAndRemoveFeature newWork = new BuildImprovementAndRemoveFeature(worker, type);
        location.setWork(newWork);
        worker.setPath(null);
        printer.println("Started the construction of a " + type.getName().toLowerCase() + " here!");
    }

    private void buildImprovement(Unit worker, ImprovementType improvementType) {
        Tile location = worker.getLocation();
        if (!askToReplace(location)) {
            printer.println("Building " + improvementType.getName() + "canceled");
            return;
        }
        if (location.getWork() != null) {
            if (location.getWork() instanceof BuildImprovement &&
                    ((BuildImprovement) location.getWork()).getImprovement() == improvementType) {
                ((BuildImprovement) location.getWork()).startWork(worker);
                printer.println("Resumed " + improvementType.getName().toLowerCase() + " construction here");
            }
            if (location.getWork() instanceof BuildImprovement &&
                    ((BuildImprovement) location.getWork()).getImprovement() == improvementType) {
                ((BuildImprovement) location.getWork()).startWork(worker);
                printer.println("Resumed " + improvementType.getName().toLowerCase() + " construction here");
                return;
            }
            printer.printlnPurple("Last project will be terminated. Are you sure you want to continue? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                printer.println("Building " + improvementType.getName().toLowerCase() + " canceled");
                return;
            }
        }
        BuildImprovement newWork = new BuildImprovement(improvementType, worker);
        location.setWork(newWork);
        worker.setPath(null);
        printer.println("Started the construction of a " + improvementType.getName().toLowerCase() + " here!");
    }

    private void clearRoutes(Unit worker) {
        Tile location = worker.getLocation();
        if (location.getWork() != null) {
            if (location.getWork() instanceof ClearRoutes) {
                ((ClearRoutes) location.getWork()).startWork(worker);
                return;
            }
            printer.printlnPurple("Last project will be terminated. Are you sure you want to continue? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                printer.println("Clearace routes canceled");
                return;
            }
        }
        location.setWork(new ClearRoutes(worker));
        worker.setPath(null);
        printer.println("Started the clearance of routes here!");
    }

    private void clearFeature(Unit worker, Feature feature) {
        Tile location = worker.getLocation();
        if (location.getWork() != null) {
            if (location.getWork() instanceof ClearFeature &&
                    ((ClearFeature) location.getWork()).getFeature() == feature) {
                ((ClearFeature) location.getWork()).startWork(worker);
                printer.println("Resumed " + feature.getName().toLowerCase() + " clearance here");
                return;
            }
            printer.printlnPurple("Last project will be terminated. Are you sure you want to continue? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                printer.println("Clearance " + feature.getName().toLowerCase() + " canceled");
                return;
            }
        }
        location.setWork(new ClearFeature(feature, worker));
        worker.setPath(null);
        printer.println("Started the clearance of a " + feature.getName().toLowerCase() + " here!");
    }

    private void fixImprovement(Unit worker, boolean isRoute) {
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
                printer.println("Resumed " + improvementType.getName().toLowerCase() + " fixation here");
                return;
            }
            printer.printlnPurple("Last project will be terminated. Are you sure you want to continue? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                printer.println("Fixation " + improvementType.getName().toLowerCase() + " canceled");
                return;
            }
        }
        location.setWork(new FixPillage(improvementType, worker));
        worker.setPath(null);
        printer.println("Started the fixation of a " + improvementType.getName().toLowerCase() + " here!");
    }

    private ArrayList<WorkerCommands> calculateWorkerAllowedActions(Unit worker) {
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
            } else if ((matcher = UnitCommands.MELEE_ATTACK.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.MELEE_ATTACK)) {
                meleeAttack(matcher, unit);
            } else if ((matcher = UnitCommands.RANGED_ATTACK.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.RANGED_ATTACK)) {
                rangedAttack(matcher, unit);
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
            } else if ((matcher = UnitCommands.SET_UP_FOR_RANGED_ATTACK.getCommandMatcher(command)) != null &&
                    allowedCommands.get(UnitCommands.SET_UP_FOR_RANGED_ATTACK)) {
                setUpForRangedAttack(unit);
            } else if ((matcher = UnitCommands.PILLAGE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.PILLAGE)) {
                pillage(unit);
            } else if ((matcher = UnitCommands.DELETE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.DELETE)) {
                deleteAUnit(unit);
                break;
            } else if ((matcher = UnitCommands.CANCEL_MOVE.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.CANCEL_MOVE)) {
                cancelUnitMove(unit);
            } else if ((matcher = UnitCommands.TELEPORT.getCommandMatcher(command)) != null) {
                teleportUnit(matcher, unit);
            } else if ((matcher = UnitCommands.INSTANT_HEAL.getCommandMatcher(command)) != null) {
                instantHealUnit(unit);
            } else if ((matcher = UnitCommands.WORK_ACTIONS.getCommandMatcher(command)) != null && allowedCommands.get(UnitCommands.WORK_ACTIONS)) {
                runWorkActionsTab();
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
        // TODO : consider all commands

        return result;
    }

    private void rangedAttack(Matcher matcher, Unit unit) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile targetTile = controller.getTileByCoordinates(x, y);
        if (targetTile.calculateDistance(unit.getLocation()) > unit.getType().getRange() ||
            !controller.getVisibleTilesByUnit(unit).contains(targetTile)) {
            printer.printlnError("You can only attack target that are seen and within range!");
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, unit.getOwner())) {
            printer.printlnError("You can't attack this tile because there are no hostile units in it!");
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, unit.getOwner());
        CombatController.getCombatController().executeRangedAttack(unit, target);
        printer.println("Ranged Attacked " + y + ", " + x);
    }

    private void meleeAttack(Matcher matcher, Unit unit) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates!");
            return;
        }
        Tile targetTile = controller.getTileByCoordinates(x, y);
        if (!controller.areTwoTilesAdjacent(targetTile, unit.getLocation())) {
            printer.printlnError("You can only melee attack adjacent tiles!");
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, unit.getOwner())) {
            printer.printlnError("You can't attack this tile because there are no hostile units in it!");
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, unit.getOwner());
        CombatController.getCombatController().executeMeleeAttack(unit, target);
        printer.println("Melee Attacked " + y + ", " + x);
    }

    private void pillage(Unit unit) {
        controller.pillageUnitsTile(unit);
        printer.println("You just successfully tore apart hard-earned value!");
    }

    private void instantHealUnit(Unit unit) {
        unit.setHitPointsLeft(unit.getType().getHitPoints());
        printer.println("by his wounds you have been healed");
    }

    private void teleportUnit(Matcher matcher, Unit unit) {
        int y = Integer.parseInt(matcher.group("y"));
        int x = Integer.parseInt(matcher.group("x"));
        if (!controller.areCoordinatesValid(x, y)) {
            printer.printlnError("Invalid coordinates! teleport failed");
            return;
        }
        Tile destination = controller.getTileByCoordinates(x, y);
        if (!controller.canUnitTeleportToTile(unit.getType(), controller.getCurrentPlayer(), destination)) {
            printer.printlnError("You can't teleport to that tile!");
            return;
        }
        controller.moveUnit(unit, destination);
        printer.println("Meow");
    }

    private void setUpForRangedAttack(Unit unit) {
        unit.assemble();
        unit.setMovePointsLeft(0);
        unit.setPath(null);
        printer.println("Unit successfully assembled!");
    }

    private void cancelUnitMove(Unit unit) {
        unit.setPath(null);
        printer.println("Unit's schedualed move was canceled");
    }

    private void deleteAUnit(Unit unit) {
        unit.getOwner().setSelectedEntity(null);
        controller.deleteUnit(unit);
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
        if (unit.getType().getCombatType() == CombatType.SIEGE) {
            printer.println((unit.isAssembled()) ? "Assembled" : "Disassembled");
        }
        if (unit.getType() == UnitType.WORKER) {
            if (controller.getWorkersWork(unit) == null) {
                printer.println("Not Currently Working...");
            } else {
                printer.println("Work: " + controller.getWorkersWork(unit).getTitle());
                Tile location = controller.getWorkersWork(unit).findLocation();
                printer.println("Work Place: Y: " + location.findTileYCoordinateInMap() + ", X: " + location.findTileXCoordinateInMap());
            }
        }
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
            city = controller.whoseTerritoryIsTileInButIsNotTheCenterOf(tile);
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
                printer.print(resource.getName());
                String requiredImprovementName = resource.getPrerequisiteImprovement().getName();
                printer.println((resource.canBeExploited(tile) ? " (exploited by " + requiredImprovementName + ")" :
                        " (not exploited, requires " + requiredImprovementName + ")"));
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

        printer.printlnBlue("Worker Project");
        if (tile.getWork() == null) {
            printer.println("No project in progress here!");
        } else {
            printer.println(tile.getWork().getTitle());
            if (tile.getWork().isInProgress()) {
                printer.println(tile.getWork().getTurnsRemaining() + " turns remaining");
            } else {
                printer.println("Work is paused, " + tile.getWork().getTurnsRemaining() + " turns remaining");
            }
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
