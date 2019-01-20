package gui;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.Service;
import service.ServiceNote;
import utils.*;
import validate.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerGrades implements Observer<GradeEvent> {

    private ServiceNote service;
    private ObservableList<Nota> model;
    @FXML
    private TableView<Nota> tabel;
    @FXML
    private TableColumn<Nota, Pairr<Integer,Integer>> IdC;
    @FXML
    private TableColumn<Nota,Integer> TemaC;
    @FXML
    private TableColumn<Nota,Integer> StudentC;
    @FXML
    private TableColumn<Nota,Integer> NotaC;
    @FXML
    private TableColumn<Nota,Integer> SaptC;
    @FXML
    private AnchorPane anchor;

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
    private TextField studentText;
    @FXML
    private TextField notaText;
    @FXML
    private TextField feedbackText;
    @FXML
    private TextField temaText;
    @FXML
    private TextArea bigText;

    @FXML
    private ComboBox<String> cBox;
    @FXML
    private ComboBox<String> cBoxWeek;

    @FXML
    private TextField filtStudentText;
    @FXML
    private TextField filtTemaText;
    @FXML
    private TextField filtDataText;
    @FXML
    private ComboBox<String> filtcBox;


    @FXML
    private void loadDataHandler(){
        //model=FXCollections.observableList(service.findAllNote());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Look..");
        Image image = new Image("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\explanation.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.showAndWait();

        model = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));

        IdC.setCellValueFactory(new PropertyValueFactory<Nota,Pairr<Integer,Integer>>("id"));
        TemaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("temaId"));
        StudentC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("studentId"));
        NotaC.setCellValueFactory(new PropertyValueFactory<Nota,Integer>("valoare"));
        SaptC.setCellValueFactory(new PropertyValueFactory<Nota,Integer>("date"));

        List<Nota> model2=new ArrayList<Nota>();
        model2.add(new Nota(1,2,10,7));
        ObservableList<Nota> mode=FXCollections.observableList(model2);

        tabel.setItems(model);

        //System.out.println(cBox.getSelectionModel().getSelectedItem());

    }

    @FXML
    private void closeHandler(){
        Platform.exit();
    }

    private void sendData(Nota n){
        try {
            Stage stage = new Stage();
            stage.setTitle("Preview");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("preview.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            PreviewController ctrl = loader.getController();

            ctrl.setInfo(n,service,stage,feedbackText.getText());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendFiltData(List<Student> model){
        try {
            Stage stage = new Stage();
            stage.setTitle("Group Table");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("filt.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            FiltController ctrl = loader.getController();

            ctrl.setInfo(model,service,stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addGradeHandler() {
        try {
            if (studentText.getText().equals("") || service.findOneByNameS(studentText.getText()) == null)
                showError("Nu exista student cu acest nume!!!");
            else if (Integer.parseInt(notaText.getText()) < 1 || Integer.parseInt(notaText.getText()) > 10)
                showError("Nota poate fi doar intre 0 si 10, numar intreg!!!");
            else if (feedbackText.getText().equals(""))
                showError("Feedback empty!!!");
            else if (service.findOneT(Integer.parseInt(cBox.getSelectionModel().getSelectedItem()))==null)
                showError("That homework hasn't been added just yet!");
            else {
                Nota nota = new Nota(Integer.parseInt(cBox.getSelectionModel().getSelectedItem()), service.findOneByNameS(studentText.getText()).getId(), Integer.parseInt(notaText.getText()), Integer.parseInt(cBoxWeek.getSelectionModel().getSelectedItem()));
                sendData(nota);
            }
        }
        catch(NumberFormatException e)
        {
            showError("Make sure you select something from each of the two combo-boxes and write something on all the textfield!");
        }

    }

    @FXML
    private void filtByStudentHandler() {
        if(filtStudentText.getText().equals(""))
            showError("Please type something in the box please!");
        else if(service.findOneByNameS(filtStudentText.getText())==null)
            showError("Student does not exist in our database!");
        else if(service.filtBySt(filtStudentText.getText())==null)
            showError("This student does not have grades associated with him!");
        else {
            model = FXCollections.observableList(StreamSupport.stream(service.filtBySt(filtStudentText.getText()).spliterator(), false).collect(Collectors.toList()));

            IdC.setCellValueFactory(new PropertyValueFactory<Nota, Pairr<Integer, Integer>>("id"));
            TemaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("temaId"));
            StudentC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("studentId"));
            NotaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
            SaptC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("date"));

            List<Nota> model2 = new ArrayList<Nota>();
            model2.add(new Nota(1, 2, 10, 7));
            ObservableList<Nota> mode = FXCollections.observableList(model2);

            tabel.setItems(model);
        }
    }
    @FXML
    private void filtByHMHandler() {
        try {
            if (filtTemaText.getText().equals(""))
                showError("Please type something in the box please!");
            else if (service.findOneT(Integer.parseInt(filtTemaText.getText())) == null)
                showError("Homework does not exist in our database!");
            else if (service.filtByHM(Integer.parseInt(filtTemaText.getText())) == null)
                showError("We don't currently have grades assigned for this homework!");
            else {

                model = FXCollections.observableList(StreamSupport.stream(service.filtByHM(Integer.parseInt(filtTemaText.getText())).spliterator(), false).collect(Collectors.toList()));

                IdC.setCellValueFactory(new PropertyValueFactory<Nota, Pairr<Integer, Integer>>("id"));
                TemaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("temaId"));
                StudentC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("studentId"));
                NotaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
                SaptC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("date"));

                List<Nota> model2 = new ArrayList<Nota>();
                model2.add(new Nota(1, 2, 10, 7));
                ObservableList<Nota> mode = FXCollections.observableList(model2);

                tabel.setItems(model);
            }
        }
        catch(NumberFormatException e)
        {
            showError("Numar prea mare");
        }
    }
    @FXML
    private void filtByDateHandler() {
        if(!filtDataText.getText().contains("-"))
            showError("You must give the dates as date1-date2 (separated by \"-\") !");
        else {

            try {
                String[] parts = filtDataText.getText().split("-");
                String start = parts[0];
                String end = parts[1];
                if(Integer.parseInt(parts[0])<1 || Integer.parseInt(parts[1])>14)
                    showError("The dates must be between 1 and 14!!!");
                else if(parts.length>2)
                    showError("Format not accepted!");
                else {

                    model = FXCollections.observableList(StreamSupport.stream(service.filtByWeek(Integer.parseInt(start), Integer.parseInt(end)).spliterator(), false).collect(Collectors.toList()));

                    IdC.setCellValueFactory(new PropertyValueFactory<Nota, Pairr<Integer, Integer>>("id"));
                    TemaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("temaId"));
                    StudentC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("studentId"));
                    NotaC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
                    SaptC.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("date"));

                    List<Nota> model2 = new ArrayList<Nota>();
                    model2.add(new Nota(1, 2, 10, 7));
                    ObservableList<Nota> mode = FXCollections.observableList(model2);

                    tabel.setItems(model);
                }
            } catch(NumberFormatException e) {
                showError("The dates must be integers between 1 and 14!!!");
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                showError("Format not accepted!");
            }
        }
    }
    @FXML
    private void filtByGrupaHandler() {
        int grupa=Integer.parseInt(filtcBox.getSelectionModel().getSelectedItem());
        List<Student> l=service.filtByGroup(grupa);
        if(l.size()==0)
            showError("There are no records for this group!");
        else
            sendFiltData(l);
    }
    @Override
    public void update(GradeEvent gradeEvent) {
        if(gradeEvent.getType() == ChangeEventType.ADD){
            model.add(gradeEvent.getData());
        }
        if(gradeEvent.getType() == ChangeEventType.UPDATE){
            model.set(tabel.getSelectionModel().getSelectedIndex(),gradeEvent.getData());
        }
        if(gradeEvent.getType() == ChangeEventType.DELETE){
            model.remove(tabel.getSelectionModel().getSelectedItem());
        }

    }

    private void validateTextFields()
    {
        // force the field to be numeric only
        notaText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    notaText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        filtTemaText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    filtTemaText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void showError(String description) {
        UpdateDataController.show(description);
    }
    public void setService(ServiceNote s){
        this.service = s;
        validateTextFields();
        service.addObserver(this);
        //setComboBox();
        anchor.getStyleClass().add("anchor");
        cBox.getStyleClass().add("buttonStMenu");
        cBoxWeek.getStyleClass().add("buttonStMenu");
        filtcBox.getStyleClass().add("buttonStMenu");

        b1.getStyleClass().add("buttonBig");
        b2.getStyleClass().add("buttonStMenu");
        b3.getStyleClass().add("buttonStMenu");
        b4.getStyleClass().add("buttonStMenu");
        b5.getStyleClass().add("buttonStMenu");

        tabel.getStyleClass().add("table-view");

        TranslateTransition transition2=new TranslateTransition();
        transition2.setDuration(Duration.seconds(3));
        transition2.setToX(10);
        transition2.setToY(0);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.setNode(bigText);

        transition2.play();

    }
    public void setComboBox()
    {
        List<String> l=new ArrayList<>();
        service.findAllT().forEach((tema)->{l.add(Integer.toString(tema.getId()));});
        ObservableList<String> options =
                FXCollections.observableArrayList(l
                );
        cBox.setItems(options);
    }
}
