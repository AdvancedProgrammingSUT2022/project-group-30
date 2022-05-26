package views.transitions;

import javafx.animation.Transition;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class LoadingProgressBarAnimation extends Transition {

    private ProgressBar progressBar;

    public LoadingProgressBarAnimation(ProgressBar progressBar){
        this.progressBar = progressBar;
        this.setCycleDuration(Duration.millis(30000));
        this.setCycleCount(1);
    }

    @Override
    protected void interpolate(double v) {
        progressBar.setProgress(v);
    }
}
