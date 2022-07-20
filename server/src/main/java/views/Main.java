package views;

import controllers.LoginPageController;
import controllers.NetworkController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.ProgramDatabase;

import java.io.IOException;
import java.net.URL;

public class Main{

    private static Scene scene;
    private static Stage stage;
    public static void main(String[] args) {
        NetworkController networkController = NetworkController.getNetworkController();
        networkController.run();
    }
    public static void loadFxmlFile(String name) throws IOException {
        URL url = new URL(Main.class.getResource("/fxml/" + name + ".fxml").toExternalForm());
        Parent root = FXMLLoader.load(url);
        if (scene == null) {
            scene = new Scene(root);
        }
        scene.setRoot(root);
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }
}

//public class Main extends Application {
//
//    private static Scene scene;
//    private static Stage stage;
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        Main.stage = stage;
//        loadFxmlFile("StartPage");
//        stage.setScene(scene);
//        stage.setTitle("Civilization V");
//        stage.setResizable(false);
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent windowEvent) {
//                if (ProgramDatabase.getProgramDatabase().getLoggedInUser() != null) {
//                    ProgramDatabase.getProgramDatabase().updateLoggedInUserLastLoginTime();
//                    LoginPageController.writeUsersListToFile();
//                }
//            }
//        });
//        stage.show();
//    }
//
//    public static void loadFxmlFile(String name) throws IOException {
//        URL url = new URL(Main.class.getResource("/fxml/" + name + ".fxml").toExternalForm());
//        Parent root = FXMLLoader.load(url);
//        if (scene == null) {
//            scene = new Scene(root);
//        }
//        scene.setRoot(root);
//    }
//
//    public static Scene getScene() {
//        return scene;
//    }
//
//    public static Stage getStage() {
//        return stage;
//    }
//}

/*  here are the previous codes for Main.java in phase1

import controllers.SceneController;
import utilities.MyScanner;
import views.LoginPageView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = MyScanner.getScanner();
        SceneController sceneController = SceneController.getSceneController();
        LoginPageView loginPageView = LoginPageView.getLoginPageView();
        loginPageView.setController();
        sceneController.setNextView(loginPageView);
        sceneController.run();
        scanner.close();
    }
}
 */
