package gui;

import domain.Student;
import domain.Tema;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import repository.StudentRepository;
import repository.TemaRepository;
import service.Service;
import service.ServiceNote;
import service.ServiceTema;
import validate.StudentValidator;
import validate.TemaValidator;
import validate.Validator;

import static javafx.scene.media.MediaPlayer.INDEFINITE;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(Main.class.getResource("LogIn.fxml"));

        Parent root = loader.load();

        String fName1="C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\studenti";
        String fName2="teme.txt";
        Validator<Student> validatorStudent=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validatorStudent,fName1);
        TemaRepository temaRepository=new TemaRepository(validatorTema,fName2);
        Service service=new Service(studentRepository,temaRepository);
        ServiceTema serviceT=new ServiceTema(studentRepository,temaRepository);
        ServiceNote serviceN=new ServiceNote(studentRepository,temaRepository);

        LogInController ctrl = loader.getController();
        ctrl.setInfo("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\security\\Pass",serviceN,service,serviceT,primaryStage);
        primaryStage.setTitle("LogIn Menu");

        Scene scene = new Scene(root);
        root.setId("logInPaneStyle");
        scene.getStylesheets().addAll(this.getClass().getResource("mainStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
