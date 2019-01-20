package gui;

import domain.Student;
import domain.Tema;
import javafx.application.Platform;
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
import service.ServiceTema;
import utils.ChangeEventType;
import utils.Observer;
import utils.StudentEvent;
import utils.TemaEvent;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerHM implements Observer<TemaEvent> {

    private ServiceTema service;
    private ServiceNote serviceNote;
    private ObservableList<Tema> model;
    @FXML
    private TableView<Tema> tabel;
    @FXML
    private TableColumn<Tema,Integer> IdC;
    @FXML
    private TableColumn<Tema,String> DescriereC;
    @FXML
    private TableColumn<Tema,Integer> DueC;
    @FXML
    private TableColumn<Tema,String> GivenC;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button findButton;
    @FXML
    private Button extButton;

    @FXML
    private TextField numeText;
    @FXML
    private AnchorPane anchor;

    @FXML
    private void loadDataHandler(){
        model = FXCollections.observableList(StreamSupport.stream(service.findAllT().spliterator(), false).collect(Collectors.toList()));

        IdC.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("id"));
        DescriereC.setCellValueFactory(new PropertyValueFactory<Tema, String>("descriere"));
        DueC.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("due"));
        GivenC.setCellValueFactory(new PropertyValueFactory<Tema,String>("given"));

        tabel.setItems(model);

    }

    @FXML
    private void closeHandler(){
        Platform.exit();
    }

    private void sendData(Tema t){
        try {
            Stage stage = new Stage();
            stage.setTitle("Add new stuff");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("addDataHM.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            AddDataHMController ctrl = loader.getController();

            ctrl.setInfo(t,service,stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDataUpdate(Tema t){
        try {
            Stage stage = new Stage();
            stage.setTitle("Edit data");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("updateDataHM.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            UpdateDataHMController ctrl = loader.getController();

            ctrl.setInfo(t,service,stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTemaHandler(){
        Tema t = new Tema(Integer.parseInt("1"),"",1,1);
        sendData(t);
    }
    @FXML
    private void updateTemaHandler(){
        Tema t = tabel.getSelectionModel().getSelectedItem();
        if(t!=null)
            sendDataUpdate(t);
        else
            showError("Selctati o tema!");
    }
    private void showError(String description)
    {
        AddDataController.show(description);
    }
    @FXML
    private void deleteTemaHandler(){
        Tema t = tabel.getSelectionModel().getSelectedItem();
        if(t!=null){
            service.forControllerDeleteT(t.getId());
            serviceNote.deleteGradeByHM(t.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Homework deleted!");
            alert.setContentText("This change may affect the grades database!\n Please reload the grades file if you have that window opened!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }
    @FXML
    private void prelugnireTemaHandler(){
        Tema t = tabel.getSelectionModel().getSelectedItem();
        if(t!=null){
            System.out.println(t);
            if(t.getDue()==14) {
                showError("Nu se poate prelungi aceasta tema!");
                return;
            }
            service.prelungireTema(t.getId());
            service.sendEmailToAll("A deadline was just extended!","The deadline for homework number "+t.getId()+" was extended to "+t.getId()+1+" !");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("The due date for this homework was successfully extended! All students were notified via email!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    @Override
    public void update(TemaEvent temaEvent) {
        if(temaEvent.getType() == ChangeEventType.ADD){
            model.add(temaEvent.getData());
        }
        if(temaEvent.getType() == ChangeEventType.UPDATE){
            model.set(tabel.getSelectionModel().getSelectedIndex(),temaEvent.getData());
        }
        if(temaEvent.getType() == ChangeEventType.DELETE){
            model.remove(tabel.getSelectionModel().getSelectedItem());
        }

    }

    public void setService(ServiceTema s,ServiceNote serviceNote){
        this.service = s;
        this.serviceNote=serviceNote;
        service.addObserver(this);
        anchor.getStyleClass().add("anchor");

        addButton.getStyleClass().add("buttonStMenu");
        extButton.getStyleClass().add("buttonBig");

        deleteButton.getStyleClass().add("buttonStMenu");
        updateButton.getStyleClass().add("buttonStMenu");

        tabel.getStyleClass().add("table-view");

    }
}
