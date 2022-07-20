package views.controllers;

import javafx.beans.binding.Bindings;
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
import javafx.scene.text.Text;
import models.Notification;
import models.ProgramDatabase;
import models.User;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;

public class ReceivedInvitationsController {

    @FXML
    private TableView<Notification> notifications;
    @FXML
    private TableColumn<Notification, String> sender;
    @FXML
    private TableColumn<Notification, String> invitationText;


    @FXML
    public void initialize(){
        notifications.setFixedCellSize(60);
        ArrayList<Notification> invitations = ProgramDatabase.getProgramDatabase().getLoggedInUser().getInvitations();
        invitationText.setCellValueFactory(new PropertyValueFactory<>("text"));
        sender.setCellValueFactory(new PropertyValueFactory<>("text"));

        sender.setCellFactory(col -> {
            TableCell<Notification, String> cell = new TableCell<>();
            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    String from = newValue.split(" ")[0];
                    Text fromSender = new Text(from);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(fromSender));
                }
            });
            return cell;
        });

        ObservableList<Notification> list = FXCollections.observableArrayList(invitations);
        notifications.setItems(list);


    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Main.loadFxmlFile("GamePage");
    }
}
