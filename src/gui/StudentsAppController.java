package gui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.sun.javafx.image.IntPixelGetter;
import domain.Student;
import domain.Tema;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import service.ServiceNote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StudentsAppController {
    private ServiceNote service;
    private String studentName;

    @FXML
    private ComboBox<String> cBox;
    @FXML
    private Text welcomeText;

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
    private TextArea txtArea;
    static Stage stage;


    public void setService(ServiceNote s,String studentName,Stage stage){
        this.service = s;
        this.stage=stage;
        this.studentName=studentName;
        welcomeText.setText("Welcome back, "+studentName+"!");
        welcomeText.setFill(Color.GREEN);
        b1.getStyleClass().add("buttonSt");
        b2.getStyleClass().add("buttonSt");
        b3.getStyleClass().add("buttonSt");
        b4.getStyleClass().add("buttonSt");
        b5.getStyleClass().add("buttonSt");
        b6.getStyleClass().add("buttonSt");
        cBox.getStyleClass().add("buttonSt");
        txtArea.getStyleClass().add("text-area");

        TranslateTransition transition2=new TranslateTransition();
        transition2.setDuration(Duration.seconds(3));
        transition2.setToX(440);
        transition2.setToY(0);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.setNode(welcomeText);

        transition2.play();
    }

    private void addTableStudentScore(PdfPTable table) {
        Stream.of("Grade", "HomeWork ID")
                .forEach(columnTitle -> {
                    addTable(table, columnTitle);
                });
    }

    static void addTable(PdfPTable table, String columnTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(columnTitle));
        table.addCell(header);
    }

    @FXML
    public void createSituationForStudentPDF() throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getDestination(studentName+" Situatie.pdf").getAbsolutePath()));

        document.open();

        Paragraph p = new Paragraph("Your situation until this point:\n\n");
        document.add(p);
        PdfPTable table = new PdfPTable(2);
        addMediiTableHeader(table);
        addMediiRows(table);

        document.add(table);
        document.add(new Chunk("Your average score at the moment is: "+Double.toString(service.medii().get(studentName))));
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
        List<Pair<Integer,Integer>> result=service.getNoteForStudent(studentName);
        populateTable(table, result);

    }

    private void populateTable(PdfPTable table, List<Pair<Integer,Integer>> result) {
        for (Pair<Integer,Integer> entry : result)
        {
            Integer nota = entry.getKey();
            Integer temaID=entry.getValue();
            table.addCell(Integer.toString(nota));
            table.addCell(Integer.toString(temaID));
        }
    }

    @FXML
    public void createAvailableHMPDF() throws IOException, DocumentException, URISyntaxException {
        try {
            Integer.parseInt(cBox.getSelectionModel().getSelectedItem());
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(getDestination("StillAvailableAssignments.pdf").getAbsolutePath()));

            document.open();
            Paragraph p = new Paragraph("These homeworks can still be presented:"+"\n\n");
            document.add(p);

            PdfPTable table = new PdfPTable(1);
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
                    System.out.println("Pressed OK.");
                }
            });
        }
        catch(NumberFormatException e) {
            showError("Select something from the box first!!!");
        }


    }
    private void addPassedTableHeader(PdfPTable table) {
        Stream.of("HomeWork ID")
                .forEach(columnTitle -> {
                    addTable(table, columnTitle);
                });
    }



    private void addPassedRows(PdfPTable table) {


        for (Tema entry : service.findAllT())
        {
            if(entry.getDue()+2<=Integer.parseInt(cBox.getSelectionModel().getSelectedItem()))
                table.addCell(Integer.toString(entry.getId()));

        }



    }

    private void showError(String description) {
        UpdateDataController.show(description);
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
        xAxis.setLabel("HomeWork ID");
        yAxis.setLabel("Score");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Final Grade");
        List<Pair<Integer,Integer>>result=service.getNoteForStudent(studentName);
        for (Pair<Integer,Integer> entry : result)
        {
            Integer nota=entry.getKey();
            String temaId = Integer.toString(entry.getValue());

            series1.getData().add(new XYChart.Data(temaId, nota));
        }
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void hardestHM()
    {
        hardestHomework(service);
    }

    static void hardestHomework(ServiceNote service) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message Here...");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("The hardest homework was that with the number "+ service.hardestHM().getKey()+" with an average score of "+ service.hardestHM().getValue());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    @FXML
    public void goodStudents() throws FileNotFoundException, DocumentException {
        rigStudents(service);

    }

    private static File getDestination(String filename)
    {
        return getFile(filename, stage);
    }

    static File getFile(String filename, Stage stage) {
        FileChooser directoryChooser = new FileChooser();
        File defaultDir=new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\PDFS");
        directoryChooser.setInitialFileName(filename);
        directoryChooser.setInitialDirectory(defaultDir);
        return directoryChooser.showSaveDialog(stage);
    }

    static void rigStudents(ServiceNote service) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream( getDestination("GoodStudents.pdf").getAbsolutePath()));

        document.open();
        Paragraph p = new Paragraph("Good students who respected all the deadlines:"+"\n\n");
        document.add(p);

        List<Student> result= service.goodStudents();
        for(Student st:result)
        {
            Phrase phrase=new Phrase(new Chunk(st+"\n"));
            document.add(phrase);
        }
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

    @FXML
    private void createGroupsHisto()
    {
        generateGroupHisto(service);
    }

    static void generateGroupHisto(ServiceNote service) {
        Stage stage=new Stage();
        stage.setTitle("Group-score's Histogram");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Overall Scores");
        xAxis.setLabel("Group");
        yAxis.setLabel("Average Score");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Score");
        Map<Integer,Double> result= service.groupsReport();
        for (Map.Entry<Integer, Double> entry : result.entrySet())
        {
            Integer key = entry.getKey();
            Double medie=entry.getValue();
            series1.getData().add(new XYChart.Data(key.toString(), medie));
        }
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }


}
