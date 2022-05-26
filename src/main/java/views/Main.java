package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        loadFxmlFile("LoginPage");
        stage.setScene(scene);
        stage.setTitle("civilization");
        stage.setResizable(false);
        stage.show();

    }

    public static void loadFxmlFile(String name) throws IOException {
        URL url = new URL(Main.class.getResource("/fxml/" + name + ".fxml").toExternalForm());
        Parent root = FXMLLoader.load(url);
        if(scene == null){
            scene = new Scene(root);
        }
        scene.setRoot(root);
    }

    public static Scene getScene(){
        return scene;
    }
}
