package gui;

import domain.Student;
import domain.Tema;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import service.ServiceTema;
import validate.ValidationException;

public class AddDataHMController {
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

    public AddDataHMController(){

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

        idText.setText(Integer.toString(currentTema.getId()));
        descriereText.setText(currentTema.getDescriere());
        dueText.setText(Integer.toString(currentTema.getDue()));
        givenText.setText(Integer.toString(currentTema.getGiven()));
        validateTextFields();
    }

    @FXML
    private void addHandler() {
        if(service.size()<14) {
            if (!descriereText.getText().equals("")) {
                try {
                    Tema newHM = new Tema(Integer.parseInt(idText.getText()), descriereText.getText(), Integer.parseInt(dueText.getText()), Integer.parseInt(givenText.getText()));
                    if (service.findOneT(newHM.getId()) != null)
                        showError("A Homework with this ID already exists! Check the table!");
                    else if (Integer.parseInt(dueText.getText()) < Integer.parseInt(givenText.getText()))
                        showError("Due date must be after or on the same week as the given date!");
                    else {
                        service.forControllerSaveT(newHM);
                        stage.close();
                        service.sendEmailToAll("New homework!", "This homework was just been added!" + newHM.toString());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Message Here...");
                        alert.setHeaderText("Look, an Information Dialog");
                        alert.setContentText("All students were notifies via email!");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                //System.out.println("Pressed OK.");
                            }
                        });
                    }
                } catch (ValidationException e) {
                    showError("Invalid data for HomeWork, ID may already be taken, if not, check the restrictions below!");
                } catch (NumberFormatException e) {
                    showError("Invalid data for due text or given or id null!");
                }
            } else
                showError("Descriere nula!");
        }
        else
            showError("14 homeworks already exists");
    }

    private void showError(String s) {
        UpdateDataController.show(s);
    }

    @FXML
    private void clearAllHandler(){
        if(addB.getText().equals("add")) {
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
