package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Controller {

    @FXML
    private VBox vbox;

    @FXML
    private Button selectFile;

    @FXML
    private TextField clientName;

    @FXML
    private TextField batteryName;

    File selectedFile;
    BufferedReader b = null;

    @FXML
    public void readData(ActionEvent event) {

        if(!clientName.getText().isEmpty() && !batteryName.getText().isEmpty() && selectedFile != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("result.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultController resultController = fxmlLoader.getController();
            resultController.initData(new Data(clientName.getText(), batteryName.getText(), selectedFile));

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();

        } else {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorForm.fxml"));
            Parent root2 = null;
            try {
                root2 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root2));
            stage.show();

        }
    }

    @FXML
    public void selectDataFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)vbox.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        selectFile.setText(selectedFile.getName());
    }
}
