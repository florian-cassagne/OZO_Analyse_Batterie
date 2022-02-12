package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(new Locale("fr"));
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("sample.fxml"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("sample.langue");
        fxmlLoader.setResources(resourceBundle);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(resourceBundle.getString("titleApplication"));
        primaryStage.setScene(new Scene(root, 450, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
