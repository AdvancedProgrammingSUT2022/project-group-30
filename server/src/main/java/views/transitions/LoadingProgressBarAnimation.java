package views.transitions;

import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import views.Main;

import java.io.IOException;

public class LoadingProgressBarAnimation extends Transition {

    private ProgressBar progressBar;

    public LoadingProgressBarAnimation(ProgressBar progressBar){
        this.progressBar = progressBar;
        this.setCycleDuration(Duration.millis(50));
        this.setCycleCount(1);
        this.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Main.loadFxmlFile("SecondPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void interpolate(double v) {
        progressBar.setProgress(v);
    }
}
