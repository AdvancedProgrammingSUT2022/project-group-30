package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import views.Main;
import views.transitions.LoadingProgressBarAnimation;

import java.io.IOException;

public class StartPageController {

    @FXML
    private Pane pane;
    @FXML
    private ProgressBar loadingProgressBar;

    @FXML
    public void initialize(){
        loadingProgressBar.setProgress(0);
        VBox box = (VBox) pane.getChildren().get(0);
        box.setLayoutX(pane.getPrefWidth() / 2 - box.getPrefWidth() / 2);
        box.setLayoutY(600);
        LoadingProgressBarAnimation animation = new LoadingProgressBarAnimation(loadingProgressBar);
        animation.play();
    }

    @FXML
    protected void goToChat() throws IOException {
        Main.loadFxmlFile("ChatMainPage");
    }

}
