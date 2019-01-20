package gui;

import domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import service.Service;
import validate.ValidationException;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class UpdateDataController {

    @FXML
    private TextField idText;
    @FXML
    private TextField numeText;
    @FXML
    private TextField grupaText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField cadruText;

    @FXML
    private Button addB;
    @FXML
    private Button clearAllB;
    @FXML
    private Button cancelB;

    Student currentSt;
    Service service;
    Stage stage;

    public UpdateDataController(){

    }

    @FXML
    private void initialize(){

    }
    private void validateTextFields()
    {
        // force the field to be numeric only
        idText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        grupaText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    grupaText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    public void setInfo(Student st, Service service, Stage stage) {
        validateTextFields();
        this.service = service;
        this.stage = stage;
        currentSt = st;

        idText.setText(Integer.toString(currentSt.getId()));
        idText.setDisable(true);
        numeText.setText(currentSt.getNume());
        grupaText.setText(Integer.toString(currentSt.getGrupa()));
        emailText.setText(currentSt.getEmail());
        cadruText.setText(currentSt.getCadruDidactic());

    }


    @FXML
    private void updateHandler(){
        if(currentSt!=null){
            try {
                if(service.findOneByNameS(numeText.getText())!=null)
                    showError("A student with this name already exists!");
                else {
                    if(grupaText.getText().equals("") || emailText.getText().equals("") || cadruText.getText().equals(""))
                        showError("Grupa sau email sau cadru invalid!");
                    else {
                        service.forControllerUpdateS(new Student(Integer.parseInt(idText.getText()), numeText.getText(), Integer.parseInt(grupaText.getText()), emailText.getText(), cadruText.getText()));
                        stage.close();
                    }
                }
            } catch (ValidationException e) {
                showError("Invalid data for Student, ID may be already taken, if not, check the restrictions below!");
                e.printStackTrace();
            }
            catch(NumberFormatException e)
            {
                showError("Invalid data for group!");
            }
        }
        else
            showError("N-ati selectat student");
    }

    private void showError(String s) {
        show(s);
    }

    static void show(String s) {
        final Task task = new Task() {

            @Override
            protected Object call() throws Exception {
                int s = INDEFINITE;
                AudioClip audio = new AudioClip(getClass().getResource("Imgs/Error.mp3").toExternalForm());
                audio.setVolume(0.5f);
                audio.play();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        thread.interrupt();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error!!!");
        alert.setContentText(s);

        alert.showAndWait();
    }

    @FXML
    private void clearAllHandler(){
        if(addB.getText().equals("Update")) {
            idText.setText("");
        }
        numeText.setText("");
        grupaText.setText("");
        emailText.setText("");
        cadruText.setText("");
    }
    @FXML
    private void cancelHandler(){
        stage.close();
    }
}
