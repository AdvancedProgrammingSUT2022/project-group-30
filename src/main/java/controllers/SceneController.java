package controllers;

import views.View;

public class SceneController {
    private static SceneController sceneController;

    private View nextView;

    private SceneController() {
        nextView = null;
    }

    public static SceneController getSceneController() {
        return sceneController == null ? sceneController = new SceneController() : sceneController;
    }

    public void run() {
        while (this.nextView != null) {
            this.nextView.run();
        }
    }

    public void setNextView(View view) {
        this.nextView = view;
    }

    public View getNextView() {
        return this.nextView;
    }

}
