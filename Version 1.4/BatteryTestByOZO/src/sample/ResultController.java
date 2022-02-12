package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class ResultController {
    private Data dataSelected;

    private File fileSelected;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("sample.langue");

    @FXML
    private VBox vBoxResult;
    @FXML
    private Text clientNameResult;
    @FXML
    private Text batteryNameResult;
    @FXML
    private Text amperage;
    @FXML
    private Text voltageFinal;
    @FXML
    private Text ampHoursFinal;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number,Number> graph;

    public int initData(Data data) {
        dataSelected = data;
        clientNameResult.setText(clientNameResult.getText() + dataSelected.getClientName());
        batteryNameResult.setText(batteryNameResult.getText() + dataSelected.getBatteryName());
        fileSelected = data.getFileData();

        xAxis.setLabel(resourceBundle.getString("axeX"));
        yAxis.setLabel(resourceBundle.getString("axeY"));
        graph.setCreateSymbols(false);
        graph.setLegendVisible(false);
        XYChart.Series series = new XYChart.Series();

        if (fileSelected != null) {

            BufferedReader b = null;
            try {
                b = new BufferedReader(new FileReader(fileSelected));
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

            String readLine = "";

            String ampHours = "";
            String voltage = "";

            while (true) {
                try {
                    if (!((readLine = b.readLine()) != null)) break;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                String[] lineSplitted = readLine.split(" ");
                ampHours =  lineSplitted[1];
                voltage =  lineSplitted[2];

                try {
                    series.getData().add(new XYChart.Data(Float.parseFloat(lineSplitted[1]),Float.parseFloat(lineSplitted[2])));
                } catch(java.lang.NumberFormatException numberFormatException) {
                    Parent root1 = null;
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorFile.fxml"));
                    fxmlLoader.setResources(resourceBundle);
                    try {
                        root1 = (Parent) fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.setScene(new Scene(root1));
                    stage.show();

                    return 0;
                }

            }

            graph.getData().add(series);

            ampHoursFinal.setText(ampHoursFinal.getText() + ampHours + " Ah");
            voltageFinal.setText(voltageFinal.getText() + voltage + " V");

        }

        return 1;
    }

    @FXML
    public void generatePDF(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage)vBoxResult.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(dir.getAbsolutePath()+"/"+
                            dataSelected.getClientName() + " - " +
                            dataSelected.getBatteryName()+".pdf"));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        Font title = new Font(Font.FontFamily.UNDEFINED,18,Font.NORMAL);
        Font data = new Font(Font.FontFamily.UNDEFINED,10,Font.NORMAL);

        Image logoNorauto = null;
        try {
            logoNorauto = Image.getInstance(Objects.requireNonNull(this.getClass().getClassLoader().getResource("logo-norauto.png")));
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        assert logoNorauto != null;
        logoNorauto.scalePercent(50);
        logoNorauto.setAlt("logo-norauto");

        WritableImage image = graph.snapshot(new SnapshotParameters(), null);
        Date date = new Date();
        DateFormat formatDate = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);

        ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();

        try {
            ImageIO.write( SwingFXUtils.fromFXImage( image, null ), "png", byteOutput );

            com.itextpdf.text.Image  graph;

            graph = Image.getInstance( byteOutput.toByteArray());
            graph.scaleAbsolute(540,280);
            document.add(logoNorauto);
            document.add(new Paragraph(new Chunk(batteryNameResult.getText(),title)));
            document.add(new Paragraph(new Chunk(clientNameResult.getText())));
            document.add(new Paragraph(new Chunk(resourceBundle.getString("testDate") + formatDate.format(date))));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(new Chunk(resourceBundle.getString("dischargeCycle"))));
            document.add(new Paragraph(new Chunk(amperage.getText(),data)));
            document.add(new Paragraph(new Chunk(ampHoursFinal.getText(),data)));
            document.add(new Paragraph(new Chunk(voltageFinal.getText(),data)));
            document.add(graph);

            document.close();

            Desktop.getDesktop().open(
                    new File(dir.getAbsolutePath()+"/"+
                            dataSelected.getClientName() + " - " +
                            dataSelected.getBatteryName()+".pdf")
            );

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }
}
