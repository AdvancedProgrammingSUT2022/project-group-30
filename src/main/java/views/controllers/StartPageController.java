package views.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import views.transitions.LoadingProgressBarAnimation;

public class StartPageController {

    @FXML
    private Pane pane;
    @FXML
    private ProgressBar loadingProgressBar;

    @FXML
    public void initialize(){
        //loadingProgressBar.setScaleX(640 - loadingProgressBar.getPrefWidth());
        //loadingProgressBar.setTranslateX(640);
       // ProgressBar loadingProgressBar = new ProgressBar(50);
        loadingProgressBar.setProgress(0);
        VBox box = (VBox) pane.getChildren().get(0);
        box.setLayoutX(pane.getPrefWidth() / 2 - box.getPrefWidth() / 2);
        box.setLayoutY(600);
        LoadingProgressBarAnimation animation = new LoadingProgressBarAnimation(loadingProgressBar);
        //pane.getChildren().add(loadingProgressBar);
        animation.play();
    }
}
