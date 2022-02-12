package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private VBox vbox;

    @FXML
    private Button selectFile;

    @FXML
    private TextField clientName;

    @FXML
    private TextField batteryName;

    @FXML
    private MenuItem langageFr;

    @FXML
    private MenuItem langageEn;

    File selectedFile;
    BufferedReader b = null;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("sample.langue");

    @FXML
    public void readData(ActionEvent event) {

        Parent root1 = null;

        if(!clientName.getText().isEmpty() && !batteryName.getText().isEmpty() && selectedFile != null) {

            String[] nameFile = selectedFile.getName().split("\\.");

            if (nameFile[nameFile.length - 1].equals("txt")) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("result.fxml"));
                fxmlLoader.setResources(resourceBundle);
                try {
                    root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ResultController resultController = fxmlLoader.getController();

                int resultDataFile = resultController.initData(
                        new Data(clientName.getText(), batteryName.getText(), selectedFile));

                if(resultDataFile == 0) {
                    return;
                }

            } else {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorFile.fxml"));
                fxmlLoader.setResources(resourceBundle);
                try {
                    root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorForm.fxml"));
            fxmlLoader.setResources(resourceBundle);
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void selectDataFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)vbox.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        selectFile.setText(selectedFile.getName());
    }

    @FXML
    public void goToLangageFr(ActionEvent event) {
        Parent root1 = null;
        Locale.setDefault(new Locale("fr"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxmlLoader.setResources(resourceBundle);
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setResizable(false);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void goToLangageEn(ActionEvent event) {
        Locale.setDefault(new Locale("en"));
    }
}
