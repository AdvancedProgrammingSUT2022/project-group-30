package views.customcomponents;

import controllers.ChatController;
import controllers.NetworkController;
import controllers.ProgramController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.ProgramDatabase;
import models.chat.Message;
import views.Main;

import java.io.IOException;

public class MessageBox extends ListCell<Message> {
    public MessageBox(String fxmlName) {
        this.fxmlName = fxmlName;
    }

    private String fxmlName;
    private boolean isViewerSender;
    @Override
    public void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        this.getStyleClass().add("messageBox");
        if (item != null && !empty) {
            isViewerSender = item.getSenderId() == ProgramController.getProgramController().getLoggedInUser(NetworkController.getNetworkController().getToken()).getId();
            AnchorPane pane = new AnchorPane();
            MessageComponent box = new MessageComponent(item, isViewerSender);
            pane.getChildren().add(box);
            if (isViewerSender) {
                AnchorPane.setRightAnchor(box, 10.0);
            } else {
                AnchorPane.setLeftAnchor(box, 10.0);
            }
            setGraphic(pane);
            if (isViewerSender) {
                this.setContextMenu(createContextMenu(item));
            }
        }
    }

    private ContextMenu createContextMenu(Message item) {
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
        return contextMenu;
    }

    private void deleteForMe(Message message) {
        getListView().getItems().remove(message);
    }

    private void deleteForEveryone(Message message) {
        ChatController.getChatController().deleteMessage(message.getId());
        try {
            Main.loadFxmlFile(fxmlName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditPopup(Message message) {
        Stage stage = new Stage();
        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(10.0);
        box.setPadding(new Insets(10, 10, 10, 10));
        TextArea textArea = new TextArea();
        textArea.setText(message.getText());
        textArea.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15");
        textArea.setPrefRowCount(3);
        textArea.selectEnd();
        Button button = new Button();
        button.setText("Edit");
        button.setPrefWidth(100);
        button.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 20; -fx-background-color: #007900; -fx-text-fill: white");
        button.setOnAction(event -> {
            ChatController.getChatController().editMessageText(message.getId(), textArea.getText());
            try {
                Main.loadFxmlFile(fxmlName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        });
        box.getChildren().addAll(textArea, button);
        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
    }
}