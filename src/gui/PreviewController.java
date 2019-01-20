package gui;

import domain.Nota;
import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.Service;
import service.ServiceNote;
import validate.ValidationException;

import java.io.IOException;

public class PreviewController {

    @FXML
    private TextArea textArea;
    @FXML
    private Button okB;
    @FXML
    private Button cancelB;

    Nota currentNota;
    String feedback;
    ServiceNote service;
    Stage stage;

    public PreviewController(){

    }

    @FXML
    private void initialize(){

    }


    public void setInfo(Nota n, ServiceNote service, Stage stage,String feedback) {
        this.service = service;
        this.stage = stage;
        this.feedback=feedback;
        currentNota = n;
        String text="Tema:"+n.getTemaId()+System.lineSeparator()+
                "Nota:"+Integer.toString(n.getValoare())+System.lineSeparator()+
                "Predata in saptamana:"+n.getDate()+System.lineSeparator()+
                "Deadline:"+Integer.toString(service.findOneT(n.getTemaId()).getDue())+System.lineSeparator()+
                "Feedback:"+this.feedback+System.lineSeparator();
        textArea.setText(text);
    }

    @FXML
    private void addHandler(){
        try {
            int nota_maxima=service.saveNotaWrite(currentNota.getStudentId(),currentNota.getTemaId(),currentNota.getValoare(),currentNota.getDate(),this.feedback);
            if(nota_maxima==-1)
            {
                UpdateDataController.show("You have already assigned a mark to this student on this homework!");
            }
            else {
                service.sendEmailToStudent(currentNota.getStudentId(),currentNota,nota_maxima);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message Here...");
                alert.setHeaderText("Look, an Information Dialog");
                if (nota_maxima < currentNota.getValoare()) {
                    alert.setContentText("Nota studentului a fost diminuata la " + nota_maxima + " din cauza intarzierilor! \n \n Studentul a fost notificat prin email unde a primit si fisierul cu activitatea scolara de pana acum!");
                } else {
                    alert.setContentText("Nota studentului a fost predata la timp deci nu a fost diminuata:" + currentNota.getValoare() + "\n \n Studentul a fost notificat prin email unde a primit si fisierul cu activitatea scolara de pana acum!");
                }
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        stage.close();
                        //System.out.println("Pressed OK.");
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void cancelHandler(){
        stage.close();
    }
}
