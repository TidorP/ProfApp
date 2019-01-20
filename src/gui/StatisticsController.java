package gui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import domain.Nota;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ServiceNote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static gui.StudentsAppController.getFile;

public class StatisticsController {
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button b5;
    @FXML
    private Button b6;
    @FXML
    private Button b7;
    @FXML
    private Button b8;
    Stage stage;

    private ServiceNote service;

    public void setService(ServiceNote s,Stage stage){
        this.service = s;
        this.stage=stage;
        b1.getStyleClass().add("button");
        b2.getStyleClass().add("button");
        b3.getStyleClass().add("button");
        b4.getStyleClass().add("button");
        b5.getStyleClass().add("button");
        b6.getStyleClass().add("button");
        b7.getStyleClass().add("button");
        b8.getStyleClass().add("button");
    }

    private void addTableStudentScore(PdfPTable table) {
        Stream.of("Students Name", "Overall Score")
                .forEach(columnTitle -> {
                    StudentsAppController.addTable(table, columnTitle);
                });
    }
    private File getDestination(String filename)
    {
        return getFile(filename, stage);
    }

    @FXML
    public void createMediiPDF() throws IOException, DocumentException, URISyntaxException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getDestination("Medii.pdf").getAbsolutePath()));

        document.open();
        Paragraph p = new Paragraph("Average scores for each student:"+"\n\n");
        document.add(p);

        PdfPTable table = new PdfPTable(2);
        addMediiTableHeader(table);
        addMediiRows(table);

        document.add(table);
        document.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message Here...");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("PDF generated successfully!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

    }
    private void addMediiTableHeader(PdfPTable table) {
        addTableStudentScore(table);
    }
    private void addMediiRows(PdfPTable table) {
        Map<String,Double> result=service.medii();
        populateTable(table, result);

    }

    private void populateTable(PdfPTable table, Map<String, Double> result) {
        for (Map.Entry<String, Double> entry : result.entrySet())
        {
            String key = entry.getKey();
            Double medie=entry.getValue();
            table.addCell(key);
            table.addCell(Double.toString(medie));
        }
    }

    @FXML
    public void createPassedPDF() throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getDestination("SuccessfulStudents.pdf").getAbsolutePath()));

        document.open();
        Paragraph p = new Paragraph("Students who passed:"+"\n\n");
        document.add(p);

        PdfPTable table = new PdfPTable(2);
        addPassedTableHeader(table);
        addPassedRows(table);

        document.add(table);
        document.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message Here...");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("PDF generated successfully!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                //System.out.println("Pressed OK.");
            }
        });

    }
    private void addPassedTableHeader(PdfPTable table) {
        addTableStudentScore(table);
    }



    private void addPassedRows(PdfPTable table) {
        Map<String,Double> result=service.studentsPassed();
        populateTable(table, result);

    }


    @FXML
    public void createMediiPieChart(){
        Stage primaryStage=new Stage();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("Students Overall scores");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);


        List<PieChart.Data> l=new ArrayList<>();
        Map<String,Double> result=service.medii();
        for (Map.Entry<String, Double> entry : result.entrySet())
        {
            String key = entry.getKey();
            Double medie=entry.getValue();
            l.add(new PieChart.Data(key, medie));
        }
        ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(l);

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Scores");


        final Label caption = new Label("Hover over the pie chart to see the score percentages for each student!");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 15 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue()*10) + "%");
                        }
                    });
        }

        ((Group) scene.getRoot()).getChildren().addAll(chart, caption);
        //((Group) scene.getRoot()).getChildren().add(chart);
        primaryStage.setScene(scene);


        primaryStage.show();

    }
    @FXML
    public void createPassedPieChart(){
        Stage primaryStage=new Stage();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("Students Overall scores");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        List<PieChart.Data> l=new ArrayList<>();
        Map<String,Double> result=service.studentsPassed();
        for (Map.Entry<String, Double> entry : result.entrySet())
        {
            String key = entry.getKey();
            Double medie=entry.getValue();
            l.add(new PieChart.Data(key, 1));
        }
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(l);

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Scores");


        final Label caption = new Label("Hover over the pie chart to see the score percentages for each student!");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 15 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue()));
                        }
                    });
        }

        ((Group) scene.getRoot()).getChildren().addAll(chart, caption);
        //((Group) scene.getRoot()).getChildren().add(chart);
        primaryStage.setScene(scene);


        primaryStage.show();

    }
    @FXML
    private void createMediiHisto()
    {
        Stage stage=new Stage();
        stage.setTitle("Score's Histogram");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Overall Scores");
        xAxis.setLabel("Student");
        yAxis.setLabel("Score");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Final Grade");
        Map<String,Double> result=service.medii();
        for (Map.Entry<String, Double> entry : result.entrySet())
        {
            String key = entry.getKey();
            Double medie=entry.getValue();
            series1.getData().add(new XYChart.Data(key, medie));
        }
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void hardestHM()
    {
        StudentsAppController.hardestHomework(service);
    }
    @FXML
    public void goodStudents() throws FileNotFoundException, DocumentException {
        StudentsAppController.rigStudents(service);

    }
    @FXML
    private void createGroupsHisto()
    {
        StudentsAppController.generateGroupHisto(service);
    }



}
