package gui;

import domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import validate.ValidationException;

public class AddDataController {

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

    public AddDataController(){

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
        this.service = service;
        this.stage = stage;
        currentSt = st;

        idText.setText(Integer.toString(service.size()+1));
        idText.setDisable(true);
        numeText.setText(currentSt.getNume());
        grupaText.setText(Integer.toString(currentSt.getGrupa()));
        emailText.setText(currentSt.getEmail());
        cadruText.setText(currentSt.getCadruDidactic());
        validateTextFields();
    }

    @FXML
    private void addHandler(){
        if(!Integer.toString(currentSt.getId()).equals("") && !grupaText.getText().equals("") && !emailText.getText().equals("")){
            try {
                if(service.forControllerSaveS(new Student(Integer.parseInt(idText.getText()),numeText.getText(),Integer.parseInt(grupaText.getText()),emailText.getText(),cadruText.getText()))==null)
                    showError("A student with this name already exists in the database!");
                else
                    stage.close();
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
        {
            showError("Data invalida!");
        }
    }

    private void showError(String description)
    {
        show(description);
    }

    static void show(String description) {
        UpdateDataController.show(description);
    }

    @FXML
    private void clearAllHandler(){
        if(addB.getText().equals("add")) {
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
