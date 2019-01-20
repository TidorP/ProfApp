package gui;

import domain.Student;
import domain.Tema;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import repository.StudentRepository;
import repository.TemaRepository;
import service.Service;
import service.ServiceNote;
import service.ServiceTema;
import validate.StudentValidator;
import validate.TemaValidator;
import validate.ValidationException;
import validate.Validator;


import java.io.File;
import java.io.IOException;

public class ControllerMain {
    private ServiceNote serviceN;
    private Service serviceSt;
    private ServiceTema serviceTema;
    @FXML
    private Button studentsButton;
    @FXML
    private Button hmButton;
    @FXML
    private Button grButton;
    @FXML
    private Button statsButton;
    @FXML
    private ImageView imageView;

    public void setImg(ServiceNote serviceN,Service service,ServiceTema serviceTema) {
        //File file = new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\explanation.png");
        //Image image = new Image(file.toURI().toString());
        Image image = new Image("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\Main.png");
        imageView.setImage(image);
        this.serviceN=serviceN;
        this.serviceSt=service;
        this.serviceTema=serviceTema;

        studentsButton.getStyleClass().add("buttonProf");
        hmButton.getStyleClass().add("buttonProf");
        grButton.getStyleClass().add("buttonProf");
        statsButton.getStyleClass().add("buttonProf");
    }


    @FXML
    private void showStudentHandler(){
        try {
            Stage primaryStage=new Stage();
            //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Main.class.getResource("mainApplicationStudents.fxml"));

            Parent root = loader.load();

            ControllerStudents ctrl = loader.getController();


            ctrl.setService(serviceSt,serviceN);

            primaryStage.setTitle("Students Menu");

            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource("studentMenuStyle.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showTemaHandler(){
        try {
            Stage primaryStage=new Stage();
            //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Main.class.getResource("mainApplicationHM.fxml"));

            Parent root = loader.load();

            ControllerHM ctrl = loader.getController();


            ctrl.setService(serviceTema,serviceN);

            primaryStage.setTitle("Homeworks Menu");

            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource("studentMenuStyle.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showGradesHandler(){
        try {
            Stage primaryStage=new Stage();
            //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Main.class.getResource("mainApplicationGrades.fxml"));

            Parent root = loader.load();

            ControllerGrades ctrl = loader.getController();

            ctrl.setService(serviceN);

            primaryStage.setTitle("Grades Menu");

            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource("studentMenuStyle.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showStatsHandler(){

        try {
            Stage primaryStage=new Stage();
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Main.class.getResource("statisticsMenu.fxml"));

            Parent root = loader.load();

            StatisticsController ctrl = loader.getController();


            ctrl.setService(serviceN,primaryStage);

            primaryStage.setTitle("Reports Menu");

            Scene scene = new Scene(root);
            root.setId("statsPane");
            scene.getStylesheets().addAll(this.getClass().getResource("statsStyle.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
