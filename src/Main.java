import controllers.SceneController;
import views.LoginPageView;

public class Main {
    public static void main(String[] args){
        SceneController sceneController = SceneController.getSceneController();
        LoginPageView loginPageView = LoginPageView.getLoginPageView();
        loginPageView.setController();
        sceneController.setNextView(loginPageView);
        sceneController.run();
    }
}
