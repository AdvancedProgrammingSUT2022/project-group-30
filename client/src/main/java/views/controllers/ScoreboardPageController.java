package views.controllers;

import controllers.MainPageController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.ProgramDatabase;
import models.User;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardPageController {


    private ProgramController controller = ProgramController.getProgramController();

    @FXML
    private TableView<User> scoreboard;
    @FXML
    private TableColumn<User, Integer> rankColumn;
    @FXML
    private TableColumn<User, String> avatarColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Integer> scoreColumn;
    @FXML
    private TableColumn<User, String> lastScoreChangeTimeColumn;
    @FXML
    private TableColumn<User, String> lastLoginTimeColumn;
    @FXML
    public TableColumn<User, Boolean> isOnline;

    @FXML
    public void initialize(){
        this.controller.sortUsersArrayList();
        scoreboard.setFixedCellSize(60);
        ArrayList<User> users = controller.getUsers();
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        avatarColumn.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        lastScoreChangeTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastScoreChangeTime"));
        lastLoginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginTime"));
        isOnline.setCellValueFactory(new PropertyValueFactory<>("isOnline"));


        avatarColumn.setCellFactory(col -> {
            TableCell<User, String> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    ImageView image = new ImageView(new Image("file:src/main/resources/images/avatars/" + newValue, 50, 50, false, false));;
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(image));
                }
            });
            return cell;
        });

        ObservableList<User> list = FXCollections.observableArrayList(users);
        scoreboard.setItems(list);

        PseudoClass higlighted = PseudoClass.getPseudoClass("highlighted");

        scoreboard.setRowFactory(tableView -> {
            TableRow<User> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldOrder, newOrder) ->
                    row.pseudoClassStateChanged(higlighted, newOrder != null && newOrder.getUsername().equals(controller.getLoggedInUserUsername(NetworkController.getNetworkController().getToken()))));
            return row;
        });
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }

    public void refresh(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("ScoreboardPage");
    }
}
