package views.controllers;

import controllers.MainPageController;
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

    private MainPageController controller = MainPageController.getMainPageController();

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
    public void initialize(){
        this.controller.sortUsersArrayList();
        scoreboard.setFixedCellSize(60);
        ArrayList<User> users = ProgramDatabase.getProgramDatabase().getUsers();
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        avatarColumn.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        lastScoreChangeTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastScoreChangeTime"));
        lastLoginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginTime"));

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
                    row.pseudoClassStateChanged(higlighted, newOrder != null && newOrder.getUsername().equals(ProgramDatabase.getProgramDatabase().getLoggedInUser().getUsername())));
            return row;
        });
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("MainPage");
    }
}
