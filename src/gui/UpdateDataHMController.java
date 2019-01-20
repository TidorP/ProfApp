package gui;

import domain.Student;
import domain.Tema;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import service.ServiceTema;
import validate.ValidationException;

public class UpdateDataHMController {
    @FXML
    private TextField idText;
    @FXML
    private TextField descriereText;
    @FXML
    private TextField dueText;
    @FXML
    private TextField givenText;

    @FXML
    private Button addB;
    @FXML
    private Button clearAllB;
    @FXML
    private Button cancelB;

    Tema currentTema;
    ServiceTema service;
    Stage stage;

    public UpdateDataHMController(){

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
        dueText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    dueText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        givenText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    givenText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    public void setInfo(Tema t, ServiceTema service, Stage stage) {
        this.service = service;
        this.stage = stage;
        currentTema = t;

        validateTextFields();
        idText.setText(Integer.toString(currentTema.getId()));
        idText.setDisable(true);
        descriereText.setText(currentTema.getDescriere());
        dueText.setText(Integer.toString(currentTema.getDue()));
        givenText.setText(Integer.toString(currentTema.getGiven()));

    }

    @FXML
    private void updateHandler(){
        if(!descriereText.getText().equals("") && !dueText.getText().equals("") && !givenText.getText().equals("")){
            try {
                if(Integer.parseInt(dueText.getText())<Integer.parseInt(givenText.getText()))
                    showError("Due date must be after or on the same week as the given date!");
                else {
                    if(service.forControllerUpdateT(new Tema(Integer.parseInt(idText.getText()), descriereText.getText(), Integer.parseInt(dueText.getText()), Integer.parseInt(givenText.getText())))==null)
                        showError("Tema invalida!");
                    else
                        stage.close();
                }
            } catch (ValidationException e) {
                showError("Invalid data for HomeWork, ID may be already taken, if not, check the restrictions below!");
                e.printStackTrace();
            }
            catch(NumberFormatException e)
            {
                showError("Invalid data for an integer!");
            }
        }
        else
            showError("Date invalide!");
    }

    private void showError(String s) {
        UpdateDataController.show(s);
    }

    @FXML
    private void clearAllHandler(){
        if(addB.getText().equals("Update")) {
            idText.setText("");
        }
        descriereText.setText("");
        dueText.setText("");
        givenText.setText("");
    }
    @FXML
    private void cancelHandler(){
        stage.close();
    }
}
