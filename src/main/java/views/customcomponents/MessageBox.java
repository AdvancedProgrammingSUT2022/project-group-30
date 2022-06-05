package views.customcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import models.ProgramDatabase;
import models.chat.Message;

public class MessageBox extends ListCell<Message> {
    @Override
    public void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        this.getStyleClass().add("messageBox");
        if (item != null && !empty) {
            AnchorPane pane = new AnchorPane();
            MessageComponent box = new MessageComponent(item);
            pane.getChildren().add(box);
            if (item.getSenderId() == ProgramDatabase.getProgramDatabase().getLoggedInUser().getId()) {
                AnchorPane.setRightAnchor(box, 10.0);
            } else {
                AnchorPane.setLeftAnchor(box, 10.0);
            }
            setGraphic(pane);
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteForMeItem = new MenuItem();
            deleteForMeItem.setText("Delete for me");
            deleteForMeItem.setOnAction(event -> {
                deleteForMe(item);
            });
            MenuItem deleteForEveryoneItem = new MenuItem();
            deleteForEveryoneItem.setText("Delete for everybody");
            deleteForEveryoneItem.setOnAction(event -> {
                deleteForEveryone(item);
            });
            MenuItem editItem = new MenuItem();
            editItem.setText("Edit");
            editItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    showEditPopup(item);
                }
            });
            contextMenu.getItems().addAll(deleteForMeItem, deleteForEveryoneItem, editItem);
            contextMenu.getStyleClass().add("messageContextMenu");
            this.setContextMenu(contextMenu);
        }
    }

    private void deleteForMe(Message message) {
        getListView().getItems().remove(message);
    }

    private void deleteForEveryone(Message message) {
        getListView().getItems().remove(message);
    }

    private void showEditPopup(Message message) {

    }
}