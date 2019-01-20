package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang.RandomStringUtils;
import security.Enc;
import service.Service;
import service.ServiceNote;
import validate.ValidationException;

public class AddRegisterController {
    @FXML
    private TextField userText;
    @FXML
    private TextField passText;
    @FXML
    private TextField codeText;

    ServiceNote service;
    Stage stage;
    private Enc security;
    private String code;


    public void setInfo(ServiceNote service, Stage stage,Enc security) {
        this.security=security;
        this.service = service;
        this.stage = stage;
    }

    @FXML
    private void addHandler(){
        if(service.findOneByNameS(userText.getText())!=null) {
            int length = 5;
            boolean useLetters = true;
            boolean useNumbers = true;
            code= RandomStringUtils.random(length, useLetters, useNumbers);
            service.sendCodeToStudent(service.findOneByNameS(userText.getText()).getEmail(),code);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("A validation code was sent to "+service.findOneByNameS(userText.getText()).getEmail());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    //System.out.println("Pressed OK.");
                }
            });
        }
        else
            showError("Your name is not in our students' database, please contact the administrator!");
    }

    private void showError(String description)
    {
        show(description);
    }

    static void show(String description) {
        UpdateDataController.show(description);
    }
    @FXML
    public void checkCode()
    {
        if(codeText.getText().equals(code)) {
            int length = 6;
            boolean useLetters = true;
            boolean useNumbers = true;
            code= RandomStringUtils.random(length, useLetters, useNumbers);
            security.register(userText.getText(), passText.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("The account was successfully registered!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    //System.out.println("Pressed OK.");
                }
            });
        }
        else
            showError("Wrong Validation Code!");

    }


    @FXML
    private void cancelHandler(){
        stage.close();
    }
}
