package views.controllers;

import controllers.CombatController;
import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import menusEnumerations.CitizenManagementPanelCommands;
import menusEnumerations.CityCommands;
import menusEnumerations.ProductionPanelCommands;
import models.*;
import models.buildings.BuildingType;
import models.interfaces.Producible;
import models.interfaces.TileImage;
import models.interfaces.combative;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.units.Unit;
import models.units.UnitType;
import views.Main;
import views.images.Images;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class CitiesGraphicalController {

    private static GameController controller = GameController.getGameController();

    private static ScrollPane cityActionTabPane;
    private static VBox cityCommandsBox;
    private static Civilization currentPlayer;
    private static TileImage[][] tilesToShow;

    public static void initializeCityActionTab(Pane pane){
        currentPlayer = controller.getCurrentPlayer();
        tilesToShow = controller.getCivilizationImageToShowOnScene(currentPlayer);
        cityActionTabPane = new ScrollPane();
        pane.getChildren().add(cityActionTabPane);
        cityCommandsBox = new VBox();
        cityActionTabPane.setLayoutX(960);
        cityActionTabPane.setLayoutY(300);
        cityActionTabPane.setPrefWidth(300);
        cityActionTabPane.setPrefHeight(400);
        cityActionTabPane.setContent(cityCommandsBox);
        cityCommandsBox.setStyle("-fx-background-color: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        cityActionTabPane.setStyle("-fx-background: #00b2ff; -fx-arc-width: 50; -fx-arc-height: 50");
        cityActionTabPane.setFitToHeight(true);
        cityActionTabPane.setFitToWidth(true);
        cityCommandsBox.setAlignment(Pos.CENTER);
        cityActionTabPane.setVisible(false);
        cityCommandsBox.setDisable(true);
        cityCommandsBox.setSpacing(10);
    }

    public static City reloadCity(City city) {
        return controller.getCity(city);
    }

    public static void makeTheCityActionTab(City city, Pane pane){
        city = reloadCity(city);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        ArrayList<CityCommands> allCommands = CityCommands.getAllCommands();
        for(int i = 0; i < allCommands.size(); i++){
            if(!allCommands.get(i).getName().equals(CityCommands.SHOW_COMMANDS.getName())){
                addButtonForCityCommand(city, pane, allCommands.get(i));
            }
        }
    }

    private static void addButtonForCityCommand(City city, Pane pane, CityCommands command){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        cityCommandsBox.getChildren().add(button);
        if(command.getName().equals(CityCommands.DESELECT.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectCity(city);
                }
            });
        }
        else if(command.getName().equals(CityCommands.PURCHASE_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToBuy(city, pane);
                }
            });
        }
        else if(command.getName().equals(CityCommands.SHOW_CITIZEN_MANAGEMENT_PANEL.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeCitizenManagementPanel(city, pane);
                }
            });
        }
        else if(command.getName().equals(CityCommands.SHOW_PRODUCTION_PANEL.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeProductionPanel(city, pane);
                }
            });
        }
        else if(command.getName().equals(CityCommands.ATTACK.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToAttack(city, pane);
                }
            });
        }
        else if(command.getName().equals(CityCommands.SHOW_INFO.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showCityInfo(city, pane);
                }
            });
        }
    }

    private static void waitForChoosingTileToAttack(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        cityRangedAttack(getTileImageFromHexagon(hexagon), city);
                    }
                });
            }
        }

    }

    private static void cityRangedAttack(TileImage tileImage, City city) {
        if (city.hasAttackedThisTurn()) {
            RegisterPageGraphicalController.showPopup("This city has already attacked in this turn!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only attack visible tiles!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Tile targetTile = (Tile) tileImage;
        if (controller.calculateDistanceFromTile(targetTile, city.getCentralTile()) >= city.getRange()) {
            RegisterPageGraphicalController.showPopup("This target is not within city's range!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!controller.doesTileContainEnemyCombative(targetTile, city.getOwner())) {
            RegisterPageGraphicalController.showPopup("There are no hostile entities in this tile!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        combative target = controller.getPriorityTargetInTile(targetTile, city.getOwner());
        if (target instanceof City) {
            RegisterPageGraphicalController.showPopup("You can't attack another city!");
            try {
                Main.loadFxmlFile("CivilizationGamePage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        controller.executeRangedAttackCity(city, target);
        RegisterPageGraphicalController.showPopup("Attacked target at " + controller.findTileYCoordinateInMap(targetTile) + ", " + controller.findTileXCoordinateInMap(targetTile));
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeProductionPanel(City city, Pane pane){
        city = reloadCity(city);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        ArrayList<ProductionPanelCommands> allCommands = ProductionPanelCommands.getAllCommands();
        for(int i = 0; i < allCommands.size(); i++){
            addProductionPanelButtonForCommand(city, pane, allCommands.get(i));
        }

    }

    private static void addProductionPanelButtonForCommand(City city, Pane pane, ProductionPanelCommands command){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        cityCommandsBox.getChildren().add(button);
        if(command.getName().equals(ProductionPanelCommands.BACK.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeTheCityActionTab(city, pane);
                }
            });
        }
        else if(command.getName().equals(ProductionPanelCommands.SHOW_INFO.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    showProductionInfo(city, pane);
                }
            });
        }
        else if(command.getName().equals(ProductionPanelCommands.CHOOSE_PRODUCTION.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeChooseProductionPanel(city, pane);
                }
            });
        }
        else if(command.getName().equals(ProductionPanelCommands.STOP_PRODUCTION.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stopProduction(city, pane);
                }
            });
        }
        else if(command.getName().equals(ProductionPanelCommands.PURCHASE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makePurchaseProductionPanel(city, pane);
                }
            });
        }
    }

    private static void stopProduction(City city, Pane pane){
        if (controller.getCityEntityInProduction(city) == null) {
            RegisterPageGraphicalController.showPopup("There is no ongoing production in this city");
            makeTheCityActionTab(city, pane);
            return;
        }
        controller.stopCityProduction(city);
        RegisterPageGraphicalController.showPopup("Production stopped.");
        makeTheCityActionTab(city, pane);
        return;
    }

    public static void makePurchaseProductionPanel(City inCity, Pane pane){
        City city = reloadCity(inCity);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        Text text = new Text("Choose what you wish to purchase with gold.");
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(text);
        Text unitsText = new Text("Purchasable Units:");
        unitsText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(unitsText);



        ArrayList<UnitType> purchasableUnits = controller.calculateCityPurchasableUnitTypes(city);
        for (UnitType unit : purchasableUnits) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            Circle circle = new Circle();
            circle.setRadius(15);
            circle.setFill(Images.getImage(unit.getName()));
            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    purchase(city, pane, unit);
                }
            });
            hBox.getChildren().add(circle);
            Text infoText = new Text(unit.getName() + "\t\t\t" + unit.getCost() + " Gold");
            infoText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
            hBox.getChildren().add(infoText);
            cityCommandsBox.getChildren().add(hBox);

        }

        Text buildingsText = new Text("Purchasable Buildings:");
        buildingsText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(buildingsText);
        ArrayList<BuildingType> purchasableBuildings = controller.calculateCityPurchasableBuildingTypes(city);
        for (BuildingType building : purchasableBuildings) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            Circle circle = new Circle();
            circle.setRadius(15);
            circle.setFill(Images.getImage(building.getName()));
            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    purchase(city, pane, building);
                }
            });
            hBox.getChildren().add(circle);
            Text info = new Text(building.getName() + "\t\t\t" + building.getCost() + " Gold");
            info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
            hBox.getChildren().add(info);
            cityCommandsBox.getChildren().add(hBox);
        }

        Button back = new Button("back");
        back.getStyleClass().add("menu-button");
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeProductionPanel(city, pane);
            }
        });
        cityCommandsBox.getChildren().add(back);

    }

    private static void purchase(City city, Pane pane, Producible chosenPurchasable){
        if (controller.getCityOwnerGoldCount(city) < chosenPurchasable.getCost()) {
            RegisterPageGraphicalController.showPopup("You don't have enough gold to purchase this item!");
            makeProductionPanel(city, pane);
            return;
        }

        if (chosenPurchasable instanceof BuildingType) {
            controller.addBuildingToCity(city, (BuildingType) chosenPurchasable);
            RegisterPageGraphicalController.showPopup("Successfully purchased " + chosenPurchasable.getName());
            makeProductionPanel(city, pane);
        }
        if (chosenPurchasable instanceof UnitType) {
            if (controller.doesPackingLetUnitEnterCity(city, (UnitType) chosenPurchasable)) {
                controller.createUnit((UnitType) chosenPurchasable, city.getOwner(), city.getCentralTile());
                RegisterPageGraphicalController.showPopup("Successfully purchased " + chosenPurchasable.getName());
                try {
                    Main.loadFxmlFile("CivilizationGamePage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                RegisterPageGraphicalController.showPopup("Your city is full! A new unit can't enter. Move the existing units and try again");
                makeProductionPanel(city, pane);
                return;
            }
        }

    }

    public static void makeChooseProductionPanel(City inCity, Pane pane){
        City city = reloadCity(inCity);
        if(controller.getCityEntityInProduction(city) != null){
            RegisterPageGraphicalController.showPopup("This city is already producing a " + controller.getCityEntityInProduction(city).getName() + ". Its production will be halted if you choose another production");
        }
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        Text text = new Text("Choose This City's Next Production From The Below Lists: (by clicking on images)");
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(text);
        ArrayList<UnitType> producibleUnits = controller.calculateCityProductionReadyUnitTypes(city);
        ArrayList<BuildingType> producibleBuildings = controller.calculateCityProductionReadyBuildingTypesFalseValue(city);
        Text unitsText = new Text("Units:");
        unitsText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(unitsText);
        for (UnitType producibleUnit : producibleUnits) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            int hammerCost = controller.calculateProductionHammerCost(producibleUnit);
            int turnsRequired = (int) Math.ceil((double) hammerCost / controller.calculateOutputForCity(city).getProduction());
            Circle circle = new Circle();
            circle.setRadius(15);
            circle.setFill(Images.getImage(producibleUnit.getName()));
            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(!controller.doesPackingLetUnitEnterCity(city, producibleUnit)){
                        RegisterPageGraphicalController.showPopup("There is already a unit in the city. You need to move it to make room!");
                        makeProductionPanel(city, pane);
                        return;
                    }
                    controller.changeCityProduction(city, producibleUnit);
                    RegisterPageGraphicalController.showPopup("Set city's production to " + producibleUnit.getName());
                    makeProductionPanel(city, pane);
                }
            });
            hBox.getChildren().add(circle);
            String info = producibleUnit.getName() + ",\t\t\t" + hammerCost + " Hammers, " + turnsRequired + " turns";

            HashMap<StrategicResource, Integer> resources = producibleUnit.getPrerequisiteResources();
            for (StrategicResource resource : resources.keySet()) {
                info = info + "\t" + resource.getName() + ": " + resources.get(resource);
            }
            Text infoText = new Text(info);
            infoText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
            hBox.getChildren().add(infoText);
            cityCommandsBox.getChildren().add(hBox);
        }
        Text buildingsText = new Text("Buildings:");
        buildingsText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
        cityCommandsBox.getChildren().add(buildingsText);

        for (BuildingType producibleBuilding : producibleBuildings) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            int hammerCost = controller.calculateProductionHammerCost(producibleBuilding);
            int turnsRequired = (int) Math.ceil((double) hammerCost / controller.calculateOutputForCity(city).getProduction());
            Circle circle = new Circle();
            circle.setRadius(15);
            circle.setFill(Images.getImage(producibleBuilding.getName()));
            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    controller.changeCityProduction(city, producibleBuilding);
                    RegisterPageGraphicalController.showPopup("Set city's production to " + producibleBuilding.getName());
                    makeProductionPanel(city, pane);
                }
            });
            hBox.getChildren().add(circle);
            Text info = new Text(producibleBuilding.getName() + ",\t\t\t" + hammerCost + " Hammers, " + turnsRequired + " turns");
            info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #ee0606;");
            hBox.getChildren().add(info);
            cityCommandsBox.getChildren().add(hBox);
        }

        Button back = new Button("back");
        back.getStyleClass().add("menu-button");
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeProductionPanel(city, pane);
            }
        });
        cityCommandsBox.getChildren().add(back);
    }

    public static void makeCitizenManagementPanel(City city, Pane pane){
        city = reloadCity(city);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        ArrayList<CitizenManagementPanelCommands> allCommands = CitizenManagementPanelCommands.getAllCommands();
        for(int i = 0; i < allCommands.size(); i++){
            addCitizenManagementButtonForCommand(city, pane, allCommands.get(i));
        }

    }

    public static void makeCitizenManagementShowInfoPanel(City inCity, Pane pane){
        City city = reloadCity(inCity);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(true);
        cityCommandsBox.setDisable(false);
        Button showWorkedTiles = new Button("Tiles being worked");
        showWorkedTiles.getStyleClass().add("menu-button");
        showWorkedTiles.setPrefWidth(150);
        showWorkedTiles.setPrefHeight(80);
        showWorkedTiles.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showWorkingTiles(city, pane);
            }
        });
        showWorkedTiles.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i = 0; i < pane.getChildren().size(); i++){
                    if(pane.getChildren().get(i) instanceof Polygon){
                        Polygon hexagon = (Polygon) pane.getChildren().get(i);
                        hexagon.setStroke(null);
                    }
                }
            }
        });
        cityCommandsBox.getChildren().add(showWorkedTiles);
        Button showNonWorkedTiles = new Button("Tiles not being worked");
        showNonWorkedTiles.getStyleClass().add("menu-button");
        showNonWorkedTiles.setPrefWidth(150);
        showNonWorkedTiles.setPrefHeight(80);
        showNonWorkedTiles.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showNonWorkingTiles(city, pane);
            }
        });
        showNonWorkedTiles.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i = 0; i < pane.getChildren().size(); i++){
                    if(pane.getChildren().get(i) instanceof Polygon){
                        Polygon hexagon = (Polygon) pane.getChildren().get(i);
                        hexagon.setStroke(null);
                    }
                }
            }
        });
        cityCommandsBox.getChildren().add(showNonWorkedTiles);

        Button workingCitizens = new Button("Working citizens");
        workingCitizens.getStyleClass().add("menu-button");
        workingCitizens.setPrefWidth(150);
        workingCitizens.setPrefHeight(80);
        workingCitizens.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showWorkingCitizenPanel(city, pane);
            }
        });
        cityCommandsBox.getChildren().add(workingCitizens);

        Button nonWorkingCitizens = new Button("Workless citizens");
        nonWorkingCitizens.getStyleClass().add("menu-button");
        nonWorkingCitizens.setPrefWidth(150);
        nonWorkingCitizens.setPrefHeight(80);
        nonWorkingCitizens.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showNonWorkingCitizenPanel(city, pane);
            }
        });
        cityCommandsBox.getChildren().add(nonWorkingCitizens);

        Button back = new Button("Back");
        back.getStyleClass().add("menu-button");
        back.setPrefWidth(150);
        back.setPrefHeight(80);
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeCitizenManagementPanel(city, pane);
            }
        });
        cityCommandsBox.getChildren().add(back);
    }

    private static void showNonWorkingTiles(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile && controller.doesCityNonWorkingTilesContainsTile(city, (Tile) tileImage)){
                    hexagon.setStroke(Color.RED);
                }
            }
        }
    }

    private static void showWorkingTiles(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile && controller.doesCityWorkingTilesContainsTile(city, ((Tile) tileImage))){
                    hexagon.setStroke(Color.RED);
                }
            }
        }
    }

    private static void addCitizenManagementButtonForCommand(City city, Pane pane, CitizenManagementPanelCommands command){
        Button button = new Button(command.getName());
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(150);
        button.setPrefHeight(80);
        cityCommandsBox.getChildren().add(button);
        if(command.getName().equals(CitizenManagementPanelCommands.BACK.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeTheCityActionTab(city, pane);
                }
            });
        }
        else if(command.getName().equals(CitizenManagementPanelCommands.WORK_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToWork(city, pane);
                }
            });
        }
        else if(command.getName().equals(CitizenManagementPanelCommands.FREE_TILE.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    waitForChoosingTileToFree(city, pane);
                }
            });

        }
        else if(command.getName().equals(CitizenManagementPanelCommands.SHOW_INFO.getName())){
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    makeCitizenManagementShowInfoPanel(city, pane);
                }
            });

        }
    }

    private static void waitForChoosingTileToWork(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        workTile(city, getTileImageFromHexagon(hexagon), pane);
                    }
                });
            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void waitForChoosingTileToFree(City city, Pane pane){
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        freeTile(city, getTileImageFromHexagon(hexagon), pane);
                    }
                });
            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void freeTile(City city, TileImage tileImage, Pane pane) {
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only choose visible tiles.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Tile tile = (Tile) tileImage;
        if (!controller.doesCityHasTileInTerritory(city, tile)) {
            RegisterPageGraphicalController.showPopup("The tile you have entered is not in this city's territory!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (!controller.isCityTileBeingWorked(city, tile)) {
            RegisterPageGraphicalController.showPopup("This tile is not being worked!");
            makeTheCityActionTab(city, pane);
            return;
        }
        controller.freeCityTile(city, tile);
        RegisterPageGraphicalController.showPopup("Tile at Y: " + controller.findTileYCoordinateInMap(tile) + ", X: " + controller.findTileXCoordinateInMap(tile) + " freed! A citizen is out of work!");
        makeTheCityActionTab(city, pane);
    }

    private static void workTile(City city, TileImage tileImage, Pane pane) {
        if(!(tileImage instanceof Tile)){
            RegisterPageGraphicalController.showPopup("You can only choose visible tiles.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Tile tile = (Tile) tileImage;
        if (!controller.doesCityHasTileInTerritory(city, tile)) {
            RegisterPageGraphicalController.showPopup("The tile you have entered is not in this city's territory!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (controller.calculateDistanceFromTile(tile, city.getCentralTile()) > 2) {
            RegisterPageGraphicalController.showPopup("This tile is too far away from city center!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (controller.isCityTileBeingWorked(city, tile)) {
            RegisterPageGraphicalController.showPopup("This tile is already being worked!");
            makeTheCityActionTab(city, pane);
            return;
        }
        if (controller.calculateWorklessCitizenCountForCity(city) == 0) {
            RegisterPageGraphicalController.showPopup("There are no workless citizens to assign to this tile! Free a citizen and try again.");
            makeTheCityActionTab(city, pane);
            return;
        }
        Citizen citizen = controller.getCityWorklessCitizen(city);
        controller.assignCitizenToWorkPlaceForCity(city, tile, citizen);
        RegisterPageGraphicalController.showPopup("Citizen assigned to tile at Y: " + controller.findTileYCoordinateInMap(tile) + ", X: " + controller.findTileXCoordinateInMap(tile));
        makeTheCityActionTab(city, pane);
    }

    public static void waitForChoosingTileToBuy(City city, Pane pane){
        ArrayList<Tile> purchasableTiles = controller.findCityPurchasableTiles(city);
        //city.getOwner().setGoldCount(1000);
        for(int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Polygon){
                Polygon hexagon = (Polygon) pane.getChildren().get(i);
                hexagon.setDisable(true);
                TileImage tileImage = getTileImageFromHexagon(hexagon);
                if(tileImage instanceof Tile){
                    for(int j = 0; j < purchasableTiles.size(); j++){
                        if(purchasableTiles.get(j).getId() == ((Tile) tileImage).getId()){
                            hexagon.setStroke(Color.RED);
                            hexagon.setDisable(false);
                            hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    purchaseTile(city, (Tile) tileImage);
                                }
                            });
                            break;
                        }
                    }

                }

            }
            else if(pane.getChildren().get(i) instanceof Circle){
                pane.getChildren().get(i).setDisable(true);
            }
        }
    }

    private static void purchaseTile(City city, Tile tile){
        int cost = controller.calculateNextTilePriceForCity(city);
        if (cost > controller.getCityOwnerGoldCount(city)) {
            RegisterPageGraphicalController.showPopup("You don't have enough gold to buy this tile!");
            return;
        }
        controller.decreaseCivilizationGold(currentPlayer, cost);
        controller.addTileToCityTerritory(city, tile);
        RegisterPageGraphicalController.showPopup("Tile successfully purchased!");
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deselectCity(City city) {
        controller.deselectCity(city);
        cityCommandsBox.getChildren().clear();
        cityActionTabPane.setVisible(false);
        cityCommandsBox.setDisable(true);
        try {
            Main.loadFxmlFile("CivilizationGamePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TileImage getTileImageFromHexagon(Polygon hexagon){
        double startXCoordinate = hexagon.getPoints().get(0);
        double startYCoordinate = hexagon.getPoints().get(1);
        for(int i = 0; i < tilesToShow.length; i++){
            for(int j = 0; j < tilesToShow[i].length; j++){
                double xCoordinate = 160 + (double) 32 / (double) 2 * (1 + 3 * j);
                int isOdd = 1;
                if(controller.findTileXCoordinateInMap(currentPlayer.getFrameBase()) % 2 == 1){
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

    private static void showNonWorkingCitizenPanel(City city, Pane gamePagePane){
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        pane.getStylesheets().addAll(gamePagePane.getStylesheets());
        pane.getStyleClass().add("shadow-pane");
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        pane.setCenter(vbox);

        Text text = new Text("Workless citizens count:");
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        Text num = new Text(String.valueOf(controller.calculateWorklessCitizenCountForCity(city)));
        num.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(text);
        vbox.getChildren().add(num);
        HBox hBox = new HBox();
        Circle circle = new Circle();
        try {
            circle.setRadius(25);
            circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/Food.png").toExternalForm()).toExternalForm())));
            hBox.getChildren().add(circle);
            Text foodCount = new Text(String.valueOf(controller.calculateWorklessCitizenCountForCity(city)));
            foodCount.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
            hBox.getChildren().add(foodCount);
            hBox.setAlignment(Pos.CENTER);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        vbox.getChildren().add(hBox);

        Button button = new Button();
        button.setText("Ok");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.hide();
            }
        });
        vbox.getChildren().add(button);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();


    }

    private static void showWorkingCitizenPanel(City city, Pane gamePagePane){
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        pane.getStylesheets().addAll(gamePagePane.getStylesheets());
        pane.getStyleClass().add("shadow-pane");
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        pane.setCenter(vbox);

        Text text = new Text("Working citizens count:");
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        Text num = new Text(String.valueOf(city.getCitizens().size() - controller.calculateWorklessCitizenCountForCity(city)));
        num.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(text);
        vbox.getChildren().add(num);
        HBox hBox = new HBox();
        Circle circle = new Circle();
        try {
            circle.setRadius(25);
            circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/Food.png").toExternalForm()).toExternalForm())));
            hBox.getChildren().add(circle);
            Text foodCount = new Text(String.valueOf(city.getCitizens().size() * 2));
            foodCount.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
            hBox.getChildren().add(foodCount);
            hBox.setAlignment(Pos.CENTER);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        vbox.getChildren().add(hBox);

        Button button = new Button();
        button.setText("Ok");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.hide();
            }
        });
        vbox.getChildren().add(button);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private static void showProductionInfo(City city, Pane gamePagePane) {
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        pane.getStylesheets().addAll(gamePagePane.getStylesheets());
        pane.getStyleClass().add("shadow-pane");
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        pane.setCenter(vbox);

        Text text = new Text("########### City Production Info ###########");
        text.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(text);

        Producible currentProduction = controller.getCityEntityInProduction(city);
        Text secondText = new Text("Currently producing: ");
        secondText.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        vbox.getChildren().add(secondText);

        if (currentProduction == null) {
            addTextToVBox(vbox, "Nothing!");
        } else {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            if(currentProduction instanceof BuildingType){
                circle.setFill(Images.getImage(currentProduction.getName()));
            }
            else if(currentProduction instanceof UnitType){
                circle.setFill(Images.getImage(currentProduction.getName()));
            }
            int hammerCost = controller.calculateProductionHammerCost(currentProduction);
            int productionOutput = controller.calculateOutputForCity(city).getProduction();
            int hammerCount = (int) city.getHammerCount();
            int turnsRemaining = (int) Math.ceil((double) (hammerCost - hammerCount) / productionOutput);
            String info = currentProduction.getName() + ": (hammers " + hammerCount + " out of " + hammerCost + ")" + " " + turnsRemaining + " turns remaining";
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, info);
            vbox.getChildren().add(hBox);
        }

        addTextToVBox(vbox, "Halted Productions:");
        for (Producible producible : city.getProductionReserve().keySet()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            if(producible instanceof BuildingType){
                circle.setFill(Images.getImage(producible.getName()));
            }
            else if(producible instanceof UnitType){
                circle.setFill(Images.getImage(producible.getName()));

            }
            String info = producible.getName() + ": " + city.getProductionReserve().get(producible) + " out of " + controller.calculateProductionHammerCost(producible);
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, info);
            vbox.getChildren().add(hBox);
        }

        addTextToVBox(vbox, "Production-Ready Units:");
        for (UnitType type : controller.calculateCityProductionReadyUnitTypes(city)) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            circle.setFill(Images.getImage(type.getName()));

            String info = type.getName();
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, info);
            vbox.getChildren().add(hBox);
        }


        addTextToVBox(vbox, "Production-Ready Buildings:");
        for (BuildingType type : controller.calculateCityProductionReadyBuildingTypes(city)) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            Circle circle = new Circle();
            circle.setRadius(30);
            circle.setFill(Images.getImage(type.getName()));

            String info = type.getName();
            hBox.getChildren().add(circle);
            addTextToHBox(hBox, info);
            vbox.getChildren().add(hBox);
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
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }

    private static void addTextToVBox(VBox box, String text){
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }

    private static void addTextToHBox(HBox box, String text){
        Text info = new Text(text);
        info.setStyle("-fx-font-family: \"Times New Roman\"; -fx-font-size: 18; -fx-fill: #00bbff;");
        box.getChildren().add(info);
    }

    private static void showCityInfo(City city, Pane gamePagePane) {
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


        if (controller.isCityCapital(city)) {
            addTextToVBox(vbox, controller.getCurrentPlayer().getName() + "'s Capital City");
        } else {
            addTextToVBox(vbox, controller.getCurrentPlayer().getName() + "'s City");
        }
        addTextToVBox(vbox, "Y: " + controller.findTileYCoordinateInMap(city.getCentralTile()) + ", X: " + controller.findTileXCoordinateInMap(city.getCentralTile()));
        addTextToVBox(vbox, "The following tiles comprise this city's territory:");
        for (Tile tile : city.getTerritories()) {
            if (tile != city.getCentralTile()) {
                String info = "Y: " + controller.findTileYCoordinateInMap(tile) + ", X: " + controller.findTileYCoordinateInMap(tile) + " ";
                if (controller.isCityTileBeingWorked(city, tile)) {
                    info = info + "(worked)";
                } else {
                    info = info + "(not worked)";
                }
                addTextToVBox(vbox, info);
            }
        }

        addTextToVBox(vbox, "Resources in this city:");
        for (Tile tile : city.getTerritories()) {
            for (Resource resource : tile.getResourcesAsArrayList()) {
                HBox hBox = new HBox();
                hBox.setSpacing(20);
                hBox.setAlignment(Pos.CENTER);
                Circle circle = new Circle();
                circle.setRadius(30);
                circle.setFill(Images.getImage(resource.getName()));
                hBox.getChildren().add(circle);
                String info = resource.getName();
                if (resource.canBeExploited(tile)) {
                    info = info + " (exploited by " + resource.getPrerequisiteImprovement().getName() + ")";
                } else {
                    info = info + " (not exploited, requires " + resource.getPrerequisiteImprovement().getName() + ")";
                }
                addTextToHBox(hBox, info);
                vbox.getChildren().add(hBox);
            }
        }
        addTextToVBox(vbox, "This city has " + city.getCitizens().size() + " citizens. " + city.calculateWorklessCitizenCount()
                + " of them are workless.");
        addTextToVBox(vbox, "Hit Points Left: " + city.getHitPointsLeft());
        addTextToVBox(vbox, "City's food balance:");
        addTextToVBox(vbox, String.valueOf(city.getFoodCount()));

        String productionName = (city.getEntityInProduction() == null) ? "nothing!" : city.getEntityInProduction().getName();
        addTextToVBox(vbox, "This city is producing " + productionName);
        addLineForOutputTypeToVBox("Food", "Food Output: " + controller.calculateOutputForCity(city).getFood(), vbox);
        addLineForOutputTypeToVBox("Production", "Production Output: " + controller.calculateOutputForCity(city).getProduction(), vbox);
        addLineForOutputTypeToVBox("Science", "Science Output: " + controller.calculateCityBeakerProduction(city), vbox);
        addLineForOutputTypeToVBox("Gold", "Gold Output: " + controller.calculateOutputForCity(city).getGold(), vbox);

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

    private static void addLineForOutputTypeToVBox(String type, String text, VBox box) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        Circle circle = new Circle();
        circle.setRadius(30);
        try {
            circle.setFill(new ImagePattern(new Image(new URL(Main.class.getResource("/images/Outputs/" + type + ".png").toExternalForm()).toExternalForm())));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        hBox.getChildren().add(circle);
        addTextToHBox(hBox, text);
        box.getChildren().add(hBox);
    }

}
