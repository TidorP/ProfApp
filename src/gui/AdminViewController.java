package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import security.Enc;
import service.ServiceNote;

public class AdminViewController {
    @FXML
    private TextField userText;
    @FXML
    private TextField passText;
    @FXML
    private TextField deleteUserText;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;

    ServiceNote service;
    Stage stage;
    private Enc security;


    public void setInfo(ServiceNote service, Stage stage,Enc security) {
        this.security=security;
        this.service = service;
        this.stage = stage;
        b1.getStyleClass().add("but");
        b2.getStyleClass().add("but");
        b3.getStyleClass().add("but");
    }

    @FXML
    private void addHandler(){
        if(userText.getText().contains("Prof")) {
            security.register(userText.getText(), passText.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("New Prof moderator created!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else
        {
            showError("The UserName must cotnain Prof in it!");
        }
    }
    @FXML
    private void deleteHandler(){
        if(!security.deleteAccount(deleteUserText.getText()))
            showError("A moderator with this username does not exist!!!");
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("The user was successfully deleted!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
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
    private void cancelHandler(){
        stage.close();
    }
}
