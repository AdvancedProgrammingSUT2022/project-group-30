package views.controllers;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseMapPageController {

    private GameController controller = GameController.getGameController();

    private int playersCount;

    private int mapHeight;

    private int mapWidth;

    private int startingXPosition;

    private int startingYPosition;


    @FXML
    private BorderPane pane;
    @FXML
    private VBox box;

    @FXML
    public void initialize(){
        this.playersCount = controller.getGameDataBase().getPlayers().size();
        setDefaultMapDimensions();
    }

    private void setDefaultMapDimensions(){
        switch (this.playersCount){
            case 2:
                this.mapHeight = 20;
                this.mapWidth = 30;
                this.startingYPosition = 0;
                this.startingXPosition = 8;
                break;
            case 3:
                this.mapHeight = 40;
                this.mapWidth = 40;
                this.startingYPosition = 12;
                this.startingXPosition = 14;
                break;
            case 4:
                this.mapHeight = 52;
                this.mapWidth = 57;
                this.startingYPosition = 0;
                this.startingXPosition = 22;
                break;
            case 5:
                this.mapHeight = 52;
                this.mapWidth = 80;
                this.startingYPosition = 0;
                this.startingXPosition = 0;
                break;
            default:
                break;
        }
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("PlayersCountPage");
    }

    public void start(MouseEvent mouseEvent) throws IOException {
        controller.initializeGame(this.mapHeight, this.mapWidth, this.startingYPosition, this.startingXPosition);
        Main.loadFxmlFile("CivilizationGamePage");
    }

    private void mapDimensionsErrorHandling(int height, int width){
        if(height > 52 || width > 80){
            RegisterPageGraphicalController.showPopup("This map is extremely large!");
            return;
        }
        switch (this.playersCount){
            case 2:
                if(height < 20 || width < 30){
                    RegisterPageGraphicalController.showPopup("Minimum height : 20, Minimum width : 30");
                    return;
                }
                break;
            case 3:
                if(height < 30 || width < 40){
                    RegisterPageGraphicalController.showPopup("Minimum height : 30, Minimum width : 40");
                    return;
                }
                break;
            case 4:
                if(height < 40 || width < 50){
                    RegisterPageGraphicalController.showPopup("Minimum height : 40, Minimum width : 50");
                    return;
                }
                break;
            case 5:
                if(height < 50 || width < 60){
                    RegisterPageGraphicalController.showPopup("Minimum height : 50, Minimum width : 60");
                    return;
                }
                break;
            default:
                break;
        }
        this.mapHeight = height;
        this.mapWidth = width;
        this.startingXPosition = 0;
        this.startingYPosition = 0;
        RegisterPageGraphicalController.showPopup("Your changes are saved.");
    }

    public void mapDimensions(MouseEvent mouseEvent) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(720);
        borderPane.setPrefWidth(1280);
        borderPane.getStylesheets().addAll(pane.getStylesheets());
        borderPane.getStyleClass().add("enteringMenus");
        VBox vbox = new VBox();
        borderPane.setCenter(vbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);

        TextField height = new TextField();
        height.getStyleClass().add("text-field");
        height.setPromptText("Enter your map Height:");
        height.setMaxHeight(50);
        height.setMaxWidth(300);
        vbox.getChildren().add(height);

        TextField width = new TextField();
        width.getStyleClass().add("text-field");
        width.setPromptText("Enter your map Width:");
        width.setMaxHeight(50);
        width.setMaxWidth(300);
        vbox.getChildren().add(width);

        Button submit = new Button();
        submit.setText("Submit");
        submit.getStyleClass().add("menu-button");
        submit.setPrefHeight(50);
        submit.setPrefWidth(200);
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mapDimensionsErrorHandling(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()));
                try {
                    start(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button back = new Button();
        back.setText("Back");
        back.getStyleClass().add("menu-button");
        back.setPrefHeight(50);
        back.setPrefWidth(200);
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.loadFxmlFile("ChooseMapPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        vbox.getChildren().add(submit);
        vbox.getChildren().add(back);

        Main.getScene().setRoot(borderPane);
    }
}
