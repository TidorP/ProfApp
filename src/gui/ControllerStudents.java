package gui;

import domain.Student;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;
import service.ServiceNote;
import utils.ChangeEventType;
import utils.Observer;
import utils.StudentEvent;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerStudents implements Observer<StudentEvent> {

    private Service service;
    private ServiceNote serviceNote;
    private ObservableList<Student> model;
    @FXML
    private TableView<Student> tabel;
    @FXML
    private TableColumn<Student,Integer> IdC;
    @FXML
    private TableColumn<Student,String> NumeC;
    @FXML
    private TableColumn<Student,Integer> GrupaC;
    @FXML
    private TableColumn<Student,String> EmailC;
    @FXML
    private TableColumn<Student,String> CadruC;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button findButton;

    @FXML
    private TextField numeText;
    @FXML
    private AnchorPane anchor;

    @FXML
    private void loadDataHandler(){
        model = FXCollections.observableList(StreamSupport.stream(service.findAllS().spliterator(), false).collect(Collectors.toList()));

        IdC.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        NumeC.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        GrupaC.setCellValueFactory(new PropertyValueFactory<Student, Integer>("grupa"));
        EmailC.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        CadruC.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));

        tabel.setItems(model);

    }

    @FXML
    private void closeHandler(){
        Platform.exit();
    }

    private void sendData(Student st){
        try {
            Stage stage = new Stage();
            stage.setTitle("Add new stuff");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("addData.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            AddDataController ctrl = loader.getController();

            ctrl.setInfo(st,service,stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDataUpdate(Student st){
        try {
            Stage stage = new Stage();
            stage.setTitle("Edit data");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("updateData.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            UpdateDataController ctrl = loader.getController();

            ctrl.setInfo(st,service,stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudentHandler(){
        Student st = new Student(Integer.parseInt("4"),"",Integer.parseInt("222"),"","");
        sendData(st);
    }
    @FXML
    private void updateStudentHandler(){
        Student st = tabel.getSelectionModel().getSelectedItem();
        if(st!=null)
            sendDataUpdate(st);
        else
        {
            showError("Please select a student from the table first");
        }

    }

    @FXML
    private void deleteStudentHandler(){
        Student st = tabel.getSelectionModel().getSelectedItem();
        if(st!=null){
            service.forControllerDeleteS(st.getId());
            serviceNote.deleteGradeByStudent(st.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("StudentDeleted");
            alert.setContentText("This change may affect the grades database!\n Please reload the grades file if you have that window opened!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    //System.out.println("Pressed OK.");
                }
            });
        }
        else
        {
            showError("Please select a student from the table first");
        }
    }
    private void showError(String description)
    {
        AddDataController.show(description);
    }
    @FXML
    private void findStudentHandler() {
        if (!numeText.getText().equals("")) {
            Student st = service.findOneByNameS(numeText.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you! The Student was found: \n" + st);
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    @Override
    public void update(StudentEvent studentEvent) {
        if(studentEvent.getType() == ChangeEventType.ADD){
            model.add(studentEvent.getData());
        }
        if(studentEvent.getType() == ChangeEventType.UPDATE){
            model.set(tabel.getSelectionModel().getSelectedIndex(),studentEvent.getData());
        }
        if(studentEvent.getType() == ChangeEventType.DELETE){
            model.remove(tabel.getSelectionModel().getSelectedItem());
        }

    }

    public void setService(Service s,ServiceNote serviceNote){
        this.service = s;
        this.serviceNote=serviceNote;
        service.addObserver(this);
        anchor.getStyleClass().add("anchor");
        addButton.getStyleClass().add("buttonStMenu");
        deleteButton.getStyleClass().add("buttonStMenu");
        updateButton.getStyleClass().add("buttonStMenu");
        findButton.getStyleClass().add("buttonBig");
        tabel.getStyleClass().add("table-view");

    }
}

